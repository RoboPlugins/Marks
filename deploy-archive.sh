#!/bin/bash

git config --global user.name $GIT_AUTHOR_NAME
git config --global user.email $GIT_AUTHOR_EMAIL

# Checkout Repo to deploy to
echo "  1. Checkout Repo to deploy to "

# Cleanup tmp directory
cd /tmp
rm -rf clonedir

cd /tmp
git clone https://${GH_OAUTH_TOKEN}@${GH_REF} clonedir

# copy jars to directory
echo " 2. copy jars to directory."
cp $TRAVIS_BUILD_DIR/build/PainPoints-*.zip /tmp/clonedir/PainPoints.zip

# Go to clone we created earlier.
echo "  3. Go to clone we created earlier.:"
cd /tmp/clonedir

# Add, commit, and push
echo "  4  . Add, commit, and push:"
git add *.zip
git commit -a -m "Committed by Travis-CI"
git push https://${GH_OAUTH_TOKEN}@${GH_REF} 2>&1