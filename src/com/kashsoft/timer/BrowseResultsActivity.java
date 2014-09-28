package com.kashsoft.timer;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class BrowseResultsActivity extends Activity {
	private static final String displayMode = "TODAY";
	private static final String modeToday = "TODAY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_results);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//display
		if (displayMode.equals(modeToday)){
			ResultHistory resHistory = new ResultHistory();
			List<Result> listOfResults = resHistory.loadDateResults(this);
			
			TableLayout table = (TableLayout) findViewById(R.id.day_table);
			fillResultsTable(listOfResults, table);
			addStatistics(listOfResults);			
		}
	}
	
	private void fillResultsTable(List<Result> listOfResults, TableLayout table){
		for (Result r: listOfResults){
			getLayoutInflater().inflate(R.layout.result_table_row, table, true);
			TableRow tr = (TableRow) table.getChildAt(table.getChildCount() - 1);
			//display attempt
			getLayoutInflater().inflate(R.layout.table_cell, tr, true);
			((TextView) tr.getChildAt(tr.getChildCount() - 1)).setText(String.valueOf(r.getAttempt()));
			//display result
			getLayoutInflater().inflate(R.layout.table_cell, tr, true);
			((TextView) tr.getChildAt(tr.getChildCount() - 1)).setText(ResultsView.resultString(r.getResult()));
		}
	}
	
	private void addStatistics(List<Result> listOfResults){
		addOneStatistic("avg", ResultsView.resultString(ResultHistory.avg(listOfResults)));
		addOneStatistic("max", ResultsView.resultString(ResultHistory.max(listOfResults)));
		addOneStatistic("min", ResultsView.resultString(ResultHistory.min(listOfResults)));
		addOneStatistic("min 3 of 5", ResultsView.resultString(ResultHistory.minTreeOfFive(listOfResults)));
		addOneStatistic("max 3 of 5", ResultsView.resultString(ResultHistory.maxTreeOfFive(listOfResults)));
	}
	
	private void addOneStatistic(String name, String value){
		TextView tv = new TextView(this);
		tv.setText(String.format(Locale.getDefault(), "%s: %s", name, value));
		tv.setGravity(Gravity.CENTER);
		LinearLayout ll = (LinearLayout) findViewById(R.id.statistics_table);
		ll.addView(tv);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
