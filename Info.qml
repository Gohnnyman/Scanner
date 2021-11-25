import QtQuick 2.15
import QtQuick.Controls 2.15

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

    header: Label {
        id: label
        font.pixelSize: Qt.application.font.pixelSize * 2
        padding: 10
    }

    Text {
        anchors.left: parent.left
        anchors.right: parent.right;
        anchors.margins: 8
        id: info
        color: "white"
        clip: true
        font.pixelSize: 21;
        wrapMode: TextInput.Wrap
    }


    ComboBox {
        id: cmbbox
        anchors.right: parent.right
        anchors.bottom: parent.bottom
        width: parent.width / 3
        model: ["us", "ua", "ru"]
        displayText: qsTr("Language: ") + currentText;
        onActivated: {
            Translator.updateLanguage(currentValue);
        }
    }

}
