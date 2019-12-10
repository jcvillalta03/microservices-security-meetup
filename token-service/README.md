# Token Service

This is a micronaut service to generate tokens, as well as generate a public JWK (TODO: fix)

## Retrieving test tokens

Test tokens can be retrieved by calling the `/token` endpoint with sample claims:

```
curl -X POST \
  http://localhost:8081/token \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 78' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8081' \
  -H 'cache-control: no-cache' \
  -d '{
	"userId": 1,
	"airline": "DL",
	"expirationTimeSec": 100,
	"role":"admin"
}'
```

and this token can then be used to invoke the airport service

## Verification of JWTs

an endpoint (`/token/keys.json`) exposes the public JWK to verify the tokens signed by this service. The private public key pairs are reset every time the token service is restarted.

Currently, the public JWK is not verifying correctly, so this will need to be investigated.