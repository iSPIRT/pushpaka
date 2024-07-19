#include "UserAuthentication.h"

#include <QtGui>
#include <QtCore>
#include <QtNetworkAuth>

UserAuthentication::UserAuthentication(QObject *parent)
    : QObject{parent}, m_isAuthenticated(false)
{
    m_oauth2.setAuthorizationUrl(QUrl("http://localhost:8080/realms/digitalsky/protocol/openid-connect/auth"));
    m_oauth2.setAccessTokenUrl(QUrl("http://localhost:8080/realms/digitalsky/protocol/openid-connect/token"));
    m_oauth2.setScope("basic");
    m_oauth2.setClientIdentifier("qgc");
    m_oauth2.setClientIdentifierSharedKey("Qhjp6iBEewABjX42jSuiuCpEWonRo83D");

    m_oauth2.setReplyHandler(new QOAuthHttpServerReplyHandler(8000, this));
    connect(&m_oauth2, &QOAuth2AuthorizationCodeFlow::statusChanged, [=](
                                                                       QAbstractOAuth::Status status) {
        if (status == QAbstractOAuth::Status::Granted) {
            qDebug() << "set authenticated TRUE";
                setAuthenticated(true);
        } else {
            qDebug() << "set authenticated FALSE";
            setAuthenticated(false);
        }
    });
    m_oauth2.setModifyParametersFunction([&](QAbstractOAuth::Stage stage, QVariantMap *parameters) {
        if (stage == QAbstractOAuth::Stage::RequestingAuthorization)
            parameters->insert("duration", "permanent");
    });

    connect(&m_oauth2, &QOAuth2AuthorizationCodeFlow::authorizeWithBrowser, &QDesktopServices::openUrl);
    /*
     * connect(&m_oauth2, &QOAuth2AuthorizationCodeFlow::stateChanged, [=](QAbstractOAuth::Status status) {
        if (status == QAbstractOAuth::Status::Granted) {
            setAuthenticated(true);
        } else {
            setAuthenticated(false);
        }
    });
*/

}

void UserAuthentication::setCredentials(const QString& clientId, const QString& clientSecret) {
    m_oauth2.setClientIdentifier(clientId);
    m_oauth2.setClientIdentifierSharedKey(clientSecret);
}

void UserAuthentication::authorise() {
    m_oauth2.grant();
}
