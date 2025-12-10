
import com.microsoft.z3.Expr
import com.microsoft.z3.FuncInterp


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

    fun findMinPresses(machine: Machine): Int {
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
