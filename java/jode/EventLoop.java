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
					function.run();	
				} finally {
					//System.out.println(Timers.INSTANCE.getScheduled());
					
					long inLoop = eventsInLoop.decrementAndGet();
					
					//System.out.println(inLoop);
					
					if (
						inLoop == 0 &&
						Timers.INSTANCE.getScheduled() == 0 
						
						) {
						service.shutdown();
					}
				}
				
			}
		});
	}
}