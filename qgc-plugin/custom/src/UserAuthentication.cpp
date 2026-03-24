#include "UserAuthentication.h"

#include <QtCore/QProcessEnvironment>
#include <QtCore/QUrl>
#include <QtDesktopServices/QDesktopServices>
#include <QtNetwork/QNetworkAccessManager>

UserAuthentication::UserAuthentication(QObject* parent)
    : QObject(parent)
{
    _oauth = new QOAuth2AuthorizationCodeFlow(this);
    _replyHandler = new QOAuthHttpServerReplyHandler(REDIRECT_PORT, this);

    const QString base = _keycloakBase();

    _oauth->setAuthorizationUrl(
        QUrl(base + "/protocol/openid-connect/auth"));
    _oauth->setAccessTokenUrl(
        QUrl(base + "/protocol/openid-connect/token"));
    _oauth->setClientIdentifier(CLIENT_ID);
    _oauth->setScope("openid profile email");
    _oauth->setReplyHandler(_replyHandler);

    // Open browser for the authorization step
    connect(_oauth, &QOAuth2AuthorizationCodeFlow::authorizeWithBrowser,
            &QDesktopServices::openUrl);

    connect(_oauth, &QOAuth2AuthorizationCodeFlow::granted,
            this, &UserAuthentication::_onGranted);

    connect(_oauth, &QOAuth2AuthorizationCodeFlow::error,
            this, &UserAuthentication::_onError);
}

void UserAuthentication::authorise()
{
    if (_isAuthenticated) {
        return;
    }
    _oauth->grant();
}

void UserAuthentication::logout()
{
    _isAuthenticated = false;
    _accessToken.clear();
    emit authenticationChanged();
}

void UserAuthentication::_onGranted()
{
    _isAuthenticated = true;
    _accessToken = _oauth->token();
    emit authenticationChanged();
}

void UserAuthentication::_onError(const QString& error,
                                   const QString& errorDescription,
                                   const QUrl& /*uri*/)
{
    _isAuthenticated = false;
    _accessToken.clear();
    emit authenticationFailed(error + ": " + errorDescription);
    emit authenticationChanged();
}

QString UserAuthentication::_keycloakBase() const
{
    const QString host = QProcessEnvironment::systemEnvironment()
        .value("KEYCLOAK_URL", DEFAULT_KEYCLOAK_URL);
    return host + "/realms/" + REALM;
}
