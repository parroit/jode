package jode;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.io.*;
import java.util.*;

import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicLong;

public enum Files {
    INSTANCE;

    private final AtomicLong pendingCallbacks;

    public long getPendingCallbacks() {
        return pendingCallbacks.get();
    }

    Files() {

        pendingCallbacks = new AtomicLong();
    }

    public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
        private final Callback cb;
        private final int bufferSize;
        private int chunksSize;
        private final List<byte[]> chunks;
        private final AsynchronousFileChannel channel ;
        private final ByteBuffer buffer;
        
        public ReadCompletionHandler (Callback cb, int bufferSize, AsynchronousFileChannel channel  ) {
            this.cb = cb;
            this.bufferSize = bufferSize;
            this.channel = channel;

            chunksSize = 0;
            
            buffer = ByteBuffer.allocate(bufferSize);

            chunks = new ArrayList<byte[]>();
        }

        public void read(){
            buffer.rewind();
                
            channel.read(buffer, chunksSize , buffer, this );
        }

        private byte[] concatChunks(){
            byte[] result = new byte[chunksSize];
            int resultPosition = 0;

            for (byte[] arr : chunks) {
            
                System.arraycopy(arr, 0, result, resultPosition, arr.length);
                resultPosition += arr.length;
            }

            return result;
        }

        public void completed(Integer bytesRead, ByteBuffer buffer) {

            if (bytesRead > 0) {

                byte[] chunk = new byte[bytesRead];
                buffer.rewind();
                buffer.get(chunk);
                chunks.add(chunk);

                
                this.chunksSize += bytesRead;
                
                this.read();

            } else {
                
                
                byte[] wholeArray = concatChunks();
               

                String res = new String( wholeArray, StandardCharsets.UTF_8 );
                pendingCallbacks.decrementAndGet();
                EventLoop.INSTANCE.run(new Runnable () {
                    public void run() {
                        //System.out.println(res);
                        try {
                            cb.invoke(null, res);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });

            }



        }

        public void failed(Throwable exception, ByteBuffer buffer) {
            pendingCallbacks.decrementAndGet();
            EventLoop.INSTANCE.run(new Runnable () {
                public void run() {
                    cb.invoke(exception, null);
                }
            });
        }
    }

    public void readFile(String filePath, int bufferSize, Callback cb ) {
        //cb.invoke(null,"Ciao");


        try {
            Path file = Paths.get(filePath);
            pendingCallbacks.incrementAndGet();

            AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);

            ReadCompletionHandler completion = 
                new ReadCompletionHandler(cb, bufferSize, channel);
            
            completion.read();
            
           

        } catch (IOException ex) {

            pendingCallbacks.decrementAndGet();

            EventLoop.INSTANCE.run(new Runnable () {
                public void run() {

                    cb.invoke(ex, null);
                }
            });
        }
    }
}
