#!/bin/sh
echo "build / unit test"
mvn clean package
echo "system test"
mvn failsafe:integration-test failsafe:verify
