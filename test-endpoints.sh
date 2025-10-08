#!/bin/bash

# Script to generate traffic and metrics for Grafana demonstration
# Usage: ./test-endpoints.sh

BASE_URL="http://localhost:8080/api"

echo "🚀 Starting load test to generate metrics..."
echo "Press Ctrl+C to stop"
echo ""

# Function to make a request
make_request() {
    local endpoint=$1
    local method=${2:-GET}

    if [ "$method" == "POST" ]; then
        curl -s -X POST "$BASE_URL/$endpoint" \
            -H "Content-Type: application/json" \
            -d '{"name":"test","value":123}' \
            > /dev/null
    else
        curl -s "$BASE_URL/$endpoint" > /dev/null
    fi
}

# Counter for tracking requests
counter=0

# Infinite loop to generate traffic
while true; do
    # Generate random traffic
    random=$((RANDOM % 100))

    if [ $random -lt 40 ]; then
        # 40% - hello endpoint
        make_request "hello"
        echo "✅ Request $counter: GET /api/hello"
    elif [ $random -lt 70 ]; then
        # 30% - random endpoint (70% success, 30% error)
        make_request "random"
        echo "🎲 Request $counter: GET /api/random"
    elif [ $random -lt 85 ]; then
        # 15% - slow endpoint
        make_request "slow"
        echo "🐌 Request $counter: GET /api/slow"
    elif [ $random -lt 95 ]; then
        # 10% - post data
        make_request "data" "POST"
        echo "📤 Request $counter: POST /api/data"
    else
        # 5% - error endpoint
        make_request "error"
        echo "❌ Request $counter: GET /api/error"
    fi

    counter=$((counter + 1))

    # Random sleep between requests (100-500ms)
    sleep_time=$(echo "scale=3; ($RANDOM % 400 + 100) / 1000" | bc)
    sleep $sleep_time

    # Show stats every 50 requests
    if [ $((counter % 50)) -eq 0 ]; then
        echo ""
        echo "📊 Total requests sent: $counter"
        echo "   View metrics at: http://localhost:8080/actuator/prometheus"
        echo "   View Grafana at: http://localhost:3000"
        echo ""
    fi
done