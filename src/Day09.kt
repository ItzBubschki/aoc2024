import utils.println
import utils.readInput
import utils.swap

fun main() {
    fun part1(input: List<String>): Long {
        val parsedInput = mutableListOf<String>()
        input.first().mapIndexed { index, it ->
            val blocks = it.digitToInt()
            val sign = if (index % 2 == 0) (index / 2).toString() else "."
            repeat(blocks) {
                parsedInput.add(sign)
            }
        }
        while (parsedInput.indexOfFirst { it == "." } != parsedInput.indexOfLast { it != "." } + 1) {
            parsedInput.swap(parsedInput.indexOfFirst { it == "." }, parsedInput.indexOfLast { it != "." })
        }
        val inputWithoutDots = parsedInput.dropLast(parsedInput.size - parsedInput.indexOfFirst { it == "." })
        return inputWithoutDots.withIndex().sumOf { (index, it) -> index * it.toLong() }
    }

    fun MutableList<Pair<Int, Int>>.mergeFreeSpaces() {
        val result = mutableListOf<Pair<Int, Int>>()
        var i = 0

        while (i < this.size) {
            if (this[i].first == -1) {
                var totalSize = this[i].second
                var j = i + 1
                while (j < this.size && this[j].first == -1) {
                    totalSize += this[j].second
                    j++
                }
                if (totalSize > 0) result.add(-1 to totalSize)
                i = j
            } else {
                result.add(this[i])
                i++
            }
        }

        clear()
        addAll(result)
    }

    fun part2(input: List<String>): Long {
        val parsedInput = mutableListOf<Pair<Int, Int>>()
        input.first().mapIndexed { index, it ->
            val blocks = it.digitToInt()
            val blockIndex = if (index % 2 == 0) (index / 2) else -1
            parsedInput.add(blockIndex to blocks)
        }
        parsedInput.mergeFreeSpaces()


        for (i in parsedInput.maxOf { it.first } downTo 0) {
            val realIndex = parsedInput.indexOfFirst { it.first == i }
            val index = parsedInput.indexOfFirst { it.first == -1 && it.second >= parsedInput[realIndex].second }
            if (index == -1 || index > realIndex) continue
            parsedInput[index] = parsedInput[index].first to parsedInput[index].second - parsedInput[realIndex].second
            parsedInput[realIndex] = -1 to parsedInput[realIndex].second
            parsedInput.add(index, i to parsedInput[realIndex].second)
            parsedInput.mergeFreeSpaces()
        }

        val finalResult = mutableListOf<String>()
        parsedInput.map {
            val blocks = it.second
            val sign = if (it.first == -1) "." else it.first.toString()
            repeat(blocks) {
                finalResult.add(sign)
            }
        }
        return finalResult.withIndex().sumOf { (index, it) -> if (it != ".") index * it.toLong() else 0 }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
