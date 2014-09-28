package com.kashsoft.timer;

import java.util.ArrayList;
import java.util.List;

import com.kashsoft.timer.ResultContract.ResultEntry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ResultHistory {
	public static final String TAG = "ResultHistoryTag";
	@SuppressLint("NewApi")
	public void removeDB(Context context){
		HistoryDbHelper mDbHelper = new HistoryDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		db.close();
		context.deleteDatabase("ResultHistory.db");
	}
	
	public List<Result> loadAllResults(Context context){
		HistoryDbHelper mDbHelper = new HistoryDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String[] projection = {
				ResultEntry.COLUMN_NAME_RESULT,
				ResultEntry.COLUMN_NAME_ATTEMPT,
				ResultEntry.COLUMN_NAME_DATE,
				ResultEntry.COLUMN_NAME_SCRAMBLE,
				ResultEntry.COLUMN_NAME_TIME};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = ResultEntry.COLUMN_NAME_TIME + " DESC";

		Cursor c = db.query(ResultEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    null,                                     // The columns for the WHERE clause
		    null,                                     // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		List<Result> results = new ArrayList<Result>();
		c.moveToFirst();
		while (c.isAfterLast() == false){
			Result r = cursorToResult(c);
			results.add(r);
			c.moveToNext();
		}
		db.close();
		return results;
	}
	
	public List<Result> loadDateResults(Context context){
		HistoryDbHelper mDbHelper = new HistoryDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String[] projection = {
				ResultEntry.COLUMN_NAME_RESULT,
				ResultEntry.COLUMN_NAME_ATTEMPT,
				ResultEntry.COLUMN_NAME_DATE,
				ResultEntry.COLUMN_NAME_SCRAMBLE,
				ResultEntry.COLUMN_NAME_TIME};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = ResultEntry.COLUMN_NAME_TIME + " DESC";

		long dateLong = ResultsView.getCurrentDate();
		String date = String.valueOf(dateLong);
		Log.i(TAG, String.format("searching db for results at date: %s", date));
		Cursor c = db.query(ResultEntry.TABLE_NAME,   // The table to query
		    projection,                               // The columns to return
		    "date=?",                                 // The columns for the WHERE clause
		    new String[]{date},                       // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		List<Result> results = new ArrayList<Result>();
		c.moveToFirst();
		while (c.isAfterLast() == false){
			Result r = cursorToResult(c);
			results.add(r);
			c.moveToNext();
		}
		db.close();
		return results;
	}
	
	public void writeResults(Context context, List<Result> results){
		HistoryDbHelper mDbHelper = new HistoryDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		for (Result r: results){
			Log.i(TAG, String.format(
					"Saving result: \n time:%d \n date:%d \n attempt:%d \n res:%d \n scramble:%s",
					r.getTime(),
					r.getDate(),
					r.getAttempt(),
					r.getResult(),
					r.getScramble()));
			// Create a new map of values, where column names are the keys
			ContentValues values = resultToCv(r);
			try {
				db.insertOrThrow(ResultEntry.TABLE_NAME, "null", values);
			} catch (SQLiteConstraintException e) {
				Log.v(TAG, "Trying to insert a duplicate entry");
			}
		}
		db.close();
	}
	
	private ContentValues resultToCv(Result r){
		ContentValues values = new ContentValues();
		values.put(ResultEntry.COLUMN_NAME_DATE, r.getDate());
		values.put(ResultEntry.COLUMN_NAME_TIME, r.getTime());
		values.put(ResultEntry.COLUMN_NAME_ATTEMPT, r.getAttempt());
		values.put(ResultEntry.COLUMN_NAME_SCRAMBLE, r.getScramble());
		values.put(ResultEntry.COLUMN_NAME_RESULT, r.getResult());
		return values;
	}

	private Result cursorToResult(Cursor c){
		int attempt = c.getInt(c.getColumnIndexOrThrow(ResultEntry.COLUMN_NAME_ATTEMPT));
		long time = c.getLong(c.getColumnIndexOrThrow(ResultEntry.COLUMN_NAME_TIME));
		long date = c.getLong(c.getColumnIndexOrThrow(ResultEntry.COLUMN_NAME_DATE));
		String s = c.getString(c.getColumnIndexOrThrow(ResultEntry.COLUMN_NAME_SCRAMBLE));
		Cube scramble = new Cube(s);
		long result = c.getLong(c.getColumnIndexOrThrow(ResultEntry.COLUMN_NAME_RESULT));
		return new Result(time, date, scramble, attempt, result);
	}
	
}