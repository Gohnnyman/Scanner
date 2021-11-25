import QtQuick 2.15
import QtQuick.Controls 2.15


ApplicationWindow {
    visible: true
    title: qsTr("Scanner")
    id: window

    SwipeView {
        id: swipeView
        anchors.fill: parent
        currentIndex: tabBar.currentIndex

        Page1Form {

        }

        Page2Form {

        }
    }

    footer: TabBar {
        id: tabBar
        currentIndex: swipeView.currentIndex

        TabButton {
            text: qsTr("Scanner Test")
        }
        TabButton {
            text: qsTr("Info")
        }
    }
}
