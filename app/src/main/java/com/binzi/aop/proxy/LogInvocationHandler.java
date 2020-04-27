package com.binzi.aop.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
public class LogInvocationHandler implements InvocationHandler {
    private final String TAG = "LogInvocationHandler";
    private Object target; //目标对象

    public LogInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //执行原有逻辑
        Object rev = method.invoke(target, args);
        //执行织入的日志，你可以控制哪些方法执行切入逻辑
        if (method.getName().equals("doSomeThing2")) {
            Log.d(TAG, "记录日志");
        }
        return rev;
    }
}
