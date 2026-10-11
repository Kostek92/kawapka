# Android Hobby Template

A ready-to-use starting point for Android hobby projects. Everything you
need to build apps lives in a Docker container, so you never install Java
or the Android SDK on your own machine. You write code in VS Code and run
the app on your physical phone over WiFi.

Stack: Kotlin + Jetpack Compose, JDK 17, Android API 34, Gradle Wrapper.
The included `app` module is a minimal Hello World to prove the setup works.

## What you need

- A PC with Docker and VS Code (with the `Dev Containers` extension).
- An Android phone (Android 11 or newer) on the same WiFi as your PC,
  with Developer options enabled.

## Start in 5 minutes

1. Clone this repo and open it in VS Code, then choose
   **Dev Containers: Reopen in Container** (press `F1` and type it).
   The first start downloads and builds the tools (a few minutes, once).

2. Connect your phone. On the phone, open Settings → Developer options →
   **Wireless debugging** and turn it on. Tap **Pair device with pairing
   code** and note the address and 6-digit code. In the VS Code terminal:
   ```bash
   scripts/adb-wireless pair <address-shown-on-phone>
   ```
   Then go back to the Wireless debugging screen, note its address, and run:
   ```bash
   scripts/adb-wireless connect <address-shown-on-phone>
   scripts/adb-wireless devices
   ```
   Your phone appears in the list — you're connected.

3. Install the app on your phone:
   ```bash
   ./gradlew installDebug
   ```
   The HelloWorld app shows up on your phone. Open it and you'll see
   "Hello Android!".


## Start a new app from this template

```bash
git clone <this-repo> MyNewApp
code MyNewApp
```

Reopen in container, connect your phone, and build. That's it.

## Code formatting

Kotlin is autoformatted with [ktlint](https://pinterest.github.io/ktlint/):

```bash
./gradlew ktlintFormat   # rewrite files to match the style
./gradlew ktlintCheck    # verify without changing files
```

VS Code formats Kotlin on save too (see `.vscode/settings.json`). Both use
the shared rules in `.editorconfig`.

## Notes

- There is no emulator in the container — a physical phone is the way to
  run apps here.
- Pairing keys and build caches are kept in Docker volumes, so reconnecting
  your phone and rebuilding stay fast.
