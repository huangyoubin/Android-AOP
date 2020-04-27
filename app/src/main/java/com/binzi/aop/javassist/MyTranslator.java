package com.binzi.aop.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
public class MyTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }
    /**
     * 类装载到JVM前进行代码织入
     */
    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        //通过获取类文件
        try {
            CtClass cc = pool.get(classname);
            //获得指定方法名的方法
            CtMethod m = cc.getDeclaredMethod("doSomeThing1");
            //在方法执行前插入代码
            m.insertBefore("{ Log.d(TAG, \"执行前\"); }");
        } catch (NotFoundException e) {

        } catch (CannotCompileException e) {

        }
    }
}
