package org.qtproject.Scanner;

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


public class ZebraScanner implements EMDKListener, DataListener, StatusListener, ScannerConnectionListener {

    public static native void sendScanResult(String scanData, String scanDataType);
    public static native void log(String message);

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    private List<ScannerInfo> deviceList = null;
    private int scannerIndex = 0;

    private final Object lock = new Object();

    public void init() {
        deviceList = new ArrayList<ScannerInfo>();
        EMDKResults results = EMDKManager.getEMDKManager(QtNative.activity().getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            logError("EMDKManager object request failed!");
            return;
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        initBarcodeManager();
        enumerateScannerDevices();
    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
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
        ScannerStates state = statusData.getState();
        String statusString;
        switch(state) {
            case IDLE:
                statusString = statusData.getFriendlyName() + " is enabled and idle...";

                scanner.triggerType = TriggerType.HARD;
                setDecoders();
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
                break;
            case SCANNING:
                statusString = "Scanning...";
                break;
            case DISABLED:
                statusString = statusData.getFriendlyName() + " is disabled.";
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
        barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
        if (barcodeManager != null) {
            barcodeManager.addConnectionListener(this);
        }
    }

    private void enumerateScannerDevices() {
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

                    if (scnInfo.getDeviceIdentifier() == BarcodeManager.DeviceIdentifier.INTERNAL_IMAGER1) {
                        scannerIndex = spinnerIndex;
                        deInitScanner();
                        initScanner();
                    }
                    ++spinnerIndex;
                }

            }
            else {
                logError("Failed to get the list of supported scanner devices! Please close and restart the application.");
            }
        }
    }

    public void deInitScanner() {
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
        log("Log Java: " + status);
    }

    private static void logError(final String status) {
        log("!!!!!!!! Error Java: " + status);
    }
}
