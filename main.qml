import QtQuick 2.15
import QtQuick.Controls 2.15

ApplicationWindow {
    width: 640
    height: 480
    visible: true
    title: qsTr("Tabs")

    SwipeView {
        id: swipeView
        anchors.fill: parent
        currentIndex: tabBar.currentIndex

        Page1Form {
            Button {
                text: "Camera Scanner";
                anchors.bottom: parent.bottom;
                anchors.left: parent.left;
                anchors.right: parent.right;
                onClicked: {
                    Android.startCameraScanner();
                }

            }
        }

        Page2Form {
            Text {
                id: scanned
                color: "white"
                clip: true
                wrapMode: TextInput.Wrap
                text: "Made by Alexander Fedorovskyi"
            }
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
