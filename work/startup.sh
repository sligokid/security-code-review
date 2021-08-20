#!/usr/bin/env bash

JAVA_HEAP_INITIAL=384m
JAVA_HEAP_MAX=768m
JAVA_METASPACE_MAX=128m

java -Djava.security.egd=file:/dev/./urandom \
-Xms$JAVA_HEAP_INITIAL \
-Xmx$JAVA_HEAP_MAX \
-XX:MaxMetaspaceSize=$JAVA_METASPACE_MAX \
-jar /app.jar
