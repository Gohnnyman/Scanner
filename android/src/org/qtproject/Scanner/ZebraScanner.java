package org.qtproject.Java;

import org.qtproject.qt5.android.QtNative;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.StatusData.ScannerStates;
import com.symbol.emdk.barcode.StatusData;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.pm.ActivityInfo;
import java.util.*;

import android.util.Log;


public class ZebraScanner /*extends Activity */implements EMDKListener, DataListener, StatusListener, ScannerConnectionListener {

    public static native void sendScanResult(String scanData, String scanDataType);
    public static native void log(String message);

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    private List<ScannerInfo> deviceList = null;
    private int scannerIndex = 0;

    private final Object lock = new Object();

    public void init() {
//        log("------------------------------------- init ------------------------------------------");
        deviceList = new ArrayList<ScannerInfo>();
        EMDKResults results = EMDKManager.getEMDKManager(QtNative.activity().getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            logError("EMDKManager object request failed!");
            return;
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
//        log("------------------------------------- onOpened ------------------------------------------");
        this.emdkManager = emdkManager;
        initBarcodeManager();
        enumerateScannerDevices();
    }

    @Override
    public void onClosed() {
//        log("------------------------------------- onClosed ------------------------------------------");
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        logInfo("------------------------------------- onData ------------------------------------------");
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList <ScanData> scanData = scanDataCollection.getScanData();
            for(ScanData data: scanData) {
                logInfo("scanDataType: " + data.getLabelType() + ", scanData: \"" + data.getData() + "\"");
                String _scanDataType = data.getLabelType().toString();
                String _scanData = data.getData().toString();
                sendScanResult(_scanData, _scanDataType);
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {
//        log("------------------------------------- onStatus ------------------------------------------");
        ScannerStates state = statusData.getState();
        String statusString;
        switch(state) {
            case IDLE:
                statusString = statusData.getFriendlyName()+" is enabled and idle...";
//                log(statusString);
                // set trigger type
//                    scanner.triggerType = TriggerType.SOFT_ONCE;
                scanner.triggerType = TriggerType.HARD;
                // set decoders
                setDecoders(); // todo move into init scanner
                // submit read
                if(!scanner.isReadPending()) {
                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        logError(e.getMessage());
                    }
                }
                break;
            case WAITING:
                statusString = "Scanner is waiting for trigger press...";
//                log(statusString);
                break;
            case SCANNING:
                statusString = "Scanning...";
//                log(statusString);
                break;
            case DISABLED:
                statusString = statusData.getFriendlyName()+" is disabled.";
//                log(statusString);
                break;
            case ERROR:
                statusString = "An error has occurred.";
                logError(statusString);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
//        log("------------------------------------- onConnectionChange ------------------------------------------");
        switch(connectionState) {
        case CONNECTED:
            synchronized (lock) {
                initScanner();
            }
            break;
        case DISCONNECTED:
            synchronized (lock) {
                deInitScanner();
            }
            break;
        }
    }

    public void initScanner() {
//        log("------------------------------------- initScanner ------------------------------------------");
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                if (barcodeManager != null)
                    scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
            }
            else {
                logError("Failed to get the specified scanner device! Please close and restart the application.");
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
//                    log("------ scanner enabled -----------");
                } catch (ScannerException e) {
                    logError(e.getMessage());
                    deInitScanner();
                }
            } else {
                logError("Failed to initialize the scanner device.");
            }
        }
    }

