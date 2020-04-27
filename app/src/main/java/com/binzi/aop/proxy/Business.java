package com.binzi.aop.proxy;


import android.util.Log;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-03-12 14:16
 */
public class Business implements IBusiness1, IBusiness2 {
    private final String TAG = "Business";

    @Override
    public void doSomeThing1() {
        Log.d(TAG, "执行业务逻辑1");
    }

    @Override
    public void doSomeThing2() {
        Log.d(TAG, "执行业务逻辑2");
    }

}
