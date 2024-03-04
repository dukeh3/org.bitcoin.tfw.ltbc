X509 Structure

There are several structures to deploy a X509 PKI 

1. Domain CA cert
2. Service cert
3. User cert

Configure Domain CA

This includes a certificate deployed to a certificate server

rest based service

Configure a service cert

Configure a user certificate

1. blockchain signed PKCS10 request.
2. register the certificate .
3. return the cert to the user.

The way to do it.

1. Register a zone example test.com
2. Create a zone CA -> ca@test.com
3. Install a zone CA server.
4. secure the DNS.
5. Deploy a HockeyPuck service for the PGP keys

Now we setup the blockchain. And run it with a reqtest.
