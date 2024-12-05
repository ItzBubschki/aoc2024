import utils.println
import utils.readInput

fun main() {
    fun List<Int>.algorithm(): Boolean {
        val decreasing = first() > this[1]
        return zipWithNext().all { (a, b) ->
            if (decreasing) {
                a - b in 1..3
            } else {
                b - a in 1..3
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ").map(String::toInt) }
            .filterNot { it.first() == it[1] }
            .count(List<Int>::algorithm)
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ").map(String::toInt) }.count {
            val sameStart = it.first() == it[1]
            val decreasing = if (sameStart) it[1] > it[2] else it.first() > it[1]
            var prev = it.first()
            val startIndex = if (sameStart) 2 else 1
            var failIndex = 1
            it.drop(startIndex).withIndex().all { (index, it2) ->
                if (decreasing) {
                    prev - it2 in 1..3
                } else {
                    it2 - prev in 1..3
                }.also {
                    prev = it2
                    failIndex = index
                }
            } || !sameStart && listOf(-1, 0, 1).any { add ->
                it.filterIndexed { i, _ -> i != failIndex + add }.algorithm()
            }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
