package com.binzi.aop;

import android.app.Activity;
import android.os.Bundle;

import com.binzi.aop.aspectj.annotation.LogMethod;

public class LogMethodActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_method);
        init();
    }

    @LogMethod
    private void init() {

    }
}
