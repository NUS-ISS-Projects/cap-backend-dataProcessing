#!/bin/bash

# Define your test message
TEST_MESSAGE='{"type": "EntityStatePdu", "entityId": {"site": 18, "application": 23, "entity": 88}, "location": {"x": 100.0, "y": 200.0, "z": 300.0}, "timestamp": 1672500000000}'

# Send the message using kafka-console-producer
docker exec -i kafka bash -c "echo '$TEST_MESSAGE' | kafka-console-producer --broker-list localhost:9092 --topic dis-pdus"

echo "Test message sent."
