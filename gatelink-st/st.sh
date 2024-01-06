#!/bin/sh
echo "build"
mvn clean compile test-compile
echo "unit test"
mvn test
echo "system test"
mvn failsafe:integration-test failsafe:verify
