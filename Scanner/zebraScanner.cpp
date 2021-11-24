#include "zebraScanner.h"
#include <QDebug>

ZebraScanner::ZebraScanner(QObject *parent) : ScannerInterface(parent)
{
    JNINativeMethod methods[] {{"sendScanResult", "(Ljava/lang/String;Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::sendScanResult)},
                                   {"log", "(Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::log)}};

        QAndroidJniObject javaClass("org/qtproject/Java/ZebraScanner");
        QAndroidJniEnvironment env;
        jclass objectClass = env->GetObjectClass(javaClass.object<jobject>());
        env->RegisterNatives(objectClass,
                             methods,
                             sizeof(methods) / sizeof(methods[0]));
        env->DeleteLocalRef(objectClass);

    javaObject = new QAndroidJniObject("org/qtproject/Java/ZebraScanner");
    javaObject->callObjectMethod("init", "()V");

    ScannerConnector::scannerConnectorInit(this);
}

ZebraScanner::~ZebraScanner()
{

}

void ZebraScanner::activateScanner()
{
    javaObject->callObjectMethod("activateScanner", "()V");
}

void ZebraScanner::deactivateScanner()
{
    javaObject->callObjectMethod("deActivateScanner", "()V");
}

