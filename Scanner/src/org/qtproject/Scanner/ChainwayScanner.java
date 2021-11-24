package org.qtproject.Scanner;

import org.qtproject.qt5.android.QtNative;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build;
import android.net.Uri;
import android.util.Log;

import com.scanner.utility.ScannerUtility;


class ChainwayScannerBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "ScannerBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String scanData = bundle.getString("barcodeStringData");
            String scanDataType = bundle.getString("barcodeNAME");
            if(scanDataType.equals("QR")) scanDataType = "QRCODE";
            if(scanDataType.equals("UPCE0")) scanDataType = "UPCE";
            ChainwayScanner.sendScanResult(scanData, scanDataType);
        }
}



public class ChainwayScanner {
    public static native void sendScanResult(String scanData, String scanDataType);
    public static native void log(String message);
    private static final int LEFT_SCAN_BUTTON = 291;
    private static final int RIGHT_SCAN_BUTTON = 293;

    private static final int OUTPUT_MODE_TO_CURSOR = 0;
    private static final int OUTPUT_MODE_CLIPBOARD = 1;
    private static final int OUTPUT_MODE_BROADCAST = 2;
    private static final int OUTPUT_MODE_ANALOG_KEYBOARD = 3;


    private static final String TAG = "ChainwayScanner";

    private Context context;
    private ScannerUtility scanner;


    public void init() {
        Log.e(TAG, "STARTED");
        context = QtNative.activity().getApplicationContext();
        scanner = ScannerUtility.getScannerInerface();

        BroadcastReceiver br = new ChainwayScannerBroadcastReceiver();
        IntentFilter filter = new IntentFilter("org.qtproject.dataScanned");
        context.registerReceiver(br, filter);


        scanner.setOutputMode(context, OUTPUT_MODE_BROADCAST);
        scanner.setContinuousScan(context, false);
        scanner.enablePlaySuccessSound(context, false);
        scanner.setScanResultBroadcast(context, "org.qtproject.dataScanned", "");
        scanner.setReleaseScan(context, true);
        scanner.setScanKey(context, 0, new int[] {LEFT_SCAN_BUTTON, RIGHT_SCAN_BUTTON});
        scanner.setBarcodeEncodingFormat(context, scanner.FORMAT_DEFAULT);
        scanner.enableTAB(context, false);
        scanner.enableEnter(context, false);
    }


    public void activateScanner() {
        scanner.enableFunction(context, scanner.FUNCTION_1D);
        scanner.enableFunction(context, scanner.FUNCTION_2D_H);
        scanner.enableFunction(context, scanner.FUNCTION_2D);
        scanner.enableFunction(context, scanner.FUNCTION_UHF);
    }


    public void deActivateScanner() {
        scanner.disableFunction(context, scanner.FUNCTION_1D);
        scanner.disableFunction(context, scanner.FUNCTION_2D_H);
        scanner.disableFunction(context, scanner.FUNCTION_2D);
        scanner.disableFunction(context, scanner.FUNCTION_UHF);
    }
}
