package com.binzi.aop.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title:
 * @author: huangyoubin
 * @description:检查用户是否登陆注解，通过aop切片的方式在编译期间织入源代码中 检查用户是否登陆，未登录提示登录，不会执行下面的逻辑
 * @version:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CheckLogin {
}
