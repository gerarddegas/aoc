import java.util.*

typealias Path = Pair<String,String>

//data class Path(
//    var isVisited: Boolean = false,
//    var prev: String = "",
//    var next: List<String> = emptyList()
//) {
//    fun visit() {
//        isVisited = true
//    }
//}

fun parseStartEndLines(input: String): List<Pair<String, String>> {
    // ["a-b"]
    // [[a,b]
    return input
            .lines()
            .map {
        val parts = it.split("-" ).filter { it.isNotEmpty() }
        Pair(parts.first(), parts.last())
    }
}

// [[a,b], [a,c]].getNextOptions(a) // [c, b]
// start
// A, b
fun List<Path>.getNextOptions(startPath: String): List<String> {
    val nextOptions = mutableListOf<String>()
    forEach { (start, finish) ->
        if (start == startPath) {
            nextOptions.add(finish)
        }
    }
    return nextOptions.toList()
}

fun List<Path>.buildAdjList(): MutableMap<String, MutableList<String>> {
    val nextOptions = mutableMapOf<String, MutableList<String>>()
    forEach { (start, finish) ->
        // start, A
        if (nextOptions[start] == null) nextOptions[start] =  mutableListOf(finish) else nextOptions[start]?.add(finish)
        if (start != "start" && finish != "end") {
            if (nextOptions[finish] == null) nextOptions[finish] =  mutableListOf(start) else nextOptions[finish]?.add(start)
        }
    }
    return nextOptions
}

val memo = mutableSetOf<String>()


fun main() {
    /*
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end

    Start -> A,b

    A -> c, b, d
    b -> d, end

   Start, A, c, A, end
   Start, A, c, A
        Start
        /    \
        >     >
   c <- A ->  b -> d
        \    /
          >
         end

      Big caves => upperase // A
      small caves => lowercase // a

      Constaints:
        1. can visit small caves only once


     RETURN number of possible paths
     */


//    fun part1(input: String): Int {
//        val paths = parseStartEndLines(input)
//        var total = 0
//        var visited = mutableMapOf<String, Boolean>()
//        fun getNumOfPaths(paths: List<Path>, currentPath: String = "start", adjList: MutableMap<String, MutableList<String>>) {
//            if(adjList[currentPath] == null) {
//                total += 1
//                return
//            }
//            for (nextOption in adjList[currentPath]!!) {
//                // if it's visited and lower case, skip it
//                var re = Regex("[a-z]")
//                if (re.matches(nextOption)) {
//                    if (visited[nextOption] == true) {
//                        continue
//                    } else {
//                        visited[nextOption] = true
//                    }
//                }
//                getNumOfPaths(paths, nextOption, adjList)
//                visited[nextOption] = false
//            }
//        }
//        getNumOfPaths(paths = paths, adjList = paths.buildAdjList())
//        return total
//    }

    fun part1(input: String): Int {
        val paths = parseStartEndLines(input)
        var allPaths = mutableListOf<List<String>>(
        )
        val adjList = paths.buildAdjList()
        fun getNumOfPaths(currentPaths: MutableList<String> = mutableListOf("start")) {
            val nextPath = currentPaths.last()
            if(nextPath == "end") {
                allPaths.add(currentPaths)
                return
            }
            for (nextOption in adjList[nextPath]!!) {
                // if it's visited and lower case, skip it
                if (nextOption.uppercase() !== nextOption && nextOption in currentPaths) {
                    continue
                }
                getNumOfPaths((currentPaths + nextOption).toMutableList())
            }
        }
        getNumOfPaths()
        return allPaths.size
    }

    fun part2(input: String): Int {
        val paths = parseStartEndLines(input)
        var visited = mutableMapOf<String, Int>()
        var allPaths = mutableListOf<List<String>>(
        )
        val adjList = paths.buildAdjList()
        fun getNumOfPaths(currentPaths: MutableList<String> = mutableListOf("start")) {
            val nextPath = currentPaths.last()
            if(nextPath == "end") {
                allPaths.add(currentPaths)
                return
            }
            for (nextOption in adjList[nextPath]!!) {

                if (nextOption.uppercase() !== nextOption) {
                    if (nextOption != "start" && nextOption != "end") {
                        // if one small cave has been visited twice and curr has ben visited once
                        val alreadyVisitedTwice = visited.values.contains(2)
                        if (alreadyVisitedTwice && visited.getOrDefault(nextOption, 0) >= 1) {
                            continue
                        } else {
                            visited[nextOption] = visited.getOrDefault(nextOption, 0) + 1
                        }
                    }
                }
                if (nextOption != "start") {
                    getNumOfPaths((currentPaths + nextOption).toMutableList())
                }
                if (nextOption.uppercase() != nextOption && nextOption != "end") {
                    visited[nextOption] = visited.getOrDefault(nextOption, 0) - 1
                }
            }
        }

        getNumOfPaths()
        println(allPaths)
        return allPaths.size
    }

    val input = readInputText("Day12")
    println(part1(input))
    println(part2(input))
}

