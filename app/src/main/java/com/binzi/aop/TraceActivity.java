package com.binzi.aop;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;

import com.binzi.aop.aspectj.annotation.Trace;

public class TraceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        init();
    }

    @Trace
    private void init() {
        for (int i = 0; i < 100; i++) {
            SystemClock.sleep(1);
        }
    }
}
