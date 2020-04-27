package com.binzi.aop.patch;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Author: huangyoubin
 * @Description:
 */
public class FileUtil {
    public final static String PATCH_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator+"patch_dex.jar";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void copyJarToFile(Activity activity) {
        try {
            verifyStoragePermissions(activity);
            File file = new File(PATCH_PATH);
            if (file.exists()) {
                return;
            }
            //取得资源文件的输入流
            InputStream inputStream = activity.getAssets().open("patch_dex.jar");
            file.createNewFile(); //创建"/mnt/sdcard/testdex.jar" 文件
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
