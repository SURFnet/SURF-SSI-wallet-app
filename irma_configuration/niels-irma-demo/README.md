# The `irma-demo` scheme manager

This repository contains the credential definitions, issuer information, and their public *and* private keys of the `irma-demo` scheme manager.

***CAREFUL!*** This scheme manager is only ever meant to be used for development, demoing and experimenting! Because the issuer private keys are included in this repository everyone can issue any credential from this scheme manager, choosing the attributes freely. Thus their authenticity cannot be trusted. (For an actual scheme manager, see the [Privacy by Design Foundation scheme manager](https://github.com/privacybydesign/pbdf-schememanager).)

Use this repository by putting it in the `irma_configuration` folder of your project (for example, [the IRMA mobile app](https://github.com/privacybydesign/irma_mobile/tree/master/ios/irma_configuration) or the [IRMA API server](https://github.com/privacybydesign/irma_api_server/tree/master/src/main/resources)). Be sure to call the folder `irma-demo`! E.g.,

    git clone https://github.com/privacybydesign/irma-demo-schememanager irma-demo

## Directory structure

A scheme manager, issuer, or credential type (call it an *entity*) is always stored in `description.xml`, contained in a folder whose name *must* be that of the entity as specified by the xml file. Multiple issuers are grouped under the scheme manager, and each issuer may issue multiple credential types.

    SchemeManager
    +-- IssuerName
    |   +-- Issues
    |   |   +-- CredentialName
    |   |       +--- description.xml
    |   |       +--- logo.png
    |   +-- PublicKeys
    |   |   +-- 0.xml
    |   |   +-- 1.xml
    |   +-- PrivateKeys (need not be present)
    |   |   +-- 0.xml
    |   |   +-- 1.xml
    |   +-- description.xml
    |   +-- logo.png
    +-- description.xml
    +-- index
    +-- index.sig
    +-- pk.pem
    +-- timestamp

## Some notes on adding a new organization

First setup up the `description.xml` files of the scheme manager, issuers, and the credentials types that fall under your scheme manager, laying out the files as above. Make sure you add logos for your issuers and credential types.

Idemix public-private keypairs can be generated using the [irma](https://irma.app/docs/irma-cli/) command from [irmago](https://github.com/privacybydesign/irmago). Be sure to put the keys in the correct place in the directory tree. The default options should be ok for most situations. For example:

```
irma scheme issuer keygen path/to/issuer/directory
```

The `index` file must contain the SHA256-hash of each file along with its location in the directory tree; the `index.sig` file must contain an ECDSA signature over this file (which thus effectively signs the entire directory tree), and the public key of this signature must be in `pk.pem`. The [IRMA app](https://github.com/privacybydesign/irma_mobile) verifies this signature when starting and when downloading new scheme manager files, and will refuse to use the entire scheme manager when this signature verification fails. You can use the scheme subcommand of the [irma](https://irma.app/docs/irma-cli/) command from the [irmago](https://github.com/privacybydesign/irmago) repository to generate an ECDSA private-public keypair, the `index` file, and the `index.sig` signature file.

# Note

This repository contains the same tree as (the now deprecated) [github.com/credentials/irma_configuration](https://github.com/credentials/irma_configuration) but with the outer `irma_configuration` folder removed.
