import java.math.BigInteger

fun main() {
    /*
    If a chunk opens with (, it must close with ).
    If a chunk opens with [, it must close with ].
    If a chunk opens with {, it must close with }.
    If a chunk opens with <, it must close with >.

    corrupted = char it opens and closes with do not form one of the four legal pairs listed
    incomplete = ??

    stop at 1st incorrect closing char on line

    illegalCharPoints {
        ): 3 points.
        ]: 57 points.
        }: 1197 points.
        >: 25137 points.
    }
    calcSyntaxError (firstIllegalChar, occurences)
        return illegalCharPoints[firstIllegalChar} * occurences

     {([([}{[]{[(<()> // found=} expected=]

     [[<[([]))<([[{}[[()]]] //
     [[<[())<([[{}[[()]]] // prevOpening = [
     prevOpening = ( prevIdx = 4
     [[<[())<([[{}[[()]]]
     prevOpening = (
     prevIdx = 4
     [[<[)<([[{}[[()]]]
     prevOpening = [
     prevIdx = 3
     [[<[)<([[{}[[()]]]


    [({([[{{

     create a map<closingBrakets, Points>
     create a map<closingBrackets, OpenedBrackets>

     if it start with a closing brace it's corrupted

     filterIncomplete()
        if we only have opening brackets and no closing brackets

     STOP condition:
        if we've gone through the list and there's no more closing brackets
            do nothing
        if theres nothing left
            do nothing
     recursively remove the first occurence of a valid pair
        keep doing so
     */


    val pointsMap = hashMapOf(
        ")" to 3,
        "]" to 57,
        "}" to 1197,
        ">" to 25137,
    )

    val pointsMap2 = hashMapOf(
        ")" to 1,
        "]" to 2,
        "}" to 3,
        ">" to 4,
    )

    val correspondingBracket = hashMapOf(
        ")" to "(",
        "]" to "[",
        "}" to "{",
        ">" to "<",
    )

    val openingToClosing = hashMapOf(
        "(" to ")",
        "[" to "]",
        "{" to "}",
        "<" to ">",
    )

    fun isClosing(bracket: String): Boolean = correspondingBracket.keys.contains(bracket)
    fun isOpening(bracket: String): Boolean = correspondingBracket.values.contains(bracket)

    fun List<String>.isIncomplete(): Boolean {
        val res = this.filterNot {
            isOpening(it) }.filterNot { it.isEmpty() }
        return res.isEmpty()
    }

    /*
    [({(<(())[]>[[{[]{<()<>>

     */

    fun filterBlocks(blocks: List<String>): List<String> {
        if(blocks.isIncomplete()) {
            return blocks
        }
        if (blocks.isEmpty()) {
            return blocks
        }
        for (i in blocks.indices) {
            val currentBracket = blocks[i]
            if(isClosing(currentBracket)) {
                val prev = blocks[i-1]
                // check previous for corresponding opening backet
                val closingBracket = correspondingBracket.get(currentBracket)
                if(prev == closingBracket) {
                    // corrupted
                    val next = blocks.filterIndexed{idx, _ ->
                        !listOf(i, i-1).contains(idx)
                    }
                    return filterBlocks(next)
                } else {
                    // return list of length 1 for corrupted and complete list for incomplete
                    return listOf(currentBracket)
                }
            }
        }
        throw Exception("NOT SURE WHAT HAPPENED")
    }

    fun part1(input: List<String>): Int {
        val blocks = input.map { it.split("").filterNot { it.isEmpty() } }
        return blocks.map { filterBlocks(it) }.filter { it.size == 1 }.sumOf { pointsMap.getOrDefault(it.first(), 0) }
    }

    fun findMiddleScore(scores: List<Long>): Long {
        if(scores.size % 2 === 0) {
            val sum = scores[scores.size / 2] + scores[scores.size / 2 - 1]
            return sum / 2
        } else {
            return scores[scores.size/2]
        }
    }

    fun getRestOfBlocks(blocks: List<String>): List<String> {
        return blocks.reversed().map { openingToClosing[it]!! }
    }

    fun part2(input: List<String>): Long {
        val blocks = input.map { it.split("").filterNot { it.isEmpty() } }
        val restBlocks = blocks
            .map { filterBlocks(it) }
            .filterNot { it.size == 1 }
            .map { getRestOfBlocks(it) }
        val totals = mutableListOf<Long>()
        for (blocks in restBlocks) {
            var s = 0L
            for (block in blocks) {
                val toAdd = pointsMap2.getOrDefault(block, 0)
                s *= 5L
                s += toAdd
            }
            totals.add(s)
        }
        return findMiddleScore(totals.sorted())
    }

    val input = readInput("Day10")
//    println(part1(input))
    println(part2(input))


}


