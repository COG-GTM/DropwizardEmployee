#!/bin/sh


set -e

shutdown() {
    echo "Received shutdown signal, stopping application..."
    if [ ! -z "$SERVER_PID" ]; then
        kill -TERM "$SERVER_PID"
        wait "$SERVER_PID"
    fi
    exit 0
}

trap shutdown TERM INT

echo "Starting DropwizardEmployee service..."

echo "Running database migration..."
java -jar DropwizardEmployee-1.0.5.jar db migrate example.yml

if [ $? -ne 0 ]; then
    echo "Database migration failed!"
    exit 1
fi

echo "Database migration completed successfully"

echo "Starting application server..."
java -jar DropwizardEmployee-1.0.5.jar server example.yml &
SERVER_PID=$!

wait "$SERVER_PID"
