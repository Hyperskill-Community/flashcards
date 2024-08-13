#!/bin/bash
openssl req -newkey rsa:2048 -nodes -keyout server.key -subj "/CN=127.0.0.1" -out server.csr

openssl x509 -req -in server.csr -signkey server.key -out server.crt -days 365 -extfile <(printf "subjectAltName=DNS:localhost,IP:127.0.0.1")
