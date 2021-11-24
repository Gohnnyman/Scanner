package org.qtproject.Scanner;

import org.qtproject.qt5.android.QtNative;

import android.os.Build;
import android.util.Log;
import android.content.Context;
import java.lang.String;
import android.widget.Toast;
import android.app.Activity;




public class Android {
    private static final String TAG = "Android";

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




