@file:Suppress("unused")

package utils

enum class TouchingDirection {
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    LEFT,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
}

val TouchingDirectionToPositionMap = mapOf(
    TouchingDirection.TOP_LEFT to (-1 to -1),
    TouchingDirection.TOP to (0 to -1),
    TouchingDirection.TOP_RIGHT to (1 to -1),
    TouchingDirection.LEFT to (-1 to 0),
    TouchingDirection.RIGHT to (1 to 0),
    TouchingDirection.BOTTOM_LEFT to (-1 to 1),
    TouchingDirection.BOTTOM to (0 to 1),
    TouchingDirection.BOTTOM_RIGHT to (1 to 1)
)

val DiagonalNeighboursToPositionMap = mapOf(
    TouchingDirection.TOP_LEFT to (-1 to -1),
    TouchingDirection.TOP_RIGHT to (1 to -1),
    TouchingDirection.BOTTOM_LEFT to (-1 to 1),
    TouchingDirection.BOTTOM_RIGHT to (1 to 1)
)

fun TouchingDirection.opposite(): TouchingDirection {
    return when (this) {
        TouchingDirection.TOP_LEFT -> TouchingDirection.BOTTOM_RIGHT
        TouchingDirection.TOP -> TouchingDirection.BOTTOM
        TouchingDirection.TOP_RIGHT -> TouchingDirection.BOTTOM_LEFT
        TouchingDirection.LEFT -> TouchingDirection.RIGHT
        TouchingDirection.RIGHT -> TouchingDirection.LEFT
        TouchingDirection.BOTTOM_LEFT -> TouchingDirection.TOP_RIGHT
        TouchingDirection.BOTTOM -> TouchingDirection.TOP
        TouchingDirection.BOTTOM_RIGHT -> TouchingDirection.TOP_LEFT
    }
}