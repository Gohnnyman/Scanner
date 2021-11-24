#ifndef ANDROIDCONNECTOR_H
#define ANDROIDCONNECTOR_H

#include <QObject>

class AndroidConnector : public QObject
{
    Q_OBJECT
public:
    AndroidConnector();
    QString getDeviceModel() const;
    QString getSerial() const;
    void toastMessage(const QString&) const;

};

#endif // ANDROIDCONNECTOR_H
