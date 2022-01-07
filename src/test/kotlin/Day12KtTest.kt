import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Day12KtTest {
    @Test
    fun testParseInput() {
        var input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        val expected = parseStartEndLines(input)
        val shouldBe = Pair("start", "A")
        assertEquals(
            shouldBe,
            expected[0]
        )
    }

    @Test
    fun testReadInputText() {
        var input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        val expected = readInputText("Day12")
        assertEquals(
            input,
            expected
        )
    }

    @Test
    fun testGetNextOptions() {
        var input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        val options = parseStartEndLines(input)
        val nextAOptions = options.getNextOptions("A")
        assertEquals(
            nextAOptions,
            listOf(
                "c",
                "b",
                "end"
            )
        )

//        org.junit.jupiter.api.assertThrows<Exception> { options.getNextOptions("end") }
        assertEquals(
            options.getNextOptions("b"),
            listOf(
                "d",
                "end"
            )
        )
        assertEquals(
            options.getNextOptions("end"),
            emptyList<String>()
        )
    }

    @Test
    fun testCreateMap() {
        var input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
        val expected = parseStartEndLines(input)
        val adjList = expected.buildAdjList()
//        adjList["A"] = ["c", "b", "end"]
        assertEquals(
            mutableListOf(
                "c",
                "b",
                "end"
            ),
            adjList["A"]
        )
        assertEquals(
            mutableListOf(
                "A",
                "b",
            ),
            adjList["start"]
        )
        assertEquals(
            null,
            adjList["end"]
        )
    }
}