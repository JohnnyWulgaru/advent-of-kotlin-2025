
import com.microsoft.z3.Expr
import com.microsoft.z3.FuncInterp
import kotlin.time.measureTimedValue


fun main() {
    data class Machine(
        val targetLights: List<Boolean>,
        val buttons: List<List<Int>>,
        val joltage: List<Int>
    )

    fun parseMachine(line: String): Machine {
        val parts = line.split(" ")

        val targetLights = parts[0].trim('[', ']').map { it == '#' }

        val buttons = parts.subList(1, parts.size - 1).map { buttonStr ->
            buttonStr.trim('(', ')').split(",").map { it.toInt() }
        }

        val joltage = parts.last().trim('{', '}').split(",").map { it.toInt() }

        return Machine(targetLights, buttons, joltage)
    }


    // Recursive version with memoization
    fun findMinPressesRecursive(machine: Machine): Int {
        val initialState = List(machine.targetLights.size) { false }
        val memo = mutableMapOf<String, Int>()

        fun dfs(currState: List<Boolean>, depth: Int, visited: Set<String>): Int {
            if (currState == machine.targetLights) {
                return depth
            }

            val stateStr = currState.toString()
            memo[stateStr]?.let { cachedDepth ->
                if (cachedDepth <= depth) return Int.MAX_VALUE
            }
            memo[stateStr] = depth

            var minPresses = Int.MAX_VALUE

            for (button in machine.buttons) {
                val newState = currState.toMutableList()
                for (index in button) {
                    newState[index] = !newState[index]
                }

                val newStateStr = newState.toString()
                if (newStateStr !in visited) {
                    val result = dfs(newState, depth + 1, visited + newStateStr)
                    minPresses = minOf(minPresses, result)
                }
            }

            return minPresses
        }

        val result = dfs(initialState, 0, setOf(initialState.toString()))
        return if (result == Int.MAX_VALUE) 0 else result
    }

    // queue & visited lists
    fun findMinPressesQueue(machine: Machine): Int {
        val initialState = List(machine.targetLights.size) { false }

        val queue = ArrayDeque<Pair<List<Boolean>, Int>>()
        queue.add(Pair(initialState, 0))
        val visited = mutableSetOf(initialState.toString())

        while (queue.isNotEmpty()) {
            val (currState, depth) = queue.removeFirst()

            if (currState == machine.targetLights) {
                return depth
            }

            for (button in machine.buttons) {
                val newState = currState.toMutableList()
                for (index in button) {
                    newState[index] = !newState[index]
                }

                val stateStr = newState.toString()
                if (stateStr !in visited) {
                    visited.add(stateStr)
                    queue.add(Pair(newState, depth + 1))
                }
            }
        }

        return 0
    }

    fun findMinPresses(machine: Machine): Int {
        val (queueResult, queueTime) = measureTimedValue { findMinPressesQueue(machine) }
        val (recursiveResult, recursiveTime) = measureTimedValue { findMinPressesRecursive(machine) }

        println("Find Min Presses Durations:")
        println("\tQueue: ${queueTime.inWholeMilliseconds}ms")
        println("\tRecursive: ${recursiveTime.inWholeMilliseconds}ms")

        if(queueResult != recursiveResult) {
            throw IllegalStateException("Results don't match! Queue: $queueResult, Recursive: $recursiveResult")
        }

        return queueResult
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val machine = parseMachine(line)
            findMinPresses(machine).toLong()
        }
    }

    fun solveWithZ3(machine: Machine): Int {
        val ctx = com.microsoft.z3.Context()
        val solver = ctx.mkOptimize()

        // Setup buttons (can't be pressed negative times!)
        val pressCount = machine.buttons.indices.map { i ->
            ctx.mkIntConst("button_$i")
        }
        pressCount.forEach { v ->
            solver.Add(ctx.mkGe(v, ctx.mkInt(0)))
        }

        // For each light position, add constraint that sum of presses equals joltage
        for (pos in machine.joltage.indices) {
            val terms = machine.buttons.mapIndexed { btnIdx, button ->
                val coefficient = if (pos in button) 1 else 0
                ctx.mkMul(pressCount[btnIdx], ctx.mkInt(coefficient))
            }

            solver.Add(ctx.mkEq(
                ctx.mkAdd(*terms.toTypedArray()),
                ctx.mkInt(machine.joltage[pos])
            ))
        }


        // additional objective so we can minimize button presses
        val objective = ctx.mkAdd(*pressCount.toTypedArray())
        solver.MkMinimize(objective)

        if (solver.Check() == com.microsoft.z3.Status.SATISFIABLE) {
            val model = solver.model
            return pressCount.sumOf { v ->
                model.evaluate(v, false).toString().toInt()
            }
        }

        return 0
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val machine = parseMachine(line)
            solveWithZ3(machine).toLong()
        }
    }



    val day = 10
    val testInput = readInput("Day%02d_test".format(day))
    val input = readInput("Day%02d".format(day))

    check("Part 1 - Test", 7, part1(testInput))
    check("Part 1", 514, part1(input))

    check("Part 2 - Test", 33, part2(testInput))
    check("Part 2", 21824, part2(input))
}
