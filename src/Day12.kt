fun main() {
    data class Region(val width: Int, val height: Int, val count: List<Int>)

    fun parseRegion(line: String): Region {
        val (dimPart, countsPart) = line.split(":", limit = 2).let {
            val left = it[0].trim()
            val right = if (it.size > 1) it[1].trim() else ""
            left to right
        }
        val (wStr, hStr) = dimPart.split("x", limit = 2)
        val width = wStr.trim().toInt()
        val height = hStr.trim().toInt()
        val count = if (countsPart.isBlank()) emptyList() else countsPart.split(" ").map { it.toInt() }
        return Region(width, height, count)
    }

    fun parseInputDay12(lines: List<String>): Pair<List<Int>, List<Region>> {
        val presents = mutableListOf<Int>()
        val regions = mutableListOf<Region>()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            if (line.isBlank()) { i++; continue }
            val header = line.trim()
            val beforeColon = header.substringBefore(":")
            val isRegionHeader = "x" in beforeColon
            if (isRegionHeader) break

            i++
            var countHash = 0
            while (i < lines.size && lines[i].isNotBlank()) {
                countHash += lines[i].count { it == '#' }
                i++
            }
            presents.add(countHash)
        }

        while (i < lines.size) {
            val raw = lines[i].trim()
            i++
            if (raw.isBlank()) continue
            regions.add(parseRegion(raw))
        }

        return presents to regions
    }

    fun checkRegion(presents: List<Int>, region: Region): Int {
        val (width, height, count) = region
        val pairs = minOf(presents.size, count.size)
        val total = (0 until pairs).sumOf { presents[it] * count[it] }
        if (total > width * height) return -1
        if ((width / 3) * (height / 3) >= count.sum()) return 1
        return 0
    }

    fun solve(presents: List<Int>, regions: List<Region>): List<Int> = regions.map { checkRegion(presents, it) }

    fun part1(input: List<String>): Long {
        val (presents, regions) = parseInputDay12(input)
        return solve(presents, regions).count { it == 1 }.toLong()
    }

    val day = 12
    val input = readInput("Day%02d".format(day))
    check("Part 1", 437, part1(input))
}
