package jode;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicLong;

public enum Files {
  INSTANCE; 
  
  private final AtomicLong pendingCallbacks;
  
  public long getPendingCallbacks(){
    return pendingCallbacks.get();
  }

  Files() {
    
    pendingCallbacks = new AtomicLong();
  }

  public void readFile(String filePath,Callback cb ) throws Exception {
    //cb.invoke(null,"Ciao");
    
    Path file = Paths.get(filePath);
    AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);

    ByteBuffer buffer = ByteBuffer.allocate(100_000);

    pendingCallbacks.incrementAndGet();

    channel.read(buffer, 0, buffer,
        new CompletionHandler<Integer, ByteBuffer>() {
          public void completed(Integer result, ByteBuffer attachment) {
            
            String res = new String( attachment.array(),0,result, StandardCharsets.UTF_8 );
            pendingCallbacks.decrementAndGet();
            EventLoop.INSTANCE.run(new Runnable () {
              public void run() {
                System.out.println(res);
                try {
                  cb.invoke(null,res);  
                } catch (Throwable e) {
                  e.printStackTrace();
                }    
              }
            });
            
            
          }

          public void failed(Throwable exception, ByteBuffer attachment) {
            pendingCallbacks.decrementAndGet();
            EventLoop.INSTANCE.run(new Runnable () {
              public void run() {
                cb.invoke(exception,null);
              }
            });
          }
        });
  }
}
