QT += quick androidextras

INCLUDEPATH += $$PWD
DEPENDPATH += $$PWD


HEADERS += \
    $$PWD/cameraScanner.h \
    $$PWD/chainwayScanner.h \
    $$PWD/honeywellScanner.h \
    $$PWD/scannerInterface.h \
    $$PWD/scanner.h \
    $$PWD/zebraScanner.h

SOURCES += \
    $$PWD/cameraScanner.cpp \
    $$PWD/chainwayScanner.cpp \
    $$PWD/honeywellscanner.cpp \
    $$PWD/scanner.cpp \
    $$PWD/scannerInterface.cpp \
    $$PWD/zebraScanner.cpp


DISTFILES += \
    $$PWD/src/org/qtproject/Scanner/CameraScanner.java \
    $$PWD/src/org/qtproject/Scanner/ChainwayScanner.java \
    $$PWD/src/org/qtproject/Scanner/HoneywellScanner.java \
    $$PWD/src/org/qtproject/Scanner/ZebraScanner.java \
    $$PWD/res/layout/rectangle.xml \
    $$PWD/res/layout/second_activity.xml \


include($$PWD/qzxing-master/src/QZXing.pri)

QMAKE_POST_LINK += $$QMAKE_COPY_DIR $$shell_path($$PWD/src) $$shell_path($$ANDROID_PACKAGE_SOURCE_DIR/src) $$escape_expand(\n\t)
QMAKE_POST_LINK += $$QMAKE_COPY_DIR $$shell_path($$PWD/res) $$shell_path($$ANDROID_PACKAGE_SOURCE_DIR/res) $$escape_expand(\n\t)
QMAKE_POST_LINK += $$QMAKE_COPY_DIR $$shell_path($$PWD/libs) $$shell_path($$ANDROID_PACKAGE_SOURCE_DIR/libs) $$escape_expand(\n\t)
