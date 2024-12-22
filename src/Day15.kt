import utils.*

fun main() {
    fun isMoveable(start: Point, direction: Direction, map: List<List<PointWithData<Char>>>): Boolean {
        val neighbour = start + direction
        return when (val sign = map[neighbour].data) {
            '#' -> false
            '[', ']' -> {
                if (direction.isHorizontal()) {
                    isMoveable(neighbour, direction, map)
                } else {
                    val otherPartAt = if (sign == '[') Direction.EAST else Direction.WEST
                    val otherPart = neighbour + otherPartAt
                    listOf(neighbour, otherPart).all { isMoveable(it, direction, map) }
                }
            }

            else -> true
        }
    }

    fun moveInDirection(
        start: Point,
        direction: Direction,
        map: List<List<PointWithData<Char>>>,
        alreadyChecked: Boolean = false
    ): Boolean {
        val neighbour = start + direction
        val sign = map[neighbour].data

        return when (sign) {
            '#' -> false

            'O' -> {
                moveInDirection(neighbour, direction, map).also {
                    if (it) map[start].swap(map[neighbour])
                }
            }

            '[', ']' -> {
                if (direction.isHorizontal()) {
                    moveInDirection(neighbour, direction, map).also {
                        if (it) map[start].swap(map[neighbour])
                    }
                } else {
                    val isValid = (isMoveable(start, direction, map) || alreadyChecked)
                    if (isValid) {
                        val otherPartAt = if (sign == '[') Direction.EAST else Direction.WEST
                        val otherPart = neighbour + otherPartAt
                        moveInDirection(neighbour, direction, map, true).also {
                            map[start].swap(map[neighbour])
                        }
                        moveInDirection(otherPart, direction, map, true)
                    }
                    isValid
                }
            }

            else -> true.also { map[start].swap(map[neighbour]) }
        }
    }

    fun List<Point>.calculateGpsScore() = sumOf { (x, y) -> 100 * y + x }

    fun List<List<PointWithData<Char>>>.moveAndCalculate(instructions: List<Direction>): Int {
        var robot = flatten().first { it.data == '@' }

        instructions.forEach {
            if (moveInDirection(robot.point(), it, this)) {
                robot = this[robot.point() + it]
            }
        }

        return flatten().filter { it.data == '[' || it.data == 'O' }.map { it.point() }.calculateGpsScore()
    }

    fun part1(input: List<String>): Int {
        val parts = input.splitInput()
        val map = parts.first().split("\n").inputToClass(::PointWithData)
        val instructions = parts.last().replace("\n", "").map { Direction.fromSign(it) }

        return map.moveAndCalculate(instructions)
    }

    fun part2(input: List<String>): Int {
        val parts = input.splitInput()
        val map = parts.first().map {
            when (it) {
                '\n' -> "\n"
                'O' -> "[]"
                '#' -> "##"
                '.' -> ".."
                else -> "@."
            }
        }.joinToString("").split("\n").inputToClass(::PointWithData)

        val instructions = parts.last().replace("\n", "").map { Direction.fromSign(it) }
        return map.moveAndCalculate(instructions)
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
