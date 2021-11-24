#ifndef CHAINWAYSCANNER_H
#define CHAINWAYSCANNER_H

#include <QObject>
#include <QtAndroidExtras>
#include <QtAndroid>
#include "scannerInterface.h"

class ChainwayScanner : public ScannerInterface
{
    Q_OBJECT
public:
    explicit ChainwayScanner(QObject *parent = nullptr);
    ~ChainwayScanner() override;
    void activateScanner() override;
    void deactivateScanner() override;

};

#endif // CHAINWAYSCANNER_H
