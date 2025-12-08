import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun check(s: String, expected: Long, solution: Long) {
    println(s)
    println("\tExpected: $expected, got: $solution")
    check(expected == solution) { "\tERROR: Expected $expected, got $solution" }
}

data class Coordinate(val x: Int, val y: Int)

fun Coordinate.plus(d: Directions): Coordinate
    = Coordinate(this.x + d.x, this.y + d.y)

operator fun Coordinate.plus(d: AllDirections): Coordinate
        = Coordinate(this.x + d.x, this.y + d.y)

enum class Directions(val x: Int, val y: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
}

enum class AllDirections(val x: Int, val y: Int) {
    UP_LEFT(-1, -1),
    UP(0, -1),
    UP_RIGHT(1, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    DOWN_LEFT(-1, 1),
    DOWN(0, 1),
    DOWN_RIGHT(1, 1)
}

data class Coord3d(val x: Long, val y: Long, val z: Long)

fun Coord3d.distanceTo(other: Coord3d): Double {
    val dx = (this.x - other.x).toDouble()
    val dy = (this.y - other.y).toDouble()
    val dz = (this.z - other.z).toDouble()
    return kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
}