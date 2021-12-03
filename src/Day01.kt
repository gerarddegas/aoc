fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        for (i in input.indices) {
            if(i > 0) {
                if(input[i] > input[i - 1]) {
                    count+=1
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        var sum = input[0].toInt() + input[1].toInt() + input[2].toInt()
        for(i in 1..input.size - 3) {
            var current = input[i].toInt()
            // get the sum of the next 3
            val nextThreeSum = current + input[i+1].toInt() + input[i+2].toInt()
            if(nextThreeSum > sum) {
                count+=1
            }
            sum = nextThreeSum
        }
        return count
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
