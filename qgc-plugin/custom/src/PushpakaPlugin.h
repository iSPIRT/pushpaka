#pragma once

#include <QtCore/QVariantList>

#include "QGCCorePlugin.h"

class UserAuthentication;
class RegistryClient;
class FlightAuthorisationClient;

/**
 * Pushpaka UTM QGC plugin.
 *
 * Entry point for all Pushpaka-specific behaviour:
 *   - Keycloak OAuth2 authentication (UserAuthentication)
 *   - Registry / pilot / UAS lookup  (RegistryClient — #74)
 *   - Flight plan submission + AUT   (FlightAuthorisationClient — #75)
 *   - Arm pre-check enforcement      (future: #76)
 */
class PushpakaPlugin : public QGCCorePlugin
{
    Q_OBJECT

    Q_PROPERTY(UserAuthentication* userAuthentication READ userAuthentication CONSTANT)
    Q_PROPERTY(QString pilotId READ pilotId NOTIFY pilotChanged)
    Q_PROPERTY(QVariantList uasList READ uasList NOTIFY uasListChanged)
    Q_PROPERTY(bool hasValidAut READ hasValidAut NOTIFY autChanged)
    Q_PROPERTY(QString autJwt READ autJwt NOTIFY autChanged)

public:
    explicit PushpakaPlugin(QObject* parent = nullptr);
    ~PushpakaPlugin() override;

    static QGCCorePlugin* instance();

    // QGCCorePlugin overrides
    void init() override;
    void cleanup() override;
    const QVariantList& toolBarIndicators() override;

    UserAuthentication* userAuthentication() const { return _userAuth; }
    RegistryClient* registryClient() const { return _registry; }

    QString pilotId() const { return _pilotId; }
    QVariantList uasList() const { return _uasList; }
    bool hasValidAut() const { return _hasValidAut; }
    QString autJwt() const { return _autJwt; }

    Q_INVOKABLE void submitFlightPlan(const QString& uasId, const QString& startTime,
                                      const QString& endTime);

signals:
    void pilotChanged();
    void uasListChanged();
    void autChanged();

private:
    UserAuthentication* _userAuth = nullptr;
    RegistryClient* _registry = nullptr;
    FlightAuthorisationClient* _flightAuth = nullptr;

    QString _pilotId;
    QVariantList _uasList;
    bool _hasValidAut = false;
    QString _autJwt;

    QVariantList _toolBarIndicators;
};
