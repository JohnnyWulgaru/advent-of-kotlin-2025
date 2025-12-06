fun main() {
    data class Column(
        var numbers: MutableList<Long> = mutableListOf<Long>(),
        var operation: Char = 'X',
    )

    fun Column.calculate(): Long {
        return when(operation) {
            '*' -> numbers.fold(1L) { acc, lng -> acc * lng }
            '+' -> numbers.fold(0L) { acc, lng -> acc + lng }
            else -> throw IllegalArgumentException("invalid operation")
        }
    }
    
    fun part1(input: List<String>): Long {
        if (input.isEmpty()) return 0L
        
        val maxWidth = input.maxOfOrNull { it.length } ?: 0
        if (maxWidth == 0) return 0L
        
        val lastIdx = input.lastIndex
        val operations = input[lastIdx].split(" ").map { it.trim() }.filter{ !it.isEmpty() }
        val numberRows = input.subList(0, lastIdx)
        
        val rows = mutableMapOf<Int,Column>()
        for(line in numberRows) {
            val numbers: List<Long> = line
                .split(" ")
                .map { it.trim() }
                .filter { !it.isEmpty() }
                .map { it.toLong() }
            for(i in numbers.indices) {
                if(!rows.contains(i)) {
                    rows[i] = Column()
                }
                rows[i]!!.numbers.add(numbers[i])
            }
        }

        for(i in operations.indices) {
            rows[i]!!.operation = operations[i].trim()[0]
        }

        val rowSums =
            rows.values
                .map { it.calculate() }

        return rowSums.sum()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val day = 6
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 4277556, part1(testInput))
    check("Part 1", 5361735137219, part1(input))

    check("Part 2 - Test", 3263827, part2(testInput))
    check("Part 2", 0, part2(input))
}
