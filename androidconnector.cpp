#include "androidconnector.h"
#include <QAndroidJniObject>

AndroidConnector::AndroidConnector()
{

}

QString AndroidConnector::getDeviceModel() const
{
    QAndroidJniObject res;
    res = QAndroidJniObject::callStaticObjectMethod
                           ("org/qtproject/Scanner/Android", "getDeviceModel", "()Ljava/lang/String;" );
    return res.toString();

}

QString AndroidConnector::getSerial() const
{
    QAndroidJniObject res;
    res = QAndroidJniObject::callStaticObjectMethod
                           ("org/qtproject/Scanner/Android", "getSerial", "()Ljava/lang/String;" );
    return res.toString();
}

void AndroidConnector::toastMessage(const QString& text) const
{
    QAndroidJniObject res;
    QAndroidJniObject n = QAndroidJniObject::fromString(text);
    res = QAndroidJniObject::callStaticObjectMethod
                           ("org/qtproject/Scanner/Android", "toastMessage", "(Ljava/lang/String;)V", n.object<jstring>());

}

