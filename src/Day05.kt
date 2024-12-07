import utils.println
import utils.readInput

fun main() {
    fun parseInput(input: List<String>): Map<Int, List<Int>> {
        val emptyList = input.indexOf("")
        val firstSection = input.subList(0, emptyList)

        val constrains = mutableMapOf<Int, MutableList<Int>>()
        firstSection.forEach {
            it.split("|").map { num -> num.toInt() }.let { nums ->
                constrains[nums.first()] =
                    constrains.getOrDefault(nums.first(), mutableListOf()).apply { add(nums.last()) }
            }
        }
        return constrains
    }

    fun part1(input: List<String>): Int {
        val constrains = parseInput(input)
        val emptyList = input.indexOf("")
        val secondSection = input.drop(emptyList + 1)
        val parsed = secondSection.map { it.split(",").map(String::toInt) }
        return parsed.filter { row ->
            row.withIndex().all { (index, num) ->
                constrains[num]?.all {
                    row.indexOf(it) == -1 || row.indexOf(it) > index
                } ?: true
            }
        }.sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val constrains = parseInput(input)
        val emptyList = input.indexOf("")
        val secondSection = input.drop(emptyList + 1)
        val parsed = secondSection.map { it.split(",").map(String::toInt) }
        val incorrectRows = parsed.filterNot { row ->
            row.withIndex().all { (index, num) ->
                constrains[num]?.all {
                    row.indexOf(it) == -1 || row.indexOf(it) > index
                } ?: true
            }
        }
        return incorrectRows.map { row ->
            row to row.map {
                it to (constrains[it]?.filter { c -> row.contains(c) } ?: emptyList())
            }
        }.map { usedConstrains ->
            val newList = usedConstrains.first.toMutableList()
            usedConstrains.second.forEach {
                newList.remove(it.first)
                if (it.second.isEmpty()) {
                    newList.addLast(it.first)
                } else {
                    val newIndex =
                        it.second.minOf { c -> newList.indexOf(c) }.let { index -> if (index == -1) 0 else index }
                    newList.add(newIndex, it.first)
                }
            }
            newList
        }.sumOf { it[it.size / 2] }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
