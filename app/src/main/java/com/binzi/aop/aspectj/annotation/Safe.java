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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Safe {
}
