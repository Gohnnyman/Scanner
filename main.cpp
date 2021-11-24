#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include "scanner.h"
#include "androidconnector.h"


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

    Scanner scanner("");
    scanner.activateScanner();
    engine.rootContext()->setContextProperty("Scanner", &scanner);

    AndroidConnector android;
    qDebug() << "MEME: " << scanner.isExternalScanner() << ' ' << android.getDeviceModel();
    engine.load(url);
    return app.exec();
}
