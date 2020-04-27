package com.binzi.aop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 */
public class App extends Application {
    private static App mApp;
    public Stack<Activity> store;

    @Override
    public void onCreate() {
        super.onCreate();
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new MyActivityLifecycle());
    }

    public static App getAppContext() {
        return mApp;
    }


    private class MyActivityLifecycle implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }
}
