import utils.*
import java.util.*

fun main() {
    val smallKeyPad: List<List<PointWithData<Char>>> = listOf(
        listOf(PointWithData(0, 0, '.'), PointWithData(1, 0, '^'), PointWithData(2, 0, 'A')),
        listOf(PointWithData(0, 1, '<'), PointWithData(1, 1, 'v'), PointWithData(2, 1, '>'))
    )
    val largeKeyPad = listOf(
        listOf(PointWithData(0, 0, '7'), PointWithData(1, 0, '8'), PointWithData(2, 0, '9')),
        listOf(PointWithData(0, 1, '4'), PointWithData(1, 1, '5'), PointWithData(2, 1, '6')),
        listOf(PointWithData(0, 2, '1'), PointWithData(1, 2, '2'), PointWithData(2, 2, '3')),
        listOf(PointWithData(0, 3, '.'), PointWithData(1, 3, '0'), PointWithData(2, 3, 'A'))
    )

    fun List<List<PointWithData<Char>>>.allPaths(): Map<Pair<Char, Char>, List<String>> =
        flatten().filter { it.data != '.' }.allPairs()
            .associate {
                (it.first.data to it.second.data) to findAllShortestPathsByPredicate(
                    it.first,
                    { p -> p == it.second },
                    { p -> p.neighbours(this).filter { n -> n.data != '.' } }
                ).map(GraphSearchResult<PointWithData<Char>>::getPath)
                    .map { p ->
                        p.zipWithNext().map { (s, e) -> Direction.fromPoints(s.point(), e.point()).sign }
                            .joinToString("") + 'A'
                    }
            }

    val smallTravelOptions = smallKeyPad.allPaths()
    val largeTravelOptions = largeKeyPad.allPaths()

    fun findCost(
        code: String,
        depth: Int,
        transitions: Map<Pair<Char, Char>, List<String>> = largeTravelOptions,
        cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
    ): Long =
        cache.getOrPut(code to depth) {
            "A$code".zipWithNext().sumOf { transition ->
                val paths: List<String> = transitions.getValue(transition)
                if (depth == 0) {
                    paths.minOf(String::length).toLong()
                } else {
                    paths.minOf { path -> findCost(path, depth - 1, smallTravelOptions, cache) }
                }
            }
        }

    fun part1(input: List<String>) = input.sumOf {
        findCost(it, 2) * it.filter(Char::isDigit).toLong()
    }

    fun part2(input: List<String>) = input.sumOf {
        findCost(it, 25) * it.filter(Char::isDigit).toLong()
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 126384L)
    check(part2(testInput) == 154115708116294)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}