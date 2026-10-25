# Development Roadmap: Coffee Pod Ratings

This roadmap orders work by user value and implementation simplicity. Deliver a
small working increment at each phase. Do not start later phases merely because
their features are listed here.

The product requirements and scope boundaries live in
[`PRODUCT_SPEC.md`](PRODUCT_SPEC.md). This file describes implementation order
and observable completion criteria, not architecture. Each phase names the spec
section that governs its rules.

## Phase 1 — Browse the catalogue (dummy data)

**Goal:** Make the catalogue visible and useful before adding personal features.

**Rules:** `PRODUCT_SPEC.md` §2, §3.

- Add a small hand-written dummy fixture of Nespresso Original capsules behind
  a catalogue repository interface.
- Show a scrollable list with image, name, and short description.
- Use bundled local images.

**Complete when:** The user can browse the dummy catalogue and no single
missing image or optional field breaks the list.

## Phase 2 — Generate the catalogue snapshot (script)

**Goal:** Replace the dummy fixture with the real Nespresso Original catalogue,
generated at build time.

**Rules:** `PRODUCT_SPEC.md` §2, §3.

- Add a script to the repo, run manually, that fetches the real Nespresso
  Original capsules and writes tailored catalogue data (including images) into
  the repo, in the same shape the app already reads.
- Switch the app from the dummy fixture to the generated snapshot.
- Keep the dummy fixture available as a fallback or test source.

**Complete when:** Re-running the script regenerates the catalogue and a built
app shows the real Original capsules, with no app-side catalogue-code change.

## Phase 3 — Build a personal collection

**Goal:** Let the user choose which coffees to track.

**Rules:** `PRODUCT_SPEC.md` §4.

- Distinguish the full catalogue from “My Coffees”.
- Add a coffee from the catalogue to the personal collection.
- Show whether a catalogue coffee is already added and prevent duplicates.
- Remove a coffee from the collection without deleting the catalogue product.
- Allow adding a coffee without rating it.

**Complete when:** The user can build and edit a personal collection
independently of the full catalogue.

## Phase 4 — Rate coffees

**Goal:** Deliver the app's core value from My Coffees, without adding a coffee
detail screen.

**Rules:** `PRODUCT_SPEC.md` §4, §7.

- Add an optional 0.5–5 star rating in half-star increments to collection entries;
  unrated is `null`, not zero stars.
- Let the user set, change, and clear a rating from My Coffees with an
  accessible star control that supports half-star selection. A compact control
  that opens a rating sheet is valid; a full detail screen is not required.
- Display rated and unrated coffees distinctly, including values such as 4.5.
- Persist ratings in Room.
- Keep catalogue cards unchanged: rating is personal collection state.

**Complete when:** Ratings can be added, edited, and cleared in My Coffees,
including half-star values such as 4.5, survive an app restart and a catalogue
refresh, and an unrated coffee is never treated as a zero-star rating.

## Phase 5 — Favourites

**Goal:** Make preferred coffees easy to identify.

**Rules:** `PRODUCT_SPEC.md` §4.

- Toggle favourite status independently of rating.
- Display favourite status clearly.
- Provide a way to view favourites within the collection.

**Complete when:** A user can mark/unmark favourites without changing ratings or
collection membership.

## Phase 6 — Search and add from results

**Goal:** Make it quick to find a coffee in a growing catalogue.

**Rules:** `PRODUCT_SPEC.md` §5.

- Search by coffee name and indicate whether each result is already in the
  collection.
- Add a result directly from search.
- Provide a no-results state and a way to clear the query.

**Complete when:** A user can find and add a known coffee without manually
browsing the entire catalogue.

## Phase 7 — Sorting

**Goal:** Keep the collection manageable.

**Rules:** `PRODUCT_SPEC.md` §5.

- Sort by name, rating (both directions), and date added (both directions);
  unrated coffees go after rated coffees.
- Make the active sort option visible and use a predictable tie-breaker.

**Complete when:** Sorting behaves consistently for rated, unrated, and equally
sorted coffees.

## Phase 8 — Optional coffee details

**Rules:** `PRODUCT_SPEC.md` §2 (explicitly deferred).

Do this only after the core release is reliable. Possible increments, each
delivered separately:

- Coffee detail view with extra catalogue information such as intensity, cup
  size, flavour notes, and decaf status. It reuses the Phase 4 rating control;
  do not add a second rating implementation.
- Filters for favourites and unrated coffees, then optional product-attribute
  filters.
- Personal tasting note, tasting date, and “would buy again” flag.

**Complete when:** Each chosen addition has a clear user benefit and does not
destabilise the core collection and rating flows.

## Phase 9 — Profiles and comparison

**Rules:** `PRODUCT_SPEC.md` §6.

**Final major feature.** The default assumption is multiple profiles on the same
device; do not add cloud sync or cross-device accounts without a separate
decision.

- Create and switch between profiles.
- Keep each profile's collection membership, ratings, and favourites
  independent.
- Compare profiles' ratings for the same catalogue coffee.

**Complete when:** Switching profiles never leaks or overwrites another
profile's personal data, and comparisons align ratings to the same catalogue
coffee.

## Working rule

Keep each phase small and build only what the phase needs — no architecture,
libraries, or optional features added in anticipation of a later phase. Run the
`phase-verification` skill before declaring any phase complete.
