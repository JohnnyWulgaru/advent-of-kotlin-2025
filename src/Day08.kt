import kotlin.time.measureTime

fun main() {
    var nextCircuitId = 0L
    data class Boxes(val coord: Coord3d, val links: MutableList<Coord3d> = mutableListOf(), var circuitId: Long? = null)
    fun Boxes.isDirectlyConnected(otherBox: Boxes): Boolean {
        return this.links.contains(otherBox.coord)
    }

    fun Boxes.connectTo(otherBox: Boxes, allBoxes: List<Boxes>) {
        val thisCircuit = this.circuitId
        val otherCircuit = otherBox.circuitId

        val newCircuit = when {
            thisCircuit != null && otherCircuit != null -> {
                allBoxes.filter { it.circuitId == otherCircuit }.forEach { it.circuitId = thisCircuit }
                thisCircuit
            }
            thisCircuit != null -> thisCircuit
            otherCircuit != null -> otherCircuit
            else -> nextCircuitId++
        }

        this.circuitId = newCircuit
        otherBox.circuitId = newCircuit

        this.links.add(otherBox.coord)
        otherBox.links.add(this.coord)
    }

    fun parseBoxes(input: List<String>): List<Boxes> = buildList {
        for (line in input) {
            val (x, y, z) = line.split(",")
            add(Boxes(Coord3d(x.toLong(), y.toLong(), z.toLong())))
        }
    }

    fun findClosestBoxes(boxes: List<Boxes>): Pair<Boxes, Boxes>? {
        var minDistance = Double.MAX_VALUE
        var closestPair: Pair<Boxes, Boxes>? = null

        for (i in boxes.indices) {
            for (j in i + 1 until boxes.size) {
                if(boxes[i].isDirectlyConnected(boxes[j])) continue
                val distance = boxes[i].coord.distanceTo(boxes[j].coord)
                if (distance < minDistance) {
                    minDistance = distance
                    closestPair = boxes[i] to boxes[j]
                }
            }
        }

        return closestPair
    }

    fun toCircuits(boxes: List<Boxes>): Map<Long,List<Boxes>> {
        val circuits = mutableMapOf<Long, MutableList<Boxes>>()
        for(box in boxes) {
            if(box.circuitId == null) {
                circuits[nextCircuitId++] = mutableListOf(box)
            } else {
                circuits.getOrPut(box.circuitId!!) { mutableListOf() }.add(box)
            }
        }
        return circuits
    }

    fun part1(input: List<String>, connections: Int = 1000): Long {
        val boxes = parseBoxes(input)

        repeat(connections) {
            val pair = findClosestBoxes(boxes) ?: return@repeat
            pair.first.connectTo(pair.second, boxes)
        }

        val circuits = toCircuits(boxes)
        val sortedSizes = circuits.values.map { it.size }.sortedDescending()

        return sortedSizes.take(3).fold(1L) { acc, size -> acc * size }
    }

    fun part2(input: List<String>): Long {
        val boxes = parseBoxes(input)

        var lastPair: Pair<Boxes, Boxes>? = null
        while (true) {
            val circuits = toCircuits(boxes)
            if (circuits.size == 1) break

            val pair = findClosestBoxes(boxes) ?: break
            pair.first.connectTo(pair.second, boxes)
            lastPair = pair
        }

        return lastPair?.let { it.first.coord.x * it.second.coord.x } ?: 0L
    }

    val day = 8
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 40, part1(testInput, 10))
    val timePart1 = measureTime {
        check("Part 1", 164475, part1(input))
    }

    check("Part 2 - Test", 25272, part2(testInput))
    val timePart2 = measureTime {
        check("Part 2", 169521198, part2(input))
    }

    println("Part 1: ${timePart1.inWholeMilliseconds}ms")
    println("Part 2: ${timePart2.inWholeMilliseconds}ms")
}
