import utils.*

fun main() {
    fun parseAndGetShortestPath(
        map: List<List<PointWithData<Char>>>,
        start: PointWithData<Char>,
        end: PointWithData<Char>,
        fieldToAvoid: Point? = null
    ): GraphSearchResult<PointWithData<Char>> {
        return findShortestPathByPredicateWithDirections(
            start to Direction.EAST,
            { p -> p == end },
            {
                it.first.neighbours(map, '#')
                    .filter { p -> p.data != '#' && p.point() != it.first.point() + it.second.opposite }
            },
            { (from, direction), (to) -> if (from.point() + direction == to.point() && to.point() != fieldToAvoid) 1 else if (to.point() != fieldToAvoid) 1001 else 1000000 })
    }

    fun part1(input: List<String>): Int {
        val map = input.inputToClass(::PointWithData)
        val start = map.flatten().first { it.data == 'S' }
        val end = map.flatten().first { it.data == 'E' }
        return parseAndGetShortestPath(map, start, end).getScore()
    }

    fun part2(input: List<String>): Int {
        val map = input.inputToClass(::PointWithData)
        val start = map.flatten().first { it.data == 'S' }
        val end = map.flatten().first { it.data == 'E' }
        val shortestPath = parseAndGetShortestPath(map, start, end)
        val bestScore = shortestPath.getScore()
        val possibleFields = shortestPath.getPath().toMutableSet()
        possibleFields.addAll(
            shortestPath.getPath().asSequence().map { parseAndGetShortestPath(map, start, end, it.point()) }
                .filter { it.getScore() == bestScore }.map { it.getPath() }.flatten()
        )
        return possibleFields.count()
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 11048)
    check(part2(testInput) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
