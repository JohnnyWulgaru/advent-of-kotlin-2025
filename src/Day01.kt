fun main() {
    fun part1(input: List<String>): Int {
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

        return zeros
    }

    fun part2(input: List<String>): Int {
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

        return zeros.toInt()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    check(part1(testInput) == 3)
    part1(input).println()
    check(part1(input) == 1089)

    part2(testInput).println()
    check(part2(testInput) == 6)
    part2(input).println()
    check(part2(input) == 6530)
}

private fun Int.floorMod(mod: Int): Int {
    val r = this % mod
    return if (r < 0) r + mod else r
}
