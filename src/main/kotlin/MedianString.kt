fun Collection<DNA>.findMedianStringByMer(mer: Int): String {
    val possiblePatterns = flatMap { dna -> dna.windowed(mer) }.toSet().toList()
    return possiblePatterns.minBy { pattern -> hammingDistance(pattern) }
}

fun Collection<DNA>.hammingDistance(pattern: String): Int =
    sumOf { dna -> minHammingDistance(pattern, dna) }

fun main() {
    val k = readln().toInt()
    val dnas = buildList { while (true) add(readlnOrNull() ?: break) }

    val medianString = dnas.findMedianStringByMer(mer = k)
    println(medianString)
}