package jode;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import jode.Timers;

public enum EventLoop {
	INSTANCE;

	private final ExecutorService service;
	private final AtomicLong eventsInLoop;

	EventLoop() {
		service = Executors.newSingleThreadExecutor();
		eventsInLoop = new AtomicLong();
	}

	public void run(final Runnable function) {
		eventsInLoop.incrementAndGet();
		service.execute(new Runnable () {
			public void run (){
				try {
					PlatformCommon.INSTANCE.invoker.invoke(function);	

				} catch(Throwable t){
					System.err.println(t.getMessage());

				} finally {
					
					long inLoop = eventsInLoop.decrementAndGet();
					
					
					if (
						inLoop == 0 &&
						Timers.INSTANCE.getScheduled() == 0 &&
						Files.INSTANCE.getPendingCallbacks() == 0
						
						) {
						service.shutdown();
					}
				}
				
			}
		});
	}
}