package com.binzi.aop.patch;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-02-26 19:00
 */
public interface Patch {
    public Object dispatchMethod(Object host, String methodSign, Object[] params);
}
