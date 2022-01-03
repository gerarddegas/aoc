import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    /*
    crab submarines can only move horizontally
    num = 0
    cheapestOutcome = 0

    16,1,2,0,4,2,7,1,2,14
    0,1,1,2,2,2,4,7,14,16

    0
    O^n

    find the smallest and largest numbers horiz numbers
    .... for each num between 0..16
        cheapestOutcome = Math.max_int
        currTotalFuel = 0
            forEach crabPos
                amountOfFuel = abs(num-crabPos)
                currTotalFuel += amountOfFuel
        cheapestOutcome = math.min(cheapestOutcome, currTotalFuel)
        currTotalFuel = 0

     */
    fun parseInput(input: List<String>): List<Int> {
        return input[0].split(",").map { it.toInt() }
    }

    fun findLargestAndSmallest(positions: List<Int>): Pair<Int, Int> {
        var smallest = positions.minOrNull()!!
        var largest = positions.maxOrNull()!!
        return Pair(smallest, largest)
    }

    fun part1(input: List<String>): Int {
        val positions = parseInput(input)
        val (smallest, largest) = findLargestAndSmallest(positions)
        var cheapestOutcome = Int.MAX_VALUE
        for (possiblePos in smallest..largest) {
            var currTotalFuel = 0
            for (i in positions.indices) {
                val fuel = kotlin.math.abs(possiblePos - positions[i])
                currTotalFuel += fuel
            }
            cheapestOutcome = kotlin.math.min(cheapestOutcome, currTotalFuel)
        }

        return cheapestOutcome
    }
    /*


    part2

    16->5
    currTotal = 1
    for each idx in new num
        currTotal += idx
    15 1
    14 3
    13 6
    12 10
    11 15
    10 21
    9 28
    8 36
    7 45
    6 55
    5 66
    4 78
    3 91
    2 105
    1 120
    0 136

    16,1,2,0,4,2,7,1,2,14
    i
    position = 16
    target = 0
    cheapestOutcome = MAX
    currTotal = 0
    i = 0
     */

    fun part2(input: List<String>): Int {
        val positions = parseInput(input)
        val (smallest, largest) = findLargestAndSmallest(positions)
        var cheapestOutcome = Int.MAX_VALUE

        for (target in smallest..largest) {
            var currTotal = 0
            for (i in positions.indices) {
                val position = positions[i]
                val distance = (target - position).absoluteValue
                var totalFuel = distance * (distance + 1) / 2

                currTotal += totalFuel
            }
            cheapestOutcome = kotlin.math.min(cheapestOutcome, currTotal)
        }
        return cheapestOutcome
    }

    val input = readInput("Day07")
//    println(part1(input))
    println(part2(input))
}
