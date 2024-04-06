#pragma once

#include <AP_HAL/AP_HAL.h>
#include <mbedtls/x509_crt.h>
#include <l8w8jwt/decode.h>

class AP_PushpakaTrustedFlight
{
public:
    void init();
    static AP_PushpakaTrustedFlight *get_singleton()
    {
        return _singleton;
    }
    AP_PushpakaTrustedFlight();
    bool is_trusted(char *buffer, size_t buflen);

private:
    void log_message(const char *message);
    bool validate_token(mbedtls_x509_crt *certificate);
    bool validate_certificate_chain(mbedtls_x509_crt* certificate_chain, const char *cn);
    bool read_certificate_from_file(const char *filepath, mbedtls_x509_crt* certificate);
    bool read_from_file(const char *filename, char **outbuf, size_t *outsize);
    bool write_pem_certificate(mbedtls_x509_crt* certificate, unsigned char **outbuf, size_t *outsize);

    const char *token_issuer_path = "@ROMFS/trusted_flight/token_issuer";
    const char *trusted_certificate_path = "@ROMFS/trusted_flight/root_ca.crt";

    const char *token_file_path = HAL_BOARD_STORAGE_DIRECTORY "/trusted_flight/token";
    const char *certificate_chain_path = HAL_BOARD_STORAGE_DIRECTORY "/trusted_flight/ca_chain.crt";
    const char *public_key_header = "-----BEGIN PUBLIC KEY-----\n";
    const char *public_key_footer = "-----END PUBLIC KEY-----\n";

    bool init_done = false;

    struct l8w8jwt_decoding_params params;
    struct mbedtls_x509_crt trusted_certificate;

    static AP_PushpakaTrustedFlight *_singleton;
};

namespace AP
{
AP_PushpakaTrustedFlight &pushpaka_trusted_flight();
};
