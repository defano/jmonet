#!/bin/bash

echo -e "Checking to publish Javadocs...\n"
echo -e "$TRAVIS_REPO_SLUG"
echo -e "\n"
echo -e "$TRAVIS_JDK_VERSION"
echo -e "\n"
echo -e "$TRAVIS_PULL_REQUEST"
echo -e "\n"
echo -e "TRAVIS_BRANCH"


if [ "$TRAVIS_REPO_SLUG" == "defano/jmonet" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "feature/travis" ]; then

  echo -e "Publishing JavaDocs...\n"

  cp -R target/site/apidocs $HOME/javadoc-latest

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/defano/jmonet gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./javadoc
  cp -Rf $HOME/javadoc-latest ./javadoc
  git add -f .
  git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi