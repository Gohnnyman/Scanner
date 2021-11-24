#include "chainwayScanner.h"
#include <QDebug>

ChainwayScanner::ChainwayScanner(QObject *parent) : ScannerInterface(parent)
{
    JNINativeMethod methods[] {{"sendScanResult", "(Ljava/lang/String;Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::sendScanResult)},
                                   {"log", "(Ljava/lang/String;)V", reinterpret_cast<void *>(ScannerConnector::log)}};

        QAndroidJniObject javaClass("org/qtproject/Java/ChainwayScanner");
        QAndroidJniEnvironment env;
        jclass objectClass = env->GetObjectClass(javaClass.object<jobject>());
        env->RegisterNatives(objectClass,
                             methods,
                             sizeof(methods) / sizeof(methods[0]));
        env->DeleteLocalRef(objectClass);
    javaObject = new QAndroidJniObject("org/qtproject/Java/ChainwayScanner");
    javaObject->callObjectMethod("init", "()V");

    ScannerConnector::scannerConnectorInit(this);
}

ChainwayScanner::~ChainwayScanner()
{

}

void ChainwayScanner::activateScanner()
{
    javaObject->callObjectMethod("activateScanner", "()V");
}

void ChainwayScanner::deactivateScanner()
{
    javaObject->callObjectMethod("deActivateScanner", "()V");
}
