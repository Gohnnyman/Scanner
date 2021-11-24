#include "cameraScanner.h"
#include <QDebug>


// full definition of supportedd barcode formats -
// https://developers.google.com/android/reference/com/google/android/gms/vision/barcode/Barcode#UPC_A

CameraScanner::CameraScanner(QObject *parent) : ScannerInterface(parent)
{

}

CameraScanner::~CameraScanner()
{

}


QString CameraScanner::decodeScanDataType(QString typeCode)
{
    QString scanDataType = "";

    switch (typeCode.toInt()) {
    case BARCODES::CODE128 :
        scanDataType = "CODE128";
        break;
    case BARCODES::CODE39  :
        scanDataType = "CODE39";
        break;
    case BARCODES::CODE93  :
        scanDataType = "CODE93";
        break;
    case BARCODES::CODABAR  :
        scanDataType = "CODABAR";
        break;
    case BARCODES::DATAMATRIX  :
        scanDataType = "DATAMATRIX";
        break;
    case BARCODES::EAN13  :
        scanDataType = "EAN13";
        break;
    case BARCODES::EAN8  :
        scanDataType = "EAN8";
        break;
    case BARCODES::ITF  :
        scanDataType = "ITF";
        break;
    case BARCODES::QRCODE  :
        scanDataType = "QRCODE";
        break;
    case BARCODES::UPCA  :
        scanDataType = "UPCA";
        break;
    case BARCODES::UPCE  :
        scanDataType = "UPCE";
        break;
    case BARCODES::PDF417  :
        scanDataType = "PDF417";
        break;
    case BARCODES::AZTEC  :
        scanDataType = "AZTEC";
        break;
    default:
        scanDataType = "";
        break;
    }

    return scanDataType;
}

void CameraScanner::scanResultReceiver(int requestCode, int resultCode, const QAndroidJniObject &data)
{
    if (requestCode == BARCODES::REQUEST_CODE) {
        if (resultCode == BARCODES::RESULT_OK) {
            const QAndroidJniObject scanTypeKey = QAndroidJniObject::fromString("scanType");
            const QAndroidJniObject scanType = data.callObjectMethod(
                        "getStringExtra", "(Ljava/lang/String;)Ljava/lang/String;", scanTypeKey.object());
            const QAndroidJniObject scanDataKey = QAndroidJniObject::fromString("scanData");
            const QAndroidJniObject scanData = data.callObjectMethod(
                        "getStringExtra", "(Ljava/lang/String;)Ljava/lang/String;", scanDataKey.object());

            if (scanData.isValid() && scanType.isValid()) {
                QString resScanType = decodeScanDataType(scanType.toString());
                QString resScanData = scanData.toString();
                qDebug() << "--------------------javaScanner-----------------------------------";
                qDebug() << resScanType.toUtf8();
                qDebug() << resScanData.toUtf8();
                qDebug() << "********************javaScanner***********************************";

                emit onScanComplete(resScanData.toUtf8(), resScanType.toUtf8());
            }
        } else {
            //emit JavaScanner::instance()->receiveFromActivityResult("undefined","undefined");
        }
    }
}


void CameraScanner::activateScanner()
{
    QAndroidIntent activityIntent(QtAndroid::androidActivity().object(),
                                  "org/qtproject/Scanner/CameraScanner");

    QtAndroid::startActivity(
                activityIntent.handle(), BARCODES::REQUEST_CODE,
                [this](int requestCode, int resultCode, const QAndroidJniObject &data) {
        scanResultReceiver(requestCode, resultCode, data);
    });
}



void CameraScanner::deactivateScanner()
{

}
