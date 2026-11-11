# Quorix Android app and GitHub Releases

Quorix is being developed as a native Android application. The web app remains a development/admin surface and the shared backend/API; it is not the required end-user Android experience.

## ABI distribution

The Android Gradle build produces separate APKs for:
- `armeabi-v7a`: 32-bit ARM devices
- `arm64-v8a`: 64-bit ARM devices
- Universal APK: both ARM ABIs in one larger package

The 32-bit build must use a reduced-motion/low-memory profile at runtime. Feature availability must be capability-gated; do not silently ship features that require unsupported native libraries or excessive memory. The 64-bit build receives the full supported experience.

## Release process

1. Run the Android Release workflow manually to validate APK compilation.
2. After checks pass, create a version tag such as `v0.1.0`.
3. The tag workflow builds APKs and attaches them to a GitHub Release.
4. Publish source code through GitHub's automatic source archives or a tagged source bundle.

Release signing is not configured yet. Before public distribution, add a protected signing keystore and passwords as GitHub Actions secrets. Never commit the keystore or passwords. Until signing is configured, workflow APKs are CI/test builds and should not be presented as trusted production releases.

## Backend

The Android app will call the shared Quorix backend over HTTPS. Provider API keys remain server-side or in an encrypted per-user vault; never embed provider secrets in the APK.
