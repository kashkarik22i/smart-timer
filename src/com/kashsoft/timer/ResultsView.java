package com.kashsoft.timer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultsView {
	private List<Result> results;
	private List<String> resultStringView;
	
	public ResultsView(List<Result> results){
		this.results = results;
		this.resultStringView = new ArrayList<String>();
		for (Result r: results) {
			//resultStringView.add(longToTime(r.getResult()) + " " + dateString(r.getDate()) + " " + timeString(r.getTime()));
			resultStringView.add(resultString(r.getResult()));
		}
	}
	
	public void addResult(Result r){
		results.add(r);
		//resultStringView.add(longToTime(r.getResult()) + " " + dateString(r.getDate()) + " " + timeString(r.getTime()));
		resultStringView.add(resultString(r.getResult()));
	}
	
	public List<String> getResultStringView() {
		return resultStringView;
	}
	
	public List<Result> getView(){
		return results;
	}
	
	public static String resultString(long millis){
		long minutes = millis/60000;
		long seconds = (millis - minutes * 60000) / 1000;
		long smaller = (millis - minutes * 60000 - seconds * 1000) / 10; //hundred's of a second 
		return String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, smaller);
	}
	
	public static String dateString(long date){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
		return df.format(new Date(date));
	}
	
	public static String timeString(long date){
		DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		return df.format(new Date(date));
	}
	
	public static long onlyTime(long date){
		return date - onlyDate(date);
	}
	
	public static long getCurrentDate(){
		return onlyDate(new Date().getTime());
	}
	
	public static long getCurrentTime(){
		return onlyTime(new Date().getTime());
	}
	
	public static long onlyDate(long date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		long res = -1;
	    try {
			Date dateWithoutTime = sdf.parse(sdf.format(date));
			res = dateWithoutTime.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return res;    
	}
	
}
