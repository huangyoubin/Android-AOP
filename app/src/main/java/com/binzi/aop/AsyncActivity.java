package com.binzi.aop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.binzi.aop.aspectj.annotation.Async;

public class AsyncActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        init();
    }
    @Async
    private void init(){
        StringBuilder sb = new StringBuilder();
        sb.append("current thread=").append(Thread.currentThread().getName());
        Log.d("AsyncActivity",sb.toString());
    }
}
