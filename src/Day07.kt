import utils.getNotNull
import utils.println
import utils.readInput

enum class Operation {
    PLUS,
    TIMES,
    CONCAT
}

fun Operation.execute(first: Long, second: Long) = when (this) {
    Operation.PLUS -> first + second
    Operation.TIMES -> first * second
    Operation.CONCAT -> (first.toString() + second.toString()).toLong()
}

fun main() {
    fun applyOperations(
        parts: List<String>,
        lengthToOptionsMap: MutableMap<Int, List<List<Operation>>>,
        spaceCount: Int
    ): Long {
        val result = parts.first().toLong()
        val nums = parts.last().trim().split(" ").map { it.toLong() }
        return if (lengthToOptionsMap.getNotNull(spaceCount).any { eq ->
                var res = nums.first()
                eq.forEachIndexed { index, op ->
                    res = op.execute(res, nums[index + 1])
                }
                result == res
            }) result else 0L
    }

    fun solve(input: List<String>, useAll: Boolean): Long {
        val lengthToOptionsMap = mutableMapOf<Int, List<List<Operation>>>()
        return input.sumOf { line ->
            val parts = line.split(":")
            val spaceCount = parts.last().trim().count { c -> c == ' ' }

            lengthToOptionsMap.computeIfAbsent(spaceCount) {
                var equations = Operation.entries.filter { it != Operation.CONCAT || useAll }.map { mutableListOf(it) }
                for (i in 1 until spaceCount) {
                    equations = Operation.entries.filter { op -> op != Operation.CONCAT || useAll }.map { op ->
                        equations.map { it.toMutableList().apply { add(op) } }
                    }.fold(emptyList()) { acc, list -> acc + list }
                }
                equations
            }


            return@sumOf applyOperations(parts, lengthToOptionsMap, spaceCount)
        }
    }

    fun part1(input: List<String>) = solve(input, false)

    fun part2(input: List<String>) = solve(input, true)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