    public void activateScanner() {
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                if (barcodeManager != null)
                    scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
            }
            else {
                logError("Failed to get the specified scanner device! Please close and restart the application.");
                return;
            }
        }
        try {
            if (scanner != null && !scanner.isEnabled()) {
                scanner.enable();
            }
        } catch (ScannerException e) {
            logError(e.getMessage());
            deInitScanner();
            initScanner();
        }
    }

    public void deActivateScanner() {
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                if (barcodeManager != null)
                    scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
            }
            else {
                logError("Failed to get the specified scanner device! Please close and restart the application.");
                return;
            }
        }
        try {
            if (scanner != null && scanner.isEnabled()) {
                scanner.disable();
            }
        } catch (ScannerException e) {
            logError(e.getMessage());
            deInitScanner();
        }
    }

    private void setDecoders() {
//        log("------------------------------------- setDecoders ------------------------------------------");
        if (scanner != null) {
            try {
                ScannerConfig config = scanner.getConfig();
                config.decoderParams.ean8.enabled = true;
                config.decoderParams.ean13.enabled = true;
                config.decoderParams.code39.enabled = true;
                config.decoderParams.code128.enabled = true;
                config.decoderParams.code11.enabled = true;
                config.decoderParams.code39.enabled = true;
                config.decoderParams.qrCode.enabled = true;
                config.decoderParams.upca.enabled = true;
                config.decoderParams.upce0.enabled = true;
                config.decoderParams.upce1.enabled = true;
                scanner.setConfig(config);
            } catch (ScannerException e) {
                logError(e.getMessage());
            }
        }
    }

    private void initBarcodeManager() {
//        log("------------------------------------- initBarcodeManager ------------------------------------------");
        barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
        // Add connection listener
        if (barcodeManager != null) {
            barcodeManager.addConnectionListener(this);
        }
    }

    private void enumerateScannerDevices() {
//        log("------------------------------------- enumerateScannerDevices ------------------------------------------");
        if (barcodeManager != null) {
            List<String> friendlyNameList = new ArrayList<String>();
            int spinnerIndex = 0;
            deviceList = barcodeManager.getSupportedDevicesInfo();
            if ((deviceList != null) && (deviceList.size() != 0)) {
                Iterator<ScannerInfo> it = deviceList.iterator();
                while(it.hasNext()) {
                    ScannerInfo scnInfo = it.next();
                    friendlyNameList.add(scnInfo.getFriendlyName());

                    String name = scnInfo.getFriendlyName();
                    String modelNumber = scnInfo.getModelNumber();
                    int deviceType = scnInfo.getDeviceType().ordinal();
                    int connectionType = scnInfo.getConnectionType().ordinal();
                    int decoderType = scnInfo.getDecoderType().ordinal();
                    int deviceIdentifier = scnInfo.getDeviceIdentifier().ordinal();

                    String message = "name: " + name + ", deviceType: " + String.valueOf(deviceType) +
                                     ", connectionType: " + String.valueOf(connectionType) +
                                     ", decoderType: " + String.valueOf(decoderType) +
                                     ", deviceIdentifier: " + String.valueOf(deviceIdentifier);
//                    log(message);
                    if (scnInfo.getDeviceIdentifier() == BarcodeManager.DeviceIdentifier.INTERNAL_IMAGER1) {
                        scannerIndex = spinnerIndex;
//                        log("-------->" + String.valueOf(spinnerIndex));
                        deInitScanner();
                        initScanner();
                    }
                    ++spinnerIndex;
                }
            /*
            V         : Log Java: name: Camera Scanner, deviceType: 0, connectionType: 0, decoderType: 1, deviceIdentifier: 1
            V         : Log Java: name: 2D Barcode Imager, deviceType: 1, connectionType: 0, decoderType: 1, deviceIdentifier: 2
            V         : Log Java: -------->1
            V         : Log Java: name: Bluetooth Scanner, deviceType: 1, connectionType: 1, decoderType: 1, deviceIdentifier: 4
            V         : Log Java: name: RS6000 Bluetooth Scanner, deviceType: 1, connectionType: 1, decoderType: 1, deviceIdentifier: 6
            V         : Log Java: name: DS3678 Bluetooth Scanner, deviceType: 1, connectionType: 1, decoderType: 1, deviceIdentifier: 8
            V         : Log Java: name: LI3678 Bluetooth Scanner, deviceType: 2, connectionType: 1, decoderType: 0, deviceIdentifier: 10
            V         : Log Java: name: DS2278 Bluetooth Scanner, deviceType: 1, connectionType: 1, decoderType: 1, deviceIdentifier: 12
            V         : Log Java: name: DS8178 Bluetooth Scanner, deviceType: 1, connectionType: 0, decoderType: 1, deviceIdentifier: 13
            */

            }
            else {
                logError("Failed to get the list of supported scanner devices! Please close and restart the application.");
            }
        }
    }

    public void deInitScanner() {
//        log("------------------------------------- deInitScanner ------------------------------------------");
        if (scanner != null) {
            try {
                scanner.disable();
            } catch (Exception e) {
                logError(e.getMessage());
            }

            try {
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
            } catch (Exception e) {
                logError(e.getMessage());
            }

            try{
                scanner.release();
            } catch (Exception e) {
                logError(e.getMessage());
            }
            scanner = null;
        }
    }

    private static void logInfo(final String status) {
//        Log.v(null, "Log Java: " + status);
        log("Log Java: " + status);
    }

    private static void logError(final String status) {
//        Log.v(null, "!!!!!!!! Error Java: " + status);
        log("!!!!!!!! Error Java: " + status);
    }
}
