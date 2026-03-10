#!/bin/bash

# File names
MAIN="Main.java"
CLASS="Main"
INPUT="input.txt"
EXPECTED="expected.txt"
OUTPUT="output.txt"

echo "🔹 Compiling..."

javac "$MAIN"
if [ $? -ne 0 ]; then
    echo -e "\033[31m❌ Compilation Error\033[0m"
    exit 1
fi

echo "🔹 Running..."

START=$(date +%s%N 2>/dev/null)

java "$CLASS" < "$INPUT" > "$OUTPUT"

END=$(date +%s%N 2>/dev/null)

if [ -n "$START" ] && [ -n "$END" ]; then
    TIME=$((($END - $START)/1000000))
    echo "⏱ Execution Time: ${TIME} ms"
fi

echo "🔹 Checking..."

# Normalize files (remove CR and trailing spaces)
sed 's/\r$//' "$OUTPUT" | sed 's/[[:space:]]*$//' > output_clean.txt
sed 's/\r$//' "$EXPECTED" | sed 's/[[:space:]]*$//' > expected_clean.txt

if diff -wB output_clean.txt expected_clean.txt > /dev/null; then
    echo "✅ Accepted"
else
    echo "❌ Wrong Answer"
    echo
    echo "🔎 Difference:"
    diff -wB output_clean.txt expected_clean.txt
fi

# Cleanup
rm -f output_clean.txt expected_clean.txt
