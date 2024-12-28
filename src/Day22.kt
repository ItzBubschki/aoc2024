import utils.println
import utils.readInput

fun main() {
    fun Long.mix(other: Long) = other xor this
    fun Long.prune() = this % 16777216
    fun Long.first() = (this * 64L).mix(this).prune()
    fun Long.second() = (this / 32L).mix(this).prune()
    fun Long.third() = (this * 2048L).mix(this).prune()
    fun Long.process() = first().second().third()

    fun part1(input: List<String>) = input.map(String::toLong).sumOf {
        (1L..2000L).fold(it) { acc, _ -> acc.process() }
    }

    fun part2(input: List<String>): Int {
        val nums = input.map(String::toLong).map {
            var curr = it
            (1..2000).map { _ ->
                curr = curr.process()
                curr.toString().last().digitToInt()
            }
        }

        val localChanges = nums.map { it.drop(1).mapIndexed { index, num -> num to it[index + 1] - it[index] } }


        val changesWithHistory = localChanges.map { changes ->
            changes.drop(3).mapIndexed { index, (key, change) ->
                val history = (index until index + 3).mapNotNull { changes[it].second }
                key to (history + change).joinToString(",")
            }
        }

        val changeToNumMap = changesWithHistory.map {
            it.map(Pair<Int, String>::second).toSet()
                .associateWith { change -> it.first { (_, c) -> c == change }.first }
        }

        val allChangesSet = changeToNumMap.map(Map<String, Int>::keys).flatten().toSet()


        return allChangesSet.maxOf { change ->
            changeToNumMap.sumOf {
                it.getOrDefault(change, 0)
            }
        }
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 37990510L)
    check(part2(testInput) == 23)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
