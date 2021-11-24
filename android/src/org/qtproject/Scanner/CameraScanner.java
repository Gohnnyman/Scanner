package org.qtproject.Scanner;


import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import android.util.Log;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import java.lang.reflect.Field;
import android.hardware.Camera;
import android.content.Context;

import java.io.IOException;


public class CameraScanner extends Activity {
        public static native void sendScanResult(String scanData, String scanDataType);
        public static native void log(String message);

        SurfaceView surfaceView;
        TextView txtBarcodeValue;
        private BarcodeDetector barcodeDetector;
        private CameraSource cameraSource;
        private static final int REQUEST_CAMERA_PERMISSION = 201;
        private static final String TAG = "CameraScanner";
        Button escButton;
        Button flashButton;
        String intentData = "";
        private Camera camera = null;
        boolean flashmode=false;
        int x_min = 145;
        int x_max = 925;
        int y_min = 750;
        int y_max = 1300;

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
           if (intent == null) {
               intent = new Intent();
           }
           super.startActivityForResult(intent, requestCode);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           Log.i(TAG, "started");
           setContentView(R.layout.second_activity);
           initViews();
        }

        private void initViews() {
           txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
           surfaceView = findViewById(R.id.surfaceView);
           escButton = findViewById(R.id.escButton);
           flashButton = findViewById(R.id.flashButton);


           flashButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                flashOnButton();
                }
               });

           escButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Log.i(TAG, "back button");
                   finish();
               }
           });
        }

        private void initialiseDetectorsAndSources() {

            Log.i(TAG, "init Detecors");
            barcodeDetector = new BarcodeDetector.Builder(this)
                   .setBarcodeFormats(Barcode.ALL_FORMATS)
                   .build();

            cameraSource = new CameraSource.Builder(this, barcodeDetector)
                   .setRequestedPreviewSize(1920, 1080)
                   .setAutoFocusEnabled(true)
                   .build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                   try {
                           cameraSource.start(surfaceView.getHolder());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                   cameraSource.stop();
                }
           });


           barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
               @Override
               public void release() {

               }

               @Override
               public void receiveDetections(Detector.Detections<Barcode> detections) {

                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {

                        int x0 = 0;
                        int y0 = 0;
                        int x2 = 0;
                        int y2 = 0;
                        intentData = "";

                        for (int i = 0; i < barcodes.size(); ++i) {
                            x0 = barcodes.valueAt(i).cornerPoints[0].x;
                            y0 = barcodes.valueAt(i).cornerPoints[0].y;
                            x2 = barcodes.valueAt(i).cornerPoints[2].x;
                            y2 = barcodes.valueAt(i).cornerPoints[2].y;

                            if((x0 > x_min) && (x2 < x_max) && (y0 > y_min) && (y2 < y_max)) {
                                Intent resultIntent = new Intent();

                                intentData = "" + barcodes.valueAt(i).displayValue;
                                intentData = intentData.replaceAll("\\P{Print}", "");
                                resultIntent.putExtra("scanData", intentData);
                                Log.i(TAG, "scanData: " + intentData);
                                intentData = "" + barcodes.valueAt(i).format;
                                resultIntent.putExtra("scanType", intentData);
                                Log.i(TAG, "scanType: " + intentData);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        }

                    }
               }
           });
        }


        private void flashOnButton() {
        camera=getCamera(cameraSource);
            if (camera != null) {
                try {
                    Camera.Parameters param = camera.getParameters();
                    param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(param);
                    flashmode = !flashmode;
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }


        private static Camera getCamera( CameraSource cameraSource) {
            Field[] declaredFields = CameraSource.class.getDeclaredFields();

            for (Field field : declaredFields) {
                if (field.getType() == Camera.class) {
                    field.setAccessible(true);
                    try {
                        Camera camera = (Camera) field.get(cameraSource);
                        if (camera != null) {
                            return camera;
                        }
                        return null;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPause() {
           super.onPause();
           cameraSource.release();
        }

        @Override
        protected void onResume() {
           super.onResume();
           initialiseDetectorsAndSources();
        }
}
