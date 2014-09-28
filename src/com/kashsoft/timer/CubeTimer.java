package com.kashsoft.timer;

import java.util.Timer;
import java.util.TimerTask;

public class CubeTimer {
	boolean started = false;
	long time;
	Timer timer; //in milliseconds
	long startTime; //in nanoseconds
	MainActivity act;
	
	public CubeTimer(MainActivity a){
		act = a;
	}
	
	public class CubeTimerTask extends TimerTask{
		public void run(){
			//time += 10;
			time =  (System.nanoTime() - startTime) / 1000000;
			act.runOnUiThread(new Runnable() {
				@Override
				public void run(){
					act.setTime(time);
				}
			});
		}
	}
	
	public void set(){
		if (!started) {
			time = 0;
			timer = new Timer();
			CubeTimerTask task = new CubeTimerTask();
			timer.scheduleAtFixedRate(task, 0, 10);
			started = true;
			startTime = System.nanoTime();
		}
	}
	
	public void stop(){
		if (started) {
			started = false;
			timer.cancel();
		}
	}
	
	public long getTime(){
		return time;
	}
	
	public void toSleepMode(){
		timer.cancel();
	}
	
	public void wakeUp(){
		timer = new Timer();
		CubeTimerTask task = new CubeTimerTask();
		timer.scheduleAtFixedRate(task, 0, 10);
	}
	
}
