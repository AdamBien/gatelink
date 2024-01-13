#!/bin/sh
mvn clean compile test-compile test
mvn quarkus:dev