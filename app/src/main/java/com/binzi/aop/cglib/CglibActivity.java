package com.binzi.aop.cglib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.binzi.aop.R;

public class CglibActivity extends AppCompatActivity implements View.OnClickListener {


    private Printer printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cglib);
        printer = (Printer) new MyProxy(this).createProxyObj(Printer.class);
    }

    @Override
    public void onClick(View view) {
        printer.print();
    }
}
