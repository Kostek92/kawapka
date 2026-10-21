package com.application.podlog.data.catalogue

import androidx.annotation.DrawableRes

/**
 * Shared product information for one coffee in the catalogue.
 *
 * This model must not contain personal data. Collection membership, ratings,
 * and favourites are stored separately and keyed by [id], so profiles can be
 * added later without changing the meaning of existing catalogue data.
 *
 * @property id Stable Nespresso SKU, or a stable slug when no SKU is available.
 * @property headline Short flavour headline shown on the catalogue tile.
 * @property intensity Nespresso intensity on a 1..[MAX_INTENSITY] scale, or
 *   `null` when no intensity applies.
 * @property aromatics Aroma/flavour tags from the catalogue. Not shown on the
 *   catalogue tile; reserved for a future detail view.
 * @property shortDescription Longer description. Not shown on the catalogue
 *   tile; reserved for a future detail view.
 * @property imageRes Bundled product image, or `null` when no image is available.
 */
data class CatalogueCoffee(
    val id: String,
    val name: String,
    val headline: String? = null,
    val intensity: Int? = null,
    val aromatics: List<String> = emptyList(),
    val shortDescription: String? = null,
    @DrawableRes val imageRes: Int? = null,
) {
    companion object {
        /** Nespresso Original capsules are rated on an intensity scale from 1 to 13. */
        const val MAX_INTENSITY = 13
    }
}
