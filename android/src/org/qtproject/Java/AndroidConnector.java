package org.qtproject.Scanner;

import org.qtproject.qt5.android.QtNative;

import android.os.Build;
import android.util.Log;
import android.content.Context;
import java.lang.String;
import android.widget.Toast;
import android.app.Activity;
import android.Manifest;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;




public class AndroidConnector {
    private static final String TAG = "AndroidConnector";

    public static void chackPermissions()
    {
        if (ContextCompat.checkSelfPermission(
                 QtNative.activity().getApplicationContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(QtNative.activity(), new String[] {Manifest.permission.CAMERA}, 0);
        }
    }

    public static String getSerial() {
        try {
                return android.os.Build.getSerial();
        }
        catch(SecurityException se) {
                return "unknown";
        }
    }

    public static void toastMessage(String text) {

        class OneShotTask implements Runnable {
            String text;
            OneShotTask(String s) { text = s; }
            public void run() {
                Toast.makeText(QtNative.activity().getBaseContext(), text, Toast.LENGTH_SHORT).show();
            }
        }

        OneShotTask obj = new OneShotTask(text);
        QtNative.activity().runOnUiThread(obj);
    }


    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
            return model;

        return manufacturer + " " + model;
    }

}




