import utils.Point
import utils.addWithWrap
import utils.println
import utils.readInput
import kotlin.math.ceil

data class Robot(var position: Point, val movement: Point) {
    fun move(x: Int, y: Int) {
        position = position.addWithWrap(movement, x to y)
    }
}

fun main() {
    fun List<String>.parseInput(): List<Robot> {
        return map { it.replace("p=", "").replace("v=", "") }.map {
            it.split(" ").let { p ->
                val position = p.first().split(",").map(String::toInt)
                val velocity = p.last().split(",").map(String::toInt)
                Robot(position.first() to position.last(), velocity.first() to velocity.last())
            }
        }
    }

    fun part1(input: List<String>, x: Int, y: Int): Int {
        val moved = input.parseInput().map { robot ->
            repeat(100) {
                robot.move(x, y)
            }
            robot
        }
        val (left, right) = moved.map { it.position }.partition { it.first + 1 < x / 2.0 }.let { (left, right) ->
            left to right.filter { it.first + 1 > ceil(x / 2.0) }
        }
        return listOf(left, right).map { list ->
            list.partition { point -> point.second + 1 < y / 2.0 }.let { (up, down) ->
                listOf(up, down.filter { it.second + 1 > ceil(y / 2.0) })
            }
        }.flatten().map { it.size }.reduce(Int::times)
    }

    fun part2(input: List<String>, x: Int, y: Int): Int {
        val robots = input.parseInput()
        (1..100000).forEach { index ->
            robots.forEach { it.move(x, y) }
            if (robots.map { it.position }.groupBy { it.second }.values.any { robots ->
                    robots.sortedBy { it.first }.zipWithNext().map { pair -> pair.second.first - pair.first.first - 1 }
                        .count { it == 0 } > 20
                }) return index
        }
        return -1
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, 11, 7) == 12)

    val input = readInput("Day14")
    part1(input, 101, 103).println()
    part2(input, 101, 103).println()
}
