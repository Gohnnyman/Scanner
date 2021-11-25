#ifndef ANDROIDCONNECTOR_H
#define ANDROIDCONNECTOR_H

#include <QObject>
#include "scanner.h"

class AndroidConnector : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString scanData READ scanData WRITE setScanData NOTIFY scanDataChanged);
    Q_PROPERTY(QString scanDataType READ scanDataType WRITE setScanDataType NOTIFY scanDataTypeChanged);

private:
    QString m_scanData;
    QString m_scanDataType;
    Scanner* scanner;

    QString scanData() const;
    QString scanDataType() const;

    void setScanData(const QString&);
    void setScanDataType(const QString&);

public:

    AndroidConnector();
    QString getDeviceModel() const;
    QString getSerial() const;
    void toastMessage(const QString&) const;



    Q_INVOKABLE void activateScanner();
    Q_INVOKABLE void deactivateScanner();
    Q_INVOKABLE void startCameraScanner();
    Q_INVOKABLE bool isExternalScanner();

signals:
    void scanDataChanged(QString);
    void scanDataTypeChanged(QString);

public slots:
    void slotScanComplete(QByteArray, QByteArray);

};

#endif // ANDROIDCONNECTOR_H
