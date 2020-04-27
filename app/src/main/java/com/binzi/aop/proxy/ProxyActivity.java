package com.binzi.aop.proxy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.binzi.aop.R;

import java.lang.reflect.Proxy;

public class ProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        proxy();
    }

    private void proxy() {
        Business business = new Business();
        Business businessProxy = (Business) Proxy.newProxyInstance(getClassLoader(),
                new Class[]{IBusiness1.class, IBusiness2.class}, new LogInvocationHandler(business));
        businessProxy.doSomeThing1();
    }
}
