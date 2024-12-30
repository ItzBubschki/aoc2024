import utils.println
import utils.readInput
import utils.splitInput

enum class GateOperation {
    XOR, OR, AND;

    fun execute(a: Int, b: Int) = when (this) {
        XOR -> a xor b
        OR -> a or b
        AND -> a and b
    }
}

data class Gate(val a: String, val b: String, val operation: GateOperation, val output: String)

fun main() {
    fun List<String>.parseInput(): Pair<List<Gate>, MutableMap<String, Int?>> {
        val parts = splitInput()
        val wires =
            parts.first().split("\n").associate { it.split(": ").let { p -> p.first() to p.last().toInt() as Int? } }
                .toMutableMap()
        val gates = parts.last().split("\n").map {
            it.split("->").let { p ->
                val inp = p.first().trim().split(" ")
                Gate(inp.first(), inp.last(), GateOperation.valueOf(inp[1]), p.last().trim())
            }
        }
        return gates to wires
    }

    fun part1(input: List<String>): Long {
        val (gates, wires) = input.parseInput()

        val remainingGates = ArrayDeque<Gate>().apply { addAll(gates) }
        while (remainingGates.isNotEmpty()) {
            val current = remainingGates.removeFirst()
            val a = wires[current.a]
            val b = wires[current.b]
            if (a != null && b != null) {
                wires[current.output] = current.operation.execute(a, b)
            } else {
                remainingGates.add(current)
            }
        }
        return gates.asSequence().map(Gate::output).filter { it.startsWith("z") }.sorted().withIndex()
            .sumOf { (index, wire) -> wires[wire]!!.toLong() shl index }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput) == 2024L)
    check(part2(testInput) == 0)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}
