#include "scanner.h"
#include "zebraScanner.h"
#include "chainwayScanner.h"
#include "honeywellScanner.h"
#include "cameraScanner.h"

Scanner::Scanner(const QString& deviceModel, QObject *parent) : QObject(parent)
{
    externalScanner = nullptr;
    cameraScanner = new CameraScanner;

    if(deviceModel.contains("ZEBRA", Qt::CaseInsensitive))
    {
        externalScanner = new ZebraScanner;
        scannerName = "ZebraScanner";
    }
    else if(deviceModel.contains("CHAINWAY", Qt::CaseInsensitive))
    {
        externalScanner = new ChainwayScanner;
        scannerName = "ChainwayScanner";
    }
    else if(deviceModel.contains("HONEYWELL", Qt::CaseInsensitive))
    {
        externalScanner = new HoneywellScanner;
        scannerName = "HoneywellScanner";
    }

    if(externalScanner) connect(externalScanner, SIGNAL(onScanComplete(QByteArray, QByteArray)), this, SIGNAL(onScanComplete(QByteArray, QByteArray)));
    if(cameraScanner) connect(cameraScanner, SIGNAL(onScanComplete(QByteArray, QByteArray)), this, SIGNAL(onScanComplete(QByteArray, QByteArray)));
}

Scanner::~Scanner()
{
    if(externalScanner) delete externalScanner;
    delete cameraScanner;
}


void Scanner::activateScanner()
{
    if(externalScanner) externalScanner->activateScanner();
}

void Scanner::deactivateScanner()
{
    if(externalScanner) externalScanner->deactivateScanner();
}

void Scanner::startCameraScanner()
{
    if(cameraScanner) cameraScanner->activateScanner();
}

bool Scanner::isExternalScanner()
{
    return externalScanner != nullptr;
}
