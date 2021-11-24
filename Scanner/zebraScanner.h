#ifndef ZEBRASCANNER_H
#define ZEBRASCANNER_H

#include <QObject>
#include <QtAndroidExtras>
#include <QtAndroid>
#include "scannerInterface.h"

class ZebraScanner : public ScannerInterface
{
    Q_OBJECT
public:
    explicit ZebraScanner(QObject *parent = nullptr);
    ~ZebraScanner() override;
    void activateScanner() override;
    void deactivateScanner() override;
};

#endif // ZEBRASCANNER_H
