@file:Suppress("unused")

package utils

val DirectionToPositionMap = mapOf(
    Direction.NORTH to (0 to -1),
    Direction.EAST to (1 to 0),
    Direction.SOUTH to (0 to 1),
    Direction.WEST to (-1 to 0)
)

enum class Direction(val char: Char, val sign: Char) {
    EAST('R', '>') {
        override val left get() = NORTH
        override val right get() = SOUTH
        override val opposite get() = WEST
    },
    SOUTH('D', 'v') {
        override val left get() = EAST
        override val right get() = WEST
        override val opposite get() = NORTH
    },
    WEST('L', '<') {
        override val left get() = SOUTH
        override val right get() = NORTH
        override val opposite get() = EAST
    },
    NORTH('U', '^') {
        override val left get() = WEST
        override val right get() = EAST
        override val opposite get() = SOUTH
    };

    abstract val left: Direction
    abstract val right: Direction
    abstract val opposite: Direction

    val pointPositiveDown: Point
        get() = DirectionPointMapping.downPositive[this]

    val pointPositiveUp: Point
        get() = DirectionPointMapping.upPositive[this]

    fun rightTurn() = when(this) {
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        NORTH -> EAST
    }

    fun isHorizontal() = when(this) {
        EAST, WEST -> true
        SOUTH, NORTH -> false
    }

    companion object {
        private val map = entries.associateBy(Direction::char)
        private val signMap = entries.associateBy(Direction::sign)

        fun fromChar(char: Char): Direction {
            return map[char] ?: throw IllegalArgumentException("Unknown direction char: $char")
        }

        fun fromSign(sign: Char): Direction {
            return signMap[sign] ?: throw IllegalArgumentException("Unknown direction sign: $sign")
        }

        fun fromPoints(start: Point, end: Point): Direction {
            return when {
                start.first < end.first -> EAST
                start.first > end.first -> WEST
                start.second < end.second -> SOUTH
                start.second > end.second -> NORTH
                else -> throw IllegalArgumentException("Points are the same: $start")
            }
        }
    }
}

class DirectionPointMapping(positiveY: Direction, positiveX: Direction) {

    val up = if (positiveY == Direction.NORTH) Point(0, 1) else Point(0, -1)
    val down = Point(0, -up.second)
    val left = if (positiveX == Direction.WEST) Point(1, 0) else Point(-1, 0)
    val right = Point(-left.first, 0)

    operator fun get(direction: Direction): Point {
        return when (direction) {
            Direction.NORTH -> up
            Direction.SOUTH -> down
            Direction.WEST -> left
            Direction.EAST -> right
        }
    }

    companion object {
        val downPositive = DirectionPointMapping(Direction.SOUTH, Direction.EAST)
        val upPositive = DirectionPointMapping(Direction.NORTH, Direction.EAST)
    }
}