# Quorix — Technical Requirements and Architecture

**Status:** Architecture baseline; confirm exact versions from repository manifests  
**Primary client:** Android native application  
**Supporting surface:** Responsive web foundation  
**Backend/data:** Existing foundation indicates Supabase and server-side API adapters; verify current implementation before changes

## 1. Architecture goals

- Keep provider secrets out of Android/web clients and Git.
- Keep provider integrations behind a normalized adapter interface.
- Support a growing provider catalog without redesign.
- Separate user-facing client, backend orchestration and provider-specific APIs.
- Support streaming chat and observable background tasks.
- Enforce per-user authorization at every data boundary.
- Allow Android-native capabilities without pretending they are available from web.

## 2. Existing foundation facts to preserve

The open draft PR #2 describes:
- Kotlin and Jetpack Compose Android foundation.
- Responsive web chat foundation.
- Supabase sign-in/sign-up and migrations.
- Server-side streaming adapters for OpenAI, OpenRouter, Groq and DeepSeek using environment keys.
- Expandable provider catalog and discovery endpoint.
- GitHub Actions for Android APK build variants and release workflow.

These are PR-described capabilities, not a substitute for inspecting the current source and running tests. Do not duplicate or replace existing implementations without review.

## 3. Logical components

### Android client
Responsibilities:
- Authentication UI.
- Chat, projects and provider screens.
- Streaming response presentation.
- File/image selection through Android system APIs.
- Settings and permissions.
- Native device-control module only with explicit permissions.

### Web client
Responsibilities:
- Responsive workspace/chat surfaces already present in the repository.
- Provider/project administration where supported.
- No direct access to provider secrets.
- No native Android device-control claims.

### Backend/API
Responsibilities:
- Authentication/session validation.
- User-owned workspace and data access.
- Provider credential protection.
- Provider/model catalog and connection tests.
- Streaming chat proxy.
- BOSS orchestration and task state.
- Usage and audit events.
- Background jobs and integration webhooks.

### Database and storage
Use the existing Supabase project and migrations as the starting point. Inspect actual migrations before adding tables. Apply Row Level Security to user-owned records. Keep secrets out of ordinary client-readable tables.

## 4. Provider adapter contract

Define a normalized provider interface, conceptually:
- listModels / discoverModels (when supported)
- validateConnection
- generateResponse
- streamResponse
- getCapabilities
- normalizeError

Provider-specific adapters may differ. OpenAI-compatible endpoints can share an adapter only where request/response compatibility is verified.

Provider metadata should include:
- stable provider ID
- display name
- category: core-model, inference, aggregator, media
- API format
- base URL
- documentation URL
- model IDs
- capabilities
- context/output limits when verified
- region/availability notes
- pricing/free-tier classification
- rate-limit metadata
- last-verified timestamp
- source evidence

Never store an API key in the public provider catalog.

## 5. Free provider discovery source

Reference: https://github.com/mnfst/awesome-free-llm-apis

Treat this repository as a discovery/input source, not as an API runtime dependency or authoritative guarantee. Before adding a provider:
1. Read its official API documentation.
2. Verify current endpoint and model ID.
3. Verify access region and account requirements.
4. Verify free-tier status, limits and data policy.
5. Record evidence URL and verification date.
6. Mark uncertain/unverified values as unknown.
7. Add integration tests before marking connected/available.

Distinguish permanent free tier, limited free tier, trial credits, paid-only and unknown. Do not promise unlimited free access.

## 6. BOSS orchestration

BOSS should:
1. Receive user task and applicable project instructions.
2. Determine whether direct single-model response or delegated work is suitable.
3. Select eligible providers/models using capabilities, user settings, availability and cost constraints.
4. Create traceable agent tasks.
5. Execute sequentially or in parallel only when supported.
6. Collect outputs and evidence.
7. Synthesize a final response.
8. Record failures, cancellations, retries and actual execution status.

Do not fabricate agent activity. Use a single-model fallback if orchestration is unavailable. Require user-visible consent for external actions.

## 7. Security requirements

- Provider API keys remain server-side.
- Use platform secret management/environment secrets; never commit credentials.
- Encrypt sensitive stored credentials with managed keys and a documented rotation strategy.
- Never log secrets, authorization headers or raw credential values.
- Validate authentication and ownership on every protected request.
- Apply RLS to user-owned database records.
- Validate uploads, file type and size.
- Apply rate limits and abuse controls.
- Use HTTPS for network traffic.
- Keep audit events for security-sensitive operations.
- OAuth only where provider officially supports and permits the intended third-party use.
- Never use cookies/passwords from subscription accounts as an unofficial API substitute.

## 8. Android device-control security

- Device operations must execute in native Android context, not a remote web page.
- Request only necessary Android permissions.
- Explain what each permission enables.
- Use supported Android APIs, ADB/Wireless Debugging, Shizuku or root only when available and authorized.
- Destructive actions (unlock, flash, wipe, package removal) require explicit confirmation, target identification and compatibility checks.
- Log action outcome without collecting unnecessary personal data.

## 9. Reliability and observability

- Normalize provider errors: invalid key, quota/rate limit, timeout, unavailable, unsupported model, malformed response.
- Add request IDs and task IDs.
- Support cancellation when provider/API allows.
- Set bounded timeouts and retry only safe/idempotent operations.
- Do not blindly retry paid generation requests.
- Record latency, status and usage where provider returns it.
- Show partial completion distinctly from success.

## 10. Testing and CI

- Unit tests for provider adapters and routing.
- Contract tests using mocked provider responses.
- Integration tests only with protected test credentials.
- Authorization/RLS tests for user data.
- Streaming and cancellation tests.
- Android build and UI smoke tests.
- Web lint/type/build/test checks based on repository tooling.
- Secret scanning and dependency review.
- Document manual test steps and test environment.

## 11. Deployment and cost

Use free-first infrastructure where it meets reliability and security requirements. Verify current free-tier limits before selecting hosting. Separate development and production credentials. Do not place production secrets in mobile build configuration. Document expected usage costs and provider quota limitations.

## 12. Decisions that must be verified, not guessed

- Exact web framework and package versions.
- Exact backend runtime and deployment target.
- Existing database tables, columns and RLS policies.
- Secret encryption/key-management implementation.
- Storage provider and upload limits.
- Job queue/scheduler implementation.
- Supported Android API levels and ABI variants.
- Exact design token values from the approved logo.
- Provider-specific API capabilities and rate limits.

Update this document when these decisions are confirmed from source code, official documentation or explicit product-owner approval.
