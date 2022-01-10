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
        dpSumMatrix(hugeMatrix, m, n)
        return hugeMatrix[m-1][n-1]
    }

    val input = readInputText("Day15")
    println(part1(input))
    println(part2(input))
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
                    val newY = y + i * 10
                    val newX = x + j * 10
                    newMatrix[newY][newX] = if (newNumber > 9) newNumber % 9 else newNumber
                }
            }
        }
    }
    return newMatrix
}
