import kotlin.random.Random

typealias Motif = String

private fun List<DNA>.mapEachToRandomPossibleMotif(mer: Int): List<Motif> = map { dna ->
    val left = Random.nextInt(dna.length - mer)
    val right = left + mer

    dna.substring(left, right)
}

private fun List<Motif>.profile(pseudoCount: Float, alphabet: CharArray): Array<FloatArray> =
    Array(alphabet.size) { i ->
        FloatArray(get(0).length) { j ->
            val column = map { it[j] }
            val count = column.count { it == alphabet[i] }
            (pseudoCount + count) / (pseudoCount + pseudoCount * size)
        }
    }

private fun List<Motif>.score(): Int =
    (0 until get(0).length).sumOf { i ->
        val column = map { it[i] }
        size - column.groupBy { it }.mapValues { it.value.size }.maxOf { it.value }
    }


private fun Motif.probability(profile: Array<FloatArray>, alphabet: CharArray): Float =
    mapIndexed { index, c -> profile[alphabet.indexOf(c)][index] }.reduce { acc, x -> acc * x }

private fun DNA.allPossibleMotifs(mer: Int): Set<Motif> =
    windowed(mer).toSet()

private fun DNA.mostProbableMotif(profile: Array<FloatArray>, alphabet: CharArray): Motif =
    allPossibleMotifs(mer = profile[0].size)
        .map { motif -> Pair(motif, motif.probability(profile, alphabet)) }
        .maxBy { it.second }.first


private fun List<DNA>.mostProbableMotifs(profile: Array<FloatArray>, alphabet: CharArray): List<Motif> = map { dna ->
    dna.mostProbableMotif(profile, alphabet)
}

private fun List<DNA>.randomMotifs(mer: Int, pseudoCount: Float = 1f): List<Motif> {
    val alphabet = charArrayOf('A', 'C', 'G', 'T')

    var bestMotifs = mapEachToRandomPossibleMotif(mer = mer)
    var bestScore = bestMotifs.score()

    while (true) {
        val profile = bestMotifs.profile(pseudoCount, alphabet)
        val motifs = mostProbableMotifs(profile, alphabet)
        val score = motifs.score()

        if (score < bestScore) {
            bestMotifs = motifs
            bestScore = score
        } else {
            break
        }
    }

    return bestMotifs
}

private fun randomizedMotifSearch(dnas: List<String>, k: Int, maxIterations: Int = 1000): List<Motif> {
    var bestMotifs = dnas.randomMotifs(mer = k)
    var bestScore = bestMotifs.score()

    var iterationsWithoutChanges = 0

    while (iterationsWithoutChanges < maxIterations) {
        val motifs = dnas.randomMotifs(mer = k)
        val score = motifs.score()

        if (score < bestScore) {
            bestMotifs = motifs
            bestScore = score

            iterationsWithoutChanges = 0
        } else {
            iterationsWithoutChanges++
        }
    }

    return bestMotifs
}

fun main() {
    val (k, t) = readln().split(" ").map { it.toInt() }
    val dnas = buildList(t) { repeat(t) { add(readln()) } }

    val motifs = randomizedMotifSearch(dnas, k)

    motifs.forEach(::println)
}