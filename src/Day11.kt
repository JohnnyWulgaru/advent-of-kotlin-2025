
object Day11 {
    data class CacheKey(
        val curr: String,
        val end: String,
        val hasVisitedDac: Boolean,
        val hasVisitedFft: Boolean
    )
    typealias Cache = MutableMap<CacheKey, Long>

    fun Cache(): Cache = mutableMapOf()

    @JvmStatic
    fun main(args: Array<String>) {
        fun parse(input: List<String>): Map<String, List<String>> {
            val nodes = mutableMapOf<String, List<String>>()
            for (line in input) {
                val (enter, rest) = line.split(": ")
                val destinations = rest.split(" ")
                nodes[enter] = destinations
            }
            nodes["out"] = emptyList()
            return nodes
        }

        fun countPaths(
            curr: String,
            end: String,
            dacVisited: Boolean,
            fftVisited: Boolean,
            allNodes: Map<String, List<String>>,
            cache: Cache
        ): Long {
            var visitedDac = dacVisited
            var visitedFft = fftVisited

            if (curr == "fft") {
                visitedDac = true
            }

            if (curr == "dac") {
                visitedFft = true
            }

            if (curr == end) {
                return if (visitedDac && visitedFft) 1 else 0
            }

            val key = CacheKey(curr, end, visitedDac, visitedFft)
            cache[key]?.let { return it }

            val result = allNodes[curr]?.sumOf { node ->
                countPaths(node, end, visitedDac, visitedFft, allNodes, cache)
            } ?: 0L

            cache[key] = result
            return result
        }

        fun part1(input: List<String>): Long {
            val nodes = parse(input)
            val cache = Cache()
            return countPaths("you", "out", dacVisited = true, fftVisited = true, nodes, cache)
        }

        fun part2(input: List<String>): Long {
            val nodes = parse(input)
            val cache = mutableMapOf<CacheKey, Long>()
            return countPaths("svr", "out", dacVisited = false, fftVisited = false, nodes, cache)
        }

        val day = 11
        val testInput = readInput("Day%02d_test".format(day))
        val testInput2 = readInput("Day%02d_test2".format(day))
        val input = readInput("Day%02d".format(day))

        check("Part 1 - Test", 5, part1(testInput))
        check("Part 1", 724, part1(input))

        check("Part 2 - Test", 2, part2(testInput2))
        check("Part 2", 473930047491888, part2(input))
    }
}
