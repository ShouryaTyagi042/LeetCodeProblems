#!/bin/bash

# File names
MAIN="Main.java"
CLASS="Main"
INPUT="input.txt"
EXPECTED="expected.txt"
OUTPUT="output.txt"

echo "🔹 Compiling..."

javac $MAIN
if [ $? -ne 0 ]; then
    echo -e "\033[31m❌ Compilation Error\033[0m"
    exit 1
fi

echo "🔹 Running..."

START=$(date +%s%N)

java $CLASS < $INPUT > $OUTPUT

END=$(date +%s%N)
TIME=$((($END - $START)/1000000))

echo "⏱ Execution Time: ${TIME} ms"

echo "🔹 Checking..."

if diff -w --strip-trailing-cr $OUTPUT $EXPECTED > /dev/null; then
    echo -e "✅ Accepted"
else
    echo -e "❌ Wrong Answer"
    echo
    echo "🔎 Difference:"
    diff -w --strip-trailing-cr $OUTPUT $EXPECTED
fi
