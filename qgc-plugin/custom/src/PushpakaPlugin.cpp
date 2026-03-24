#include "PushpakaPlugin.h"

#include <QtCore/QCoreApplication>
#include <QtCore/QUrl>

#include "FlightAuthorisationClient.h"
#include "RegistryClient.h"
#include "UserAuthentication.h"

PushpakaPlugin::PushpakaPlugin(QObject* parent) : QGCCorePlugin(parent) {}

PushpakaPlugin::~PushpakaPlugin() {}

QGCCorePlugin* PushpakaPlugin::instance()
{
    static PushpakaPlugin* _instance = nullptr;
    if (!_instance) {
        _instance = new PushpakaPlugin(qApp);
    }
    return _instance;
}

void PushpakaPlugin::init()
{
    QGCCorePlugin::init();

    _userAuth = new UserAuthentication(this);
    _registry = new RegistryClient(this);
    _flightAuth = new FlightAuthorisationClient(this);

    // On login: fetch pilot profile + UAS list; propagate token to flight auth client.
    connect(_userAuth, &UserAuthentication::authenticationChanged, this, [this]() {
        if (!_userAuth->isAuthenticated()) {
            return;
        }
        const QString token = _userAuth->accessToken();
        _registry->setAccessToken(token);
        _flightAuth->setAccessToken(token);
        _registry->fetchPilotMe();
        _registry->fetchUasList();
    });

    connect(_registry, &RegistryClient::pilotFetched, this,
            [this](const QString& pilotId, const QString&) {
                _pilotId = pilotId;
                emit pilotChanged();
            });

    connect(_registry, &RegistryClient::uasListFetched, this, [this](const QVariantList& uasList) {
        _uasList = uasList;
        emit uasListChanged();
    });

    connect(
        _registry, &RegistryClient::fetchFailed, this, [](const QString& op, const QString& err) {
            qWarning("[pushpaka] registry fetch failed: %s — %s", qPrintable(op), qPrintable(err));
        });

    connect(_flightAuth, &FlightAuthorisationClient::autReceived, this,
            [this](const QString& signedJwt) {
                _autJwt = signedJwt;
                _hasValidAut = true;
                emit autChanged();
            });

    connect(_flightAuth, &FlightAuthorisationClient::submitFailed, this, [](const QString& err) {
        qWarning("[pushpaka] flight plan submission failed: %s", qPrintable(err));
    });
}

void PushpakaPlugin::cleanup() { QGCCorePlugin::cleanup(); }

const QVariantList& PushpakaPlugin::toolBarIndicators()
{
    if (_toolBarIndicators.isEmpty()) {
        _toolBarIndicators.append(QVariant::fromValue(
            QUrl::fromUserInput(QStringLiteral("qrc:/pushpaka/qml/PushpakaStatusIndicator.qml"))));
        for (const QVariant& v : QGCCorePlugin::toolBarIndicators()) {
            _toolBarIndicators.append(v);
        }
    }
    return _toolBarIndicators;
}

void PushpakaPlugin::submitFlightPlan(const QString& uasId, const QString& startTime,
                                      const QString& endTime)
{
    _flightAuth->submitFlightPlan(_pilotId, uasId, startTime, endTime);
}
