# Phase 5 implementation plan — Search and sorting

**Status:** In progress. 5.1–5.5 are implemented; 5.6 remains.

**Roadmap:** This phase replaces the former separate search (old Phase 6) and
sorting (old Phase 7) phases. Favourites remains skipped for the first release;
see `PRODUCT_SPEC.md` §2.

## Goal

Make it quick to find a coffee in a growing catalogue and keep the personal
collection manageable:

- Find a catalogue coffee by name.
- See whether a search result is already in My Coffees.
- Add a coffee directly from search results.
- Sort the catalogue by name or intensity.
- Sort My Coffees by name, intensity, or rating.

The phase is deliberately split into small increments. Each increment should be
independently buildable and verifiable, and should end with
`./gradlew ktlintCheck`, `./gradlew test`, and `./gradlew assembleDebug`; UI
increments also end with `./gradlew installDebug` and a quick launch on the
phone.

## Scope and decisions

### In scope

- Catalogue search by coffee name only, using a case-insensitive substring
  match. Leading and trailing whitespace in the query is ignored.
- Search results use the existing catalogue tiles in the catalogue grid. The
  existing membership indicator and Add/Added action stay in place; no separate
  search screen or second card implementation.
- A visible no-results state with a way to clear the query.
- Sorting the catalogue (Browse) by:
  - name, A–Z;
  - intensity, high to low.
- Sorting My Coffees by:
  - name, A–Z;
  - intensity, high to low;
  - rating, high to low.
- Coffees without an intensity appear after those with a value.
- Unrated coffees appear after rated coffees.
- A sort action on each tab whose menu marks and changes that tab's active
  sort.
- Independent sort choices for Browse and My Coffees.
- A stable tie-breaker for equal sort values.

### Out of scope

- Favourites and a favourites screen (skipped for the first release).
- Full-text search over descriptions, flavour notes, or aromas.
- Filters (intensity, cup size, decaf, unrated-only, etc.).
- A coffee detail screen.
- Persisting the search query or sort choice in Room.
- Sorting by date added. Date added is legacy storage only and is not displayed
  or used for ordering.
- Sorting Browse by rating. Ratings are personal collection state and do not
  affect the catalogue tab.
- Diacritic folding (for example matching `zbozowy` to `zbożowy`). Search is
  case-insensitive but otherwise matches the coffee name as written.
- New libraries, database schema changes, or a Room migration.

### Behaviour decisions

- Search and sort state live in `HomeViewModel`, not in composables. They
  survive tab switches and configuration changes; they reset on app restart.
  Persisting them is not required.
- `catalogueSort` and `collectionSort` are independent. Changing the sort on one
  tab does not change the other.
- The sort action lives in the shared top app bar and appears on both Browse and
  My Coffees. It is icon-only; the open menu marks the active option with a
  check icon, and the action's accessibility description names the current
  sort.
- The sort action is hidden when the active tab has no items but stays visible
  when a search on that tab returns no matches.
- The active sort is not shown as text in the top bar. This is a deliberate
  change from the earlier header-row design; “visible active sort” means the
  checked option in the open menu.
- The default catalogue sort is name, A–Z. The default collection sort is also
  name, A–Z.
- Every sort ends with a stable tie-breaker. Name sorts use the coffee id;
  intensity and rating sorts use the coffee name (case-insensitive) and then the
  coffee id. This makes equal values predictable without relying on input order.
- Missing intensity and unrated coffees always sort after present values.
- `dateAdded` stays in Room for backwards compatibility only. It is no longer
  carried into `CollectionItemUiState` or used for ordering, and should be
  removed in a future database migration.
- The search bar appears on both Catalogue and My Coffees. Sorting applies to
  the visible results, including filtered search results, on both tabs.

## Increments

### 5.1 — Search query state and filtering (logic only)

**Deliverable:** The ViewModel can hold a query and produce a filtered catalogue
list, with no visible UI change yet.

**Changes:**

- Add `catalogueQuery` to `HomeUiState`.
- Add a pure search helper in `ui/catalogue/` that takes the catalogue UI items
  and a query and returns matching items in catalogue order. A blank or
  whitespace-only query returns all items. Matching is a case-insensitive
  substring check against `CatalogueCoffee.name` only.
- Add `setCatalogueQuery(query)` to `HomeViewModel` and combine it with the
  collection entries flow when building `HomeUiState`.
- Keep `buildHomeUiState` usable from tests with a default blank query.

**Tests:**

