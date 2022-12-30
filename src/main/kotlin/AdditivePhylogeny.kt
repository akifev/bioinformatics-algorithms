import kotlin.math.min

private typealias DistanceMatrix = Array<IntArray>
private typealias Graph = MutableList<MutableSet<Pair<Int, Int>>>

private fun DistanceMatrix.limbLength(n: Int): Int {
    var min = Int.MAX_VALUE

    for (i in 0 until n) {
        for (j in 0 until n) {
            if (i == j || i == n - 1 || j == n - 1) continue
            min = min(min, (get(n - 1)[i] + get(n - 1)[j] - get(j)[i]) / 2)
        }
    }

    return min
}

private fun DistanceMatrix.additivePhylogeny(n: Int): Graph {
    if (n == 2) {
        val g: Graph = MutableList(size) { mutableSetOf() }

        g[0].add(Pair(1, get(0)[1]))
        g[1].add(Pair(0, get(1)[0]))

        return g
    }

    val limbLength = limbLength(n)

    fun findIJ(): Pair<Int, Int> {
        for (i in 0 until n - 1) {
            for (j in 0 until n - 1) {
                if (i == j) continue

                if (get(i)[j] == get(i)[n - 1] + get(n - 1)[j] - 2 * limbLength) {
                    return Pair(i, j)
                }
            }
        }
        error("something went wrong")
    }

    val (i, j) = findIJ()

    fun Graph.addEdge(u: Int, v: Int, w: Int) {
        get(u).add(Pair(v, w))
        get(v).add(Pair(u, w))
    }

    fun Graph.removeEdge(u: Int, v: Int, w: Int) {
        get(u).remove(Pair(v, w))
        get(v).remove(Pair(u, w))
    }

    fun Graph.addNode(node: Int, prev: Int, distance: Int): Boolean {
        val distances = this@additivePhylogeny
        val restDist = distances[i][n - 1] - limbLength

        if (node == j) {
            if (distance == restDist) {
                addEdge(node, n - 1, limbLength)
            }
            return true
        }

        for ((u, wu) in get(node)) {
            if (u == prev) continue

            if (addNode(u, node, distance + wu)) {
                if (distance == restDist) {
                    addEdge(node, n - 1, limbLength)
                } else if (distance < restDist && restDist < distance + wu) {
                    add(mutableSetOf())

                    removeEdge(node, u, wu)

                    val newNode = lastIndex

                    addEdge(node, newNode, restDist - distance)
                    addEdge(u, newNode, distance + wu - restDist)
                    addEdge(n - 1, newNode, limbLength)
                }
                return true
            }
        }
        return false
    }

    return additivePhylogeny(n - 1).apply { addNode(i, -1, 0) }
}

fun main() {
    val n = readln().toInt()
    val distances = buildList {
        repeat(n) {
            add(readln()
                .replace("\\s+".toRegex(), " ")
                .split(" ")
                .map { it.toInt() }
                .toIntArray()
            )
        }
    }.toTypedArray()

    val graph = distances.additivePhylogeny(n)

    graph.forEachIndexed { from, nexts ->
        nexts.forEach { (to, dist) ->
            println("$from->$to:$dist")
        }
    }
}