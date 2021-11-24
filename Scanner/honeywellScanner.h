#ifndef HONEYWELLSCANNER_H
#define HONEYWELLSCANNER_H
#include <QObject>
#include <QtAndroidExtras>
#include <QtAndroid>

#include "scannerInterface.h"


class HoneywellScanner : public ScannerInterface
{
    Q_OBJECT
public:
    explicit HoneywellScanner(QObject *parent = nullptr);
    ~HoneywellScanner() override;
    void activateScanner() override;
    void deactivateScanner() override;

};

#endif // HONEYWELLSCANNER_H


