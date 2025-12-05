fun main() {
    fun part1(input: List<String>): Long {
        val blankLineIndex = input.indexOf("")
        val ranges = input.subList(0, blankLineIndex).map { line ->
            val (start, end) = line.split("-").map { it.toLong() }
            start..end
        }.sortedBy { it.first }
        val ingredientIds = input.subList(blankLineIndex + 1, input.size).map { it.toLong() }

        return ingredientIds.count { id ->
            ranges.any { range -> id in range }
        }.toLong()
    }

    fun part2(input: List<String>): Long {
        val blankLineIndex = input.indexOf("")
        val ranges = input.subList(0, blankLineIndex).map { line ->
            val (start, end) = line.split("-").map { it.toLong() }
            start to end
        }.sortedBy { it.first }

        println("Ranges Count: ${ranges.size}")

        // Merge overlapping ranges
        val merged = mutableListOf<Pair<Long, Long>>()
        for (range in ranges) {
            if (merged.isEmpty()) {
                merged.add(range)
            } else {
                if (merged.last().second < range.first - 1) {
                    merged.add(range)
                } else {
                    val last = merged.removeLast()
                    merged.add(last.first to maxOf(last.second, range.second))
                }
            }
        }

        println("Merged Count: ${merged.size}")

        return merged.sumOf { (start, end) -> end - start + 1 }
    }

    val day = 5
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 3, part1(testInput))
    check("Part 1", 862, part1(input))

    check("Part 2 - Test", 14, part2(testInput))
    check("Part 2", 357907198933892, part2(input))
}
