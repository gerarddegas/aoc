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

    fun part2(input: List<String>) {
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}



