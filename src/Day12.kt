import utils.*

fun main() {
    fun <T> Set<PointWithData<T>>.getPerimeter(input: List<List<PointWithData<T>>>): Int {
        if (isEmpty()) return 0
        return first().point().neighbours().map { input.get2dOptional(it) }
            .count { it == null || it.data != first().data } + toMutableSet().also { it.remove(first()) }
            .getPerimeter(input)
    }

    fun getAreas(input: List<String>): Pair<List<List<PointWithData<Char>>>, List<Set<PointWithData<Char>>>> {
        val parsed = input.mapIndexed { y, row -> row.mapIndexed { x, c -> PointWithData(x, y, c) } }
        val chars = parsed.flatten().map { it.data }.toSet()
        val areas = mutableListOf<Set<PointWithData<Char>>>()
        chars.forEach { char ->
            val remainingFieldsWithChar = parsed.flatten().filter { it.data == char }.toMutableList()
            while (remainingFieldsWithChar.isNotEmpty()) {
                val area = AreaFinder(parsed).getArea(remainingFieldsWithChar.first())
                remainingFieldsWithChar.removeAll(area)
                areas.add(area)
            }
        }
        return parsed to areas
    }

    fun part1(input: List<String>): Int {
        val (parsed, areas) = getAreas(input)
        return areas.sumOf { it.size * it.getPerimeter(parsed) }
    }

    fun part2(input: List<String>): Int {
        val (_, areas) = getAreas(input)
        val inputAsCharList = input.map { it.toCharArray().toList() }
        return areas.sumOf { (it.size * it.sumOf { p -> p.point().countCorners(inputAsCharList) }) }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 140)
    check(part2(testInput).also { it.println() } == 80)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
