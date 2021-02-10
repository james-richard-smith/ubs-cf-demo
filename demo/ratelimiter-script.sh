#!/bin/bash

for i in 1 2 3 4 5
do
    echo "Request $i"
    curl "https://{$1}/employee"
    echo ""
    sleep 0.1
done