package com.binzi.aop.javassist;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-03-12 21:57
 */
public class Modify {

    public static void modifyClass() throws Exception {
        // 获取要修改的类
        Class<?> clazz = Class.forName("com.binzi.aop.proxy.Business");

        // 实例化类型池对象
        ClassPool classPool = ClassPool.getDefault();
        // 设置类搜索路径
        classPool.appendClassPath(new ClassClassPath(clazz));
        // 从类型池中读取指定类型
        CtClass ctClass = classPool.get(clazz.getName());

        // 获取String类型参数集合
        // CtClass[] paramTypes = {classPool.get(String.class.getName())};
        // 获取指定方法名称
        CtMethod method = ctClass.getDeclaredMethod("doSomeThing1", null);
        // 赋值方法到新方法中
        CtMethod newMethod = CtNewMethod.copy(method, ctClass, null);
        // 修改源方法名称
        String oldName = method.getName() + "$Impl";
        method.setName(oldName);

        // 修改原方法
        newMethod.setBody("Log.d(TAG, \"执行前\");" + oldName + "($$);Log.d(TAG, \"执行后\");");
        // 将新方法添加到类中
        ctClass.addMethod(newMethod);

        // 加载重新编译的类
        clazz = ctClass.toClass();      // 注意，这一行会将类冻结，无法在对字节码进行编辑
        ctClass.defrost();  // 解冻一个类，对应freeze方法
        // 执行方法
        clazz.getMethod("show", String.class).invoke(clazz.newInstance(), "hello");

    }
}
