import QtQuick 2.15
import QtQuick.Controls 2.15

Page {
    property var updateText: function(){
        label.text = qsTr("Page 2");
        info.text = qsTr("Made by Alexander Fedorovskyi");
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

}
