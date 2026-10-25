package com.application.kawapka.ui.collection

import org.junit.Assert.assertEquals
import org.junit.Test

class RatingDisplayTest {
    @Test
    fun `whole ratings are formatted without a decimal`() {
        assertEquals("1", formatRating(1f))
        assertEquals("4", formatRating(4f))
        assertEquals("5", formatRating(5f))
    }

    @Test
    fun `half ratings keep one decimal place`() {
        assertEquals("0.5", formatRating(0.5f))
        assertEquals("1.5", formatRating(1.5f))
        assertEquals("4.5", formatRating(4.5f))
    }

    @Test
    fun `star fill is empty for an unrated coffee`() {
        assertEquals(0f, starFill(rating = null, star = 1), 0f)
        assertEquals(0f, starFill(rating = null, star = 5), 0f)
    }

    @Test
    fun `star fill distinguishes full half and empty stars`() {
        assertEquals(0.5f, starFill(rating = 0.5f, star = 1), 0f)
        assertEquals(0f, starFill(rating = 0.5f, star = 2), 0f)
        assertEquals(1f, starFill(rating = 4.5f, star = 1), 0f)
        assertEquals(1f, starFill(rating = 4.5f, star = 4), 0f)
        assertEquals(0.5f, starFill(rating = 4.5f, star = 5), 0f)
        assertEquals(0f, starFill(rating = 4f, star = 5), 0f)
    }
}
