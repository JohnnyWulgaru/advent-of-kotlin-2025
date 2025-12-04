fun main() {
    data class MapSpec(val map: Map<Coordinate,Boolean>, val width: Int, val height: Int)

    fun parseMap(input: List<String>): MapSpec {
        val map = mutableMapOf<Coordinate, Boolean>()
        for(row in input.indices) {
            for(col in input[row].indices) {
                if(input[row][col] == '@') {
                  map[Coordinate(row, col)] = true
                }
            }
        }
        return MapSpec(map, input[0].length, input.size)
    }

    fun part1(input: List<String>): Long {
        val (map, width, height) = parseMap(input)
        var count = 0L

        for(x in 0 .. width) {
            for(y in 0 .. height) {
                val coord = Coordinate(x, y)
                if(map.containsKey(coord) && map[coord] == true) {
                    var adjacentCount = 0

                    for(direction in AllDirections.entries) {
                        if(map.containsKey(coord + direction) && map[coord + direction] == true) adjacentCount++
                    }

                    if (adjacentCount < 4) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Long {
        val (initialMap, width, height) = parseMap(input)
        val map = initialMap.toMutableMap()
        var totalRemoved = 0L

        while (true) {
            val toRemove = mutableListOf<Coordinate>()

            for (x in 0..width) {
                for (y in 0..height) {
                    val coord = Coordinate(x, y)
                    if (map.containsKey(coord) && map[coord] == true) {
                        var adjacentCount = 0

                        for (direction in AllDirections.entries) {
                            if (map.containsKey(coord + direction) && map[coord + direction] == true) {
                                adjacentCount++
                            }
                        }

                        if (adjacentCount < 4) {
                            toRemove.add(coord)
                        }
                    }
                }
            }

            if (toRemove.isEmpty()) break
            toRemove.forEach { map.remove(it) }
            totalRemoved += toRemove.size
        }

        return totalRemoved
    }

    val day = 4
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 13, part1(testInput))
    check("Part 1", 1480, part1(input))

    check("Part 2 - Test", 43, part2(testInput))
    check("Part 2", 8899, part2(input))
}
