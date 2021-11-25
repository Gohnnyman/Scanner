#ifndef TRANSLATOR_H
#define TRANSLATOR_H
#include <QObject>
#include <QTranslator>
#include <QApplication>
#include <QDebug>

class Translator : public QObject
{
    Q_OBJECT
public:
    Translator();

signals:
    void languageChanged();

public slots:
    void updateLanguage(const QString& lang);

private:
    QTranslator mTranslator;
};

#endif // TRANSLATOR_H
