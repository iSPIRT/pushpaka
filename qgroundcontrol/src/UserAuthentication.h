#ifndef USERAUTHENTICATION_H
#define USERAUTHENTICATION_H

#include <QObject>
#include <QtNetwork>
#include <QtCore>
#include <QtQml/qqml.h>

#include <QOAuth2AuthorizationCodeFlow>

class UserAuthentication : public QObject
{
    Q_OBJECT
    QML_ELEMENT

    Q_PROPERTY(bool isAuthenticated READ isAuthenticated WRITE setAuthenticated NOTIFY isAuthenticatedChanged)
public:
    explicit UserAuthentication(QObject *parent = nullptr);
    void setAuthenticated(bool isAuthenticated) {
        if (m_isAuthenticated != isAuthenticated) {
            m_isAuthenticated = isAuthenticated;
            emit isAuthenticatedChanged();
        }
    }

    bool isAuthenticated() const {
        return m_isAuthenticated;
    }

    // QNetworkReply*


public slots:
    void setCredentials(const QString& clientId, const QString& clientSecret);
    void authorise();

signals:
    void isAuthenticatedChanged();

private:
    QOAuth2AuthorizationCodeFlow m_oauth2;
    bool m_isAuthenticated;
};

#endif // USERAUTHENTICATION_H
