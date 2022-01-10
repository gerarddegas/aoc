import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Day15KtTest {
    @Test
    fun testBuildMegaMatrix() {
        val input = """
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
        """.trimIndent()
        val parsedInput = parseInput15(input)
        val matrix = buildMegaMatrix(parsedInput)
        val y = 1
        val x = 2
        val nextInput = """
            89123
            91234
            12345
            23456
            34567
        """.trimIndent()
        val nextParsedInput = parseInput15(nextInput)
        for (i in 0 until nextParsedInput.size) {
            for (j in 0 until nextParsedInput[0].size) {
                val matrixY = i * 10 + y
                val matrixX = j * 10 + x
                val actual = matrix[matrixY][matrixX]
                val expected = nextParsedInput[i][j]
                assertEquals(
                    expected,
                    actual
                )

            }
        }
    }

    @Test
    fun testDpSumMatrix() {
        val input = """
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
        """.trimIndent()
        val parsedInput = parseInput15(input)
        val matrix = buildMegaMatrix(parsedInput)
        dpSumMatrix(matrix, matrix.size, matrix[0].size)
        assertEquals(
            315,
            matrix[matrix.size -1][matrix[0].size-1]
        )
    }
}