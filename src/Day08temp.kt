


fun main() {
    val segmentToNum = hashMapOf<String, Int>(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9,
    )
    fun <R> permutations(l: List<R>): List<List<R>> {
        if (l.size == 0) return emptyList()
        if (l.size == 1) return listOf(l)
        val first = l[0]
        val rest = l.drop(1)
        val res = mutableListOf<List<R>>()
        for (p in permutations((rest))) {
            for (i in 0..p.size) {
                res.add(p.take(i) + first + p.drop(i))
            }
        }
        return res.toList()
    }

    fun decode(signals: List<String>): String {
        val orig = "abcdefg".toList()
        val permutes = permutations(orig)
        // a permutation represents a mapping from wire to segment. i.e. when permutation = "cabdefg", a wire of "c" means the segment "a"
        perm@ for (p in permutes) {
            // for a valid permutation, each signal will correspond to one of the 10 possible segment configs
            for (s in signals) {
//                val segment = s.map{c -> orig[p.indexOf(c)]}.sorted().joinToString("")
                val seg1 = s.map { p.indexOf(it) }
                val seg2 = seg1.map { orig[it] }
                val seg3 = seg2.sorted().joinToString("")
                if (!segmentToNum.contains(seg3)) continue@perm
            }
            return p.joinToString("")
        }
        return "NONEFOUND"
    }

    fun encode(signal: String, permutation: String): Int {
        val orig = "abcdefg".toList()
        val segment = signal.map{c -> orig[permutation.indexOf(c)]}.sorted().joinToString("")
        return segmentToNum[segment]!!
    }

    val input = readInput("Day08")
    var s = 0
    val inpp = input.map{it.split(" | ")}
    for ((signals, code) in inpp) {
        val permutation = decode(signals.split(" "))
        val code = code.split(" ").map { encode(it, permutation) }.joinToString("").toInt()
        s += code
    }

    println(s)
}
