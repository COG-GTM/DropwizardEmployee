#!/bin/sh


curl -f -s http://localhost:8081/healthcheck > /dev/null

if [ $? -eq 0 ]; then
    echo "Health check passed"
    exit 0
else
    echo "Health check failed"
    exit 1
fi
