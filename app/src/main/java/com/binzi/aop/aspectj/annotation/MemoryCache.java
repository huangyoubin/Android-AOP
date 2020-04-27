package com.binzi.aop.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MemoryCache {
}
