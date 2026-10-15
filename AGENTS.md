# podlog — Coffee Pod Ratings

Kotlin + Jetpack Compose Android app for keeping a personal collection of
Nespresso Original capsules and rating them, developed inside a VS Code
devcontainer (Docker). Phone-only: no emulator (WSL2 has no `/dev/kvm`).

## Environment

- You run **inside the container**  with JDK 17,
  Android SDK at `/opt/android-sdk` (API 34 + 35, build-tools 34.0.0/35.0.0),
  Gradle wrapper 8.7, AGP 8.5.2, Kotlin 2.0.20, Compose BOM 2024.06.00.
- `local.properties` (`sdk.dir=/opt/android-sdk`) is gitignored and generated
  by `postCreateCommand` — never commit it, never hand-write SDK paths.
- opencode CLI is baked into the image (always latest release, installed via
  the upstream script with `--no-modify-path`, on `PATH` via `ENV`).
  Dockerfile keeps SDK install and tooling in **separate RUN layers** so SDK
  layers stay cached — do not merge them.
- App stack: Room for personal-data persistence. Coil (images) and a network
  client (Retrofit/Ktor + kotlinx.serialization) arrive with the
  catalogue-download phase. Add each dependency as its phase needs it and
  record it here in the same change.

## Build, install, verify

- `./gradlew assembleDebug` — build. APK lands in
  `app/build/outputs/apk/debug/app-debug.apk`.
- `./gradlew installDebug` — install to the connected phone.
- `./gradlew ktlintFormat` — autoformat Kotlin (`app/`, incl. build scripts
  under the module). `./gradlew ktlintCheck` verifies; it is wired into
  `check`. Style lives in `.editorconfig` (ktlint code style
  `ktlint_official`) and is read by both ktlint and the editor.
- `scripts/adb-wireless pair|connect|devices` — WiFi debugging (Android 11+).
  Pair once per phone; `connect` each session.
- `./gradlew test` — JVM unit tests for pure logic (rating set/change/clear,
  sort comparators, snapshot/stable-id mapping). Add tests as that logic lands.
- Verification of user-visible work = successful build + install + launch on the
  physical phone (`adb shell am start -n <applicationId>/.MainActivity`).
- Kotlin IntelliSense (JetBrains `kotlin-server`) needs a successful Gradle
  import first. If completion is dead, check Output → Gradle channel for
  import errors before blaming the editor.

## Kotlin & Compose rules

- Compose-only: no XML layouts, no Fragments. Single Activity hosts the NavHost.
- State hoisting: split stateful `*Route` (ViewModel, lifecycle collection)
  from stateless `*Screen` (pure rendering of state + event callbacks).
- ViewModels expose `StateFlow`; never `LiveData`. State flows down,
  events flow up.
- No business logic in `@Composable` functions; it belongs in
  ViewModels/repositories.
- Kotlin idioms over Java patterns: `val` by default, safe calls over `!!`
  (`!!` needs a justification comment), coroutines/`Flow` over
  callbacks/threads.
- Composables take `modifier: Modifier = Modifier` as first optional param.
- Use Material 3 theme colors/typography; never hardcode colors.
- minSdk is 26 — check API-level compatibility when using newer Android APIs.
- Always invoke Gradle via `./gradlew` (wrapper), never a global `gradle`.

## App scope: coffee pod ratings

- Product requirements and scope boundaries live in `docs/PRODUCT_SPEC.md`;
  implementation order and completion criteria live in `docs/ROADMAP.md`.
  Read the relevant document before planning or implementing app features.
- Visual reference lives in `docs/prototype/` — `screens/*.png` are the mockups
  and `code/*.html` their HTML source. Use it as a prototype/visual source for
  layout, styling, and flow only: its labels and copy contain deliberate or
  outdated wording that does not match this app (e.g. “Discover”, “My Coffees”,
  “Favourites” where the spec says catalogue / personal collection). When the
  prototype and `PRODUCT_SPEC.md` disagree, the spec wins.
- Follow `docs/ROADMAP.md` for the implementation sequence. Prefer the smallest
  useful increment and do not implement later-phase features prematurely.
- After finishing any roadmap phase, run the `phase-verification` skill before
  declaring the phase complete.

## Working agreements

- After code changes, verify with `./gradlew assembleDebug` (+ install on the
  phone when the change is user-visible) before moving on.
- For broad "where is X used / find all implementers" searches, dispatch the
  Explore subagent instead of dumping files into context.
- Keep this file current: when you add a dependency, change the architecture,
  or learn a new gotcha, update the relevant section in the same change.
- Explain before executing when a request is ambiguous; prefer `plan` mode
  for multi-file changes the user hasn't explicitly approved.

with `adb uninstall <old.application.id>` when renaming an installed app.

## Conventions

- Keep README.md human-focused (setup, phone pairing, daily commands).
  Agent/operational detail lives here, not there.
- Never commit secrets (`google-services.json` is gitignored for per-app
  Firebase configs). Never bake credentials into the Docker image.
- Commit messages: short, imperative, no trailing period.
- NEVER `git push`. Prepare commits locally and stop — the user reviews and
  pushes.
