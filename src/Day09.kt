fun main() {
    /*

    2199943210
    3987894921
    9856789892
    8767896789
    9899965678

    track totalRiskLevels
    for each row
        for each col
            for each adjacent value
                if the curr number is less than all adjacent values
                    totalRiskLevels += currNum + 1

     */
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { it.split("").filterNot { it.isEmpty() }.map { it.toInt() } }
    }

    fun getAdjacents(row: Int, col: Int, matrix:  List<List<Int>>): MutableMap<Pair<Int, Int>, Int> {
        // left row[j-1]
        val left = matrix.getOrNull(row)?.getOrNull(col - 1)
        // right row[j+1]
        val right = matrix.getOrNull(row)?.getOrNull(col + 1)
        // up heights[i-1][j]
        val up = matrix.getOrNull(row - 1)?.getOrNull(col)
        // down heights[i+1][j]
        val down = matrix.getOrNull(row +1)?.getOrNull(col)

        val adjacents = mutableMapOf<Pair<Int, Int>, Int>()
        if (left !== null) {
            adjacents[Pair(row, col - 1)] = left
        }
        if (right !== null) {
            adjacents[Pair(row, col + 1)] = right
        }
        if (up !== null) {
            adjacents[Pair(row - 1, col)] = up
        }
        if (down !== null) {
            adjacents[Pair(row + 1, col)] = down
        }
        return adjacents
    }


    fun part1(input: List<String>): Int {
        val heights = parseInput(input)
        var totalRiskLevels = 0
        for (i  in heights.indices) {
            val row = heights[i]
            for (j in row.indices) {
                val currHeight = row[j]
                val adjacentVals = getAdjacents(i, j, heights)
                val isSmallest = adjacentVals.all { (_, value) ->
                    value > currHeight
                }
                if (isSmallest) {
                    totalRiskLevels += currHeight + 1
                }
            }
        }
        return totalRiskLevels
    }

    fun part2(input: List<String>): Int {
        val heights = parseInput(input)
        var totalRiskLevels = mutableListOf<Int>()
        for (i  in heights.indices) {
            val row = heights[i]
            for (j in row.indices) {
                val currHeight = row[j]
               val adjacents = getAdjacents(i, j, heights)
                val isSmallest = adjacents.all { (_, value) ->
                    value > currHeight
                }
                if (isSmallest) {
                    var basinSize = 1
                    val visited = mutableMapOf(
                        Pair(i, j) to true
                    )
                    val queue = mutableListOf<Pair<Int, Pair<Int, Int>>>()
                    for ((coords, num) in adjacents) {
                        if (num != 9) {
                            queue.add(Pair(num, coords))
                            visited[coords] = true
                        }
                    }
                    // for each one that's smallest, add both indexes to queue
                    while (queue.size > 0) {
                        val (height, coords) = queue.removeAt(0)
                            basinSize +=1
                            val (row, col) = coords
                            val nextAdjacents = getAdjacents(row, col, heights)
                            for ((nextCoords, nextNum) in nextAdjacents) {
                                if(nextNum !== 9 && nextNum > height && visited[nextCoords] == null) {
                                    queue.add(Pair(nextNum, nextCoords))
                                    visited[nextCoords] = true
                                }
                            }
                    }
                    totalRiskLevels.add(basinSize)
                }
            }
        }
        val sortedRisks = totalRiskLevels.sorted()
        return sortedRisks.takeLast(3).reduce{acc, sum -> acc * sum}
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

