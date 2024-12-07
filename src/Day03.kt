import utils.println
import utils.readInput

data class InstanceIndexes(
    val mulIndexes: List<Pair<Int, Int>>,
    val doIndexes: List<Pair<Int, Int>>,
    val dontIndexes: List<Pair<Int, Int>>
)

enum class InstructionType {
    MUL, ACTIVATE, DEACTIVATE
}

fun main() {
    fun findMulInstances(input: String): List<String> {
        val regex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }

    fun findInstanceIndexes(input: String): InstanceIndexes {
        val mulRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
        val doRegex = """do\(\)""".toRegex()
        val dontRegex = """don't\(\)""".toRegex()

        val mulIndexes = mulRegex.findAll(input).map { it.range.first to it.range.last + 1 }.toList()
        val doIndexes = doRegex.findAll(input).map { it.range.first to it.range.last + 1 }.toList()
        val dontIndexes = dontRegex.findAll(input).map { it.range.first to it.range.last + 1 }.toList()

        return InstanceIndexes(mulIndexes, doIndexes, dontIndexes)
    }

    fun part1(input: List<String>): Int {
        return input.map { findMulInstances(it) }.flatten().sumOf { innerIt ->
            innerIt.replace("mul(", "").replace(")", "").split(",")
                .map { num -> num.toInt() }
                .reduce { acc, i -> acc * i }
        }
    }

    fun part2(input: List<String>): Int {
        val longInput = input.joinToString(separator = "")
        val indexes = findInstanceIndexes(longInput)

        val combinedList = mutableListOf<Pair<Pair<Int, Int>, InstructionType>>()

        combinedList.addAll(indexes.mulIndexes.map { it to InstructionType.MUL })
        combinedList.addAll(indexes.doIndexes.map { it to InstructionType.ACTIVATE })
        combinedList.addAll(indexes.dontIndexes.map { it to InstructionType.DEACTIVATE })

        var activated = true
        val activeInstructions = mutableListOf<String>()
        combinedList.sortedBy { it.first.first }.toMap().forEach {
            when (it.value) {
                InstructionType.MUL -> {
                    if (activated) {
                        activeInstructions.add(longInput.subSequence(it.key.first, it.key.second).toString())
                    }
                }

                InstructionType.ACTIVATE -> activated = true
                InstructionType.DEACTIVATE -> activated = false
            }
        }
        return activeInstructions.sumOf { innerIt ->
            innerIt.replace("mul(", "").replace(")", "").split(",")
                .map { num -> num.toInt() }
                .reduce { acc, i -> acc * i }
        }
    }


    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput("Day03")
    check(part1(input) == 190604937)
    part2(input).println()
}
