import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class Day14KtTest {
    @Test
    fun testParseInput() {
        val input = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
        """.trimIndent()
        val (template, instructions) = parseInput14(input)
        assertEquals(
            mutableListOf(
                "N",
                "N",
                "C",
                "B"
            ),
            template
        )
        assertEquals(
            "C",
            instructions["CN"]
        )
        assertEquals(
            "B",
            instructions["CH"]
        )

        assertEquals(
            16,
            instructions.size
        )
    }

    fun testBuildTemplate() {
        val input = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
        """.trimIndent()
        val (template, instructions) = parseInput14(input)
        val afterOne = template.buildTemplate(instructions)
        assertEquals(
            mutableListOf(
                "N",
                "C",
                "N",
                "B",
                "C",
                "H",
                "B"
            ),
            afterOne
        )

    }
}