import kotlin.math.abs
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory

fun main() {
    fun part1(input: List<String>): Long {
        val redTiles = input.map { line ->
            val (x, y) = line.split(",").map { it.toLong() }
            x to y
        }

        var maxArea = 0L
        for (i in redTiles.indices) {
            for (j in i + 1 until redTiles.size) {
                val (x1, y1) = redTiles[i]
                val (x2, y2) = redTiles[j]

                val w = abs(x2 - x1) + 1
                val h = abs(y2 - y1) + 1
                val a = w * h

                maxArea = maxOf(maxArea, a)
            }
        }
        return maxArea
    }

    fun lineCrossProduct(px: Long, py: Long, x1: Long, y1: Long, x2: Long, y2: Long): Int {
        val cross = (x2 - x1) * (py - y1) - (y2 - y1) * (px - x1)
        return cross.compareTo(0)
    }

    fun isLineCrossProductZero(px: Long, py: Long, x1: Long, y1: Long, x2: Long, y2: Long) =
        lineCrossProduct(px, py, x1, y1, x2, y2) == 0

    fun isPointOnLine(px: Long, py: Long, x1: Long, y1: Long, x2: Long, y2: Long): Boolean {
        if (!isLineCrossProductZero (px, py, x1, y1, x2, y2)) {
            return false
        }
        return (px >= minOf(x1, x2) && px <= maxOf(x1, x2)) &&
               (py >= minOf(y1, y2) && py <= maxOf(y1, y2))
    }

    fun isPointInside(px: Long, py: Long, points: List<Pair<Long, Long>>): Boolean {
        var inside = false
        for (i in points.indices) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[(i + 1) % points.size]

            if (isPointOnLine(px, py, x1, y1, x2, y2)) {
                return true
            }
            if ((y1 > py) == (y2 > py)) {
                continue
            }
            if (px < (x2 - x1) * (py - y1) / (y2 - y1) + x1) {
                inside = !inside
            }
        }
        return inside
    }

    fun doLinesIntersect(
        a1: Pair<Long, Long>, a2: Pair<Long, Long>,
        b1: Pair<Long, Long>, b2: Pair<Long, Long>
    ): Boolean {
        val c1 = lineCrossProduct(a1.first, a1.second, a2.first, a2.second, b1.first, b1.second)
        val c2 = lineCrossProduct(a1.first, a1.second, a2.first, a2.second, b2.first, b2.second)
        val c3 = lineCrossProduct(b1.first, b1.second, b2.first, b2.second, a1.first, a1.second)
        val c4 = lineCrossProduct(b1.first, b1.second, b2.first, b2.second, a2.first, a2.second)
        return c1 * c2 < 0 && c3 * c4 < 0
    }

    fun isRectInside(x1: Long, x2: Long, y1: Long, y2: Long, points: List<Pair<Long, Long>>): Boolean {
        val corners = listOf(x1 to y1, x1 to y2, x2 to y1, x2 to y2)
        if (!corners.all { (cx, cy) -> isPointInside(cx, cy, points) }) {
            return false
        }

        val rectEdges = listOf(
            (x1 to y1) to (x2 to y1),
            (x2 to y1) to (x2 to y2),
            (x2 to y2) to (x1 to y2),
            (x1 to y2) to (x1 to y1)
        )

        for (i in points.indices) {
            val p1 = points[i]
            val p2 = points[(i + 1) % points.size]
            for ((start, end) in rectEdges) {
                if (doLinesIntersect(start, end, p1, p2)) {
                    return false
                }
            }
        }
        return true
    }

    fun part2(input: List<String>): Long {
        val points = input.map { line ->
            val (x, y) = line.split(",").map { it.toLong() }
            x to y
        }

        var maxArea = 0L

        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val a = points[i]
                val b = points[j]
                val x1 = minOf(a.first, b.first)
                val x2 = maxOf(a.first, b.first)
                val y1 = minOf(a.second, b.second)
                val y2 = maxOf(a.second, b.second)
                val area = (x2 - x1 + 1) * (y2 - y1 + 1)

                if (isRectInside(x1, x2, y1, y2, points)) {
                    maxArea = maxOf(maxArea, area)
                }
            }
        }

        return maxArea
    }

    fun part2UsingLib(input: List<String>): Long {
        val coords = input.map { line ->
            val (x, y) = line.split(",").map { it.toDouble() }
            Coordinate(x, y)
        }.toTypedArray()

        val factory = GeometryFactory()
        val poly = factory.createPolygon(coords + coords[0])

        var maxArea = 0L
        for (i in coords.indices) {
            val a = coords[i]
            for (j in coords.indices) {
                if (i == j) continue
                val b = coords[j]

                val envelope = Envelope(a, b)
                if(factory.toGeometry(envelope).within(poly)) {
                    val area = (abs(b.x - a.x) + 1) * (abs(b.y - a.y) + 1)
                    maxArea = maxOf(maxArea, area.toLong())
                }
            }
        }
        return maxArea
    }


    val day = 9
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 50, part1(testInput))
    check("Part 1", 4782151432, part1(input))

    check("Part 2 - Test", 24, part2(testInput))
    check("Part 2", 1450414119, part2(input))
}
