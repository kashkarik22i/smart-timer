package com.kashsoft.timer;

import java.util.Timer;
import java.util.TimerTask;

public class CubeTimer {
	boolean started = false;
	long time; //in milliseconds
	Timer timer; 
	Timer preTimer;
	Timer starter;
	long startTime; //in nanoseconds
	long startPreTime;
	int preTime; //in seconds
	MainActivity act;
	
	public CubeTimer(MainActivity a){
		act = a;
		time = 0;
	}
	
	public class CubeTimerTask extends TimerTask{
		public void run(){
			time =  (System.nanoTime() - startTime) / (long) Math.pow(10, 6);
			act.runOnUiThread(new Runnable() {
				@Override
				public void run(){
					act.setTime(time);
				}
			});
		}
	}
	
	public class StartTimerTask extends TimerTask {
		public void run() {
			if (preTimer != null) preTimer.cancel();
			set();
		}
	}
	
	public class PreTimerTask extends TimerTask {
		public void run() {
			preTime = (int) ((System.nanoTime() - startPreTime) / (int) Math.pow(10, 9));
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					act.setPreTime(preTime);
				}
			});
		}
	}
	
	public void set(int toWait) {
		if (! started) {
			time = 0;
			timer = new Timer();
			starter = new Timer(); 
			preTimer = new Timer();
			
			TimerTask preTimerTask = new PreTimerTask();
			TimerTask startTimerTask = new StartTimerTask();
			startPreTime = System.nanoTime();
			preTimer.scheduleAtFixedRate(preTimerTask, 0, 100);
			starter.schedule(startTimerTask, toWait);
			started = true;
		}
	}
	
	private void set(){
		CubeTimerTask task = new CubeTimerTask();
		startTime = System.nanoTime();
		timer.scheduleAtFixedRate(task, 0, 10);
	}
	
	public void stop(){
		started = false;
		if (timer != null) timer.cancel();
		if (starter != null) starter.cancel();
		if (preTimer != null) preTimer.cancel();
	}
	
	public long getTime(){
		return time;
	}
	
	public void toSleepMode(){
		if (timer != null) timer.cancel();
	}
	
	public void wakeUp(){
		timer = new Timer();
		CubeTimerTask task = new CubeTimerTask();
		timer.scheduleAtFixedRate(task, 0, 10);
	}
	
}
