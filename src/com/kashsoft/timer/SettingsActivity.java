package com.kashsoft.timer;

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	public static final String TAG = "SettingActivity";
	private boolean inspectionOn;
	public static final boolean defaultInspectionOn = false;
	public static final String INSPECTION_ON_KEY = "inspection_on_key";
	private int inspectionTime;
	public static final int defaultInspectionTime = 15;
	public static final String INSPECTION_TIME_KEY = "inspection_time_key";
	TextView inspectionTimeView;
	Switch inspectionSwitch;
	SeekBar inspectionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		inspectionBar = (SeekBar) findViewById(R.id.inspection_bar);
		inspectionTimeView = (TextView) findViewById(R.id.inspection_text);
		
		inspectionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) updateInspection(progress + 1);
			}
		});
		
		inspectionSwitch = (Switch) findViewById(R.id.inspection_switch);
		inspectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					inspectionTimeView.setVisibility(View.VISIBLE);
					inspectionBar.setVisibility(View.VISIBLE);
					inspectionOn = true;
				} else {
					inspectionTimeView.setVisibility(View.GONE);
					inspectionBar.setVisibility(View.GONE);
					inspectionOn = false;
				}
			}
		});
		
		updateFromPreferences();
	}

	private void updateFromPreferences() {
		SharedPreferences commonPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		inspectionOn = commonPrefs.getBoolean(INSPECTION_ON_KEY, defaultInspectionOn);
		inspectionSwitch.setChecked(inspectionOn);
		inspectionTime = commonPrefs.getInt(INSPECTION_TIME_KEY, defaultInspectionTime);
		updateInspection(inspectionTime);
	}
	
	private void savePreferences() {
		Log.i(TAG, String.format(Locale.getDefault(),
				"Saving shared preferences: inspection is %s, its length is %d",
				Boolean.toString(inspectionOn), inspectionTime));
		SharedPreferences commonPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = commonPrefs.edit();
		editor.putBoolean(INSPECTION_ON_KEY, inspectionOn);
		editor.putInt(INSPECTION_TIME_KEY, inspectionTime);
		editor.commit();
	}
	
	private void updateInspection(int i) {
		inspectionTimeView.setText(Integer.toString(i));
		inspectionBar.setProgress(i - 1);
		inspectionTime = i;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		savePreferences();
	}
}
