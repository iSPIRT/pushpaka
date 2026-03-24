#pragma once

#include <QtCore/QVariantList>
#include "QGCCorePlugin.h"

class UserAuthentication;
class RegistryClient;

/**
 * Pushpaka UTM QGC plugin.
 *
 * Entry point for all Pushpaka-specific behaviour:
 *   - Keycloak OAuth2 authentication (UserAuthentication)
 *   - Registry / pilot / UAS lookup  (RegistryClient — #74)
 *   - Flight plan submission + AUT   (future: #75)
 *   - Arm pre-check enforcement      (future: #76)
 */
class PushpakaPlugin : public QGCCorePlugin
{
    Q_OBJECT

    Q_PROPERTY(QString pilotId READ pilotId NOTIFY pilotChanged)
    Q_PROPERTY(QVariantList uasList READ uasList NOTIFY uasListChanged)

public:
    explicit PushpakaPlugin(QObject* parent = nullptr);
    ~PushpakaPlugin() override;

    static QGCCorePlugin* instance();

    // QGCCorePlugin overrides
    void init() override;
    void cleanup() override;

    UserAuthentication* userAuthentication() const { return _userAuth; }
    RegistryClient* registryClient() const { return _registry; }

    QString pilotId() const { return _pilotId; }
    QVariantList uasList() const { return _uasList; }

signals:
    void pilotChanged();
    void uasListChanged();

private:
    UserAuthentication* _userAuth = nullptr;
    RegistryClient* _registry = nullptr;
    QString _pilotId;
    QVariantList _uasList;
};
