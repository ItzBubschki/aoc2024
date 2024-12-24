import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>) = Computer.of(input).runProgram().joinToString(",")

    //from TG
    fun part2(input: List<String>): Long {
        val computer = Computer.of(input)
        return computer.input
            .reversed()
            .map { it.toLong() }
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap { candidate ->
                    val shifted = candidate shl 3
                    (shifted..shifted + 8).mapNotNull { attempt ->
                        computer.copy().run {
                            a = attempt
                            attempt.takeIf { runProgram().first() == instruction }
                        }
                    }
                }
            }.first()
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == "5,7,3,0")
    check(part2(testInput) == 117440L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

data class Computer(var a: Long, private var b: Long, private var c: Long, val input: List<Int>) {
    private var instructionPointer = 0
    private var output = mutableListOf<Long>()
    private fun Int.getRealOperand() = when (this) {
        in 0..3 -> toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalArgumentException("Invalid operand $this")
    }

    private fun adv(operand: Int) {
        a = a shr operand.getRealOperand().toInt()
        instructionPointer += 2
    }

    private fun bxl(operand: Int) {
        b = b xor operand.toLong()
        instructionPointer += 2
    }

    private fun bst(operand: Int) {
        b = operand.getRealOperand() % 8
        instructionPointer += 2
    }

    private fun jnz(operand: Int) = if (a == 0L) {
        instructionPointer += 2
    } else {
        instructionPointer = operand
    }

    @Suppress("UNUSED_PARAMETER")
    private fun bxc(operand: Int) {
        b = b.xor(c)
        instructionPointer += 2
    }

    private fun outF(operand: Int) {
        output.add(operand.getRealOperand() % 8)
        instructionPointer += 2
    }

    private fun bdv(operand: Int) {
        b = a shr operand.getRealOperand().toInt()
        instructionPointer += 2
    }

    private fun cdv(operand: Int) {
        c = a shr operand.getRealOperand().toInt()
        instructionPointer += 2
    }

    private val opToInstructionMap = mapOf(
        0 to ::adv,
        1 to ::bxl,
        2 to ::bst,
        3 to ::jnz,
        4 to ::bxc,
        5 to ::outF,
        6 to ::bdv,
        7 to ::cdv
    )

    fun runProgram(): List<Long> {
        while (instructionPointer < input.size) {
            opToInstructionMap[input[instructionPointer]]!!(input[instructionPointer + 1])
        }
        return output.toList()
    }

    companion object {
        fun of(input: List<String>): Computer {
            return Computer(
                input.first().substringAfter(":").trim().toLong(),
                input[1].substringAfter(":").trim().toLong(),
                input[2].substringAfter(":").trim().toLong(),
                input.last().substringAfter(":").trim().split(",").map(String::toInt)
            )
        }
    }
}