package com.application.kawapka.ui.collection

/**
 * Formats a rating for display without a redundant trailing `.0`.
 *
 * Ratings are stored in half-star steps, so only one decimal place is needed.
 */
internal fun formatRating(rating: Float): String =
    if (rating % 1f == 0f) {
        rating.toInt().toString()
    } else {
        rating.toString()
    }

/**
 * Returns how much of [star] is filled by [rating]: empty, half, or full.
 */
internal fun starFill(
    rating: Float?,
    star: Int,
): Float {
    if (rating == null) return 0f
    return (rating - (star - 1)).coerceIn(0f, 1f)
}
