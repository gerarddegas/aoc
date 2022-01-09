fun main() {
    /*

1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
     */
    fun part1(input: List<String>): Int {
        val initMatrix = parseInput15(input)

        // create sums of the far right  col
        for (i in initMatrix.lastIndex - 2 downTo 0) {
            val lastX = initMatrix[0].lastIndex
            initMatrix[i][lastX] = initMatrix[i][lastX] + initMatrix[i + 1][lastX]
        }
        // create sums of the bottom row
        for (i in initMatrix[0].lastIndex - 2 downTo 0) {
            val lastY = initMatrix.lastIndex
            initMatrix[lastY][i] = initMatrix[lastY][i] + initMatrix[lastY][i+1]
        }

        for (y in initMatrix.lastIndex - 1 downTo 0) {
            for (x in initMatrix[0].lastIndex - 1 downTo 0) {
                val right = initMatrix[y][x +1]
                val down = initMatrix[y+1][x]
                initMatrix[y][x] = initMatrix[y][x] + minOf(right, down)
            }
        }

        return initMatrix[0][0]
    }

    fun part2(input: List<String>) {

    }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

typealias IntListMatrix = List<List<Int>>

fun parseInput15(input: List<String>): List<MutableList<Int>> {
    return input.map { it.split("").filterNot { it.isEmpty() }.map { it.toInt() }.toMutableList() }
}
/*

1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581

742
672
328


26

23 16  12
19 17  10
13 10 8


{
    "-
}


fun traverse(x: Int, y: Int, matrix: IntListMatrix, currSum: Int) {
            val currValue = matrix[y][x]
            if (x == 0 && y == 0) {
                lowestSum = minOf(lowestSum, currSum + currValue)
                return
            }
            val nextSum = if (x == matrix[0].lastIndex && y == matrix.lastIndex) currSum else currSum + Pair(y,x)
            if (x-1 >= 0) {
                traverse(x-1, y, matrix, nextSum)
            }
            if (y-1 >= 0) {
                traverse(x, y-1, matrix, nextSum)
            }
        }
 */