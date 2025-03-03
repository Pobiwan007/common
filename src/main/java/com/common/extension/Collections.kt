package com.common.extension

import androidx.compose.ui.util.fastJoinToString

typealias ListPairStrings = MutableList<Pair<String, List<String>>>

fun ListPairStrings.findItemByFirstValue(first: String): Pair<String, List<String>>?{
    if(this.isEmpty()) return null
    for(item in this){
        if(item.first == first){
            return item
        }
    }
    return null
}

fun MutableList<Pair<String, MutableList<Boolean>>>.addFirstPair(pair: Pair<String, MutableList<Boolean>>){
    val index = this.indexOfFirst { it.first == pair.first }
    if(index != -1){
        this[index] = pair
    }else{
        this.add(pair)
    }
}

fun MutableList<Pair<String, Boolean>>.updateBooleanByFirst(first: String){
    val index = this.indexOfFirst { it.first == first }
    if (index != -1) {
        val currentPair = this[index]
        this[index] = currentPair.copy(second = !currentPair.second)
    }
}