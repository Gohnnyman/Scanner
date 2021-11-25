import QtQuick 2.15
import QtQuick.Controls 2.15


ApplicationWindow {
    visible: true
    title: qsTr("Scanner")
    id: window

    function updateText() {
        tab1.text = qsTr("Scanner Test");
        tab2.text = qsTr("Info");
        firstSwap.updateText();
        secondSwap.updateText();
    }

    Connections {
        target: Translator
        function onLanguageChanged() {
            updateText();
        }
    }

    Component.onCompleted: updateText();

    SwipeView {
        id: swipeView
        anchors.fill: parent
        currentIndex: tabBar.currentIndex

        Page1Form {
            id: firstSwap
        }

        Page2Form {
            id: secondSwap
        }
    }

    Button {
        text: "CLICK"
        onClicked: {
            Translator.updateLanguage("ru");
        }
    }

    Text {
        id: jopa
        text: Translator.emptyString;
    }


    footer: TabBar {
        id: tabBar
        currentIndex: swipeView.currentIndex

        TabButton {
            id: tab1
        }
        TabButton {
            id: tab2
        }
    }
}
