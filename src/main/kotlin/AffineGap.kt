object Blossum62 {
    private val alphabet =
        charArrayOf('A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y')

    private val matrix = buildList {
        add(intArrayOf(4, 0, -2, -1, -2, 0, -2, -1, -1, -1, -1, -2, -1, -1, -1, 1, 0, 0, -3, -2))
        add(intArrayOf(0, 9, -3, -4, -2, -3, -3, -1, -3, -1, -1, -3, -3, -3, -3, -1, -1, -1, -2, -2))
        add(intArrayOf(-2, -3, 6, 2, -3, -1, -1, -3, -1, -4, -3, 1, -1, 0, -2, 0, -1, -3, -4, -3))
        add(intArrayOf(-1, -4, 2, 5, -3, -2, 0, -3, 1, -3, -2, 0, -1, 2, 0, 0, -1, -2, -3, -2))
        add(intArrayOf(-2, -2, -3, -3, 6, -3, -1, 0, -3, 0, 0, -3, -4, -3, -3, -2, -2, -1, 1, 3))
        add(intArrayOf(0, -3, -1, -2, -3, 6, -2, -4, -2, -4, -3, 0, -2, -2, -2, 0, -2, -3, -2, -3))
        add(intArrayOf(-2, -3, -1, 0, -1, -2, 8, -3, -1, -3, -2, 1, -2, 0, 0, -1, -2, -3, -2, 2))
        add(intArrayOf(-1, -1, -3, -3, 0, -4, -3, 4, -3, 2, 1, -3, -3, -3, -3, -2, -1, 3, -3, -1))
        add(intArrayOf(-1, -3, -1, 1, -3, -2, -1, -3, 5, -2, -1, 0, -1, 1, 2, 0, -1, -2, -3, -2))
        add(intArrayOf(-1, -1, -4, -3, 0, -4, -3, 2, -2, 4, 2, -3, -3, -2, -2, -2, -1, 1, -2, -1))
        add(intArrayOf(-1, -1, -3, -2, 0, -3, -2, 1, -1, 2, 5, -2, -2, 0, -1, -1, -1, 1, -1, -1))
        add(intArrayOf(-2, -3, 1, 0, -3, 0, 1, -3, 0, -3, -2, 6, -2, 0, 0, 1, 0, -3, -4, -2))
        add(intArrayOf(-1, -3, -1, -1, -4, -2, -2, -3, -1, -3, -2, -2, 7, -1, -2, -1, -1, -2, -4, -3))
        add(intArrayOf(-1, -3, 0, 2, -3, -2, 0, -3, 1, -2, 0, 0, -1, 5, 1, 0, -1, -2, -2, -1))
        add(intArrayOf(-1, -3, -2, 0, -3, -2, 0, -3, 2, -2, -1, 0, -2, 1, 5, -1, -1, -3, -3, -2))
        add(intArrayOf(1, -1, 0, 0, -2, 0, -1, -2, 0, -2, -1, 1, -1, 0, -1, 4, 1, -2, -3, -2))
        add(intArrayOf(0, -1, -1, -1, -2, -2, -2, -1, -1, -1, -1, 0, -1, -1, -1, 1, 5, 0, -2, -2))
        add(intArrayOf(0, -1, -3, -2, -1, -3, -3, 3, -2, 1, 1, -3, -2, -2, -3, -2, 0, 4, -3, -1))
        add(intArrayOf(-3, -2, -4, -3, 1, -2, -2, -3, -3, -2, -1, -4, -4, -2, -3, -3, -2, -3, 11, 2))
        add(intArrayOf(-2, -2, -3, -2, 3, -3, 2, -1, -2, -1, -1, -2, -3, -1, -2, -2, -2, -1, 2, 7))
    }

    fun score(a: Char, b: Char): Int {
        return matrix[alphabet.indexOf(a)][alphabet.indexOf(b)]
    }
}

fun getAffineGapScore(a: String, b: String, se: Int, so: Int, score: (Char, Char) -> Int): Triple<Int, String, String> {
    val n = a.length + 1
    val m = b.length + 1
    val matches = Array(n) { Array(m) { 0 } }
    val deletions = Array(n) { Array(m) { 0 } }
    val insertions = Array(n) { Array(m) { 0 } }
    val matchesDirections = Array(n) { Array(m) { -1 } }
    val deletionsDirections = Array(n) { Array(m) { -1 } }
    val insertionsDirections = Array(n) { Array(m) { -1 } }

    matches[0][0] = 0
    deletions[0][0] = 0
    insertions[0][0] = 0
    for (i in 1 until n) {
        matches[i][0] = so + (i - 1) * se
        deletions[i][0] = so + (i - 1) * se
        insertions[i][0] = so + (i - 1) * se
    }
    for (j in 1 until m) {
        matches[0][j] = so + (j - 1) * se
        deletions[0][j] = so + (j - 1) * se
        insertions[0][j] = so + (j - 1) * se
    }

    for (i in 1 until n) {
        for (j in 1 until m) {
            fun processTransition(sources: IntArray, scoreMatrix: Array<Array<Int>>, directions: Array<Array<Int>>) {
                val maximum = sources.withIndex().maxBy { it.value }

                val direction = maximum.index
                scoreMatrix[i][j] = maximum.value

                directions[i][j] = direction
            }

            val insertionsSources = intArrayOf(
                insertions[i - 1][j] + se,
                Int.MIN_VALUE,
                matches[i - 1][j] + so
            )
            processTransition(insertionsSources, insertions, insertionsDirections)

            val deletionsSources = intArrayOf(
                Int.MIN_VALUE,
                deletions[i][j - 1] + se,
                matches[i][j - 1] + so
            )
            processTransition(deletionsSources, deletions, deletionsDirections)

            val matchesSources = intArrayOf(
                insertions[i][j],
                deletions[i][j],
                matches[i - 1][j - 1] + score(a[i - 1], b[j - 1])
            )
            processTransition(matchesSources, matches, matchesDirections)
        }
    }

    val totalScore = matches.last().last()

    val directionPath: List<Int> = buildList {
        var i = n - 1
        var j = m - 1

        var directions = matchesDirections

        while (i != 0 && j != 0) {
            val direction = directions[i][j]

            when {
                directions === insertionsDirections -> {
                    i--
                    if (direction == 2) {
                        directions = matchesDirections
                    } else {
                        add(direction)
                    }
                }
                directions === deletionsDirections -> {
                    j--
                    if (direction == 2) {
                        directions = matchesDirections
                    } else {
                        add(direction)
                    }
                }
                directions === matchesDirections -> {
                    add(direction)
                    when (direction) {
                        0 -> directions = insertionsDirections
                        1 -> directions = deletionsDirections
                        2 -> {
                            i--
                            j--
                        }
                    }
                }
            }
        }
    }.reversed()

    val aAligned = buildString {
        var i = 0
        directionPath.forEach { direction ->
            when (direction) {
                0 -> append(a[i++])
                1 -> append('-')
                2 -> append(a[i++])
            }
        }
    }
    val bAligned = buildString {
        var i = 0
        directionPath.forEach { direction ->
            when (direction) {
                0 -> append('-')
                1 -> append(b[i++])
                2 -> append(b[i++])
            }
        }
    }

    return Triple(totalScore, aAligned, bAligned)
}

fun main() {
    val a = readln()
    val b = readln()
    val (totalScore, aAligned, bAligned) = getAffineGapScore(a, b, se = -1, so = -11, score = Blossum62::score)

    println(totalScore)
    println(aAligned)
    println(bAligned)
}