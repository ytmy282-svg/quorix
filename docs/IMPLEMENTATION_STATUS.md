# Quorix implementation status

Last updated: 2026-11-11

This document separates code that can be completed in the repository from work that requires account credentials, provider approval, infrastructure, or physical-device validation.

## Implemented in the current branch

- Android Kotlin/Jetpack Compose application shell.
- Android ABI split configuration for ARMv7, ARM64, and a universal APK.
- Quorix white/frosted sidebar, mode selector, thinking selector, and capsule composer.
- Android document picker for user-selected files (selection only; files are not uploaded to a backend yet).
- Persistent wallpaper URI selection, wallpaper reset, and image display.
- Android system speech-recognition intent for voice-to-text (requires a compatible speech-recognition service on the device).
- Expandable provider metadata catalog with core, inference, aggregator, and media categories.
- Initial authenticated server-side streaming adapters for OpenAI, OpenRouter, Groq, and DeepSeek.
- Final partial SSE line handling in the web chat client.
- Supabase workspace/provider-connection schema and basic sign-in/sign-up screens.
- GitHub Actions workflow to build APK variants and attach artifacts; tagged builds can publish a GitHub Release.

## Partially implemented

- Android chat UI: currently local preview only; it does not call the authenticated chat API.
- Attachments: Android can pick a file and show its name; upload, storage, parsing, and model context are not wired.
- Voice: speech-to-text launches the Android system recognizer; in-app speech playback and backend transcription are not wired.
- Wallpaper: selection and persistence work; blur, brightness, overlay controls are not implemented.
- 32-bit Lite mode: architecture detection/label only; reduced animations, memory tuning, and feature gating remain.
- Provider Hub: catalog exists; credential management, per-user encrypted secrets, model discovery, and most provider adapters remain.
- BOSS and AI Review: UI concepts/catalog only; orchestration and review execution are not implemented.
- Web workspace: early preview; modules are navigation placeholders.

## Requires user-owned accounts, credentials, or external setup

- Supabase project creation and production URL/keys, database migration execution, redirect URLs, and auth configuration.
- Provider API keys and confirmation of account/model access. No API key should be committed to GitHub or pasted into public source.
- Hosting account/project and environment variables for the backend.
- OAuth client registration and provider approval for any official account-login integrations.
- GitHub repository secrets for release signing, if signed production APKs are required.
- Domain, privacy policy, terms, and store accounts if public distribution is planned.

No paid plan or subscription is required or authorized by this implementation. Free-tier availability, limits, and trial eligibility depend on each provider and can change.

## Requires physical-device or external acceptance testing

- Build/install/launch tests on real ARMv7 and ARM64 Android phones.
- Android 13/14/15 file picker, speech recognition, wallpaper persistence, keyboard, and lifecycle tests.
- Network interruption, long-stream, rate-limit, and provider error tests.
- APK signing, upgrade/install behavior, and release artifact verification.
- Security review, API-key storage review, and database RLS verification.

## Not complete yet

1. Native Android authentication and secure session handling.
2. Android-to-backend chat API integration and persistent chat history.
3. File upload/storage/processing and multimodal prompt handling.
4. Voice playback, transcription service integration, and permission/error UX.
5. Per-user encrypted provider credential vault and provider adapters beyond the initial four.
6. Real BOSS task planning, delegation, parallel/sequential execution, and result synthesis.
7. AI Review rounds (maximum five) with real test/tool execution and evidence.
8. AI Builder terminal/editor/preview/export/deploy pipeline.
9. Research/web retrieval with citations and source verification.
10. Image/video/audio generation integrations and usage controls.
11. Projects, library, export, scheduled jobs, notifications, plugins, and integrations.
12. Android device-control module (ADB/Shizuku/USB debugging) with safety confirmations.
13. 32-bit performance profile and accessibility/reduced-motion controls.
14. Production monitoring, rate limits, audit logs, backups, privacy/data deletion, and threat model.
15. Verified CI build artifacts, release signing, and physical-device QA.

## Release safety

The current release workflow builds APKs but does not configure a production signing keystore. Until signing secrets and a verified release process are configured, treat generated APKs as test artifacts. Never publish API keys, signing keys, or service-role credentials in the repository.
