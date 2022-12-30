fun reconstructStringFromPairComposition(k: Int, d: Int, composition: List<Pair<String, String>>): String {
    val shifts = composition.mapNotNull { (left, right) ->
        composition.find { (l, r) ->
            left.substring(1) == l.substring(0, l.length - 1) && right.substring(1) == r.substring(0, r.length - 1)
        }?.let { x -> Pair(Pair(left, right), x) }
    }.toMap()

    var cur = composition.find { (left, right) ->
        shifts.values.all { (l, r) -> left != l || right != r }
    }

    val res = CharArray(k * 2 + d + composition.size - 1) { '0' }
    var p = 0

    while (cur != null) {
        val (left, right) = cur

        left.forEachIndexed { i, c -> res[p + i] = c }
        right.forEachIndexed { i, c -> res[p + i + d + k] = c }

        if (++p >= composition.size) {
            break
        }

        cur = shifts[cur]
    }

    return res.concatToString()
}

fun main() {
    val (k, d) = readln().split(" ").map { it.toInt() }
    val composition = buildList { while (true) add(readlnOrNull() ?: break) }
        .map {
            val split = it.split("|")
            Pair(split[0], split[1])
        }

    println(reconstructStringFromPairComposition(k, d, composition))
}
