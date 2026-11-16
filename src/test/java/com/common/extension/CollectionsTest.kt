package com.common.extension

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CollectionsTest {

    @Test
    fun `findItemByFirstValue returns matching pair or null`() {
        val list: ListPairStrings = mutableListOf("a" to listOf("1"), "b" to listOf("2"))

        assertEquals("b" to listOf("2"), list.findItemByFirstValue("b"))
        assertNull(list.findItemByFirstValue("c"))
        assertNull(mutableListOf<Pair<String, List<String>>>().findItemByFirstValue("a"))
    }

    @Test
    fun `addFirstPair replaces existing key and appends a new one`() {
        val list = mutableListOf("a" to mutableListOf(true))

        list.addFirstPair("a" to mutableListOf(false))
        list.addFirstPair("b" to mutableListOf(true))

        assertEquals(listOf("a" to listOf(false), "b" to listOf(true)), list)
    }

    @Test
    fun `updateBooleanByFirst toggles only the matching key`() {
        val list = mutableListOf("a" to false, "b" to true)

        list.updateBooleanByFirst("a")
        list.updateBooleanByFirst("missing")

        assertEquals(listOf("a" to true, "b" to true), list)
    }
}
