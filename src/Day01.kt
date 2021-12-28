fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        for (i in input.indices) {
            if(i > 0) {
                if(input[i].toInt() > input[i - 1].toInt()) {
                    count+=1
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        val newInp = input.map { it.toInt() }
        var sum = newInp[0] + newInp[1] + newInp[2]
        for(i in 1..newInp.size - 3) {
            var current = newInp[i]
            // get the sum of the next 3
            val nextThreeSum = current + newInp[i+1] + newInp[i+2]
            if(nextThreeSum > sum) {
                count+=1
            }
            sum = nextThreeSum
        }
        return count
    }
    val sampleInput = listOf<String>(
        "199",
        "200",
        "208",
        "210",
        "200",
        "207",
        "240",
        "269",
        "260",
        "263",
    )
    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
