package com.binzi.aop.aspectj;

import android.text.TextUtils;
import android.util.Log;

import com.binzi.aop.utils.MemoryCacheManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

/**
 * @title:
 * @author: huangyoubin
 * @description: 根据MemoryCache注解自动添加缓存代理代码，通过aop切片的方式在编译期间织入源代码中
 * 功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。
 * @version:
 */
@Aspect
public class MemoryCacheAspect {
    @Pointcut("execution(@com.binzi.aop.aspectj.annotation.MemoryCache * *(..))")
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName);
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof String) {
                keyBuilder.append((String) obj);
            } else if (obj instanceof Class) {
                keyBuilder.append(((Class) obj).getSimpleName());
            }
        }
        String key = keyBuilder.toString();
        //key规则 ： 方法名＋参数1+参数2+...
        Object result = MemoryCacheManager.get(key);
        Log.d("MemoryCache", "key：" + key + "--->" + (result != null ? "not null" : "null"));
        //缓存已有，直接返回
        if (result != null) {
            return result;
        }
        //执行原方法
        result = joinPoint.proceed();
        if (result instanceof List && result != null && ((List) result).size() > 0
                || result instanceof String && !TextUtils.isEmpty((String) result)
                || result instanceof Object && result != null) {
            MemoryCacheManager.add(key, result);
        }
        Log.d("MemoryCache", "key：" + key + "--->" + "save");
        return result;
    }

}
