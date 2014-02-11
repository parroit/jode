package jode;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import jode.EventLoop;
import java.util.concurrent.ConcurrentHashMap;

public enum Timers{
	INSTANCE;

	private long nextId;
	private final Timer systemTimer;
	private final ConcurrentHashMap<Long,Task> scheduled;

	Timers () {
		systemTimer = new Timer("system-timer", true);		
		scheduled = new ConcurrentHashMap<Long,Task>();
		nextId = 0;
	}

	public long getScheduled(){
		return scheduled.size();
	}

	private class Task extends TimerTask{
		private final Runnable runnable;
		private boolean interval;
		private long id;

		public Task(Runnable runnable, boolean interval, long id){
			this.runnable = runnable;
			this.interval = interval;
		}

		public void run(){
			if (!interval) {
				scheduled.remove(id);	
			}
			
			EventLoop.INSTANCE.run(runnable);
		}
	}

	public long setTimeout(Runnable runnable, long delay) {
		Task task = new Task(runnable, false, nextId++);
		systemTimer.schedule(task, delay); 

		scheduled.put(task.id, task);
		return task.id;
	}

	public long setInterval(Runnable runnable, long delay) {
		Task task = new Task(runnable, true, nextId++);
		systemTimer.schedule(task, delay, delay); 
		scheduled.put(task.id, task);
		return task.id;
	}

	public void clearInterval(long id) {
		Task task = scheduled.get(id);
		if (task != null) {
			task.cancel();
			scheduled.remove(id);	
		}
	}

	public void clearTimeout(long id) {
		clearInterval(id);
	}


}