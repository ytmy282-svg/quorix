# Quorix — Product Requirements Document

**Status:** Product specification baseline  
**Product:** Quorix  
**Launch strategy:** Personal-use first; multi-user/public support later  
**Primary platform:** Android  
**Companion surfaces:** Responsive web/backend; desktop is a later target

## 1. Product summary

Quorix is an Android-first AI workspace that brings multi-provider AI chat, BOSS agent orchestration, research, coding/build tools, media capabilities, projects, files, automations and optional Android device utilities into one product.

Quorix is intended to let one user connect providers they are authorized to use, select models, and delegate tasks to a BOSS coordinator that can route work to specialist agents and synthesize the results.

## 2. Problem and product intent

Users currently need separate applications and provider dashboards for chat, model selection, coding, research, media generation and task automation. Quorix aims to provide one consistent workspace and a provider-independent architecture.

The product must be transparent about provider limits, permissions, costs and task status. It must never imply that a provider is free, unlimited, or supports a capability unless that information has been verified.

## 3. Primary persona

### Personal power user / builder
- Uses an Android phone as the primary development and access device.
- Wants to connect multiple AI providers with their own authorized API credentials.
- Wants one chat workspace with direct model selection and BOSS routing.
- Wants to build and manage software projects, research tasks and media outputs.
- Needs clear controls for privacy, API keys, device permissions and cost.

A future public release may add teams, multiple accounts, roles and subscription features. Those are not launch requirements.

## 4. Product principles

1. Android-first and mobile-usable.
2. Provider-agnostic, adapter-based architecture.
3. Secure server-side handling of provider secrets.
4. Free-first infrastructure without promising free provider usage.
5. Honest status: show queued, running, completed, failed and unverified states.
6. User control over model selection, routing, permissions and destructive device actions.
7. Build in small, testable phases; do not represent planned features as implemented.

## 5. MVP scope

### P0 — first usable release
- Authentication and personal workspace.
- Responsive web foundation for account/project administration where useful.
- Android app shell with approved Quorix branding and navigation.
- AI chat with streaming, stop, retry/regenerate, copy and chat history.
- Provider Hub with secure server-side credentials and connection testing.
- Provider/model registry with capability metadata.
- Initial adapters for the providers already represented in the foundation: OpenAI, OpenRouter, Groq and DeepSeek, subject to configuration and tests.
- Direct provider/model selection.
- Basic BOSS routing interface with a safe single-agent fallback; do not claim multi-agent orchestration until implemented and tested.
- Projects and project-linked chats.
- Basic usage/error status and privacy/security settings.
- Documentation, CI checks and reproducible test instructions.

### P1 — after P0 is stable
- BOSS orchestration with specialist agents, delegation and result synthesis.
- Parallel and sequential task execution.
- AI Review with a maximum of five rounds and BOSS-controlled stopping.
- File and image attachments; PDF/document extraction and analysis.
- Research Center with web sources and citations.
- Voice input/output where supported.
- Library and artifact management.
- Automation scheduler and background job status.
- Media Studio integrations for supported image/audio/video providers.

### P2 — later expansion
- Android-native Device Control using explicit Android permissions and approved mechanisms.
- ADB/Wireless Debugging, Shizuku and optional root integrations where technically and legally appropriate.
- Desktop application.
- Public multi-user workspaces, roles and admin tools.
- Plugin/connector marketplace and developer SDK.
- Local model support and advanced analytics.

## 6. User stories and acceptance criteria

### Chat
- As a user, I can start a conversation and choose an available provider/model.
- As a user, I can see streamed output and stop generation.
- As a user, I can retry or regenerate a response.
- As a user, I can find, rename, archive and delete my chats.
- Acceptance: messages persist to the authenticated user's workspace; errors are visible and retryable; secrets never appear in client logs.

### Provider Hub
- As a user, I can add a provider connection using an API key or an explicitly supported authorization flow.
- As a user, I can test a connection and see which models are available.
- Acceptance: credentials are stored server-side using an approved secret-protection design; test results distinguish invalid credentials, rate limits, provider outages and unsupported models.

### BOSS
- As a user, I can ask BOSS to choose a suitable model or agent.
- As a user, I can see the selected agent(s), task status and final synthesis.
- Acceptance: routing decisions and failures are observable; the system falls back safely; no unsupported provider capability is assumed.

### Projects
- As a user, I can group chats, instructions and files in a project.
- Acceptance: project data is isolated by owner and access policy.

### AI Review
- As a user, I can request a review of an artifact.
- Acceptance: each finding includes severity, evidence, explanation and a suggested fix; maximum five rounds; a review is marked complete only after actual execution.

## 7. Success measures

Initial measures:
- A user can sign in and create a workspace.
- A configured provider passes a connection test.
- A user can complete a streamed chat round-trip.
- Chat history can be reopened.
- No API key is exposed in the Android/web client or repository.
- Automated checks pass for the changed code.
- A BOSS task can be traced from request through routing to final response once orchestration is delivered.

Do not invent numeric targets before baseline usage is available.

## 8. Non-goals for the first release

- Unlimited free AI usage.
- Bypassing provider subscription rules or account restrictions.
- Scraping private sessions, cookies or credentials.
- Full device-control capability from a web page.
- Automatic bootloader unlocking, flashing or wiping without explicit confirmation and device checks.
- Public marketplace, billing and team administration in the personal MVP.
- Claiming every listed provider or model works without integration tests.

## 9. Constraints and assumptions

- Development is primarily from an Android phone.
- GitHub repository: `ytmy282-svg/quorix`.
- Existing foundation PR describes Kotlin/Jetpack Compose Android work, a responsive web foundation, Supabase authentication/migrations and server-side provider adapters. Verify current source files before extending those components.
- Existing foundation PR is a draft and must not be merged without reviewing its diff and passing checks.
- Exact framework/library versions, deployment topology and database schema must be taken from repository manifests/migrations, not guessed.
- Free tiers and model availability change; provider catalog entries require source URLs and last-verified dates.

## 10. Release definition

A release is ready only when its acceptance criteria are implemented, automated checks pass, the relevant manual test steps are recorded, security-sensitive settings are reviewed, and known limitations are documented. A feature listed in this document is a requirement, not evidence that it already exists.
