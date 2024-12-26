@file:Suppress("unused")

package utils

import java.util.*

typealias NeighbourFunction<K> = (K) -> Iterable<K>
typealias NeighbourFunctionDifferentReturn<K, T> = (K) -> Iterable<T>
typealias CostFunction<K> = (K, K) -> Int
typealias HeuristicFunction<K> = (K) -> Int

class GraphSearchResult<K>(val start: K, val end: K?, private val result: Map<K, SeenVertex<K>>) {
    fun getScore(vertex: K) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")

    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")
    fun seen(): Set<K> = result.keys
    fun end(): K = end ?: throw IllegalStateException("No path found")

    private tailrec fun getPath(endVertex: K, pathEnd: List<K>): List<K> {
        val previous = result[endVertex]?.prev

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous, listOf(endVertex) + pathEnd)
        }
    }
}

/**
 * Implements A* search to find the shortest path between two vertices using a predicate to determine the ending vertex
 */
fun <K> findShortestPathByPredicate(
    start: K,
    endFunction: (K) -> Boolean,
    neighbours: NeighbourFunction<K>,
    cost: CostFunction<K> = { _, _ -> 1 },
    heuristic: HeuristicFunction<K> = { 0 }
): GraphSearchResult<K> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start, 0, heuristic(start))))
    var endVertex: K? = null
    val seenPoints: MutableMap<K, SeenVertex<K>> = mutableMapOf(start to SeenVertex(0, null))

    while (endVertex == null) {
        if (toVisit.isEmpty()) {
            return GraphSearchResult(start, null, seenPoints)
        }

        val (currentVertex, currentScore) = toVisit.remove()
        endVertex = if (endFunction(currentVertex)) currentVertex else null

        val nextPoints = neighbours(currentVertex)
            .filter { it !in seenPoints }
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next), heuristic(next)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    return GraphSearchResult(start, endVertex, seenPoints)
}

fun <T> findShortestPathByPredicateWithDirections(
    start: Pair<PointWithData<T>, Direction>,
    endFunction: (PointWithData<T>) -> Boolean,
    neighbours: NeighbourFunctionDifferentReturn<Pair<PointWithData<T>, Direction>, PointWithData<T>>,
    cost: CostFunction<Pair<PointWithData<T>, Direction>> = { _, _ -> 1 },
    heuristic: HeuristicFunction<Pair<PointWithData<T>, Direction>> = { 0 }
): GraphSearchResult<PointWithData<T>> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start.first, 0, heuristic(start))))
    var endVertex: PointWithData<T>? = null
    val seenPoints: MutableMap<PointWithData<T>, SeenVertex<PointWithData<T>>> =
        mutableMapOf(start.first to SeenVertex(0, null))
    val directionMap: MutableMap<PointWithData<T>, Direction> = mutableMapOf(start.first to start.second)

    while (endVertex == null) {
        if (toVisit.isEmpty()) {
            return GraphSearchResult(start.first, null, seenPoints)
        }

        val (currentVertex, currentScore) = toVisit.remove()
        val currentDirection = directionMap[currentVertex] ?: start.second
        endVertex = if (endFunction(currentVertex)) currentVertex else null

        val nextPoints = neighbours(currentVertex to currentDirection)
            .filter { it !in seenPoints }
            .map { next ->
                val nextDirection = Direction.fromPoints(currentVertex.point(), next.point())
                directionMap[next] = nextDirection
                ScoredVertex(
                    next,
                    currentScore + cost(currentVertex to currentDirection, next to nextDirection),
                    heuristic(next to nextDirection)
                )
            }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    return GraphSearchResult(start.first, endVertex, seenPoints)
}

fun <K> findAllShortestPathsByPredicate(
    start: K,
    endFunction: (K) -> Boolean,
    neighbours: NeighbourFunction<K>,
): List<GraphSearchResult<K>> {
    val queue = PriorityQueue<Pair<List<K>, Int>>(compareBy { it.second })
        .apply { add(listOf(start) to 0) }
    val seen = mutableMapOf<K, Int>()
    var costAtGoal: Int? = null
    val allPaths: MutableList<Map<K, SeenVertex<K>>> = mutableListOf()

    while (queue.isNotEmpty()) {
        val (path, cost) = queue.poll()
        val location = path.last()

        if (costAtGoal != null && cost > costAtGoal) {
            return allPaths.map { GraphSearchResult(start, it.keys.first(), it) }
        } else if (endFunction(location)) {
            costAtGoal = cost
            val parsedPath = path.reversed().zipWithNext()
                .associate { (target, from) -> target to SeenVertex(seen[target] ?: cost, from) }.toMutableMap()
            parsedPath[path.first()] = SeenVertex(0, null)
            allPaths.add(parsedPath)
        } else if (seen.getOrDefault(location, Int.MAX_VALUE) >= cost) {
            seen[location] = cost
            neighbours(location)
                .filter { it !in path }
                .forEach { queue.add(path + it to cost + 1) }
        }
    }
    return allPaths.map { GraphSearchResult(start, it.keys.first(), it) }
}

fun <K> findAlternativeRoutes(
    comparison: GraphSearchResult<K>,
    specialTreatment: Set<K>,
    neighbours: (K, K) -> Iterable<K>
): List<GraphSearchResult<K>> {
    if (comparison.end == null) throw IllegalArgumentException("Can't find alternatives for unsuccessful route")
    return specialTreatment.map { s ->
        findShortestPathByPredicate(
            comparison.start,
            { p -> p == comparison.end },
            { neighbours(it, s) }
        )
    }.filter { it.end != null && it.getScore() == comparison.getScore() }
}

data class SeenVertex<K>(val cost: Int, val prev: K?)

data class ScoredVertex<K>(val vertex: K, val score: Int, val heuristic: Int) : Comparable<ScoredVertex<K>> {
    override fun compareTo(other: ScoredVertex<K>): Int = (score + heuristic).compareTo(other.score + other.heuristic)
}