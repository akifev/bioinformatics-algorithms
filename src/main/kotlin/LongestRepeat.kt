fun DNA.longestRepeat(): String? =
    (length - 1 downTo 1)
        .firstNotNullOfOrNull { windowSize -> windowed(windowSize).find { window -> split(window).size > 2 } }

fun main() {
    val dna = readln()
    val longestRepeat = dna.longestRepeat()
    println(longestRepeat)
}