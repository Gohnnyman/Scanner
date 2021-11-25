#include "honeywellScanner.h"
#include <QDebug>

HoneywellScanner::HoneywellScanner(QObject *parent) : ScannerInterface(parent)
{
    JNINativeMethod methods[] {{"sendScanResult", "(Ljava/lang/String;Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::sendScanResult)},
                                   {"log", "(Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::log)}};

        QAndroidJniObject javaClass("org/qtproject/Scanner/HoneywellScanner");
        QAndroidJniEnvironment env;
        jclass objectClass = env->GetObjectClass(javaClass.object<jobject>());
        env->RegisterNatives(objectClass,
                             methods,
                             sizeof(methods) / sizeof(methods[0]));
        env->DeleteLocalRef(objectClass);

    javaObject = new QAndroidJniObject("org/qtproject/Scanner/HoneywellScanner");
    javaObject->callObjectMethod("init", "()V");

    ScannerConnector::scannerConnectorInit(this);
}

HoneywellScanner::~HoneywellScanner()
{

}

void HoneywellScanner::activateScanner()
{
    javaObject->callObjectMethod("activateScanner", "()V");
}

void HoneywellScanner::deactivateScanner()
{
    javaObject->callObjectMethod("deActivateScanner", "()V");
}
