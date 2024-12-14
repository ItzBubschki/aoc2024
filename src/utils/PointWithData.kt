@file:Suppress("unused")

package utils

import kotlin.math.abs

data class PointWithData<T>(val x: Int, val y: Int, val data: T)

operator fun <T> PointWithData<T>.plus(other: Point): PointWithData<T> {
    return PointWithData(this.x + other.first, this.x + other.second, data)
}

operator fun <T> PointWithData<T>.plus(other: Direction): PointWithData<T> {
    val direction = DirectionToPositionMap.getNotNull(other)
    return this + direction
}

operator fun <T> PointWithData<T>.minus(other: Point): PointWithData<T> {
    return PointWithData(this.x - other.first, this.y - other.second, data)
}

operator fun <T> PointWithData<T>.times(times: Int): PointWithData<T> {
    return PointWithData(this.x * times, this.y * times, data)
}

operator fun <T> PointWithData<T>.rem(bounds: Point): PointWithData<T> {
    val x = Math.floorMod(x, bounds.first)
    val y = Math.floorMod(y, bounds.second)
    return PointWithData(x, y, data)
}

fun <T> PointWithData<T>.calculateDistance(other: PointWithData<T>): Int {
    return abs(this.x - other.x) + abs(this.y - other.y)
}

fun <T> PointWithData<T>.point(): Point {
    return Point(x, y)
}