#!/bin/bash

# Function to get current timestamp in milliseconds
get_timestamp() {
    date +%s%3N  # %s for seconds since epoch, %3N for milliseconds
}

# Default PDU type if none specified
PDU_TYPE=${1:-"EntityStatePdu"}

# Test messages with dynamic timestamps
case "$PDU_TYPE" in
       "EntityStatePdu")
    TIMESTAMP=$(get_timestamp)
    TEST_MESSAGE=$(cat <<EOF
{
  "type": "EntityStatePdu",
  "entityId": {
    "site": 1,
    "application": 3001,
    "entity": 278
  },
  "forceId": 2,
  "numberOfArticulationParameters": 6,
  "entityType": {
    "entityKind": 1,
    "domain": 3,
    "country": 225,
    "category": 4,
    "subcategory": 0,
    "specific": 0,
    "extra": 0
  },
  "entityLinearVelocity": {
    "X": -3.57824,
    "Y": 4.99641,
    "Z": -3.7307
  },
  "entityLocation": {
    "X": -551760.45708376,
    "Y": -2240186.9551209,
    "Z": 2276600.91421459
  },
  "entityOrientation": {
    "psi": 2.2037,
    "theta": 0.541031,
    "phi": 2.1432
  },
  "timestamp": $TIMESTAMP
}
EOF
)
    ;;
    "FirePdu")
        TIMESTAMP=$(get_timestamp)
        TEST_MESSAGE="{\"type\": \"FirePdu\", \"firingEntityId\": {\"site\": 1, \"application\": 1, \"entity\": 1}, \"targetEntityId\": {\"site\": 2, \"application\": 2, \"entity\": 2}, \"munitionId\": {\"site\": 3, \"application\": 3, \"entity\": 3}, \"timestamp\": $TIMESTAMP}"
        ;;
    "both")
        # Send both messages with a slight delay for demonstration
        TIMESTAMP_ENTITY=$(get_timestamp)
        TEST_MESSAGE_ENTITY="{\"type\": \"EntityStatePdu\", \"entityId\": {\"site\": 18, \"application\": 23, \"entity\": 88}, \"location\": {\"x\": 100.0, \"y\": 200.0, \"z\": 300.0}, \"timestamp\": $TIMESTAMP_ENTITY}"
        docker exec -i kafka bash -c "echo '$TEST_MESSAGE_ENTITY' | kafka-console-producer --broker-list localhost:9092 --topic dis-pdus"
        echo "Sent EntityStatePdu test message."

        sleep 1  # Small delay to differentiate timestamps
        TIMESTAMP_FIRE=$(get_timestamp)
        TEST_MESSAGE_FIRE="{\"type\": \"FirePdu\", \"firingEntityId\": {\"site\": 1, \"application\": 1, \"entity\": 1}, \"targetEntityId\": {\"site\": 2, \"application\": 2, \"entity\": 2}, \"munitionId\": {\"site\": 3, \"application\": 3, \"entity\": 3}, \"timestamp\": $TIMESTAMP_FIRE}"
        docker exec -i kafka bash -c "echo '$TEST_MESSAGE_FIRE' | kafka-console-producer --broker-list localhost:9092 --topic dis-pdus"
        echo "Sent FirePdu test message."
        exit 0  # Exit after sending both
        ;;
    *)
        echo "Invalid PDU type. Use 'EntityStatePdu', 'FirePdu', or 'both'."
        exit 1
        ;;
esac

# Send the message using kafka-console-producer
docker exec -i kafka bash -c "echo '$TEST_MESSAGE' | kafka-console-producer --broker-list localhost:9092 --topic dis-pdus"

echo "Test message sent for $PDU_TYPE with timestamp $TIMESTAMP."
