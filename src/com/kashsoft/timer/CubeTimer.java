package com.kashsoft.timer;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class CubeTimer {
	public static final String TAG = "CubeTimer";
	private int inspectionTime;
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
	
	public void setInspectionTime(int inspectionTime) {
		this.inspectionTime = inspectionTime;
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
			setMainTimer();
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
	
	public void set() {
		Log.i(TAG, String.format(Locale.getDefault(), "Setting timer with inspection time %d", inspectionTime));
		if (! started) {
			time = 0;
			timer = new Timer();
			if (inspectionTime == 0) {
				setMainTimer();
				started = true;
				return;
			}
			starter = new Timer(); 
			preTimer = new Timer();
			
			TimerTask preTimerTask = new PreTimerTask();
			TimerTask startTimerTask = new StartTimerTask();
			startPreTime = System.nanoTime();
			preTimer.scheduleAtFixedRate(preTimerTask, 0, 100);
			starter.schedule(startTimerTask, inspectionTime * 1000);
			started = true;
		}
	}
	
	private void setMainTimer(){
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
