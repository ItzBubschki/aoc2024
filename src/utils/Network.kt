package utils

data class Node<T>(val source: T, val connections: MutableSet<Node<T>> = mutableSetOf()) {
    override fun toString(): String {
        return "$source: ${connections.map(Node<*>::source).joinToString()}"
    }

    override fun hashCode(): Int {
        return source.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (source != other.source) return false
        if (!connections.containsAll(connections.union(other.connections))) return false

        return true
    }
}

/**
 * Uses [Bron Kerbosch](https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm) to find the largest clique
 */
fun <T>findLargestClique(
    p: Set<Node<T>>,
    r: Set<Node<T>> = emptySet(),
    x: Set<Node<T>> = emptySet()
): Set<Node<T>> =
    if (p.isEmpty() && x.isEmpty()) r
    else {
        val withMostNeighbors: Node<T> = (p + x).maxBy { it.connections.size }
        p.minus(withMostNeighbors.connections).map { v ->
            findLargestClique(
                p intersect v.connections,
                r + v,
                x intersect v.connections
            )
        }.maxBy { it.size }
    }