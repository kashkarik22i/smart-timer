package com.kashsoft.timer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public class MainActivity extends Activity implements RecognitionListener{
	private static final String TAG = "MainActivity";
	CubeTimer timer;
	int attempt;
	Cube scramble;
	ResultsView resultView;
	private static final String KWS_SEARCH_START = "start";
	private static final String KWS_SEARCH_STOP = "stop"; 
	private static final String KEYPHRASE_START = "start timer";
    private static final String KEYPHRASE_STOP = "stop timer";
	private SpeechRecognizer recognizer;
	private String speechMode;
	private boolean withSpeech;
	public static final String FAKE_KEY = "fake_key";
	private String[] fake;
	private MenuItem micro;
	
	private AsyncTask<Void, Void, Exception> loading; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//ResultHistory resHistory = new ResultHistory();
		//resHistory.removeDB(this);

		timer = new CubeTimer(this);
		
		final Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchTimer();
			}
		});
		
		attempt = 0;
		ListView lv = (ListView) findViewById(R.id.results);
		resultView = new ResultsView(new ArrayList<Result>());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_list_item_1, 
					resultView.getResultStringView());
		lv.setAdapter(adapter);
		((TextView) findViewById(R.id.time)).setKeepScreenOn(true); 
		updateScramble();
	}

	private void updateScramble(){
		scramble = new Cube();
		TextView scrambleView = (TextView) findViewById(R.id.scramble);
		scrambleView.setText(scramble.getRotationsString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu); 
		micro = menu.findItem(R.id.microphone);
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    	withSpeech = sharedPref.getBoolean(getString(R.string.speech_setting), false);
    	Log.i(TAG, "Loaded settings for speech");
    	if (withSpeech){
    		Log.i(TAG, "Speech is on");
    	} else {
    		Log.i(TAG, "Speech is off");
    	}
    	updateActionBarMicro();
		return true;
	}
	
	private void updateActionBarMicro(){
		Log.i(TAG, "updating microphone icon");
		if (withSpeech){
    		micro.setIcon(R.drawable.ic_action_mic_muted);
    		Log.i(TAG, "microphone on");
    	} else{
    		micro.setIcon(R.drawable.ic_action_mic);
    		Log.i(TAG, "microphone off");
    	}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.microphone:
	        	withSpeech = !withSpeech;
	        	updateActionBarMicro();
	        	return true;
	        case R.id.browse_results:
	        	Intent i = new Intent(this, BrowseResultsActivity.class);
	        	i.putExtra(FAKE_KEY, fake);
	        	startActivity(i);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setTime(long time){
		final TextView timeDisplay = (TextView) findViewById(R.id.time);
		timeDisplay.setText(ResultsView.resultString(time));
	}
	
	public void postResult(long time){
		resultView.addResult(new Result(scramble, attempt, time));
	}
    
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        String text = hypothesis.getHypstr();
        if ((text.equals(KEYPHRASE_START)) && (speechMode.equals(KWS_SEARCH_START))){
        	switchTimer();
        } else if ((text.equals(KEYPHRASE_STOP)) && (speechMode.equals(KWS_SEARCH_STOP))){
        	switchTimer();
        } else{
            Log.i(TAG, String.format("Recognized something different: %s", text));
        }
    }
	
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //TODO need to have a closer look at what needs to be done where
        }
        
    }

    private void switchTimer(){
    	if (timer.started){
    		stopTimer();
    		updateScramble();
    	} else{
    		startTimer();
    	}
    }
    
    private void startTimer(){
    	attempt += 1;
    	timer.set();
    	updateButton();
    	switchSpeechMode(KWS_SEARCH_STOP);
    }
    
    private void stopTimer(){
		timer.stop();
		updateButton();
		postResult(timer.getTime());
		switchSpeechMode(KWS_SEARCH_START);
    }
    
    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
        recognizer.stop();
        recognizer.startListening(recognizer.getSearchName());
    } 
    
    private void setupRecognizer(File assetsDir) {
        File modelsDir = new File(assetsDir, "models");
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-10f)
                .getRecognizer();
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(KWS_SEARCH_START, KEYPHRASE_START);
        recognizer.addKeyphraseSearch(KWS_SEARCH_STOP, KEYPHRASE_STOP);
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	if (recognizer != null){
    		recognizer.cancel();
    	}
    	loading.cancel(true);
    }
    
    protected void onPause(){
    	super.onPause();
    	if (recognizer != null){ 
    		recognizer.cancel();
    	}
    	loading.cancel(true);
    	if (timer.started){
    		timer.toSleepMode();
    	}
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.speech_setting), withSpeech);
    	editor.commit();
    	saveResults();
    }
    
    protected void onResume(){
    	super.onResume();
    	final Button startButton = (Button) findViewById(R.id.startButton);
    	startButton.setBackgroundColor(getResources().getColor(R.color.button_wait));
    	startButton.setText(getResources().getString(R.string.wait_string));
    	
    	loading = new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    //TODO need some way to show that speech recognition could not start
                } else {
                	if (speechMode == null){
                		speechMode = KWS_SEARCH_START;
                	}
                	recognizer.startListening(speechMode);
                	updateButton();
                	Log.d(TAG, "Recognizer setup finished");
                }
            }
        };
    	loading.execute();
    	if (timer.started){
    		timer.wakeUp();
    	}
    }
    
    private void updateButton(){
    	final Button startButton = (Button) findViewById(R.id.startButton);
    	if (timer.started){
    		startButton.setBackgroundColor(getResources().getColor(R.color.button_go));
    		startButton.setText(getResources().getString(R.string.stop_string));
    	} else{
    		startButton.setBackgroundColor(getResources().getColor(R.color.button_ready));
    		startButton.setText(getResources().getString(R.string.start_string));
    	}
    }
    
    private void switchSpeechMode(String mode){
    	speechMode = mode;
    	//if (!loading.isCancelled()) {
    	if (loading.getStatus() != AsyncTask.Status.FINISHED) {
    		Log.w(TAG, "trying to switch speech mode when recognizer is loading");
    		return;
    	}
    	recognizer.stop();
    	recognizer.startListening(mode);
    }
    
    private void saveResults(){
    	Log.i(TAG, "Saving results to db");
    	ResultHistory resHistory = new ResultHistory();
    	resHistory.writeResults(this, resultView.getView());
    }
}
