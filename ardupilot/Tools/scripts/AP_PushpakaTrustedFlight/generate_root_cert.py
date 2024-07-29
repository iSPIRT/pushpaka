#!/usr/bin/env python3
'''
script to generate self signed root certificate for Aerobridge Trusted Flight module testing
NOTE:   The certificate generated by this script is for only for demonstration & local testing. 
        It is not intended to be used in production systems.
'''

import os
import sys

from cryptography.hazmat.primitives import serialization

from utils.constants import ROOT_CA_DIR
from utils.helpers import create_csr, sign_csr

def create_root_cert(dir_path: str) -> None:
    key, csr = create_csr(cname='root.cname', is_ca=True)
    cert = sign_csr(csr=csr, signing_key=key)

    with open(f'{dir_path}/{ROOT_CA_DIR}/private.pem', 'wb') as f:
        f.write(key.private_bytes(encoding=serialization.Encoding.PEM,
                                  format=serialization.PrivateFormat.TraditionalOpenSSL,
                                  encryption_algorithm=serialization.NoEncryption()))
        print(f'Root private key written to file: {f.name}')

    with open(f'{dir_path}/{ROOT_CA_DIR}/certificate.crt', 'wb') as f:
        f.write(cert.public_bytes(encoding=serialization.Encoding.PEM))
        print(f'Root certificate written to file: {f.name}')


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: generate_root_cert.py <dir_name>")
        sys.exit(1)

    dir_name = sys.argv[1]
    os.makedirs(f'{dir_name}/{ROOT_CA_DIR}', exist_ok=True)

    create_root_cert(dir_name)