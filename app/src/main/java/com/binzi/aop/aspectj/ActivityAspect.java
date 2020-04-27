package com.binzi.aop.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
@Aspect
public class ActivityAspect {
    private static final String TAG = "ActivityAspect";

    @Pointcut("execution(* android.app.Activity.on**(..))")
    public void lifeCycle() {

    }
//
//    @Before("lifeCycle()")
//    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
//        String key = joinPoint.getSignature().toString();
//        Log.d(TAG, "onActivityMethodBefore: " + key);
//    }
//
//    @After("lifeCycle()")
//    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
//        String key = joinPoint.getSignature().toString();
//        Log.d(TAG, "onActivityMethodAfter: " + key);
//    }

    @Around("execution(* com.binzi.aop.MainActivity.testAOP())")
    public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String key = proceedingJoinPoint.getSignature().toString();
        Log.d(TAG, "onActivityMethodAroundFirst: " + key);
        proceedingJoinPoint.proceed();
        Log.d(TAG, "onActivityMethodAroundSecond: " + key);
    }
}
