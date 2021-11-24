#ifndef CAMERASCANNER_H
#define CAMERASCANNER_H

#include <QObject>
#include <QtAndroidExtras>
#include <QtAndroid>
#include "scannerInterface.h"

class CameraScanner : public ScannerInterface
{
    Q_OBJECT
public:
    explicit CameraScanner(QObject *parent = nullptr);
    ~CameraScanner();
    void activateScanner() override;
    void deactivateScanner() override;
private:
    QString decodeScanDataType(QString typeCode);
    void scanResultReceiver(int requestCode, int resultCode, const QAndroidJniObject &data);
};

namespace BARCODES {

const int REQUEST_CODE = 123;
const jint RESULT_OK = QAndroidJniObject::getStaticField<jint>("android/app/Activity", "RESULT_OK");

    enum BARCODE_Types {
        NONE       = 0,
        CODE128    = 1,
        CODE39     = 2,
        CODE93     = 4,
        CODABAR    = 8,
        DATAMATRIX = 16,
        EAN13      = 32,
        EAN8       = 64,
        ITF        = 128,
        QRCODE     = 256,
        UPCA       = 512,
        UPCE       = 1024,
        PDF417     = 2048,
        AZTEC      = 4096,
        //MAXICODE,
        //RSS_14,
        //RSS_EXPANDED,
        //UPC_EAN_EXTENSION,
        //ASSUME_GS1
    };
}

#endif // CAMERASCANNER_H
