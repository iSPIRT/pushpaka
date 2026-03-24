#pragma once

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtNetworkAuth/QOAuth2AuthorizationCodeFlow>
#include <QtNetworkAuth/QOAuthHttpServerReplyHandler>

/**
 * Keycloak OAuth2 Authorization Code Flow for Pushpaka UTM.
 *
 * Realm:     pushpaka
 * Client ID: backend
 * Base URL:  http://localhost:18080  (override via KEYCLOAK_URL env var)
 */
class UserAuthentication : public QObject
{
    Q_OBJECT

    Q_PROPERTY(bool    isAuthenticated READ isAuthenticated NOTIFY authenticationChanged)
    Q_PROPERTY(QString accessToken     READ accessToken     NOTIFY authenticationChanged)

public:
    explicit UserAuthentication(QObject* parent = nullptr);
    ~UserAuthentication() override = default;

    bool    isAuthenticated() const { return _isAuthenticated; }
    QString accessToken()     const { return _accessToken; }

    Q_INVOKABLE void authorise();
    Q_INVOKABLE void logout();

signals:
    void authenticationChanged();
    void authenticationFailed(const QString& error);

private slots:
    void _onGranted();
    void _onError(const QString& error, const QString& errorDescription, const QUrl& uri);

private:
    QString _keycloakBase() const;

    static constexpr const char* DEFAULT_KEYCLOAK_URL = "http://localhost:18080";
    static constexpr const char* REALM               = "pushpaka";
    static constexpr const char* CLIENT_ID            = "backend";
    static constexpr int          REDIRECT_PORT        = 8000;

    QOAuth2AuthorizationCodeFlow* _oauth           = nullptr;
    QOAuthHttpServerReplyHandler* _replyHandler    = nullptr;
    bool                          _isAuthenticated = false;
    QString                       _accessToken;
};
