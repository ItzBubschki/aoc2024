import utils.*

fun main() {
    fun walkTrails(input: List<String>): List<List<List<Point>>> {
        val parsedInput = input.mapIndexed { y, row -> row.mapIndexed { x, c -> PointWithData(x, y, c.digitToInt()) } }
        val starts = parsedInput.flatten().filter { it.data == 0 }
        return starts.map {
            var nextHeight = 1
            var trails = listOf(listOf(it.point()))
            while (nextHeight < 10) {
                val newTrails = mutableListOf<List<Point>>()
                trails.forEach { t ->
                    val trailNextNeighbours = t.last().existingNeighbours(parsedInput)
                        .filter { n -> parsedInput[n.second][n.first].data == nextHeight }
                    if (trailNextNeighbours.isNotEmpty()) {
                        trailNextNeighbours.forEach { newTrailEnd ->
                            newTrails.add(t + newTrailEnd)
                        }
                    }
                }
                trails = newTrails
                nextHeight++
            }
            trails
        }
    }

    fun part1(input: List<String>): Int {
        return walkTrails(input).sumOf {
            it.map { trail -> trail.last() }.toSet().size
        }
    }

    fun part2(input: List<String>): Int {
        return walkTrails(input).sumOf { it.size }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
