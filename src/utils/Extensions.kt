@file:Suppress("unused")

package utils

import kotlin.math.absoluteValue


infix fun IntRange.intersects(other: IntRange): Boolean = first <= other.last && last >= other.first

tailrec fun Long.gcd(other: Long): Long {
    return if (this == 0L || other == 0L) {
        this + other
    } else {
        val bigger = maxOf(this.absoluteValue, other.absoluteValue)
        val smaller = minOf(this.absoluteValue, other.absoluteValue)
        (bigger % smaller).gcd(smaller)
    }
}

fun Long.lcm(other: Long) = if (this == 0L || other == 0L) {
    0
} else {
    (this * other).absoluteValue / this.gcd(other)
}

fun Int.mirroredIndex(mirror: Int): Int {
    return mirror - this + mirror + 1
}

fun Int.calculateColumnIndex(rows: Int, rotated: Boolean = false): Int {
    return if (rotated) (rows - this) % rows else this
}

fun String.getDifferenceCount(other: String): Int {
    require(this.length == other.length) { "Input strings must have the same length" }
    return this.zip(other).count { it.first != it.second }
}

fun String.replaceAt(index: Int, replacement: Char): String {
    if (index in indices) {
        val stringBuilder = StringBuilder(this)
        stringBuilder.setCharAt(index, replacement)
        return stringBuilder.toString()
    }
    return this
}

fun <A, B> Map<A, B>.getNotNull(key: A): B {
    return this[key]!!
}

fun <T> List<String>.inputToClass(transform: (Int, Int, Char) -> T): List<List<T>> {
    return this.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            transform(x, y, c)
        }
    }
}

fun Iterable<Int>.product(): Int = reduce(Int::times)

fun Iterable<Long>.product(): Long = reduce(Long::times)

fun List<CharArray>.findPointForChar(char: Char): Point {
    val y = this.indexOfFirst { it.contains(char) }
    val x = this[y].indexOf(char)
    return Point(x, y)
}

operator fun List<CharArray>.get(at: Point): Char? = getOrNull(at.second)?.getOrNull(at.first)

operator fun List<CharArray>.set(at: Point, c: Char) {
    this[at.second][at.first] = c
}

fun String.repeat(times: Int): String {
    return buildString {
        repeat(times) {
            append(this@repeat)
        }
    }
}

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    this[i] = this[j].also { this[j] = this[i] }
}

fun Long.hasEvenDigits(): Boolean =
    toString().length % 2 == 0

fun Long.split(): List<Long> {
    val s = toString()
    return listOf(
        s.substring(0, s.length / 2).toLong(),
        s.substring(s.length / 2, s.length).toLong()
    )
}

fun List<String>.splitInput() = joinToString("\n").split("\n\n")

fun <T> Collection<T>.allPairs(): List<Pair<T, T>> =
    flatMap { left ->
        map { right -> left to right }
    }

fun <T> Pair<Collection<T>, Collection<T>>.allPairs(): List<Pair<T, T>> = first.flatMap { left ->
    second.map { right -> left to right }
}

fun <T> Collection<T>.allPairsUnidirectional(): List<Pair<T, T>> =
    toList().let { list ->
        list.flatMapIndexed { index, first ->
            list.drop(index + 1).map { second ->
                first to second
            }
        }
    }

fun List<String>.rotate(clockwise: Boolean = true): List<String> {
    val rowCount = size
    val colCount = first().length
    return if (clockwise) {
        (0 until colCount).map { col ->
            (rowCount - 1 downTo 0).joinToString("") { row ->
                this[row][col].toString()
            }
        }
    } else {
        (0 until colCount).map { col ->
            (0 until rowCount).joinToString("") { row ->
                this[row][colCount - col - 1].toString()
            }
        }
    }
}

fun <T, R> Pair<T,T>.callOnPair(func: (T) -> R): Pair<R,R> {
    return func(first) to func(second)
}