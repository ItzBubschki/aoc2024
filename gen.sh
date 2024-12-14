#!/bin/bash

# Prompt the user for a number
read -p "Enter a number: " number

# Create the necessary directories if they don't exist
mkdir -p inputs

# Create the files
touch "src/inputs/Day${number}.txt"
touch "src/inputs/Day${number}_test.txt"
touch "src/Day${number}.kt"

# Add the content to the Kotlin file
cat <<EOL > "src/Day${number}.kt"
import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day${number}_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Day${number}")
    part1(input).println()
    part2(input).println()
}
EOL

git add "src/Day${number}.kt"