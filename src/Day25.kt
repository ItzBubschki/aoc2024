import utils.*

fun main() {
    fun part1(input: List<String>): Int {
        val parts = input.splitInput()

        val (keys, locks) = parts.partition { it.split("\n").first().all { c -> c == '#' } }
            .callOnPair { part -> part.map { it.split("\n") }.map { it.rotate() } }
            .callOnPair { part -> part.map { key -> key.map { it.count { c -> c == '#' } - 1 } } }

        return (keys to locks).allPairs()
            .count { (key, lock) -> key.withIndex().all { (index, len) -> lock[index] + len <= 5 } }
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == 3)

    val input = readInput("Day25")
    part1(input).println()
}
