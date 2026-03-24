#pragma once

#include "QGCCorePlugin.h"

class UserAuthentication;

/**
 * Pushpaka UTM QGC plugin.
 *
 * Entry point for all Pushpaka-specific behaviour:
 *   - Keycloak OAuth2 authentication (UserAuthentication)
 *   - Registry / pilot / UAS lookup  (future: #74)
 *   - Flight plan submission + AUT   (future: #75)
 *   - Arm pre-check enforcement      (future: #76)
 */
class PushpakaPlugin : public QGCCorePlugin
{
    Q_OBJECT
public:
    explicit PushpakaPlugin(QObject* parent = nullptr);
    ~PushpakaPlugin() override;

    static QGCCorePlugin* instance();

    // QGCCorePlugin overrides
    void init()    override;
    void cleanup() override;

    UserAuthentication* userAuthentication() const { return _userAuth; }

private:
    UserAuthentication* _userAuth = nullptr;
};
