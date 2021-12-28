fun main() {
    /**
     * input = list of number
     *
     * 1st get the power consumption by:
     * take each number and generate two new numbers:
     * 1. gamma rate
     *  in order to calculate gamma, go through entire list and find the most common number in that position
     *
     *
     *  11010
     *  01101 i
     *  10101
     *
     *  with the above dat set, the gamme rate would be
     *  11101
     *  [{0:1, 1:2}, {0:1, 1:2}, {0:1, 1:2}, {0:2, 1:1}, {0:1, 1:2}]
     *  gamma [1,1,1,0,1]
     *  epsilon [0,0,0,1,0]
     * 2. epsilon rate
     *  to find this rate, find the LEAST common number in each position for all numbers
     * powerConsumption = gammaRate * epsilonRate
     *
     */
    fun parseBinaryToInt(str: String): Int {
        return Integer.parseInt(str, 2)
    }


    fun findMostCommonNumbers(input: List<String>): List<String> {
        val freqContainer = MutableList(input[0].length) { _ -> object{
            var zero = 0
            var one = 0
        } }
        for (num in input) {
            // num === 11001
            for (i in num.indices) {
                val currNum = num[i].toString(); // 0 or 1
                // if fequency container has a value at current index
                when (currNum) {
                    "1" -> { freqContainer[i].one += 1 }
                    "0" -> { freqContainer[i].zero += 1 }
                    else -> {
                        throw  RuntimeException("DANGIT! hit else condition")
                    }
                }
            }
        }
        return freqContainer.map {
            if(it.one > it.zero) "1" else "0"
        }
    }

    fun findMostCommonCount(input: List<String>, idx: Int): String {
        val count1 = input.count { it[idx].toString() == "1" } // 1
        val count0 = input.size - count1
        if (count1 > count0) {
            return "1"
        } else if (count1 < count0) {
            return "0"
        } else {
            throw Exception()
        }
    }
    fun part1(input: List<String>): Int {
        val gamma = findMostCommonNumbers(input)
        val epsilon = gamma.map { if(it == "1") "0" else "1" }
        val joinedGamma = gamma.joinToString("")
        val joinedEps = epsilon.joinToString("")
        val gammaNum = parseBinaryToInt(joinedGamma)
        val epsNum = parseBinaryToInt(joinedEps)
        return gammaNum * epsNum
    }

    fun filterOutOthers(input: List<String>, numToKeep: String, idx: Int): List<String> {
        return input.filter { it[idx].toString() == numToKeep }
    }
    /**
     * now to get lifeSupportRating = oxygenRating * co2Scrubber
     *
     * to get oygenRating
     *      find the most common number in the current pos staring at first
     *          if they have equal amts, keep values with 1 in current pos
     *
     *          get rid of all number that don't have the most common in curr pos
     *          repeat until one number is left
     *
     *
     */
    fun recurse(input: List<String>, pos: Int, shouldKeepOnes: Boolean): Int {
        // input: ["101001", "011100", "10101"]
        // out: ["111001"...]
        if(input.size === 1 || pos > input[0].length - 1) {
            return parseBinaryToInt(input[0])
        }

        var numToKeep: String
        try {
            val mostCommon = findMostCommonCount(input, pos)
            numToKeep = mostCommon
            if (!shouldKeepOnes) {
                // flip the number if were looking for co2 instead of oxygen
                numToKeep = if (mostCommon == "1") "0" else "1"
            }
        } catch (_: Throwable) {
            numToKeep = if (shouldKeepOnes) "1" else "0"
        }
        var filteredNums = filterOutOthers(input, numToKeep, pos)
        if(filteredNums.isEmpty()) {
            filteredNums = input
        }
        return recurse(filteredNums, pos + 1, shouldKeepOnes)
    }
    fun part2(input: List<String>): Int {
        val oxygen = recurse(input, 0, true)
        val co2 = recurse(input, 0, false)
        val result =  oxygen * co2
        return result
    }


    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
