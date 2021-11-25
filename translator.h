#ifndef TRANSLATOR_H
#define TRANSLATOR_H
#include <QObject>
#include <QTranslator>
#include <QApplication>
#include <QDebug>

class Translator : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString emptyString READ getEmptyString NOTIFY languageChanged)
public:
    Translator() {};
    QString getEmptyString() { return ""; }


signals:
    void languageChanged();

public slots:
    void updateLanguage(const QString& lang) {
        mTranslator.load("lang_" + lang, ":/translation");
        qApp->installTranslator(&mTranslator);
        emit languageChanged();
    }

private:
    QTranslator mTranslator;
};

#endif // TRANSLATOR_H
