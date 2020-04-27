package com.binzi.aop.aspectj;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
@Aspect
public class LogMethodAspect {
    private static final String TAG = "LogMethodAspect";

    @Pointcut("execution(@com.binzi.aop.aspectj.annotation.LogMethod * *(..))")
    public void onLogMethod() {
    }

    @Around("onLogMethod()")
    public Object doLogMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint);
    }

    private Object logMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, joinPoint.getSignature().toShortString() + " Args : " + (joinPoint.getArgs() != null ? Arrays.deepToString(joinPoint.getArgs()) : ""));
        Object result = joinPoint.proceed();
        String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();
        Log.d(TAG, joinPoint.getSignature().toShortString() + " Result : " + ("void".equalsIgnoreCase(type) ? "void" : result));
        return result;
    }
}
