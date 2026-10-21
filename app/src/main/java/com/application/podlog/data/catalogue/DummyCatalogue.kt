package com.application.podlog.data.catalogue

import com.application.podlog.R

/**
 * Hand-written Phase 1 fixture.
 *
 * It deliberately contains a coffee without an image, a coffee without a
 * description or headline, and a coffee without an intensity so the catalogue
 * screen always exercises the optional fields.
 */
object DummyCatalogue {
    val coffees: List<CatalogueCoffee> =
        listOf(
            CatalogueCoffee(
                id = "ispirazione-ristretto-italiano",
                name = "Ispirazione Ristretto Italiano",
                headline = "Mocna i pełna kontrastów",
                intensity = 10,
                aromatics = listOf("INTENSYWNIE PALONYCH ZIAREN", "OWOCE LEŚNE"),
                shortDescription = "Rich and contrasting, with roasted notes.",
                imageRes = R.drawable.capsule_ristretto,
            ),
            CatalogueCoffee(
                id = "ispirazione-genova-livanto",
                name = "Ispirazione Genova Livanto",
                headline = "Krągła i wyważona",
                intensity = 7,
                aromatics = listOf("AROMATY KARMELU", "ZRÓWNOWAŻONE"),
                shortDescription = "Round and caramelized, with a lingering sweetness.",
                imageRes = R.drawable.capsule_livanto,
            ),
            CatalogueCoffee(
                id = "ispirazione-firenze-arpeggio",
                name = "Ispirazione Firenze Arpeggio",
                headline = "Intensywna i kremowa",
                intensity = 9,
                aromatics = listOf("KAKAO", "INTENSYWNIE PALONYCH ZIAREN"),
                shortDescription = "Dense and creamy, with cocoa and velvety notes.",
                imageRes = R.drawable.capsule_arpeggio,
            ),
            CatalogueCoffee(
                id = "master-origin-india",
                name = "Master Origin India",
                headline = "Intensywna i korzenna",
                intensity = 11,
                aromatics = listOf("KORZENNY"),
                shortDescription = "Intense and spicy, with monsooned Robusta.",
                imageRes = R.drawable.capsule_india,
            ),
            CatalogueCoffee(
                id = "barista-creations-chiaro",
                name = "Barista Creations Chiaro",
                headline = "Łagodna i kremowa",
                shortDescription = "Mild and sweet, with biscuit and milk notes.",
                imageRes = R.drawable.capsule_chiaro,
            ),
            CatalogueCoffee(
                id = "master-origin-nicaragua",
                name = "Master Origin Nicaragua",
                headline = "Słodka i harmonijna",
                intensity = 5,
                aromatics = listOf("SŁODKI / ZBOŻOWY"),
                shortDescription = "Sweet and cereal, with black honey notes.",
                imageRes = R.drawable.capsule_nicaragua,
            ),
            CatalogueCoffee(
                id = "capriccio",
                name = "Capriccio",
                intensity = 5,
                aromatics = listOf("ZBOŻOWY", "ZRÓWNOWAŻONE"),
                imageRes = R.drawable.capsule_capriccio,
            ),
            CatalogueCoffee(
                id = "volluto",
                name = "Volluto",
                headline = "Słodka i delikatna",
                intensity = 4,
                aromatics = listOf("ZRÓWNOWAŻONE", "OWOCOWY", "ZBOŻOWY"),
                shortDescription = "Sweet and biscuity, with a light roast.",
            ),
        )
}
