#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include "androidconnector.h"
#include "translator.h"


int main(int argc, char *argv[])
{
#if QT_VERSION < QT_VERSION_CHECK(6, 0, 0)
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
#endif

    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    const QUrl url(QStringLiteral("qrc:/main.qml"));
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
                     &app, [url](QObject *obj, const QUrl &objUrl) {
        if (!obj && url == objUrl)
            QCoreApplication::exit(-1);
    }, Qt::QueuedConnection);

    AndroidConnector android;
    Translator translator;
    engine.rootContext()->setContextProperty("Translator", &translator);
    engine.rootContext()->setContextProperty("Android", &android);

    engine.load(url);
    return app.exec();
}
