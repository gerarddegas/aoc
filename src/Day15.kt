fun main() {
    fun part1(input: String): Int {
        val initMatrix = parseInput15(input)
        val n = initMatrix[0].size
        val m = initMatrix.size

        dpSumMatrix(initMatrix, m, n)

        return initMatrix[m-1][n-1]
    }

    fun part2(input: String): Int {
        val initMatrix = parseInput15(input)
        val hugeMatrix = buildMegaMatrix(initMatrix)
        val n = hugeMatrix[0].size
        val m = hugeMatrix.size

        // dikjstras
        val visited = hashMapOf<Pair<Int,Int>, Boolean>()
        val totalRiskLevels = hashMapOf<Pair<Int,Int>, Int>()
        totalRiskLevels[Pair(0,0)] = 0
        val Q = mutableListOf<Pair<Pair<Int, Int>, Int>>()
        Q.add(Pair(Pair(0,0), 0))


        while (Q.isNotEmpty()) {
            Q.sortByDescending { it.second }
            val (location, riskLevel) = Q.removeAt(Q.lastIndex)
            visited[location] = true
            // if the location already has a riskLevel lower than the current, skip it
            if (totalRiskLevels.getOrDefault(location, Int.MAX_VALUE) < riskLevel) continue

            for ((adjRow, adjCol) in hugeMatrix.getAdjacentPairs(location.first, location.second)) {
                // if we've already visited, skip it
                if(visited[Pair(adjRow, adjCol)] == true) continue
                // get distance up until curr location + value at next location
                val nextRiskLevel = totalRiskLevels.getOrDefault(location, Int.MAX_VALUE) + hugeMatrix[adjRow][adjCol]
                if (nextRiskLevel < totalRiskLevels.getOrDefault(Pair(adjRow, adjCol), Int.MAX_VALUE)) {
                    Q.add(Pair(Pair(adjRow, adjCol), nextRiskLevel))
                    totalRiskLevels[Pair(adjRow, adjCol)] = nextRiskLevel
                }
            }
        }

        return totalRiskLevels[Pair(m-1, n-1)]!!
    }

    val input = readInputText("Day15")
//    println(part1(input))
    println(part2(input))
}

private enum class Direction15 {
    UP, DOWN, LEFT, RIGHT
}

private fun List<MutableList<Int>>.printMatrix(){
    for (row in 0 until this.size) {
        for (col in 0 until this[0].size) {
            print(this[row][col])
            print(" ")
        }
        println()
    }
    println()
}

private fun isWithinBounds(row: Int, col: Int, matrix: List<MutableList<Int>>): Boolean {
    val m = matrix.size
    val n = matrix.first().size
    return row in 0 until m && col in 0 until n
}

private fun List<MutableList<Int>>.getAdjacentPairs(row: Int, col: Int): List<Pair<Int, Int>> {
    val up = Pair(row-1, col)
    val right = Pair(row, col+1)
    val down = Pair(row+1, col)
    val left = Pair(row, col - 1)
    return listOf(up, right, down, left).filter { isWithinBounds(it.first, it.second, this) }
}

typealias IntListMatrix = List<List<Int>>

fun parseInput15(input: String): List<MutableList<Int>> {
    return input.lines().map { it.split("").filterNot { it.isEmpty() }.map { it.toInt() }.toMutableList() }
}

fun dpSumMatrix(matrix: List<MutableList<Int>>, m: Int, n: Int) {
    for (i in 2 until matrix.size) {
        matrix[i][0] = matrix[i][0] + matrix[i - 1][0]
    }

    for (i in 2 until matrix[0].size) {
        matrix[0][i] = matrix[0][i] + matrix[0][i-1]
    }

    for (y in 1 until m) {
        for (x in 1 until n) {
            val left = matrix[y][x-1]
            val up = matrix[y-1][x]
            matrix[y][x] = matrix[y][x] + minOf(left, up)
        }
    }

}

fun buildMegaMatrix(initialMatrix: IntListMatrix): List<MutableList<Int>> {
    val newMatrix = List(initialMatrix.size * 5) { MutableList(initialMatrix[0].size * 5) { 0 } }
    for (y in initialMatrix.indices) {
        for (x in 0 until initialMatrix[0].size) {
            newMatrix[y][x] = initialMatrix[y][x]
        }
    }
    for (y in initialMatrix.indices) {
        for (x in 0 until initialMatrix[0].size) {
            for (i in 0..4) {
                for (j in 0..4) {
                    var newNumber = initialMatrix[y][x] + (j * 1) + (i * 1)
                    val newY = y + i * initialMatrix.size
                    val newX = x + j * initialMatrix[0].size
                    newMatrix[newY][newX] = if (newNumber > 9) newNumber % 9 else newNumber
                }
            }
        }
    }
    return newMatrix
}
