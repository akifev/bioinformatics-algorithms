typealias DNA = String

fun minHammingDistance(pattern: String, text: String): Int =
    text.windowed(pattern.length).minOf { hammingDistance(pattern, it) }

fun hammingDistance(a: String, b: String): Int =
    a.zip(b).count { (ai, bi) -> ai != bi }