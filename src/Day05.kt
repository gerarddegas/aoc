import java.io.File

fun main() {
    /*
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
[[0,9,5,9]]
[((0, 9), (5, 9)), ((9, 4), (3, 4)), ((2, 2), (2, 1)), ((7, 0), (7, 4)), ((0, 9), (2, 9)), ((3, 4), (1, 4))]
..........
..........
..........
..........
..........
..........
..........
..........
..........
..........
Pair<Pair<String, String>, Pair<String, String>>
* only consider vertical lines, where x1=x2 and y1=y2
    1. find the largest x and the largest y in the list
    2. create a 2d grid with largest x and largest y
    3. for each pair
    find the axis coodrinate that is different and return the pair
        for each number ranging from lower pair to higher pair
            increment the grid at that position


     Sum up
     for each row in grid
        if number is 2 or higher
            add it to total sum
     return sum
     */
    fun getLargestXAndY(coords:  List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Pair<Int, Int> {
        var largestX = 0
        var largestY = 0
        for ((pair1, pair2) in coords) {
            val (x1,y1) = pair1
            val (x2,y2) = pair2
            largestX = maxOf(x1, x2, largestX)
            largestY = maxOf(y1, y2, largestY)
        }
        return Pair(largestX, largestY)
    }

    fun createGridFromCoords(xCoord: Int, yCoord: Int): Array<IntArray> {
       return Array(xCoord + 1) { IntArray(yCoord + 1) }
    }

    fun drawLinesOnGrid(grid: Array<IntArray>, coords: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
        for((pair1, pair2) in coords) {
            val (x1,y1) = pair1
            val (x2,y2) = pair2
            // find which of the two that are the same
            var coordToRangeOver = if (x1 !== x2) "x" else "y"
            if(coordToRangeOver == "x") {
                for (x in minOf(x1, x2)..maxOf(x1, x2)) {
                    grid[x][y1] += 1
                }
            }
            if(coordToRangeOver == "y") {
                for (y in minOf(y1, y2)..maxOf(y1, y2)) {
                    grid[x1][y] += 1
                }
            }

        }
    }

    fun sumUpGrid(grid: Array<IntArray>): Int {
        return grid.fold(0) {acc, curr ->
            val rowSum = curr.count { it >= 2 }
            rowSum + acc
        }
    }

    fun part1(input: List<String>): Int {
        val data = input.map { str ->
            val (first, second) = str.split(" -> ").map{ it.trim() }
            // first -> [0,9]
            // seoncd -> [9,0]
            val (x1, y1) = first.split(",")
            val (x2, y2) = second.split(",")
            Pair(Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
        }
        val straightCoordinates = data.filter { pair ->
            val firstPair = pair.first
            val secondPair = pair.second
            val (x1, y1) = firstPair
            val (x2, y2) = secondPair
            x1 == x2 || y1 == y2
        }
        val (largestX, largestY) = getLargestXAndY(straightCoordinates)
        val grid = createGridFromCoords(largestX, largestY)
        drawLinesOnGrid(grid, straightCoordinates)
        return sumUpGrid(grid)
    }

    fun part2(input: List<String>) {
    }

    val input = readInput("Day05")

    println(part1(input))
    println(part2(input))
}


