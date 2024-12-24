import utils.*

fun main() {
    fun parseInput(
        input: List<String>,
        boundaries: Int,
        take: Int
    ): Pair<List<List<PointWithData<Boolean>>>, List<Pair<Int, Int>>> {
        val map = (".".repeat(boundaries + 1) + "\n").repeat(boundaries + 1).split("\n").dropLast(1)
            .inputToClass { x, y, _ -> PointWithData(x, y, false) }
        val instructions = input.map { it.split(",").let { p -> p.first().toInt() to p.last().toInt() } }
        instructions.take(take).forEach {
            map[it].data = true
        }
        return map to instructions
    }

    fun part1(input: List<String>, boundaries: Int = 70, take: Int = 1024): Int {
        val map = parseInput(input, boundaries, take).first

        val path = findShortestPathByPredicate(
            map[0 to 0],
            { p -> p.point() == boundaries to boundaries },
            { it.neighbours(map, true).filterNot(PointWithData<Boolean>::data) }
        )
        return path.getScore()
    }

    fun part2(input: List<String>, boundaries: Int = 70, take: Int = 1024): Point {
        val (map, instructions) = parseInput(input, boundaries, take)
        return instructions.drop(take).first { instruction ->
            map[instruction].data = true
            findShortestPathByPredicate(
                map[0 to 0],
                { p -> p.point() == boundaries to boundaries },
                { it.neighbours(map, true).filterNot(PointWithData<Boolean>::data) }
            ).end == null
        }
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput, 6, 12) == 22)
    check(part2(testInput, 6, 12) == 6 to 1)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).let { "${it.first},${it.second}" }.println()
}
