package com.binzi.plugin;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-02-26 18:51
 */
public class AOPConfig {

    // 排除不需要注入的类（只根据简单类名判断）
    public final
    static List<String> noInjectClasses = ['BuildConfig.class', 'R.class', 'Patch.class']
    // 如果类名包含以下关键字，不注入
    public final
    static List<String> noInjectKeyword = ['R$', 'android\\support\\', 'android.support.', "\$Patch.class"]

    //应用包名，依据这个来切割路径,获取一个类的完整类名
    public final static def appPackageName = 'com.binzi.aop'

    // 需要添加这两个jar包，否者javassist会报异常，获取到的compileSdkVersion一直为null，干脆写死了，自行更改
    public final static def androidJar = "/Users/huangyoubin/Library/Android/sdk/platforms/android-28/android.jar"
}
