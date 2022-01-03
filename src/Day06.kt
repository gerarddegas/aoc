import java.math.BigInteger

fun main() {
    /*
    each fish creates a new fish every 7 days
    day1
    1 fish
    day7
    2 fish
    day14
    4 fish

    model of fish
    Fish {
        daysRemaining: 8
    }

    when spawning a new fish, the internal timer is set to 8
    the current fish get's its timer reset to 6 after spawning a new one

    Initial state: 3,4,3,1,2 countOfNewFish = 0 daysRemaining = 3
                   i
    Day1           [2,3,2,0,1] countOfNewFish = 1 daysRemaining = 2
                            i
    Day2            1,2,1,6,0,8   countOfNewFish = 0 daysRemaining = 1
                              i
    Day3            0,1,0,5,6,8 countOfNewFish = 1 daysRemaining = 0

    Day4            0,1,0,5,6,8,8 countOfNewFish = 1 daysRemaining = 0
    input: [1,2,3,4]
    out: 10

     */
    fun spawnFish(fishList: MutableList<Int>, newFishCount: Int, daysRemaining: Int): Int {
        // base case
        // daysRemaining === 0
        if(daysRemaining < 0) {
            return fishList.size
        }
        // for each newFishCount
            // add 8 to fishList
        // newFishCount = 0
        // for each fish
            // decrement the fish count
            // if curr fish count is < 0
            // newFishCount += 1
            // newFishCount = 6
        // return spawnFish(newFishList, newFishCount, daysRemaining - 1)
        for (i in 0 until newFishCount) {
            fishList.add(8)
        }

        var nextNewFishCount = 0 // 0
        val newList = fishList.map {
            var newCurrCount = it - 1
            if(newCurrCount < 0) {
                nextNewFishCount += 1
                newCurrCount = 6
            }
            newCurrCount
        }.toMutableList()
        return spawnFish(newList, nextNewFishCount, daysRemaining - 1)
    }

    fun spawnFish2(fishList: MutableList<Int>): Int {
        for (i in 0 until 80) {
            for (j in 0 until fishList.size) {
                val fish = fishList[j]
                var newCurrCount = fish - 1
                if(newCurrCount < 0) {
                    fishList.add(8)
                    newCurrCount = 6
                }
                fishList[j] = newCurrCount
            }
        }
        return fishList.size
    }

    fun part1(input: String): Int {
//        var countOfNewFish = 0
//        var daysRemaining = 256

//        return spawnFish(newInput, countOfNewFish, daysRemaining)
        val newInput = input.split(",").map{ it.toInt()}.toMutableList()
        return spawnFish2(newInput)
    }
    /*
        3,4,3,1,2
        0,1,1,2,1,0,0,0,0
        lastEl = 0
        toBeSpawned = 0
        1,1,2,1,0,0,0,0,0
        lastEl = 0
        toBeSpawned = 1
        1,2,1,0,0,0,

        create a list of length 9 with default big ints

        shuffleDay
         lastEl = last element
         toBeSpawned = first element
         move all the elements down one index
         bigInt[6] += toBeSpawned
         bigInt[8] = toBeSpawned
         move toBeSpawned count and make it last element




        for each num in list
            increment bigInt list at idx num

        for each day
            shuffleDay

     */

    fun part2(input: String): BigInteger {
        val newInput = input.split(",").map{ it.toInt()}.toMutableList()
        var fishCounts = Array<BigInteger>(9) {BigInteger.ZERO }
        for (i in newInput) {
            fishCounts[i] += BigInteger.ONE
        }
        (1..256).forEach{
            val tempCounts = Array<BigInteger>(9) {BigInteger.ZERO}
            for (i in 1..8) {
                tempCounts[i -1] = fishCounts[i]
            }
            tempCounts[8] = fishCounts[0]
            tempCounts[6] += fishCounts[0]
            fishCounts = tempCounts
        }

        return fishCounts.fold(BigInteger.ZERO) { acc, sum ->
            acc + sum
        }
    }

    val input = readInput("Day06")[0]
    println(part1(input))
    println(part2(input))
}
