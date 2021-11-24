#ifndef SCANNER_H
#define SCANNER_H

#include <QObject>
#include <QtAndroidExtras>
#include "scannerInterface.h"

class Scanner : public QObject
{
    Q_OBJECT
public:


    explicit Scanner(const QString& deviceModel, QObject *parent = nullptr);
    Q_INVOKABLE void activateScanner();
    Q_INVOKABLE void deactivateScanner();
    Q_INVOKABLE void startCameraScanner();
    Q_INVOKABLE bool isExternalScanner();
    ~Scanner();


signals:
     void onScanComplete(QByteArray scanData, QByteArray scanDataType);

private:
     QString scannerName;
     ScannerInterface* externalScanner;
     ScannerInterface* cameraScanner;
};

#endif // SCANNER_H

