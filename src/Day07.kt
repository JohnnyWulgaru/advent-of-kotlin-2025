fun main() {
    data class Beam(val row: Int, val col: Int, val dirRow: Int, val dirCol: Int)

    fun part1(input: List<String>): Long {
        val startCol = input[0].indexOf('S')

        val queue = mutableListOf<Beam>()
        val visited = mutableSetOf<Beam>()
        val splitsSeen = mutableSetOf<Pair<Int, Int>>()

        queue.add(Beam(0, startCol, 1, 0))
        var splitCount = 0L
        while (queue.isNotEmpty()) {
            val beam = queue.removeAt(0)

            if (beam in visited) continue
            visited.add(beam)

            var row = beam.row
            var col = beam.col

            while (true) {
                row += beam.dirRow
                col += beam.dirCol

                if (row !in input.indices || col !in input[0].indices) {
                    break
                }

                if (input[row][col] == '^') {
                    if (Pair(row, col) !in splitsSeen) {
                        splitsSeen.add(Pair(row, col))
                        splitCount++
                        queue.add(Beam(row, col - 1, 1, 0))
                        queue.add(Beam(row, col + 1, 1, 0))
                    }
                    break
                }
            }
        }

        return splitCount
    }

    fun part2(input: List<String>): Long {
        val startCol = input[0].indexOf('S')
        val cache: MutableMap<Pair<Int,Int>, Long> = mutableMapOf()
        fun countTimelines(startRow: Int, startCol: Int): Long {
            return cache.getOrPut(Pair(startRow, startCol)) {
                var row = startRow
                val col = startCol

                while (true) {
                    row++

                    if (row !in input.indices || col !in input[0].indices) {
                        break
                    }

                    if (input[row][col] == '^') {
                        val leftTimelines = if (col - 1 in input[0].indices) {
                            countTimelines(row, col - 1)
                        } else {
                            0L
                        }
                        val rightTimelines = if (col + 1 in input[0].indices) {
                            countTimelines(row, col + 1)
                        } else {
                            0L
                        }
                        return@getOrPut leftTimelines + rightTimelines
                    }
                }
                return@getOrPut 1L
            }
        }

        return countTimelines(0, startCol)
    }

    val day = 7
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 21L, part1(testInput))
    check("Part 1", 1518L, part1(input))

    check("Part 2 - Test", 40L, part2(testInput))
    check("Part 2", 25489586715621L, part2(input))
}
