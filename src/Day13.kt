import utils.Point
import utils.println
import utils.readInput

data class Machine(val buttonA: Point, val buttonB: Point, val destination: Point)

fun main() {
    fun Double.hasNoFractionalPart(): Boolean {
        return this == this.toLong().toDouble()
    }

    operator fun Point.plus(other: Pair<Long, Long>): Pair<Long, Long> {
        return (first + other.first to second + other.second)
    }

    /**
     * A = (p_x*b_y - p_y*b_x) / (a_x*b_y - a_y*b_x)
     * B = (a_x*p_y - a_y*p_x) / (a_x*b_y - a_y*b_x)
     **/
    fun Machine.cramer(offset: Long = 0L): Long {
        val offsetDestination = destination + (offset to offset)
        val divisor = buttonA.first * buttonB.second - buttonA.second * buttonB.first
        val aPresses =
            (offsetDestination.first * buttonB.second - offsetDestination.second * buttonB.first) / divisor.toDouble()
        val bPresses =
            (buttonA.first * offsetDestination.second - buttonA.second * offsetDestination.first) / divisor.toDouble()
        return if (listOf(aPresses, bPresses).all { it.hasNoFractionalPart() })
            bPresses.toLong() + (3 * aPresses.toLong()) else 0
    }

    fun List<String>.parseInput(): List<Machine> {
        val parts = joinToString("\n").split("\n\n")
        return parts.map {
            it.split("\n").let { m ->
                val buttons = m.map { line ->
                    val replaced = line.replace("=", "+")
                    replaced.substringAfter("X+").split(",").first().toInt() to replaced.substringAfter("Y+").trim()
                        .toInt()
                }
                Machine(buttons.first(), buttons[1], buttons.last())
            }
        }
    }

    fun part1(input: List<String>) = input.parseInput().sumOf(Machine::cramer)

    fun part2(input: List<String>) = input.parseInput().sumOf { it.cramer(10000000000000) }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)
    check(part2(testInput) == 875318608908)


    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
