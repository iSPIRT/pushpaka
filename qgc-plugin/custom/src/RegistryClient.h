#pragma once

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtCore/QVariantList>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkReply>
#include <QtNetwork/QNetworkRequest>

/**
 * HTTP client for the Pushpaka UTM Registry service.
 *
 * After the user authenticates, call fetchPilotMe() to resolve the pilot
 * UUID, then fetchUasList() to populate the aircraft selector.
 *
 * Base URL: http://localhost:8082  (override via REGISTRY_URL env var)
 */
class RegistryClient : public QObject
{
    Q_OBJECT

public:
    explicit RegistryClient(QObject* parent = nullptr);

    void setAccessToken(const QString& token) { _accessToken = token; }

    void fetchPilotMe();
    void fetchUasList();

signals:
    void pilotFetched(const QString& pilotId, const QString& displayName);
    void uasListFetched(const QVariantList& uasList);
    void fetchFailed(const QString& operation, const QString& error);

private:
    QString _registryBase() const;
    QNetworkRequest _authorizedRequest(const QUrl& url) const;

    static constexpr const char* DEFAULT_REGISTRY_URL = "http://localhost:8082";

    QNetworkAccessManager* _nam = nullptr;
    QString _accessToken;
};
