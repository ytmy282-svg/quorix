# Quorix Master Specification

## Product
Quorix is one unified AI workspace delivered across Web, Android, and Desktop. Personal use comes first; multi-user/public support is planned later. Infrastructure should be free-first, with paid services added only when usage or reliability requires them.

## Product principles
- One application and one shared project; do not split features into separate products.
- One expandable provider catalog containing 20 core providers and media providers in the same list.
- Keep provider metadata internally (model provider, inference provider, aggregator, media) for filtering and capability handling.
- Do not claim a provider, model, subscription entitlement, or feature is connected until it has been configured and tested.
- Never expose API keys in frontend code or public repositories.

## Main workspaces
1. Home dashboard.
2. Normal Chat: model selection, chat history, file/image upload, voice, web search, edit/regenerate, share, pin, archive, projects.
3. BOSS Network: select a BOSS agent; delegate to agents in parallel or sequence; supervise and review results. BOSS can also act as a worker. Review up to five rounds, with BOSS making the final decision and reporting unresolved issues.
4. AI Builder: websites, e-commerce, Android apps, Telegram/Discord bots, Python/Node services, dashboards, APIs, code editor, terminal, preview, tests, GitHub and deployment workflows.
5. Android Device Control: USB OTG, Wireless Debugging, ADB, Fastboot where supported, Shizuku, optional root, device info, APK/package tools, logs, file operations and diagnostics. Native Android permissions and device-specific limitations apply. Destructive actions require explicit confirmation and device/firmware checks.
6. Research: live search, URL/PDF analysis, multi-model research, citations, fact checking and reports.
7. Media Studio: image, video, audio, voice and transcription capabilities through supported media providers.
8. Projects, Library, Artifacts, Templates and Export Center.
9. Automation: scheduled tasks, background jobs, webhooks, notifications and logs.
10. Plugins and integrations: GitHub, Telegram, Google Drive, Supabase, Cloudflare, Vercel and custom APIs, subject to user authorization.
11. API Manager: API keys, official OAuth where explicitly supported, model discovery, connection tests, usage, cost and error tracking.
12. AI Monitor, Review Center, Settings, account and security controls.

## Provider connection policy
Support API keys and official OAuth/approved authorization side by side where providers permit them. A consumer subscription is not automatically API access. Use subscription-included access only where third-party use is expressly authorized. Otherwise use the user's API key or a genuinely available free tier. Encrypt credentials server-side; never ask users to share passwords, cookies or tokens in chat.

## Provider catalog
The 20 core-provider target is extensible. Candidate entries include OpenAI, Anthropic, Google, DeepSeek, xAI, Perplexity, Mistral, Alibaba/Qwen, Meta/Llama, Microsoft/Azure AI, Cohere, Amazon Bedrock, Groq, Cerebras, Together AI, Fireworks AI, NVIDIA NIM, Hugging Face Inference, OpenRouter and Moonshot/Kimi. Media providers are included in the same user-facing list; examples include ElevenLabs, Stability AI, Black Forest Labs, Runway, fal.ai and Deepgram. Final availability depends on supported APIs, account eligibility, region, pricing and licensing. Aggregators must not create duplicate model entries.

## Platforms and architecture direction
- Web: Next.js + React.
- Android: Kotlin + Jetpack Compose; native USB/ADB/Shizuku integration.
- Desktop: Tauri or Electron, decision pending.
- Backend: TypeScript/Node.js services, provider adapters, orchestration and task queue.
- Database/Auth: PostgreSQL and Supabase are the initial candidate.
- Object storage: Supabase Storage or Cloudflare R2.
- Deployment: Vercel for the Next.js web MVP; Cloudflare for selected edge/storage services where suitable.
- Shared account, project and history data across platforms.

## Security and quality
Authentication, role-based access for later multi-user rollout, encrypted secrets, audit logs, session controls, data export/deletion, backups, rate limits and usage caps. Test integrations against real provider APIs. Run unit, integration, build, security and Android device tests where applicable.

## Current implementation status
The initial web dashboard is being developed on feature/quorix-foundation. The UI is a prototype; AI chat, provider connections, database, authentication, Android device operations, media generation and automation are not yet connected. Do not merge into main until build and review checks pass.

## Rollout plan
1. Web foundation and responsive dashboard.
2. Authentication, database, projects and file storage.
3. API Manager and first two real provider adapters.
4. Normal Chat with real streaming responses.
5. BOSS orchestration and review pipeline.
6. Expand provider catalog and media capabilities.
7. AI Builder, Research and Automation.
8. Native Android app and device-control module.
9. Desktop client.
10. Security, testing, deployment, backups and public multi-user readiness.
