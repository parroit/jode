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
        private final int resultsSize;
        private final List<byte[]> results;
        private final AsynchronousFileChannel channel ;

        public ReadCompletionHandler (Callback cb, int bufferSize, List<byte[]> results, int resultsSize, AsynchronousFileChannel channel  ) {
            this.cb = cb;
            this.results = results;
            this.resultsSize = resultsSize;
            this.bufferSize = bufferSize;
            this.channel = channel;
        }

        public void completed(Integer result, ByteBuffer attachment) {

            if (result > 0) {

                byte[] chunk = new byte[result];
                attachment.rewind();
                attachment.get(chunk);
                
                results.add(chunk);

                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                ReadCompletionHandler completion = new ReadCompletionHandler(cb, bufferSize, results, resultsSize + result, channel);
                channel.read(buffer, resultsSize + result, buffer, completion );

            } else {
                
                byte[] wholeArray = new byte[resultsSize];
                int wholePosition = 0;

                for (byte[] arr : results) {
                    System.arraycopy(arr, 0, wholeArray, wholePosition, arr.length);
                    wholePosition += arr.length;
                }

               // System.out.println(wholeArray.length);
               //System.out.println(resultsSize);

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

        public void failed(Throwable exception, ByteBuffer attachment) {
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

            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

            List<byte[]> results = new ArrayList<byte[]>();
            ReadCompletionHandler completion = new ReadCompletionHandler(cb, bufferSize, results, 0, channel);

            channel.read(buffer, 0, buffer, completion );

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
