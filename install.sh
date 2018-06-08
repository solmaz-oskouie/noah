#!/bin/bash

WORKSPACE=$(cd `dirname $0`; pwd)
cd $WORKSPACE

echo "Using JAVA_HOME:       $JAVA_HOME"
echo "Using MAVEN_HOME:      $MAVEN_HOME"
echo "Using MAVEN_OPTS:      $MAVEN_OPTS"

./mvnw -U clean install &&
./mvnw -U jetty:run --projects=noah-web --activate-profiles=dev

echo '[OK] SUCCESS'
