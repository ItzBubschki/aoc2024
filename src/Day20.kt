import utils.*

fun main() {
    fun part1(input: List<String>, countIf: Int = 100): Int {
        val map = input.inputToClass(::PointWithData)
        val start = map.flatten().first { it.data == 'S' }
        val end = map.flatten().first { it.data == 'E' }
        val legalPath = findShortestPathByPredicate(
            start,
            { p -> p == end },
            { it.neighbours(map).filter { n -> n.data != '#' } }
        )

        val possibleCheats = legalPath.getPath().map {
            it.neighbours(map).filter { n ->
                n.data == '#' && map.get2dOptional(n.point() + Direction.fromPoints(it.point(), n.point()))?.data == '.'
            }
        }.flatten().toSet()

        return findAlternativeRoutes(
            legalPath, possibleCheats
        ) { it, allowedWall ->
            it.neighbours(map).filter { n -> n.data != '#' || n == allowedWall }
        }.count { legalPath.getScore() - it.getScore() >= countIf }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput, 20) == 5)
    check(part2(testInput) == 0)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}


// find the shortest path
// go through path and check where a wall with thickness 1 is around
// add wall to allow list
// go through allow list and use "it" in the neighbour condition check or part