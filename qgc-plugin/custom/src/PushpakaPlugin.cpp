#include "PushpakaPlugin.h"

#include <QtCore/QCoreApplication>

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
}

void PushpakaPlugin::cleanup() { QGCCorePlugin::cleanup(); }
