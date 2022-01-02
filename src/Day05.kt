import java.io.File
import java.lang.Math.abs
import kotlin.math.max
import kotlin.math.min

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

    fun isDiag(firstPair: Pair<Int, Int>, secondPair: Pair<Int, Int>): Boolean = abs(firstPair.first-secondPair.first) === abs(firstPair.second-secondPair.second)

    fun createGridFromCoords(xCoord: Int): Array<IntArray> {
       return Array(xCoord + 1) { IntArray(xCoord + 1) }
    }

    fun drawLinesOnGrid(grid: Array<IntArray>, coords: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
        for((pair1, pair2) in coords) {
            val (x1,y1) = pair1
            val (x2,y2) = pair2
            if(x1 !== x2) {
                for (x in minOf(x1, x2)..maxOf(x1, x2)) {
                    grid[x][y1] += 1
                }
            }
            if(y1 !== y2) {
                for (y in minOf(y1, y2)..maxOf(y1, y2)) {
                    grid[x1][y] += 1
                }
            }

        }
    }


fun drawAllLines(grid: Array<IntArray>, coords: List<Line>) {
    for((x1, y1, x2, y2) in coords) {
//        val (x1,y1) = pair1
//        val (x2,y2) = pair2
        if(isDiag(Pair(x1, y1), Pair(x2, y2))) {
            // 1,1 -> 3,3
            // 6,5 -> 3,2
            val dx = if (x2 > x1) 1 else -1
            val dy = if (y2 > y1) 1 else -1
            for (i in 0..kotlin.math.abs(x1 - x2)) {
                val y = y1 + i * dy
                val x = x1 + i * dx
                grid[y][x] += 1
            }
        } else  if (x1 == x2 || y1 == y2)  {

            for (i in min(x1, x2)..max(x1, x2)) for (j in min(y1, y2)..max(y1, y2))
                grid[j][i]++
        }
    }
}

    fun sumUpGrid(grid: Array<IntArray>): Int {
        return grid.fold(0) {acc, curr ->
            val rowSum = curr.count { it >= 2 }
            rowSum + acc
        }
    }



    fun filterCoordinates(filterType: CoordinateFilter, coords: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        return coords.map { str ->
            val (first, second) = str.split(" -> ").map{ it.trim() }
            /*
            first -> [0,9]
            seoncd -> [9,0]
            */
            val (x1, y1) = first.split(",")
            val (x2, y2) = second.split(",")
            Pair(Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
        }.filter { pair ->
            val firstPair = pair.first
            val secondPair = pair.second
            val (x1, y1) = firstPair
            val (x2, y2) = secondPair
            var onlyStraight = x1 == x2 || y1 == y2
            if (filterType == CoordinateFilter.ALL) {
                // to find diag, find difference between x1-y1 === x2-y2
                 onlyStraight || isDiag(firstPair, secondPair)
            } else onlyStraight
        }
    }


    fun part1(input: List<String>): Int {
        val straightCoordinates = filterCoordinates(CoordinateFilter.STRAIGHT, input)
        val (largestX, largestY) = getLargestXAndY(straightCoordinates)
        val grid = createGridFromCoords(largestX)
        drawLinesOnGrid(grid, straightCoordinates)
        return sumUpGrid(grid)
    }

    fun part2(input: List<String>): Int {
        /*
        to tell if diagonal, find the difference in both x1,x2 and y1,y2
        isDiagonal = abs(x1-y1) === abs(x2-y2)
        1,1 => 3,3 // diagonal
        0,1 -> 0,3
        1,1 -> 1,3
        1,0 -> 3,0
        0111
        1200
        1110
        1101

        overlap_count = 1

        0,1 -> 0,5
        4,1 -> 4,5
        1,9 -> 4,9
         */


//        val coords = filterCoordinates(CoordinateFilter.ALL, input)
//        val (largestX, largestY) = getLargestXAndY(coords)
        val lines: List<Line>  = input.map{it.split(" -> ").flatMap{it.split(",").map {it.toInt()}}}
        val largestX = lines.maxOf {l -> l.maxOf { it }}
        val grid = Array(largestX + 1) { IntArray(largestX + 1) {0} }
//        val grid = createGridFromCoords(largestX)
        drawAllLines(grid, lines)
        return sumUpGrid(grid)
    }

    val input = readInput("Day05")

    println(part1(input))
    println(part2(input))
    println(day5(input))
}


enum class CoordinateFilter {
    STRAIGHT, ALL
}


data class Point2d(val x: Int, val y: Int)