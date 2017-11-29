#!/bin/bash

# build and upload
./gradlew install
./gradlew bintrayUplaod
