import utils.*

fun main() {
    // Helper function to build the map and collect unique characters
    fun buildMapAndChars(input: List<String>): Pair<List<PointWithData<Char>>, Set<Char>> {
        val uniqueChars = mutableSetOf<Char>()
        val map = input.flatMapIndexed { y, row ->
            row.mapIndexed { x, char ->
                PointWithData(x, y, char).also { if (char != '.') uniqueChars.add(char) }
            }
        }
        return map to uniqueChars
    }

    // Helper function to calculate mirrored positions
    fun calculateMirroredPositions(
        nodes: List<PointWithData<Char>>,
        mapBounds: Pair<Int, Int>,
        allowChains: Boolean
    ): Set<Point> {
        val positions = mutableSetOf<Point>()
        nodes.forEach { self ->
            nodes.forEach { other ->
                if (self != other) {
                    var start = other.point()
                    var next = self.point().mirror(start)
                    while (next.second in 0 until mapBounds.first && next.first in 0 until mapBounds.second) {
                        positions.add(next)
                        if (!allowChains) break
                        next = start.mirror(next).also { start = next }
                    }
                }
            }
        }
        return positions
    }

    fun part1(input: List<String>): Int {
        val (map, uniqueChars) = buildMapAndChars(input)
        val antiNodes = mutableSetOf<Point>()
        val mapBounds = input.size to input.first().length

        uniqueChars.forEach { char ->
            val nodesWithSameFrequency = map.filter { it.data == char }
            antiNodes += calculateMirroredPositions(nodesWithSameFrequency, mapBounds, allowChains = false)
        }

        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        val (map, uniqueChars) = buildMapAndChars(input)
        val antiNodes = mutableSetOf<Point>()
        val mapBounds = input.size to input.first().length

        uniqueChars.forEach { char ->
            val nodesWithSameFrequency = map.filter { it.data == char }
            antiNodes += calculateMirroredPositions(nodesWithSameFrequency, mapBounds, allowChains = true)
            if (nodesWithSameFrequency.size >= 2) {
                antiNodes += nodesWithSameFrequency.map { it.point() }
            }
        }

        return antiNodes.size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
