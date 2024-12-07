import utils.*

fun main() {
    fun List<List<Char>>.getNeighboursWithChar(point: Point, searchingFor: Char): List<Point> {
        return point.getTouchingPoints().filter { get2dOptional(it) == searchingFor }
    }

    fun spellsWord(start: Point, second: Point, charMap: List<List<Char>>): Boolean {
        val word = "AS".toCharArray()

        val direction = second - start
        var last = second
        word.forEach {
            last += direction
            if (charMap.get2dOptional(last) != it) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val charMap = input.map { it.toCharArray().toList() }
        var count = 0
        input.mapIndexed { y, row ->
            row.mapIndexed { x, char ->
                if (char == 'X') {
                    val neighbours = charMap.getNeighboursWithChar(x to y, 'M')
                    count += neighbours.count { spellsWord(x to y, it, charMap) }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val charMap = input.map { it.toCharArray().toList() }
        var count = 0
        for ((y, row) in charMap.withIndex()) {
            if (y in listOf(0, charMap.size - 1)) continue
            for ((x, char) in row.withIndex()) {
                if (x in listOf(0, row.size - 1) || char != 'A') continue
                val neighbours = DiagonalNeighboursToPositionMap.mapNotNull {
                    charMap.get2dOptional(it.value + (x to y))?.let { value -> value to it.key }
                }
                neighbours.filter { it.first == 'M' }.apply {
                    if (size == 2 && all {
                            val newDirection = TouchingDirectionToPositionMap.getNotNull(it.second.opposite())
                            charMap.get2dOptional((x to y) + newDirection) == 'S'
                        }
                    ) {
                        count++
                    }
                }
            }
        }

        return count
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
