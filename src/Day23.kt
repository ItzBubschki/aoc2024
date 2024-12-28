import utils.*

fun main() {
    fun List<String>.parseInput(): Set<Node<String>> {
        val connections = map { it.split("-").let { p -> p.first() to p.last() } }
        val nodes =
            (connections.map(Pair<String, String>::first) + connections.map(Pair<String, String>::second)).toSet()
                .map(::Node).toSet()
        nodes.forEach { node ->
            node.connections.addAll(connections.filter { (a, b) -> a == node.source || b == node.source }
                .map { (a, b) -> if (a == node.source) b else a }
                .map { connection -> nodes.first { it.source == connection } })
        }
        return nodes
    }

    fun part1(input: List<String>): Int {
        val nodes = input.parseInput()

        val groups = nodes.map { node ->
            node.connections.allPairsUnidirectional().filter { (a, b) -> a.connections.contains(b) }
                .map { (a, b) -> listOf(node.source, a.source, b.source).sorted() }
        }.flatten().toSet()

        return groups.count { it.any { n -> n.startsWith("t") } }
    }

    fun part2(input: List<String>) =
        findLargestClique(input.parseInput()).map(Node<String>::source).sorted().joinToString(",")

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
