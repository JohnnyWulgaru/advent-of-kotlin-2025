fun main() {
    fun part1(input: List<String>): Long {
        // Join all lines, split by commas, and parse ranges "a-b"
        val tokens = input.joinToString("").split(",").map { it.trim() }.filter { it.isNotEmpty() }

        fun sumInvalid(l: Long, r: Long): Long {
            var total = 0L
            for (num in l..r) {
                val str = num.toString()
                val len = str.length
                if (len % 2 == 0) {
                    val mid = len / 2
                    val leftHalf = str.substring(0, mid)
                    val rightHalf = str.substring(mid)
                    if (leftHalf == rightHalf) {
                        total += num
                    }
                }
            }
            return total
        }

        var sum = 0L
        for (token in tokens) {
            val parts = token.split("-")
            val l = parts[0].toLong()
            val r = parts[1].toLong()
            sum += sumInvalid(l, r)
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val tokens = input.joinToString("").split(",").map { it.trim() }.filter { it.isNotEmpty() }

        fun isRepeatedPattern(s: String): Boolean {
            val n = s.length
            for (len in 1..n / 2) {
                if (n % len == 0) {
                    val pattern = s.substring(0, len)
                    if (pattern.repeat(n / len) == s) return true
                }
            }
            return false
        }

        fun sumInvalid(l: Long, r: Long): Long {
            var total = 0L
            for (num in l..r) {
                if (isRepeatedPattern(num.toString())) total += num
            }
            return total
        }

        isRepeatedPattern("999")

        var sum = 0L
        for (token in tokens) {
            val parts = token.split("-")
            val l = parts[0].toLong()
            val r = parts[1].toLong()
            sum += sumInvalid(l, r)
        }
        return sum
    }

    val day = 2
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 1227775554, part1(testInput))
    check("Part 1", 37314786486, part1(input))

    check("Part 2 - Test", 4174379265, part2(testInput))
    check("Part 2", 47477053982, part2(input))
}
