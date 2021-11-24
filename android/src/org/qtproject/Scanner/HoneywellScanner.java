package org.qtproject.Java;

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
import java.util.HashMap;


import com.honeywell.aidc.*;

import android.widget.Toast;



class HoneywellScannerListener implements BarcodeReader.BarcodeListener {
        private static final String TAG = "HoneywellScannerListener";

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        String scanData = event.getBarcodeData();
        String scanDataType = event.getAimId();

        // Converting barcode type from AIM to human readable
        // https://support.honeywellaidc.com/s/article/Where-to-find-the-AIM-Symbology-ID-Table-reference-for-Honeywell-Mobility-SDK-Android
        switch(scanDataType.substring(0, 2)) {
            case "]C":
                scanDataType = "CODE128";
                break;
            case "]A":
                scanDataType = "CODE39";
                break;
            case "]G":
                scanDataType = "CODE93";
                break;
            case "]F":
                scanDataType = "CODABAR";
                break;
            case "]d":
                scanDataType = "DATAMATRIX";
                break;
            case "]E":
                if(scanDataType.substring(2, 3).equals("4")) scanDataType = "EAN8"; else
                if(scanData.length() == 8) scanDataType = "UPCE"; else
                if(scanData.length() == 12) scanDataType = "UPCA"; else
                scanDataType = "EAN13";
                break;
            case "]Q":
                scanDataType = "QRCODE";
                break;
            case "]z":
                scanDataType = "AZTEC";
                break;
            case "]L":
                scanDataType = "PDF417";
                break;
        }
        HoneywellScanner.sendScanResult(scanData, scanDataType);
    }

    @Override
    public void onFailureEvent(final BarcodeFailureEvent event) { }
}



public class HoneywellScanner {
    public static native void sendScanResult(String scanData, String scanDataType);
    public static native void log(String message);

    private static final String TAG = "HoneywellScanner";
    private Context context;

    private AidcManager manager;
    private BarcodeReader reader;


    public void init() {
        context = QtNative.activity().getApplicationContext();

        AidcManager.create(context, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {

            try {
                manager = aidcManager;
                reader = manager.createBarcodeReader();

                reader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);

                reader.setProperty(BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED, true);
                reader.setProperty(BarcodeReader.PROPERTY_EAN_8_CHECK_DIGIT_TRANSMIT_ENABLED, true);
                reader.setProperty(BarcodeReader.PROPERTY_UPC_A_CHECK_DIGIT_TRANSMIT_ENABLED, true);
                reader.setProperty(BarcodeReader.PROPERTY_UPC_E_CHECK_DIGIT_TRANSMIT_ENABLED, true);


            } catch (AidcException e) {
                Log.e(TAG, "Scanner init failed");
            }
                reader.addBarcodeListener(new HoneywellScannerListener());
            }
        });
    }


    public void activateScanner() {
        if (reader != null) {
            try {
                reader.claim();
            } catch (AidcException e) {
                Log.e(TAG, "Scanner activation error");
            }
        }
        Log.w(TAG, "Scanner claimed");
    }


    public void deActivateScanner() {
        reader.release();
        Log.w(TAG, "Scanner released");
    }
}

