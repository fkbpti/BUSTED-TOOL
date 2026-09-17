package com.bustedtool

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {
    @Test
    fun sha256IsStable() {
        assertEquals(
            "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
            digest("SHA-256", "hello".toByteArray())
        )
    }

    @Test
    fun sha512IsProduced() {
        val result = digest("SHA-512", "hello".toByteArray())
        assertEquals(128, result.length)
        assertTrue(result.all { it in "0123456789abcdef" })
    }
}
