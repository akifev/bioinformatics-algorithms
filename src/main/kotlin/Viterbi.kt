import kotlin.math.log2

fun main() {
    val string = readln()
    readln()
    val alphabet = readSplitted()
    readln()
    val states = readSplitted()
    readln()
    val transitions = readTable(states.size)
    readln()
    val emissions = readTable(states.size)

    val observations = string.map { alphabet.indexOf(it.toString()) }

    println(viterbi(observations, states, transitions, emissions))
}

fun viterbi(
    observations: List<Int>,
    states: List<String>,
    transitions: List<List<Float>>,
    emissions: List<List<Float>>
): String {
    val probs = Array(observations.size) { FloatArray(states.size) }
    probs[0] = emissions.map { log2(it[observations[0]] * 0.5f) }.toFloatArray()

    val previous = Array(observations.size) { IntArray(states.size) }

    for (t in 1 until observations.size) {
        for (j in states.indices) {
            val emission = log2(emissions[j][observations[t]])

            val (index, bestProb) = probs[t - 1]
                .zip(transitions.map { log2(it[j]) })
                .map { (x, y) -> x + y + emission }
                .withIndex()
                .maxBy { it.value }

            previous[t - 1][j] = index
            probs[t][j] = bestProb
        }
    }

    val indexes = mutableListOf<Int>()
    indexes.add(probs.last().withIndex().maxBy { it.value }.index)

    for (i in 0 until observations.size - 1) {
        indexes.add(previous[observations.size - 2 - i][indexes[i]])
    }

    return indexes.reversed().joinToString("") { states[it] }
}

private fun readTable(size: Int): List<List<Float>> =
    buildList {
        readln()
        repeat(size) {
            val dropped = readSplitted().drop(1)
            add(dropped.map { it.toFloat() })
        }
    }

private fun readSplitted(): List<String> = readln().split("\\s+".toRegex()).filterNot { it.isEmpty() }