#include "FlightAuthorisationClient.h"

#include <QtCore/QJsonDocument>
#include <QtCore/QJsonObject>
#include <QtCore/QProcessEnvironment>

FlightAuthorisationClient::FlightAuthorisationClient(QObject* parent)
    : QObject(parent), _nam(new QNetworkAccessManager(this))
{
}

QString FlightAuthorisationClient::_flightAuthBase() const
{
    return QProcessEnvironment::systemEnvironment().value(QStringLiteral("FLIGHT_AUTH_URL"),
                                                          QStringLiteral(DEFAULT_FLIGHT_AUTH_URL));
}

QNetworkRequest FlightAuthorisationClient::_authorizedRequest(const QUrl& url) const
{
    QNetworkRequest req(url);
    req.setRawHeader("Authorization", QStringLiteral("Bearer %1").arg(_accessToken).toUtf8());
    req.setRawHeader("Content-Type", "application/json");
    req.setRawHeader("Accept", "application/json");
    return req;
}

void FlightAuthorisationClient::submitFlightPlan(const QString& pilotId, const QString& uasId,
                                                 const QString& startTime, const QString& endTime)
{
    QJsonObject body{
        {QStringLiteral("id"), QJsonValue(QStringLiteral("00000000-0000-0000-0000-000000000000"))},
        {QStringLiteral("pilot"), QJsonObject{{QStringLiteral("id"), pilotId}}},
        {QStringLiteral("uas"), QJsonObject{{QStringLiteral("id"), uasId}}},
        {QStringLiteral("start_time"), startTime},
        {QStringLiteral("end_time"), endTime},
    };

    QUrl url(_flightAuthBase() + QStringLiteral("/api/v1/flightPlan"));
    QNetworkReply* reply =
        _nam->post(_authorizedRequest(url), QJsonDocument(body).toJson(QJsonDocument::Compact));

    connect(reply, &QNetworkReply::finished, this, [this, reply]() {
        reply->deleteLater();
        if (reply->error() != QNetworkReply::NoError) {
            emit submitFailed(reply->errorString());
            return;
        }
        QJsonObject fp = QJsonDocument::fromJson(reply->readAll()).object();
        QString fpId = fp.value(QStringLiteral("id")).toString();
        if (fpId.isEmpty()) {
            emit submitFailed(QStringLiteral("flight plan response missing id"));
            return;
        }
        _fetchAutForFlightPlan(fpId);
    });
}

void FlightAuthorisationClient::_fetchAutForFlightPlan(const QString& flightPlanId)
{
    QUrl url(_flightAuthBase() + QStringLiteral("/api/v1/airspace-usage-tokens/by-flight-plan/") +
             flightPlanId);
    QNetworkReply* reply = _nam->get(_authorizedRequest(url));

    connect(reply, &QNetworkReply::finished, this, [this, reply]() {
        reply->deleteLater();
        if (reply->error() != QNetworkReply::NoError) {
            emit submitFailed(reply->errorString());
            return;
        }
        QJsonObject aut = QJsonDocument::fromJson(reply->readAll()).object();
        QString signedJwt = aut.value(QStringLiteral("signed_jwt")).toString();
        if (signedJwt.isEmpty()) {
            emit submitFailed(QStringLiteral("AUT response missing signed_jwt"));
            return;
        }
        emit autReceived(signedJwt);
    });
}
