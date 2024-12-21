package utils

class AreaFinder<T>(private val input: List<List<PointWithData<T>>>) {
    private val area = mutableSetOf<PointWithData<T>>()

    fun getArea(start: PointWithData<T>): Set<PointWithData<T>> {
        if (area.isEmpty()) area.add(start)
        val sameNeighbours = start.point().existingNeighbours(input).mapNotNull { input.get2dOptional(it) }
            .filter { it.data == start.data && it !in area }
        area.addAll(sameNeighbours)
        sameNeighbours.forEach { getArea(it) }
        return area
    }
}