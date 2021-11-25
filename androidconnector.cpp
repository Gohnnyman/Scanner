#include "androidconnector.h"
#include <QAndroidJniObject>

AndroidConnector::AndroidConnector()
{
    QAndroidJniObject::callStaticMethod<void>
                           ("org/qtproject/Scanner/AndroidConnector", "chackPermissions", "()V" );
    scanner = new Scanner(getDeviceModel());
    connect(scanner, SIGNAL(onScanComplete(QByteArray, QByteArray)), this, SLOT(slotScanComplete(QByteArray, QByteArray)));
}

QString AndroidConnector::getDeviceModel() const
{
    QAndroidJniObject res;
    res = QAndroidJniObject::callStaticObjectMethod
                           ("org/qtproject/Scanner/AndroidConnector", "getDeviceModel", "()Ljava/lang/String;" );
    return res.toString();

}

QString AndroidConnector::getSerial() const
{
    QAndroidJniObject res;
    res = QAndroidJniObject::callStaticObjectMethod
                           ("org/qtproject/Scanner/AndroidConnector", "getSerial", "()Ljava/lang/String;" );
    return res.toString();
}

void AndroidConnector::toastMessage(const QString& text) const
{
    QAndroidJniObject string = QAndroidJniObject::fromString(text);
    QAndroidJniObject::callStaticMethod<void>
                           ("org/qtproject/Scanner/AndroidConnector", "toastMessage", "(Ljava/lang/String;)V", string.object<jstring>());

}

void AndroidConnector::activateScanner()
{
    scanner->activateScanner();
}

void AndroidConnector::deactivateScanner()
{
    scanner->deactivateScanner();
}

void AndroidConnector::startCameraScanner()
{
    scanner->startCameraScanner();
}

bool AndroidConnector::isExternalScanner()
{
    return scanner->isExternalScanner();
}

void AndroidConnector::slotScanComplete(QByteArray scanData, QByteArray scanDataType)
{
    qDebug() << "MEME";
    setScanData(scanData);
    setScanDataType(scanDataType);
}

QString AndroidConnector::scanData() const
{
    return m_scanData;
}

QString AndroidConnector::scanDataType() const
{
    return m_scanDataType;
}

void AndroidConnector::setScanData(const QString& scanData)
{
    m_scanData = scanData;
    emit scanDataChanged(scanData);
}


void AndroidConnector::setScanDataType(const QString& scanDataType)
{
    m_scanDataType = scanDataType;
    emit scanDataTypeChanged(scanDataType);
}
