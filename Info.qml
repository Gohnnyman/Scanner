import QtQuick 2.15
import QtQuick.Controls 2.15
import QtQuick.Controls.Material 2.12

Page {
    property var updateText: function(){
        label.text = qsTr("Info");
        info.text = qsTr("Made by Alexander Fedorovskyi<br><br>" +
                         "How to use:<br><br>" +
                         "Just go to \"Scanner Test\" page and click \"Camera Scanner\" button for scanning with your " +
                         "camera.<br><br>Or whether you're using phone with external scanner like Chainway C66, Honeywell EDA51 or Zebra " +
                         "just scan some barcode with it's scanner.");
        cmbbox.displayText = qsTr("Language: ") + cmbbox.currentText;
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


    Flickable {
        anchors.fill: parent
        contentHeight: info.implicitHeight + pizzaCat.implicitHeight + parent.height / 5
        Text {
            anchors.left: parent.left
            anchors.right: parent.right;
            anchors.margins: 8
            id: info
            color: "white"
            clip: true
            font.pixelSize: Qt.application.font.pixelSize * 1.5;
            wrapMode: TextInput.Wrap
        }

        Image {
            anchors.top: info.bottom
            anchors.margins: 10
            anchors.horizontalCenter: parent.horizontalCenter
            id: pizzaCat
            sourceSize {
                width: parent.width / 2;
                height: parent.height / 3
            }
            source: "img/pizzaCat.jpg"
        }

    }

    ComboBox {
        id: cmbbox
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        width: parent.width / 2.5
        model: ["ua", "us", "ru"]
        displayText: qsTr("Language: ") + currentText;
        onActivated: {
            Translator.updateLanguage(currentValue);
        }
    }

}
