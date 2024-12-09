import utils.*

fun main() {
    fun traverse(start: Point, map: List<CharArray>): Pair<Set<Point>, Boolean> {
        val seen = mutableSetOf<Pair<Point, Direction>>()
        var location = start
        var direction = Direction.NORTH

        while (map[location] != null && (location to direction) !in seen) {
            seen += location to direction
            val next = location + direction

            if (map[next] == '#') direction = direction.rightTurn()
            else location = next
        }
        return seen.map { it.first }.toSet() to (map[location] != null)
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val start = map.findPointForChar('^')

        return traverse(start, map).first.count()
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val start = map.findPointForChar('^')

        return traverse(start, map)
            .first
            .filterNot { it == start }
            .count { candidate ->
                map[candidate] = '#'
                traverse(start, map).also { map[candidate] = '.' }.second
            }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
