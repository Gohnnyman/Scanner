import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.3

Page {
    header: Label {
        text: qsTr("Page 1")
        font.pixelSize: Qt.application.font.pixelSize * 2
        padding: 10
    }

    ColumnLayout {
        spacing: 4
        anchors.margins: 8
        anchors.left: parent.left
        anchors.right: parent.right

        Text {

            Layout.alignment: Qt.AlignLeft
            Layout.fillWidth: true
            id: lastDataType
            color: "white"
            clip: true
            font.pixelSize: 21;
            wrapMode: TextInput.Wrap
            text: "<b>Last Data type: </b>" + (!!Android.scanDataType ? Android.scanDataType : "-")
        }

        Text {
            width: parent.width
            Layout.preferredWidth: parent.width
            Layout.alignment: Qt.AlignLeft
//                    Layout.fillWidth: true
            id: lastData
            color: "white"
            clip: true
            font.pixelSize: 21;
            wrapMode: TextInput.Wrap
            text: "<b>Last Data: </b>" + (!!Android.scanData ? Android.scanData : "-")
        }
    }

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
