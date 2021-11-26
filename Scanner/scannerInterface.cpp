#include "scannerInterface.h"

namespace ScannerConnector
{
    ScannerInterface *scannerConnector;

    void scannerConnectorInit(ScannerInterface *scanner) {
        scannerConnector = scanner;
    }

    void sendScanResult(JNIEnv *env, jobject obj, jstring scanData, jstring scanDataType)
    {
        QByteArray qscanData(env->GetStringUTFChars(scanData, 0));
        QByteArray qscanDataType(env->GetStringUTFChars(scanDataType, 0));
        emit scannerConnector->onScanComplete(qscanData, qscanDataType);
    }

    void log(JNIEnv *env, jobject obj, jstring message)
    {
        QByteArray qMessage(env->GetStringUTFChars(message, 0));
    }

}