- Blank and whitespace-only queries return the full catalogue.
- Partial names and different casing match.
- Headline, description, and aromatics do not match; name search only.
- No match returns an empty list.
- Membership flags are preserved by filtering.
- Collection contents are unaffected by the catalogue query.

**Done when:** Unit tests pass and the app builds with no visible change.

### 5.2 — Search bar on the Catalogue tab

**Deliverable:** A search field above the catalogue grid filters the catalogue
as the user types.

**Changes:**

- Add `query` and `onQueryChange` to `CatalogueScreen`; pass them through
  `HomeScreen` and `HomeRoute` to `HomeViewModel.setCatalogueQuery`.
- Restructure the Catalogue tab so the search field is always visible, including
  when the result list or catalogue is empty.
- Use a single-line Material 3 text field with a search icon, a placeholder such
  as “Search coffees”, and a trailing clear icon shown only when the query is
  non-blank. Clearing restores the full catalogue.
- Add the necessary strings to `res/values/strings.xml`.
- Add previews for the default and filtered states.

**Tests:**

- Existing state tests still pass with the new field.
- ViewModel test: setting a query updates `catalogueQuery` and the visible
  catalogue; clearing it restores all items.
- Previews render with a query.

**Done when:** Typing filters the grid live, clearing restores it, and the
search bar remains usable when there are no matches.

### 5.3 — Search result tiles, membership, and add from results

**Deliverable:** Search results behave like normal catalogue tiles: they show
membership and can be added directly.

**Changes:**

- Reuse `CatalogueCard`/`CoffeeCard` for filtered results. Do not create a
  separate search card or a separate search screen; the existing tile already
  satisfies the membership and add requirements.
- Keep the existing Add/Added action wired to `onAdd` while a query is active.
  After adding, the result stays visible (the name still matches) and changes
  to the Added state through the Room-backed collection flow.
- Add a no-results state that is distinct from the empty-catalogue state. It
  names the active query and offers a clear-query action.
- Ensure the clear action is available both in the search field and in the
  no-results state.
- Add content descriptions for the search field and clear action.
- Add a preview for the no-results state.

**Tests:**

- Adding a coffee from a filtered result marks it as in the collection and
  leaves the query unchanged.
- Adding twice does not create a duplicate.
- An already-added result remains visible and shows Added.
- A query with no matches produces the no-results state, and clearing the query
  returns the full catalogue.
- Missing images and optional fields still render safely in filtered results.

**Done when:** A user can type a known coffee name, add it from the result, and
see the Added state without leaving the Catalogue tab.

### 5.4 — Collection sort model and comparators (logic only)

**Deliverable:** The ViewModel can hold a selected sort and order collection
items correctly, with no visible sort control yet.

**Changes:**

- Add a `CollectionSort` enum with `Name`, `IntensityDescending`, and
  `RatingDescending`.
- Add `collectionSort` to `HomeUiState`, defaulting to `Name`.
- Add a pure sort helper in `ui/collection/` that sorts collection UI items by
  the selected option.
- Add `setCollectionSort(sort)` to `HomeViewModel` and include the selected sort
  when building `HomeUiState`.
- Apply sorting after mapping entries to catalogue coffees, so entries whose
  catalogue product disappeared remain safely hidden.
- Remove date-added ordering from the state and DAO query. Keep the
  `dateAdded` column and value for now, with a note to delete it in a future
  database migration.

**Sorting rules:**

- Name: case-insensitive name ascending, then id.
- Intensity: highest first; coffees without an intensity after all coffees with
  one; ties by name, then id.
- Rating: highest first; unrated coffees after all rated; ties by name, then id.
- Empty lists and missing catalogue products are safe.

**Tests:**

- Each sort option produces the expected order.
- Coffees without an intensity are last.
- Unrated coffees are last.
- A half-star rating sorts above unrated.
- Equal intensity and equal ratings use the documented tie-breaker.
- Name sorting is case-insensitive.
- Missing catalogue entries are ignored and do not affect the order.
- The default sort is name, A–Z.

**Done when:** Unit tests cover every sort option and the app builds with the
collection in name order and no sort control yet.

### 5.5 — Sort actions on Browse and My Coffees

**Deliverable:** The user can see and change the active sort independently on
Browse and My Coffees.

**Changes:**

- Add a `CatalogueSort` enum with `Name` and `IntensityDescending`, and a pure
  `sortCatalogue` helper in `ui/catalogue/`. Apply it to filtered catalogue
  results so search and sort compose.
