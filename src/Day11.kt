fun main() {
    /*
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526

    for each step
    1. each oct energy += 1
    2. each oct with energy > 9 flashes
        a. increases the level of adj oct by +1 (including diag), if adj oct is > 9 then it also flashes
        NOTE: oct can only flash ONCE per step
    3. oct energy goes to 0

    keep trash of points that have:
        flashed already

    hasFlashed = {
        1,1
        1,2
        1,3
        2,3
        3,3
        3,2
        3,1
        2,1
    }
    Pair {row,col}
    nextFlashes = [
        1,1
        1,2
        1,3
        2,3
        3,3
        3,2
        3,1
        2,1
    ]
    toCheck = 1,3
    Before any steps:
    23321
    2111
    22411
    10001
    11111

    After step 1:
    34543
    40004
    50005
    40004
    34543

    After step 2:
    45654
    51115
    61116
    51115
    45654
     */
    fun parseInput(input: List<String>): List<MutableList<Int>> {
        return input.map { s ->
            s.split("").filterNot { it.isEmpty() }.map { it.toInt() }.toMutableList()
        }
    }

    fun increaseAllByOne(matrix: List<MutableList<Int>>){
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                matrix[i][j] += 1
            }
        }
    }

    fun getZeros(matrix: List<MutableList<Int>>): Int {
        return matrix.sumOf { it.count{it == 0} }
    }



    fun part1(input: List<String>): Int {
        var totalFlashes = 0
//        var hasFlashed = mutableMapOf<Pair<Int, Int>, Boolean>()
        val matrix = parseInput(input)
        val m = matrix.size
        val n = matrix.size
        for (num in 0 until 100) {
            // increment all values by 1
            increaseAllByOne(matrix)
            // check for oct with value 10 ie. flash
                // incr surrounding
            for (i in matrix.indices) {
                for (j in matrix[0].indices) {
                    val flashed = mutableListOf<Pair<Int, Int>>()
                    if (matrix[i][j] > 9) {
                        // add to flash Map
                        matrix[i][j] = 0
                        flashed.add(Pair(i,j))
//                        hasFlashed[Pair(i,j)] = true
                    }
                    while (flashed.isNotEmpty()) {
                        // next one that has flashed, we increment all surrounding ones that haven't already flashed
                        val (row, col) = flashed.removeAt(0)
//                        totalFlashes += 1
                        val neighbors = mutableListOf<Pair<Int,Int>>()
                        /*
                        3  3  3  2  2
                        3  0 10 10  2
                        2 10 2  10  2
                        2 10 10 10  2
                        2  2  2  2  2
                         */
                        listOf(
                            Pair(-1, -1),
                            Pair(1, -1),
                            Pair(0, -1),
                            Pair(-1, 1),
                            Pair(1, 1),
                            Pair(0, 1),
                            Pair(-1, 0),
                            Pair(1, 0),
                        ).forEach { (dx, dy) ->
                            val nextRow = dx + row
                            val nextCol = dy + col
                            if (nextRow >= 0 && nextRow < matrix.size && nextCol >= 0 && nextCol < matrix[0].size) {
                                if (matrix[nextRow][nextCol] in 1..9) {
                                    neighbors.add(Pair(nextRow, nextCol))
                                }
                            }
                        }
                        for ((nextRow, nextCol) in neighbors) {
//                            matrix[nextRow][nextCol] += if (matrix[nextRow][nextCol] !== 0 || matrix[nextRow][nextCol] < 10) 1 else 0
                            matrix[nextRow][nextCol] += 1
                            if (matrix[nextRow][nextCol] > 9) {
                                matrix[nextRow][nextCol] = 0
                                flashed.add(Pair(nextRow, nextCol))
                            }
                        }
                    }
                }
            }
            if(getZeros(matrix) == m * n) {
                return num
            }
            totalFlashes += getZeros(matrix)
        }
        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        val matrix = parseInput(input)
        val m = matrix.size
        val n = matrix.size
        for (num in 1 until 10000) {
            // increment all values by 1
            increaseAllByOne(matrix)
            // check for oct with value 10 ie. flash
            // incr surrounding
            for (i in matrix.indices) {
                for (j in matrix[0].indices) {
                    val flashed = mutableListOf<Pair<Int, Int>>()
                    if (matrix[i][j] > 9) {
                        // add to flash Map
                        matrix[i][j] = 0
                        flashed.add(Pair(i,j))
//                        hasFlashed[Pair(i,j)] = true
                    }
                    while (flashed.isNotEmpty()) {
                        // next one that has flashed, we increment all surrounding ones that haven't already flashed
                        val (row, col) = flashed.removeAt(0)
//                        totalFlashes += 1
                        val neighbors = mutableListOf<Pair<Int,Int>>()
                        /*
                        3  3  3  2  2
                        3  0 10 10  2
                        2 10 2  10  2
                        2 10 10 10  2
                        2  2  2  2  2
                         */
                        listOf(
                            Pair(-1, -1),
                            Pair(1, -1),
                            Pair(0, -1),
                            Pair(-1, 1),
                            Pair(1, 1),
                            Pair(0, 1),
                            Pair(-1, 0),
                            Pair(1, 0),
                        ).forEach { (dx, dy) ->
                            val nextRow = dx + row
                            val nextCol = dy + col
                            val isWithinBounds = nextRow in matrix.indices && nextCol in matrix[0].indices
                            if (isWithinBounds) {
                                if (matrix[nextRow][nextCol] in 1..9) {
                                    neighbors.add(Pair(nextRow, nextCol))
                                }
                            }
                        }
                        for ((nextRow, nextCol) in neighbors) {
                            matrix[nextRow][nextCol] += 1
                            if (matrix[nextRow][nextCol] > 9) {
                                matrix[nextRow][nextCol] = 0
                                flashed.add(Pair(nextRow, nextCol))
                            }
                        }
                    }
                }
            }
            if(getZeros(matrix) == m * n) {
                return num
            }
        }
        throw Exception("NEVER HAD ALL ZEROS")
    }

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))
}
