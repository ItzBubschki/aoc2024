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
    return Pair(this.first * times, this.second * times)
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

fun Point.mirror(mirror: Point): Point {
    return mirror + (mirror - this)
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

fun Point.existingNeighbours(input: List<List<Any>>): List<Point> {
    return Direction.entries.map {
        DirectionToPositionMap.getNotNull(it) + this
    }.filter { it.second in input.indices && it.first in input.first().indices }
}

fun Point.getTouchingPoints(): List<Point> {
    return TouchingDirection.entries.map {
        TouchingDirectionToPositionMap.getNotNull(it) + this
    }
}

operator fun <E> Collection<Collection<E>>.contains(point: Point): Boolean =
    this.isNotEmpty() && point.second in this.indices && point.first in this.first().indices

fun <T>Point.countCorners(garden: List<List<T>>): Int =
    listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH)
        .zipWithNext()
        .map { (first, second) ->
            listOf(
                garden.get2dOptional(this),
                garden.get2dOptional(this + first),
                garden.get2dOptional(this + second),
                garden.get2dOptional(this + first + second)
            )
        }.count { (target, side1, side2, corner) ->
            (target != side1 && target != side2) ||
                    (side1 == target && side2 == target && corner != target)
        }