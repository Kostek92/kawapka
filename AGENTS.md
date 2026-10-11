# Android Dev Container Template

Kotlin + Jetpack Compose Android project developed inside a VS Code devcontainer
(Docker). Phone-only: no emulator (WSL2 has no `/dev/kvm`). This repo is a
template — real apps start via GitHub "Use this template", then get renamed
(see "New app checklist" below).

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
- No unit tests yet. Verification = successful build + install + launch on
  the physical phone (`adb shell am start -n <applicationId>/.MainActivity`).
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

## Working agreements

- After code changes, verify with `./gradlew assembleDebug` (+ install on the
  phone when the change is user-visible) before moving on.
- For broad "where is X used / find all implementers" searches, dispatch the
  Explore subagent instead of dumping files into context.
- Keep this file current: when you add a dependency, change the architecture,
  or learn a new gotcha, update the relevant section in the same change.

## New app checklist (after "Use this template")

1. `scripts/rename-app <new.application.id> <"App Display Name">` — rewrites
   namespace, applicationId, package dirs (via `git mv`), theme, app_name,
   rootProject.name. Requires a clean git tree. Then update README.md.
2. `applicationId` is the on-phone identity: two apps sharing one overwrite
   each other. Uninstall the old sample id if needed:
   `adb uninstall <old.application.id>`.

## Conventions

- Keep README.md human-focused (setup, phone pairing, daily commands).
  Agent/operational detail lives here, not there.
- Explain before executing when a request is ambiguous; prefer `plan` mode
  for multi-file changes the user hasn't explicitly approved.
- Never commit secrets (`google-services.json` is gitignored for per-app
  Firebase configs). Never bake credentials into the Docker image.
- Commit messages: short, imperative, no trailing period.
- NEVER `git push`. Prepare commits locally and stop — the user reviews and
  pushes.