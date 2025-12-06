import kotlin.streams.toList

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

    fun parse(input: List<String>): Map<Int,Column> {
        val lastIdx = input.lastIndex
        val operations = input[lastIdx].split(" ").map { it.trim() }.filter{ !it.isEmpty() }
        val numberRows = input.subList(0, lastIdx)

        val columns = mutableMapOf<Int,Column>()
        for(line in numberRows) {
            val numbers: List<Long> = line
                .split(" ")
                .map { it.trim() }
                .filter { !it.isEmpty() }
                .map { it.toLong() }
            for(i in numbers.indices) {
                if(!columns.contains(i)) {
                    columns[i] = Column()
                }
                columns[i]!!.numbers.add(numbers[i])
            }
        }

        for(i in operations.indices) {
            columns[i]!!.operation = operations[i].trim()[0]
        }
        return columns
    }

    fun part1(input: List<String>): Long {
        val cols = parse(input)
        val colSums =
            cols.values
                .map { it.calculate() }

        return colSums.sum()
    }

    fun part2(input: List<String>): Long {
        val transposed = input
            .map { it.toList() }

        val cols = mutableListOf<Long>()
        val numbers = mutableListOf<Long>()
        for(x in transposed[0].count()-1 downTo 0) {
            var op = ' '
            var number = ""
            for(y in 0 ..< input.count()) {
                val char = if(transposed[y].count() <= x) {
                    " "
                } else {
                    transposed[y][x]
                }
                when(char) {
                    '*' -> {
                        op = '*'
                        break
                    }
                    '+' -> {
                        op = '+'
                        break
                    }
                    else -> number += char
                }
            }
            if (op != ' ') {
                numbers.add(number.trim().toLong())
                when(op) {
                    '+' -> cols.add(numbers.sum())
                    '*' -> cols.add(numbers.fold(1L) { a, i -> a * i })
                }
                numbers.clear()
            } else {
                if(number.trim().isEmpty()) continue
                numbers.add(number.trim().toLong())
            }
        }

        return cols.sum()
    }

    val day = 6
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 4277556, part1(testInput))
    check("Part 1", 5361735137219, part1(input))

    check("Part 2 - Test", 3263827, part2(testInput))
    check("Part 2", 11744693538946, part2(input))
}
