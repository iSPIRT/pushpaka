import QtQuick
import QtQuick.Controls

// Pushpaka UTM status indicator for the QGC toolbar.
// Shows: Not logged in / Logged in (no AUT) / AUT valid
// Click: triggers login if not authenticated, opens FlightPlanPanel otherwise.
Rectangle {
    id: root
    width: statusText.implicitWidth + 16
    height: 32
    radius: 4
    color: _bgColor()

    property var _plugin: QGroundControl.corePlugin
    property bool authenticated: _plugin.userAuthentication.isAuthenticated
    property bool autValid: _plugin.hasValidAut

    function _bgColor() {
        if (!authenticated) return "#555555"
        if (!autValid) return "#cc6600"
        return "#007700"
    }

    Text {
        id: statusText
        anchors.centerIn: parent
        color: "white"
        font.pixelSize: 12
        text: {
            if (!root.authenticated) return "UTM: Login"
            if (!root.autValid) return "UTM: No AUT"
            return "UTM: AUT Valid"
        }
    }

    MouseArea {
        anchors.fill: parent
        onClicked: {
            if (!root.authenticated) {
                root._plugin.userAuthentication.authorise()
            } else {
                flightPlanPanel.open()
            }
        }
    }

    FlightPlanPanel {
        id: flightPlanPanel
    }
}
