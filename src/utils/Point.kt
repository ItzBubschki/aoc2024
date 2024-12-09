@file:Suppress("unused")

package utils

import kotlin.math.abs

typealias Point = Pair<Int, Int>

operator fun Point.plus(other: Point): Point {
    return Pair(this.first + other.first, this.second + other.second)
}

operator fun Point.plus(other: Direction): Point {
    val direction = DirectionToPositionMap.getNotNull(other)
    return this + direction
}

operator fun Point.minus(other: Point): Point {
    return Pair(this.first - other.first, this.second - other.second)
}

operator fun Point.times(times: Int): Point {
    return Pair(this.first*times, this.second*times)
}

operator fun Point.rem(bounds: Point): Point {
    val x = Math.floorMod(first, bounds.first)
    val y = Math.floorMod(second, bounds.second)
    return Point(x, y)
}

operator fun <T> List<List<T>>.get(position: Point): T {
    return this[position.second][position.first]
}

operator fun List<String>.get(position: Point): Char {
    return this[position.second][position.first]
}

fun <A> List<List<A>>.get2dOptional(position: Point): A? {
    return this.getOrNull(position.second)?.getOrNull(position.first)
}

fun Point.calculateDistance(other: Point): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

fun Point.addWithWrap(other: Point, size: Pair<Int, Int>): Point {
    val unwrapped = this + other
    return Math.floorMod(unwrapped.first, size.first) to Math.floorMod(unwrapped.second, size.second)
}

fun Point.neighbours(): List<Point> {
    return Direction.entries.map {
        DirectionToPositionMap.getNotNull(it) + this
    }
}

fun Point.getTouchingPoints(): List<Point> {
    return TouchingDirection.entries.map {
        TouchingDirectionToPositionMap.getNotNull(it) + this
    }
}

operator fun <E> Collection<Collection<E>>.contains(point: Point): Boolean =
    this.isNotEmpty() && point.second in this.indices && point.first in this.first().indices