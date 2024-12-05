import utils.println
import utils.readInput
import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val parts = input.map { it.split(",") }
        val first = parts.map { it.first() }.map { it.toInt() }.sorted()
        val second = parts.map { it.last() }.map { it.toInt() }.sorted()
        return first.zip(second).sumOf { (a, b) -> (a - b).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val parts = input.map { it.split(",") }
        val first = parts.map { it.first() }.map { it.toInt() }
        val second = parts.map { it.last() }.map { it.toInt() }

        val scoreMap = mutableMapOf<Int, Int>()
        var score = 0
        first.forEach{ num ->
            score += num * scoreMap.computeIfAbsent(num) {second.count { it == num }}
        }
        return score
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
