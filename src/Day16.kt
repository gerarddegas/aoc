fun main() {
    fun hexToBinary(h: String): String {
        val list = mutableListOf<String>()
        h.forEach { char ->
            val toAdd = char.toString().toLong(16).toString(2).padStart(4, '0')
            list.add(toAdd)
        }
        return list.joinToString("")
    }

    data class Packet(
        val start: Int,
        val end: Int,
        val version: Long,
        val typeId: Long,
        val value: Long,
        val children: List<Packet>?,
        val totalVersion: Long
    )

    fun calcTypeIds(typeId: Long, children: List<Packet>): Long {
        return when (typeId.toInt()) {
            0 -> children.sumOf { it.value }
            1 -> children.fold(1) {a,c -> a * c.value}
            2 -> children.minOf {it.value}
            3 -> children.maxOf {it.value}
            5 -> if(children[0].value > children[1].value) 1 else 0
            6 -> if(children[0].value < children[1].value) 1 else 0
            7 -> if(children[0].value == children[1].value) 1 else 0
            else -> {
                throw Exception("FAAAG")
            }
        }
    }

    fun part1(input: List<String>): Long {
        var currBinary = hexToBinary(input.first())

        fun parse(content: String, startIdx: Int): Packet {
            var currIdx = startIdx
            var version = content.slice(currIdx until currIdx+3).toLong(2)
            currIdx+=3
            var typeId = content.slice(currIdx until currIdx+3).toLong(2)
            var totalVersion = version
            currIdx+=3

            if (typeId.toInt() == 4) {
                var shouldEnd = false
                var binaryParts = mutableListOf<String>()

                while (!shouldEnd) {

                    val group = content.substring(currIdx until currIdx + 5)
                    currIdx += 5

                    if (group.first() == '0'){
                        shouldEnd = true
                    }
                    val binary = group.substring(1)
                    binaryParts.add(binary)

                }
                // add decode binary parts and add to literal values
                val literalValue = binaryParts.joinToString("").toLong(2)
                // Q: DO I GET RID OF THE REST OF 0's HERE?

                val toReturn = Packet(
                    start = startIdx,
                    end = currIdx,
                    version = version,
                    typeId = typeId,
                    value = literalValue,
                    null,
                    totalVersion = totalVersion
                )
                return toReturn
            } else  {
                val lengthTypeId = content[currIdx]
                currIdx += 1
                if (lengthTypeId == '0') {
                    var len = content.substring(currIdx until currIdx + 15)
                    currIdx+=15
                    // 001110
                    // 0
                    // 000000000011011
                    // 1101000101001010010001001000000000
                    var lengthOfSubPackets = len.toLong(2)
                    val children = arrayListOf<Packet>()
                    while (lengthOfSubPackets > 0) {
                        // add version number
                        val child = parse(content, currIdx)
                        children.add(child)
                        println("child: $child")
                        totalVersion += child.totalVersion
                        val diff = child.end - child.start
                        lengthOfSubPackets -= diff
                        // 123456789
                        currIdx = child.end
                    }
                    return Packet(
                        start = startIdx,
                        end = currIdx,
                        version = version,
                        typeId = typeId,
                        value = calcTypeIds(typeId, children),
                        children = children,
                        totalVersion = totalVersion
                    )
                } else  {
                    var numOfSubPackets = content.substring(currIdx until currIdx + 11).toLong(2)
                    currIdx += 11
                    val children = arrayListOf<Packet>()
                    while (numOfSubPackets > 0) {
                        numOfSubPackets-=1
                        val child = parse(content, currIdx)
                        children.add(child)
                        println("child: $child")
                        currIdx = child.end
                        totalVersion += child.totalVersion
                    }
                    return Packet(
                        start = startIdx,
                        end = currIdx,
                        version = version,
                        typeId = typeId,
                        value = calcTypeIds(typeId, children),
                        children = children,
                        totalVersion = totalVersion
                    )
                }
                throw Exception("HOW DID WE GET HERE")
            }
        }

        val final = parse(currBinary, 0)
        // part1
        // return final.totalVersion

        // part2
        return final.value
    }
    fun part2(input: List<String>) {

    }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
