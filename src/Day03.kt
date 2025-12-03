fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { bank ->
            val digits = bank.map { it.digitToInt() }
            var max = 0
            for (i in digits.indices) {
                for (j in i + 1 until digits.size) {
                    val current = digits[i] * 10 + digits[j]
                    if (current > max) {
                        max = current
                    }
                }
            }

            max.toLong()
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { bank ->
            val n = bank.length
            val stack = mutableListOf<Char>()
            var toRemove = n - 12

            for (digit in bank) {
                while (stack.isNotEmpty() && toRemove > 0 && stack.last() < digit) {
                    stack.removeLast()
                    toRemove--
                }
                stack.add(digit)
            }

            while (toRemove > 0) {
                stack.removeLast()
                toRemove--
            }

            stack.joinToString("").toLong()
        }
    }

    val day = 3
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 357, part1(testInput))
    check("Part 1", 17034, part1(input))

    check("Part 2 - Test", 3121910778619, part2(testInput))
    check("Part 2", 168798209663590, part2(input))
}
