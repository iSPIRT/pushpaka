#include "PushpakaPlugin.h"

#include <QtCore/QCoreApplication>

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

    // When the user logs in, fetch pilot profile + UAS list from the registry.
    connect(_userAuth, &UserAuthentication::authenticationChanged, this, [this]() {
        if (!_userAuth->isAuthenticated()) {
            return;
        }
        _registry->setAccessToken(_userAuth->accessToken());
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
}

void PushpakaPlugin::cleanup() { QGCCorePlugin::cleanup(); }
