import utils.println
import utils.readInput

fun main() {
    fun countPossibleDesigns(
        design: String,
        available: List<String>,
        cache: MutableMap<String, Long> = mutableMapOf()
    ): Long = if (design.isEmpty()) 1
    else cache.getOrPut(design) {
        available.filter { design.startsWith(it) }.sumOf {
            countPossibleDesigns(design.removePrefix(it), available, cache)
        }
    }

    fun List<String>.parseInput(): Pair<List<String>, List<String>> {
        val parts = joinToString("\n").split("\n\n")
        val available = parts.first().split(",").map(String::trim)
        val patterns = parts.last().split("\n")
        return available to patterns
    }

    fun part1(input: List<String>) =
        input.parseInput().let { (available, patterns) -> patterns.count { countPossibleDesigns(it, available) > 0 } }

    fun part2(input: List<String>) =
        input.parseInput().let { (available, patterns) -> patterns.sumOf { countPossibleDesigns(it, available) } }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
