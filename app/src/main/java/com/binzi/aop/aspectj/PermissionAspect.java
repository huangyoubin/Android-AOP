package com.binzi.aop.aspectj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.binzi.aop.App;
import com.binzi.aop.aspectj.annotation.Permission;
import com.binzi.aop.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-03-11 22:06
 */
public class PermissionAspect {
    @Pointcut("execution(@com.binzi.aop.aspectj.annotation.Permission * *(..))")
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        final Permission permission = method.getAnnotation(Permission.class);
        final Activity ac = App.getAppContext().getCurActivity();
        new AlertDialog.Builder(ac)
                .setTitle("提示")
                .setMessage("为了应用可以正常使用，请您点击确认申请权限。")
                .setNegativeButton("取消", null)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.requestPermissionsResult(ac, 1, permission.value()
                                , new PermissionUtils.OnPermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        try {
                                            joinPoint.proceed();//获得权限，执行原方法
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onPermissionDenied() {
                                        PermissionUtils.showTipsDialog(ac);
                                    }
                                });
                    }
                })
                .create()
                .show();


    }
}
