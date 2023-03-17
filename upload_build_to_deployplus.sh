#!/bin/sh

curl --fail \
        -H "Authorization: ApiToken $DEPLOYPLUS_UPLOAD_TOKEN" \
        -F app=61dcf4fd-2624-4221-a0a0-c357b4405f03 \
        -F type=android \
        -F is_distribution_build=true \
        -F multipart=true \
        -F binary=@app/build/outputs/apk/release/eduwallet-release.apk \
        'https://api.deployplus.com/builds/upload/' || exit 1