- Add `catalogueSort` to `HomeUiState` and `setCatalogueSort` to `HomeViewModel`
  alongside the existing collection sort.
- Add `hasCatalogueItems` to `HomeUiState`, computed from the unfiltered
  catalogue, so the Browse sort action stays visible when a search has no
  matches and hides for an empty catalogue.
- Extract the icon-only menu from `CollectionSortAction` into a shared
  `SortAction` used by both tabs. Each tab supplies its own options and strings.
- Show the Browse action with name and intensity options. Keep the My Coffees
  action with name, intensity, and rating.
- Add strings for Browse sort options and its accessibility description, and
  update the My Coffees strings as needed.
- Add previews for both actions, with a selected sort, an unrated coffee, and a
  coffee without an intensity.
- Reset the grid scroll position to the top when the selected sort changes, so
  the new order starts from its first item.
- Keep the rating sheet working when the order changes; it already looks up its
  coffee by id.

**Tests:**

- `sortCatalogue`: name order is case-insensitive; intensity is highest first;
  missing intensity is last; equal values use name then id.
- Search plus sort produces filtered results in the selected order.
- ViewModel tests: changing either sort updates only that tab's state and order;
  each default is name.
- `hasCatalogueItems` and `hasCollectionItems` stay true when the matching query
  has no results and are false for empty data.
- Sort selections are retained when collection membership or ratings change.
- Previews render both sort actions.

**Done when:** Browse reorders by name or intensity, My Coffees reorders by
name, intensity, or rating, each tab marks its active option, and changing one
tab's sort leaves the other unchanged.

### 5.6 — Hardening, documentation, and verification

**Deliverable:** Search and sorting are covered by tests, documented, and
verified on the phone.

**Changes:**

- Add or extend tests for:
  - search plus collection membership changes;
  - query and sort retained while adding, removing, rating, and clearing a
    rating;
  - catalogue entries missing from the snapshot with every sort option;
  - coffees without an intensity sorted last on both tabs;
  - unrated coffees never treated as zero stars;
  - independent catalogue and collection sort state.
- Check tab switching manually; the tab selection lives in `HomeRoute`, while
  the query and sort live in the ViewModel.
- Update `AGENTS.md` with the search/sort state location, the sort options, and
  the decision that query and sort are in-memory ViewModel state, not Room data.
- Confirm no Room schema change or new dependency was introduced beyond the
  `dateAdded` column remaining as legacy storage.
- Run the `phase-verification` skill.

**Manual checks on the phone:**

- Search by partial name and with different casing; results filter live.
- Search for a word that only appears in a description or aroma; it does not
  match.
- Clear the query from both the field and the no-results state.
- Add an unadded result; it changes to Added and appears in My Coffees.
- Confirm an already-added result cannot be added twice.
- Switch tabs and back; the query and each sort choice are retained
  independently.
- Rotate the phone; the queries and sort choices are retained.
- Add several coffees, rate some (including a half-star value), and leave at
  least one unrated.
- Confirm the sort action appears on both Browse and My Coffees and is hidden
  when the active tab has no items.
- Open each sort menu; the active option is checked.
- On Browse, check name and intensity sorts. Coffees without an intensity stay
  after coffees with a value.
- On My Coffees, check every sort option. Unrated coffees and coffees without an
  intensity stay after coffees with a value.
- Confirm the sort action stays available when a search on either tab has no
  matches.
- Confirm equal ratings and equal intensities keep a stable order.
- Change and clear a rating while sorted by rating; the coffee moves to the
  correct group.
- Confirm date added is not displayed or used for ordering.
- Restart the app. Collection membership and ratings persist; the queries clear
  and both sorts return to name order, as expected.
- Refresh the catalogue and confirm personal data and sorting remain safe.
- Check empty catalogue, empty collection, no search results, missing image,
  and missing optional fields.

**Done when:** `./gradlew ktlintCheck`, `./gradlew test`,
`./gradlew assembleDebug`, and `./gradlew installDebug` pass; the app launches;
the manual checklist passes; and the phase-verification report has no unresolved
persistence, catalogue-refresh, or edge-state failure.

## Suggested commit sequence

1. `Add catalogue search filtering`
2. `Add catalogue search bar`
3. `Complete search result states`
4. `Drop date added from collection sorting`
5. `Add collection sort comparators`
6. `Add collection sort action`
7. `Add catalogue sort comparators`
8. `Add catalogue sort action`
9. `Verify search and sorting`

Each commit should leave the app buildable and the tests green.
