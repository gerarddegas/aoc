import java.util.*

fun main() {
    /*
    find out numbers that correspond to digits segments
    {
        1: cf, // 2
        2: acdeg // 5
        3: acdfg // 5
        4: bcdf // 4
        5: abdfg // 5
        6: abdefg // 6
        7: acf // 3
        8: abcdefg // 7
        9: abcdfg // 6
        0: abcefg // 6
    }
    acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
    2: acdeg
    3: acdfg
    5: abdfg

    before the |, we can know what signals the rest are based on number of digits

     */
    val digitMap = mapOf<Int, String>(
        1 to "cf", // 2
        2 to "acdeg", // 5
        3 to "acdfg", // 5
        4 to "bcdf", // 4
        5 to "abdfg", // 5
        6 to "abdefg", // 6
        7 to "acf", // 3
        8 to "abcdefg", // 7
        9 to "abcdfg", // 6
        0 to "abcefg", // 6
    )

    fun getNumLength(num: Int): Int {
        return digitMap.getOrDefault(num, "").length
    }

    val commonEasyLengths = listOf(
        getNumLength(1),
        getNumLength(4),
        getNumLength(7),
        getNumLength(8),
    )

    fun parseInput(input: List<String>): Pair<List<List<String>>, List<List<String>>> {
        // "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
        // [cdfeb, fcadb, cdfeb, cdbaf]
        return Pair(input.map { it.substringBefore(" | ").split(" ") }, input.map { it.substringAfterLast(" | ").split(" ") })
    }

    fun sumCommons(codeList: List<String>): Int {
        return codeList.fold(0) { acc, code ->
            // cdfeb
            if (commonEasyLengths.contains(code.length)) {
                acc + 1
            } else {
                acc
            }
        }
    }


    fun part1(input: List<String>): Int {
        val (_, segments) = parseInput(input)
        return segments.sumOf { codeList ->
            sumCommons(codeList)
        }
    }

    /*

    [x] 1 to "cf", // 2
    [ ] 2 to "acdeg", // 5
    [x] 3 to "acdfg", // 5
    [x] 4 to "bcdf", // 4
    [x] 5 to "abdfg", // 5
    [x] 6 to "abdefg", // 6
    [x] 7 to "acf", // 3
    [x] 8 to "abcdefg", // 7
    [x] 9 to "abcdfg",// 6 includes all chars of 4
    [x] 0 to "abcefg", // 6

    0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg
     */
    fun merge(first: Set<Char>, second: String): MutableSet<Char> {
        val newSet = mutableSetOf<Char>()
        for (value in first) {
            newSet.add(value)
        }
        second.toCollection(newSet)
        return newSet
    }

    fun getMappings(input: List<String>): MutableMap<String, Int> {
        val result = mutableMapOf<String, Int>()
        var lettersInOne = mutableSetOf<Char>()
        var lettersInFour = mutableSetOf<Char>()
        var lettersInSeven = mutableSetOf<Char>()
        var lettersInEight = mutableSetOf<Char>()
        for (segment in input) {
            if(segment.length == 2) {
                result[segment] = 1
                segment.toCollection(lettersInOne)
            } else if(segment.length == 4) {
                result[segment] = 4
                segment.toCollection(lettersInFour)
            } else if(segment.length == 3) {
                result[segment] = 7
                segment.toCollection(lettersInSeven)
            } else if(segment.length == 7) {
                result[segment] = 8
                segment.toCollection(lettersInEight)
            }
        }
        for (segment in input) {
            if (segment.length === 6) {
                val merged: MutableSet<Char> = merge(lettersInOne, segment)
                val compared = segment.toSet()
                val hasBoth = merged.containsAll(compared) && compared.containsAll(merged)
                if (merged == compared) {
                    val nextMerge = merge(lettersInFour, segment)
                    if (nextMerge.size == 6) {
                        // 9
                        result[segment] = 9

                    } else {
                        result[segment] = 0
                    }
                } else {
                        result[segment] = 6
                    }
            }

            if(segment.length === 5) {
                // 2,3,5
                val merge = merge(lettersInSeven, segment)
                val set = segment.toSet()
                val mergeFive = merge(lettersInFour, segment)
                if(merge == set) {
                    result[segment] = 3
                } else if(mergeFive.size == 6) {
                    result[segment] = 5
                } else {
                    result[segment] = 2
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val(signals, segments) = parseInput(input)
        var totalSum = 0
        val sortedSignals = signals.map { signal ->
            signal.map { it.toCharArray().sorted().joinToString("") }
        }
        for ((sortedSignal, segments) in sortedSignals zip segments) {

            val mappings = getMappings(sortedSignal)
            var curr = ""
            for (combo in segments) {
                curr += mappings[combo.toCharArray().sorted().joinToString("")].toString()
            }
            totalSum += curr.toInt()
        }

        return totalSum
    }
    val example = listOf(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
    )
    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

