fun main() {
    fun part1(input: String): Int {
        val (visibleCoords, foldingInstructions) = parseInput(input)
        val (largestX, largestY) = visibleCoords.findLargestAndSmallestCoords()
        var matrix = createGid(largestY, largestX, visibleCoords)
        val (axisToFold, idxToFold) = foldingInstructions[0]
        if (axisToFold == "y") {
            val (up, down) = matrix.splitGridHorizontally(idxToFold)
            joinMatrices(up, down, Direction.HORIZONTAL)
            matrix = up
        }
        if (axisToFold == "x") {
            val (left, right) = matrix.splitGridVertically(idxToFold)
            joinMatrices(left, right, Direction.VERTICAL)
            matrix = left
        }
        return matrix.countDots()
    }

    fun part2(input: String): Int {
        val (visibleCoords, foldingInstructions) = parseInput(input)
        val (largestX, largestY) = visibleCoords.findLargestAndSmallestCoords()
        var matrix = createGid(largestY, largestX, visibleCoords)
        for ((axisToFold, idxToFold) in foldingInstructions) {
            if (axisToFold == "y") {
                val (up, down) = matrix.splitGridHorizontally(idxToFold)
                joinMatrices(up, down, Direction.HORIZONTAL)
                matrix = up
            }
            if (axisToFold == "x") {
                val (left, right) = matrix.splitGridVertically(idxToFold)
                joinMatrices(left, right, Direction.VERTICAL)
                matrix = left
            }
        }
        println(matrix)
        return matrix.countDots()
    }

    val input = readInputText("Day13")
    println(part1(input))
    println(part2(input))
}

enum class Direction {
    VERTICAL,
    HORIZONTAL,
}
fun joinMatrices(first: List<MutableList<Int>>, second: List<MutableList<Int>>, direction: Direction) {
    /*
    FOLD ALONG Y:
    traverse the bottom part of the fold and for each # we find, x,y:
        to get the top parts corresponding x,y:
            topY = rowsInTop.length - currRowInBottom
            topX = bottomX

    FOLD ALONG X:
    traverse the right part of the fold and for each # we find, x,y:
        to get the top parts corresponding x,y:
            leftX = colsInLeft.length - currColInRight
            leftY = rightY
    */

    for (row in second.indices) {
        for (col in second[0].indices) {
            val correspondingY = if (direction == Direction.HORIZONTAL) first.lastIndex - row else row
            val correspondingX = if (direction == Direction.VERTICAL) first[0].lastIndex - col else col
            if (second[row][col] == 1) {
                first[correspondingY][correspondingX] = 1
            }
        }
    }
}

fun Matrix.countDots(): Int = sumOf { row -> row.count{ it == 1} }

fun Matrix.splitGridVertically(xAxis: Int): Pair<List<MutableList<Int>>, List<MutableList<Int>>> {
        var left = map{ row -> row.take(xAxis ).toMutableList()}
        var right = map{ row -> row.drop(xAxis + 1 ).toMutableList()}
        return Pair(left, right)
}

fun Matrix.splitGridHorizontally(yAxis: Int): Pair<List<MutableList<Int>>, List<MutableList<Int>>> {
    // 3
    // [1,2,3,4]
    var up = take(yAxis).toMutableList()
    var down = drop(yAxis + 1).toMutableList()
    return Pair(up, down)
}


fun createGid(largestY: Int, largestX: Int, visibleCoords: List<VisibleCoord>): Matrix {
    val newMatrix = Array(largestY + 1) { Array(largestX + 1) { 0 }.toMutableList()}
    for ((x, y) in visibleCoords) {
        newMatrix[y][x] = 1
    }
    return newMatrix.toList()
}

fun List<VisibleCoord>.findLargestAndSmallestCoords(): Pair<Int, Int> {
    var largestX = 0
    var largestY = 0
    forEach { (x,y) ->
        if (x > largestX) {
            largestX = x
        }
        if (y > largestY) {
            largestY = y
        }
    }
    return Pair(largestX, largestY)
}

typealias VisibleCoord = Pair<Int, Int>
typealias FoldInstruction = Pair<String, Int>
typealias Matrix = List<MutableList<Int>>

fun parseInput(input: String): Pair<List<VisibleCoord>, List<FoldInstruction>> {
    var outputCoords = listOf<VisibleCoord>()
    var outputInstructions = listOf<FoldInstruction>()
    val restInput = input.lines()
    for (i in restInput.indices) {
        if (restInput[i].isEmpty()) {
            outputCoords = restInput.take(i).map { coord ->
                val (x, y) = coord.split(",")
                Pair(x.trim().toInt(), y.trim().toInt())
            }
            outputInstructions = restInput.drop(i+1).map { instruction ->
                val (prefix, value) = instruction.split("=")
                Pair(prefix.last().toString(), value.toInt())
            }
        }
    }
    return Pair(outputCoords, outputInstructions)
}

