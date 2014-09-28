package com.kashsoft.timer;

import android.os.Bundle;

public class SimpleViewServerActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewServer.get(this).addWindow(this);
    }
 
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

}
