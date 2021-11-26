import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.3
import QtQuick.Controls.Material 2.12


Page {
    property var updateText: function(){
        label.text = qsTr("Scanner Test");
        lastDataType.text = qsTr("<b>Last Data type: </b>") + ((!!Android.scanDataType) ? Android.scanDataType : "-")
        lastData.text = qsTr("<b>Last Data: </b>") + ((!!Android.scanData) ? Android.scanData : "-");
        cameraScannerBut.text = qsTr("Camera Scanner");
    }


    header: Rectangle {
        Material.theme: Material.Dark
        anchors.left: parent.left
        anchors.right: parent.right
        height: label.implicitHeight
        color: Material.backgroundColor

        Label {
            Material.theme: Material.Dark
            id: label
            background: Qt.AutoColor
            font.pixelSize: Qt.application.font.pixelSize * 2
            padding: 10
        }
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
            font.pixelSize: Qt.application.font.pixelSize * 1.5;
            wrapMode: TextInput.Wrap
        }

        Text {
            width: parent.width
            Layout.preferredWidth: parent.width
            Layout.alignment: Qt.AlignLeft
            id: lastData
            color: "white"
            clip: true
            font.pixelSize: Qt.application.font.pixelSize * 1.5;
            wrapMode: TextInput.Wrap
        }
    }

    Button {
        id: cameraScannerBut
        anchors.bottom: parent.bottom;
        anchors.left: parent.left;
        anchors.right: parent.right;
        onClicked: {
            Android.startCameraScanner();
        }

    }
}
