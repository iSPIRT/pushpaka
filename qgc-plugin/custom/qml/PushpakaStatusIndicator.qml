import QtQuick
import QtQuick.Controls

// Pushpaka UTM status indicator for the QGC toolbar.
// Shows: Not logged in / Logged in (no AUT) / AUT valid / AUT expired
// Wired up in PushpakaPlugin::init() — expanded in #75.
Rectangle {
    id: root
    width: statusText.implicitWidth + 16
    height: 32
    radius: 4
    color: _bgColor()

    property bool  authenticated: false  // bound to UserAuthentication.isAuthenticated
    property bool  autValid:      false  // bound to AUT state (#75)

    function _bgColor() {
        if (!authenticated) return "#555555"
        if (!autValid)      return "#cc6600"
        return "#007700"
    }

    Text {
        id: statusText
        anchors.centerIn: parent
        color: "white"
        font.pixelSize: 12
        text: {
            if (!root.authenticated) return "UTM: Login"
            if (!root.autValid)      return "UTM: No AUT"
            return "UTM: AUT Valid"
        }
    }

    MouseArea {
        anchors.fill: parent
        // TODO (#74/#75): open login / flight plan panel on click
    }
}
