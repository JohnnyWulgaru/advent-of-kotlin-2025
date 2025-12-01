fun main() {
    fun part1(input: List<String>): Long {
        var position = 50
        var zeros = 0

        for (line in input) {
            val dir = line[0]
            val distance = line.substring(1).toInt()
            val move = distance % 100

            position = when (dir) {
                'L' -> (position - move).floorMod(100)
                'R' -> (position + move).floorMod(100)
                else -> error("Invalid direction: $dir")
            }

            if (position == 0) zeros++
        }

        return zeros.toLong()
    }

    fun part2(input: List<String>): Long {
        fun Int.move(distance: Int, dir: Int): Int {
            var z = 0
            var p = this
            repeat(distance) {
                p += dir
                if (p >= 100) p -= 100
                if (p < 0) p += 100
                if (p == 0) z++
            }
            return z
        }

        fun Int.moveBetter(distance: Int, dir: Int): Int {
            val firstDistanceToZero = when (dir) {
                1 -> if (this == 0) 100 else 100 - this
                -1 -> if (this == 0) 100 else this
                else -> error("Invalid direction delta: $dir")
            }

            if (distance < firstDistanceToZero) return 0
            return 1 + (distance - firstDistanceToZero) / 100
        }
        
        var position = 50
        var zeros = 0L

        for (line in input) {
            val dir = line[0]
            val distance = line.substring(1).toInt()

            val move = distance % 100

            when(dir) {
                'L' -> {
                    zeros += position.moveBetter(distance, -1)
                    position = (position - move).floorMod(100)
                }
                'R' -> {
                    zeros += position.moveBetter(distance, 1)
                    position = (position + move).floorMod(100)
                }
                else -> error("Invalid direction: $dir")
            }
        }

        return zeros
    }

    val day = 1
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 3, part1(testInput))
    check("Part 1", 1089, part1(input))

    check("Part 2 - Test", 6, part2(testInput))
    check("Part 2", 6530, part2(input))
}

private fun Int.floorMod(mod: Int): Int {
    val r = this % mod
    return if (r < 0) r + mod else r
}
