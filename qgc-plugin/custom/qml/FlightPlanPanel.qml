import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

// Flight plan submission panel.
// Opens as a Popup; on submit calls PushpakaPlugin.submitFlightPlan().
Popup {
    id: root
    width: 360
    height: contentCol.implicitHeight + 48
    modal: true
    focus: true
    closePolicy: Popup.CloseOnEscape | Popup.CloseOnPressOutside

    property var _plugin: QGroundControl.corePlugin

    // Close automatically when the AUT is received.
    Connections {
        target: root._plugin
        function onAutChanged() {
            if (root._plugin.hasValidAut) root.close()
        }
    }

    ColumnLayout {
        id: contentCol
        anchors { left: parent.left; right: parent.right; top: parent.top; margins: 16 }
        spacing: 12

        Text {
            text: "Submit Flight Plan"
            font.pixelSize: 16
            font.bold: true
        }

        Text { text: "Aircraft" }
        ComboBox {
            id: uasPicker
            Layout.fillWidth: true
            model: root._plugin.uasList
            textRole: "id"
        }

        Text { text: "Start time (ISO 8601)" }
        TextField {
            id: startField
            Layout.fillWidth: true
            placeholderText: "2024-01-01T09:00:00+05:30"
        }

        Text { text: "End time (ISO 8601)" }
        TextField {
            id: endField
            Layout.fillWidth: true
            placeholderText: "2024-01-01T10:00:00+05:30"
        }

        RowLayout {
            Layout.fillWidth: true
            Item { Layout.fillWidth: true }
            Button {
                text: "Cancel"
                onClicked: root.close()
            }
            Button {
                text: busyIndicator.running ? "Submitting…" : "Submit"
                enabled: !busyIndicator.running && uasPicker.currentIndex >= 0
                         && startField.text.length > 0 && endField.text.length > 0
                onClicked: {
                    busyIndicator.running = true
                    var uasId = root._plugin.uasList[uasPicker.currentIndex]["id"]
                    root._plugin.submitFlightPlan(uasId, startField.text, endField.text)
                }
            }
        }

        BusyIndicator {
            id: busyIndicator
            running: false
            Layout.alignment: Qt.AlignHCenter
            visible: running
        }
    }
}
