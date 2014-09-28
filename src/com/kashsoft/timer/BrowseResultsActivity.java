package com.kashsoft.timer;

import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
			for (Result r: listOfResults){
				TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.result_table_row, null,false);
				TextView tv = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null,false);
				tv.setText(ResultsView.resultString(r.getResult()));
				tr.addView(tv);
				table.addView(tr);
			}
		}
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
