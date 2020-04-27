package com.binzi.aop.javassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.binzi.aop.R;
import com.binzi.aop.proxy.Business;

import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class JavassistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_javassist);
    }

    private void proxy() throws Exception {
        // 实例化代理类工厂
        ProxyFactory factory = new ProxyFactory();

        //设置父类，ProxyFactory将会动态生成一个类，继承该父类
        factory.setSuperclass(Business.class);

        //设置过滤器，判断哪些方法调用需要被拦截
        factory.setFilter(new MethodFilter() {
            @Override
            public boolean isHandled(Method m) {
                return m.getName().startsWith("get");
            }
        });

        Class<?> clazz = factory.createClass();
        Business proxy = (Business) clazz.newInstance();
        ((ProxyObject) proxy).setHandler(new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                //拦截后前置处理，改写name属性的内容
                //实际情况可根据需求修改
                System.out.println(thisMethod.getName() + "被调用");
                try {
                    Object ret = proceed.invoke(self, args);
                    System.out.println("返回值: " + ret);
                    return ret;
                } finally {
                    System.out.println(thisMethod.getName() + "调用完毕");
                }
            }
        });

        proxy.doSomeThing1();
        proxy.doSomeThing2();
    }

    /**
     * 自定义类加载器
     */
    private void customClassLoader(){
        //获取存放CtClass的容器ClassPool
        ClassPool cp = ClassPool.getDefault();
        //创建一个类加载器
        Loader cl = new Loader();
        //增加一个转换器
        try {
            cl.addTranslator(cp, new MyTranslator());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }


}
