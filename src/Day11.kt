import utils.hasEvenDigits
import utils.println
import utils.readInput
import utils.split

fun main() {
    val cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()
    fun Long.progressStone(times: Int, key: Pair<Long, Int> = this to times): Long =
            when {
                times == 0 -> 1L
                key in cache -> cache.getValue(key)
                else -> {
                    val result = when {
                        this == 0L -> (1L).progressStone(times - 1)
                        this.hasEvenDigits() -> this.split().sumOf { stone -> stone.progressStone(times - 1) }
                        else -> (this * 2024L).progressStone(times - 1)
                    }
                    cache[key] = result
                    result
            }
        }

    fun part1(input: List<String>) = input.first().split(" ").map(String::toLong).sumOf { it.progressStone(25) }

    fun part2(input: List<String>) = input.first().split(" ").map(String::toLong).sumOf { it.progressStone(75) }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
