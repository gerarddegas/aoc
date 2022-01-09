fun main() {
    fun part1(input: String): Int {
        var (template, insertionRules) = parseInput14(input)
        var map = mapOf<String,Int>()
        repeat(10) {
            map = template.buildTemplate(insertionRules)
        }
        println(map)
        val counts = map.values.sortedDescending()
        return counts.first() - counts.last()
    }

    fun part2(input: String): Long {
        var (template, insertionRules) = parseInput14(input)
        var allPossibleTerminals = mutableMapOf<String,Pair<String, String>>()
        for (rule in insertionRules) {
            val pair = rule.key
            val toInsert = rule.value
            allPossibleTerminals[pair] = Pair(
                pair.first() + toInsert,
                toInsert + pair.last()
            )
        }
        val lastChar = template.last()
        var pairCounts = mutableMapOf<String, Long>()

        for (i in 0 until template.size - 1) {
            var currPair = template[i] + template[i+1]
            addLongCountToCountMap(currPair, pairCounts, pairCounts.getOrDefault(currPair, 1L))
        }
        repeat(40) {
            var newPairCounts = mutableMapOf<String, Long>()
            for (pairAndValue in pairCounts) {
                /*
                (firstPair, secondPair) = allPossibleTermainals.get(pair)
                newMap[firstPair] +=1
                newMap[secondPair] +=1
                 */
                var pair = pairAndValue.key
                val (firstNextPair, secondNextPair) = allPossibleTerminals[pair]!!
                addLongCountToCountMap(firstNextPair, newPairCounts, pairCounts.getOrDefault(pair, 1L))
                addLongCountToCountMap(secondNextPair, newPairCounts, pairCounts.getOrDefault(pair, 1L))
            }
            pairCounts = newPairCounts
        }

        var countMap = mutableMapOf<String, Long>()
        for (pairAndValue in pairCounts) {
            val char = pairAndValue.key.first()
            val count = pairAndValue.value
            addLongCountToCountMap(char.toString(), countMap, count)
        }
        addLongCountToCountMap(lastChar, countMap, 1L)
        val counts = countMap.values.sortedDescending()
        return counts.first() - counts.last()

    }

    val input = readInputText("Day14")
    println(part1(input))
    println(part2(input))
}

fun mergeCountMaps(first: MutableMap<String, Int>, second: MutableMap<String, Int>): MutableMap<String, Int> {
    val countMap: MutableMap<String, Int> = mutableMapOf()
    for (count in first) {
        addCountToCountMap(count.key, countMap, count.value)
    }
    for (count in second) {
        addCountToCountMap(count.key, countMap, count.value)
    }
    return countMap
}

fun MutableList<String>.buildTemplate(instructionsMap: HashMap<String, String>): MutableMap<String, Int> {
    var j = 1
    var quantityMap = mutableMapOf<String,Int>()
    while (j < this.size) {
        val curr = this[j]
        val prev = this[j - 1]
        addCountToCountMap(prev, quantityMap)
        if (j == this.lastIndex) {
            addCountToCountMap(curr, quantityMap)
        }
        val toInsert = instructionsMap[prev + curr]
        if (toInsert != null) {
            this.add(j, toInsert)
            addCountToCountMap(toInsert, quantityMap)
            j++
        }
        j++
    }
    return quantityMap
}

fun addCountToCountMap(key: String, countMap: MutableMap<String, Int>, count: Int = 1) {
    countMap[key] = countMap.getOrDefault(key, 0) + count
}

fun addLongCountToCountMap(key: String, countMap: MutableMap<String, Long>, count: Long) {
    countMap[key] = countMap.getOrDefault(key, 0L) + count
}

fun parseInput14(input: String): Pair<MutableList<String>, HashMap<String, String>> {
    val result = input
        .split("\n\n")
    val instructions = hashMapOf<String, String>()
    result[1].lines().map{ it.split(" -> ") }.forEach { instructions[it.first()] = it.last() }
    val template = result.first().split("").filter { it.isNotEmpty() }.toMutableList()
    return Pair(template, instructions)
}