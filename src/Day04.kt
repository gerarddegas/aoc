data class ParsedInput(val guesses: List<Int>, val boards: MutableList<MutableList<MutableList<Int>>>)

fun main() {
    fun transpose(twoDeeMatrix: List<List<Int>>): Array<IntArray> {
        val rows = twoDeeMatrix.size
        val columns = twoDeeMatrix[0].size
        val transposed = Array(rows) { IntArray(columns) }
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                transposed[j][i] = twoDeeMatrix[i][j]
            }
        }
        return transposed
    }
    fun splitUpInput(input: List<String>): ParsedInput {
        var numGuesses: List<Int> = input[0].trim().split(",").map { it.toInt() }
        val boards: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
//        [
//            [
//                [1, 2, 3, 5],
//                [1, 2, 3, 5],
//                [1, 2, 3, 5],
//            ],
//            [
//                [1, 2, 3, 5],
//                [1, 2, 3, 5],
//                [1, 2, 3, 5],
//            ],
//        ]
        var i = 1;
        while (i < input.size - 1) {
            val curr = input[i]
            if(curr.isEmpty()) {
                var j = i + 1
                val board: MutableList<MutableList<Int>> = mutableListOf()
                while (j < i + 6) {
                    val currSet: String = input[j]
                    val numSet = currSet.trim().split("\\s+".toRegex()).map { it.toInt() }.toMutableList() // [1,2,3,4,5]
                    board.add(numSet)
                    j++
                }
                boards.add(board)
                i = j
            }

        }
        return ParsedInput(numGuesses, boards)
    }
    fun isWinningBoard(board:  MutableList<MutableList<Int>>): Boolean {
        board.forEach {
            if(it.sum() === -5) {
                return true
            }
        }
        val transposed = transpose(board)
        transposed.forEach {
            if(it.sum() === -5) {
                return true
            }
        }
        return false
    }
    /**
     * input = random numbers
     *
     * find winning board
     *
     * for each guess
     *  currGuess
     *  for each board
     *      isWinningBoard = false
     *      for each row
     *          hasMatchingNumber = false
     *          for each num
     *              if curr num matches currGuess,
     *                  change it to -1
     *                  hasBeenMakred = true
     *          if hasMatchingNumber
     *              if any row sum is -5 or if any column sum is -5
     *                  isWinningBoard = true
     *      if isWinningBoard
     *          count the sum of all numbers that are not -1
     *          return sumOfWinningBoard * currGuess
     */
    fun sumUpBoard(board:  MutableList<MutableList<Int>>): Int {
        var sum = 0
        board.forEach { row ->
            row.forEach {
                if (it > 0) {
                    sum+=it
                }
        }
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        val (numGuesses, boards) = splitUpInput(input)
        numGuesses.forEach { guess ->
            boards.forEach { board ->
                for (i in board.indices) {
                    val row = board[i]
                    var hasMatchingNumber = false
                    for (j in row.indices) {
                        val num = row[j]
                        if (num === guess) {
                            hasMatchingNumber = true
                            board[i][j] = -1
                        }
                    }
                    if (hasMatchingNumber) {
                        if (isWinningBoard(board)) {
                            return sumUpBoard(board) * guess
                        }
                    }
                }
            }
        }
        throw RuntimeException("No winning board :(")
    }

    fun findAndRemoveWinningBoardIdx(boards:  MutableList<MutableList<MutableList<Int>>>, guesses: List<Int>): Int {
        for (l in guesses.indices) {
            val currGuess = guesses[l]
            for (k in boards.indices) {
                val board = boards[k]
                for (i in board.indices) {
                    val row = board[i]
                    var hasMatchingNumber = false
                    for (j in row.indices) {
                        val num = row[j]
                        if (num === currGuess) {
                            hasMatchingNumber = true
                            board[i][j] = -1
                        }
                    }
                    if (hasMatchingNumber) {
                        if (isWinningBoard(board)) {
                            if (boards.size == 1) {
                                return sumUpBoard(boards[0]) * currGuess
                            }
                            val res = boards.filterIndexed { idx, _ ->
                                idx != k
                            }.toMutableList()
                            return findAndRemoveWinningBoardIdx(res, guesses.slice(l + 1 until guesses.size))
                        }
                    }
                }
            }
        }
        throw RuntimeException("No winning board :(")
    }

    fun part2(input: List<String>): Int {
        /*
         1. find winning board and return index
         2. remove it from list
         3. repeat
         4. stop condition is if one board exists
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
                                    i
22 13* 17* 11*  0*
 8  2 23*  4* 24
21*  9* 14* 16*  7*
 6 10*  3 18  5*
 1 12 20 15 19

 3 15  0*  2* 22
 9* 18 13* 17*  5*
19  8  7* 25 23*
20 11* 10* 24*  4*
14* 21* 16* 12  6

//14* 21* 17 24*  4*
//10 16* 15  9* 19
//18  8 23* 26 20
//22 11* 13*  6  5*
// 2* 0* 12  3  7*
         */
        val (numGuesses, boards) = splitUpInput(input)
        return findAndRemoveWinningBoardIdx(boards, numGuesses)
    }


    val input = readInput("Day04")
//    println(part1(input))
    println(part2(input))
}



