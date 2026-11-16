package com.common.extension

typealias ListPairStrings = MutableList<Pair<String, List<String>>>

/** Returns the first pair whose key equals [first], or `null` if there is none. */
fun ListPairStrings.findItemByFirstValue(first: String): Pair<String, List<String>>? =
    firstOrNull { it.first == first }

/** Replaces the pair with the same key as [pair], or appends [pair] if the key is new. */
fun MutableList<Pair<String, MutableList<Boolean>>>.addFirstPair(pair: Pair<String, MutableList<Boolean>>) {
    val index = indexOfFirst { it.first == pair.first }
    if (index != -1) this[index] = pair else add(pair)
}

/** Toggles the boolean value of the pair whose key equals [first]. No-op if the key is absent. */
fun MutableList<Pair<String, Boolean>>.updateBooleanByFirst(first: String) {
    val index = indexOfFirst { it.first == first }
    if (index != -1) {
        val current = this[index]
        this[index] = current.copy(second = !current.second)
    }
}
