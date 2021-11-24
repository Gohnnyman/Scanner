#ifndef SCANNERINTERFACE_H
#define SCANNERINTERFACE_H

#include <QObject>
#include <QtAndroidExtras>

class ScannerInterface : public QObject
{
    Q_OBJECT
public:

    explicit ScannerInterface(QObject *parent = nullptr): QObject(parent) {}
    virtual void activateScanner() = 0;
    virtual void deactivateScanner() = 0;
    virtual ~ScannerInterface() {if(javaObject) delete javaObject;}
signals:
     void onScanComplete(QByteArray scanData, QByteArray scanDataType);

protected:
    QAndroidJniObject *javaObject;

};

namespace ScannerConnector
{
    void scannerConnectorInit(ScannerInterface *scanner);

    void sendScanResult(JNIEnv *env, jobject obj, jstring scanData, jstring scanDataType);

    void log(JNIEnv *env, jobject obj, jstring message);
}

#endif // SCANNERINTERFACE_H
