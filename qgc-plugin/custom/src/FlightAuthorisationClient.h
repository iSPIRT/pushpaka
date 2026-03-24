#pragma once

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkReply>
#include <QtNetwork/QNetworkRequest>

/**
 * HTTP client for the Pushpaka UTM Flight Authorisation service.
 *
 * submitFlightPlan() posts to POST /api/v1/flightPlan, then fetches the
 * resulting signed AUT via GET /api/v1/airspace-usage-tokens/by-flight-plan/{id}.
 *
 * Base URL: http://localhost:8083  (override via FLIGHT_AUTH_URL env var)
 */
class FlightAuthorisationClient : public QObject
{
    Q_OBJECT

public:
    explicit FlightAuthorisationClient(QObject* parent = nullptr);

    void setAccessToken(const QString& token) { _accessToken = token; }

    void submitFlightPlan(const QString& pilotId, const QString& uasId, const QString& startTime,
                          const QString& endTime);

signals:
    void autReceived(const QString& signedJwt);
    void submitFailed(const QString& error);

private:
    QString _flightAuthBase() const;
    QNetworkRequest _authorizedRequest(const QUrl& url) const;
    void _fetchAutForFlightPlan(const QString& flightPlanId);

    static constexpr const char* DEFAULT_FLIGHT_AUTH_URL = "http://localhost:8083";

    QNetworkAccessManager* _nam = nullptr;
    QString _accessToken;
};
