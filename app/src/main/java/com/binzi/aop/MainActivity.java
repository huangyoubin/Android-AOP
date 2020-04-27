package com.binzi.aop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.binzi.aop.utils.Cost;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Cost
    public void testAOP() {
        Log.d("MainActivity", "test");
    }

}

