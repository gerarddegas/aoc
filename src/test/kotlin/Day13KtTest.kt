import org.jetbrains.annotations.TestOnly
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Day13KtTest {

    @Test
    fun testParseInput() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
        """.trimIndent()
        val result = parseInput(input)

        assertEquals(
            Pair(6,10),
            result.first[0]
        )

        assertEquals(
            Pair(9,0),
            result.first.last()
        )

        assertEquals(
            Pair("y",7),
            result.second[0]
        )

        assertEquals(
            Pair("x",5),
            result.second.last()
        )
    }

    @Test
    fun testLargestSmallest() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
        """.trimIndent()
        val (coords, _) = parseInput(input)
        val (largestX, largestY)  = coords.findLargestAndSmallestCoords()
        assertEquals(
            10,
            largestX
        )
        assertEquals(
            14,
            largestY
        )
    }

    @Test
    fun testCreateGrid() {
        val coords = listOf(
            Pair(2, 2),
            Pair(4,5),
            Pair(11, 10)
        )
        val matrix = createGid(10, 11, coords)
        assertEquals(
            -1,
            matrix[0][0]
        )
        assertEquals(
            0,
            matrix[10][11]
        )
        assertEquals(
            0,
            matrix[5][4]
        )
        assertEquals(
            0,
            matrix[2][2]
        )
        assertEquals(
            -1,
            matrix[8][10]
        )
    }

    @Test
    fun testSplitHorizontallyMatrix() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
        """.trimIndent()
        val (coords, instructions) = parseInput(input)
        val (largestX, largestY)  = coords.findLargestAndSmallestCoords()
        val matrix = createGid(largestY, largestX, coords)
        val yAxis = instructions[0].second
        val (up, down) = matrix.splitGridHorizontally(yAxis)

        assertEquals(
            15,
            matrix.size
        )
        assertEquals(
            11,
            matrix[0].size
        )
        assertEquals(
            7,
            up.size
        )
        assertEquals(
            7,
            down.size
        )
        assertEquals(
            largestX + 1,
            up[0].size
        )
        assertEquals(
            largestX + 1,
            down[0].size
        )
    }

    @Test
    fun testSplitVerticallyMatrix() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
        """.trimIndent()
        val (coords, instructions) = parseInput(input)
        val (largestX, largestY)  = coords.findLargestAndSmallestCoords()
        val matrix = createGid(largestY, largestX, coords)
        val yAxis = instructions[0].second
        val (up, _) = matrix.splitGridHorizontally(yAxis)
        val xAxis = instructions[1].second
        val (left, right) = up.splitGridVertically(xAxis)

        assertEquals(
            7,
            left.size
        )

        assertEquals(
            7,
            right.size
        )
        assertEquals(
            5,
            left[0].size
        )

        assertEquals(
            5,
            right[0].size
        )
    }

    @Test
    fun testJoinMatrices() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()
        val (coords, instructions) = parseInput(input)
        val (largestX, largestY)  = coords.findLargestAndSmallestCoords()
        val matrix = createGid(largestY, largestX, coords)
        val yAxis = instructions[0].second
        val (up, down) = matrix.splitGridHorizontally(yAxis)

        assertEquals(
            -1,
            up[0][0]
        )
        joinMatrices(up, down, Direction.HORIZONTAL)

        assertEquals(
            0,
            up[0][0]
        )

        assertEquals(
            0,
            up[0][2]
        )
        assertEquals(
            0,
            up[0][3]
        )

        val (left, right) = up.splitGridVertically(5)
        assertEquals(
            -1,
            left[0][1]
        )
        assertEquals(
            -1,
            left[4][0]
        )

        joinMatrices(left, right, Direction.VERTICAL)

        assertEquals(
            0,
            left[0][1]
        )

        assertEquals(
            0,
            left[0][4]
        )
        assertEquals(
            0,
            left[4][0]
        )
    }

    @Test
    fun testCountDots() {
        var input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
            
            fold along y=7
            fold along x=5
        """.trimIndent()
        val (coords, instructions) = parseInput(input)
        val (largestX, largestY)  = coords.findLargestAndSmallestCoords()
        val matrix = createGid(largestY, largestX, coords)
        val yAxis = instructions[0].second
        val (up, down) = matrix.splitGridHorizontally(yAxis)
        joinMatrices(up, down, Direction.HORIZONTAL)
        assertEquals(
            17,
            up.countDots()
        )
        val (left, right) = up.splitGridVertically(instructions[1].second)
        joinMatrices(left, right, Direction.VERTICAL)
        assertEquals(
            16,
            left.countDots()
        )
    }
}