#include "translator.h"


Translator::Translator() : QObject()
{
    updateLanguage("ua");
    qDebug() << "updated";
}


void Translator::updateLanguage(const QString& lang) {
    mTranslator.load("lang_" + lang, ":/translation");
    qApp->installTranslator(&mTranslator);
    emit languageChanged();
}
