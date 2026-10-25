---
name: phase-verification
description: Use after completing any phase in docs/ROADMAP.md for the coffee pod ratings app to verify it. Covers persistence of collection membership, ratings, and date-added across app restarts; catalogue-refresh safety; empty, error, and missing-data states; plus ktlint/check, JVM unit tests, build, install, and on-phone manual checks.
---

# Phase verification (coffee pod ratings)

Run this before declaring any roadmap phase complete. The goal is to keep the
basic app dependable for everyday use: personal data must survive restarts and
catalogue refreshes, edge states must not crash, and the build must be clean and
installed.

Read `docs/ROADMAP.md` for phase order and `docs/PRODUCT_SPEC.md` for the
product rules this verification protects (catalogue vs. personal data, unrated
is not zero-star).

## 1. Automated checks

Run from the repo root:

- `./gradlew ktlintCheck` — formatting is wired into `check`; fix with
  `./gradlew ktlintFormat`.
- `./gradlew test` — JVM unit tests for pure logic (rating set/change/clear,
  sort comparators, unrated ordering, snapshot/stable-id mapping). Add tests as
  that logic lands.
- `./gradlew assembleDebug` — must build cleanly.
- `./gradlew installDebug` — install to the connected phone.
- `adb shell am start -n <applicationId>/.MainActivity` — launch. The
  application id is in `app/build.gradle.kts`.

If Kotlin IntelliSense is dead, check Output → Gradle for import errors before
blaming the editor.

## 2. Personal data persistence (on the phone)

Manually exercise these flows, restarting the app where noted:

- Add coffees to the personal collection; add at least one without rating it.
- Set, change, and clear ratings; confirm a cleared rating returns to the
  unrated state (never rendered as zero stars).
- Confirm personal data is read from and written to Room (the single source of
  truth), not in-memory state.
- Fully close and reopen the app. Collection membership, ratings, and date-added
  values must all persist.
- Confirm date-added sorting stays consistent for entries added at different
  times.

## 3. Catalogue refresh safety

Applies once the catalogue-download phase exists. Before then, verify that
replacing the bundled fixture or changing the repository source leaves the Room
data untouched.

- Trigger a catalogue refresh. Personal collection membership, ratings, and
  date-added values must be unaffected.
- Remove a product from the catalogue source (or simulate its disappearance) and
  refresh. A coffee that disappears from the latest catalogue must keep its
  existing personal history; do not infer discontinuation from a failed
  request.

## 4. Edge states

Confirm each state is handled without breaking the screen and offers a retry
where retrying makes sense. Loading and failed-request states apply once the
catalogue-download phase exists; until then, cover the rest.

- Loading state.
- Failed catalogue request.
- Empty catalogue / empty collection / no search results; a way to clear the
  query.
- Missing image and absent optional fields on an individual coffee.

## 5. Report

- Summarize which checks passed and which failed, with the phase and the exact
  flow that broke.
- Do not mark a phase complete while a persistence, catalogue-refresh, or edge
  state check fails; report the failure instead of fixing it as part of this
  skill.
