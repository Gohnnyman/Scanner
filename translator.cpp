#include "translator.h"


Translator::Translator() : QObject()
{
    updateLanguage("ua");
}


void Translator::updateLanguage(const QString& lang) {
    mTranslator.load("lang_" + lang, ":/translation");
    qApp->installTranslator(&mTranslator);
    emit languageChanged();
}
