#include "RegistryClient.h"

#include <QtCore/QJsonArray>
#include <QtCore/QJsonDocument>
#include <QtCore/QJsonObject>
#include <QtCore/QProcessEnvironment>

RegistryClient::RegistryClient(QObject* parent)
    : QObject(parent)
    , _nam(new QNetworkAccessManager(this))
{
}

QString RegistryClient::_registryBase() const
{
    return QProcessEnvironment::systemEnvironment().value(
        QStringLiteral("REGISTRY_URL"), QStringLiteral(DEFAULT_REGISTRY_URL));
}

QNetworkRequest RegistryClient::_authorizedRequest(const QUrl& url) const
{
    QNetworkRequest req(url);
    req.setRawHeader("Authorization",
                     QStringLiteral("Bearer %1").arg(_accessToken).toUtf8());
    req.setRawHeader("Accept", "application/json");
    return req;
}

void RegistryClient::fetchPilotMe()
{
    QUrl url(_registryBase() + QStringLiteral("/api/v1/pilots/me"));
    QNetworkReply* reply = _nam->get(_authorizedRequest(url));
    connect(reply, &QNetworkReply::finished, this, [this, reply]() {
        reply->deleteLater();
        if (reply->error() != QNetworkReply::NoError) {
            emit fetchFailed(QStringLiteral("pilots/me"), reply->errorString());
            return;
        }
        QJsonObject obj =
            QJsonDocument::fromJson(reply->readAll()).object();
        QString pilotId = obj.value(QStringLiteral("id")).toString();
        emit pilotFetched(pilotId, pilotId);
    });
}

void RegistryClient::fetchUasList()
{
    QUrl url(_registryBase() + QStringLiteral("/api/v1/uas"));
    QNetworkReply* reply = _nam->get(_authorizedRequest(url));
    connect(reply, &QNetworkReply::finished, this, [this, reply]() {
        reply->deleteLater();
        if (reply->error() != QNetworkReply::NoError) {
            emit fetchFailed(QStringLiteral("uas"), reply->errorString());
            return;
        }
        QVariantList uasList =
            QJsonDocument::fromJson(reply->readAll()).array().toVariantList();
        emit uasListFetched(uasList);
    });
}
