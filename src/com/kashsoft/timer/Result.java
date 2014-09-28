package com.kashsoft.timer;

public class Result {
	private final long time;
	private final long date;
	private final Cube scramble;
	private final int attempt;
	private final long result;
	
	public Result(long time, long date, Cube scramble, int attempt, long result){
		this.time = time;
		this.date = date;
		this.scramble = scramble;
		this.attempt = attempt;
		this.result = result;
	}
	
	public Result(Cube scramble, int attempt, long result){
		this.date = ResultsView.getCurrentDate();
		this.time = ResultsView.getCurrentTime();
		this.scramble = scramble;
		this.attempt = attempt;
		this.result = result;
	}
	
	public long getTime(){
		return time;
	}
	public long getDate(){
		return date;
	}
	public String getScramble(){
		return scramble.getRotationsString();
	}
	public int getAttempt(){
		return attempt;
	}
	public long getResult(){
		return result;
	}
	
}
