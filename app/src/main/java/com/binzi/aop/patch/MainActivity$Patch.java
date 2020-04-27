package com.binzi.aop.patch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.binzi.aop.MainActivity;
import com.binzi.aop.R;

/**
 * @Author: huangyoubin
 * @Description:
 */
public class MainActivity$Patch extends Activity implements Patch{

    @Override
    public Object dispatchMethod(Object host, String methodSign, Object[] params) {
        MainActivity mainActivity = (MainActivity) host;
        switch (methodSign.hashCode()) {
            case -641568046:
                onCreate(mainActivity, (Bundle) (params[0]));
                break;
            case -340027132:
                show(mainActivity);
                break;
        }

        return null;
    }

    protected void onCreate(final MainActivity host, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(host);
            }
        });

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PatchLoader.getInstance().loadPatch(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + "/patch.dex");
                    Toast.makeText(host, "load success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void show(MainActivity host) {
        Toast.makeText(host, "我是补丁，没有bug啦！", Toast.LENGTH_SHORT).show();
    }
}