# Quorix — Claude Code Project Instructions

You are working on Quorix. Before changing code, read:
1. `docs/01_PRD.md`
2. `docs/02_AppFlow.md`
3. `docs/03_UI_Spec.md`
4. `docs/04_TRD.md`
5. `docs/05_Schema.md`
6. `docs/06_Plan.md)

## Required workflow
- Inspect the existing repository and relevant files before proposing or editing implementation.
- Treat the six docs as the product/architecture baseline. If source code conflicts with a document, explain the discrepancy and propose a safe resolution; do not silently rewrite architecture.
- Start each task with a concise plan and identify affected files.
- Make small, dependency-ordered changes.
- Preserve existing working features and conventions.
- Do not invent framework versions, API behavior, database columns, provider limits or credentials.
- For providers, verify current official documentation and distinguish free tiers from trial credits and paid access.
- Never place secrets in source, client bundles, logs, commits or documentation.
- Do not claim a feature, test, build, deployment or device action succeeded unless it was actually executed and verified.
- Do not merge or push directly to the default branch unless the user explicitly asks.
- Prefer a feature branch and a reviewable pull request.
- Add/update tests for changed behavior.
- Update `docs/06_Plan.md` when checklist status or acceptance criteria change.
- Update relevant docs when an approved product/architecture decision changes.

## Product constraints
- Android-first; user primarily develops from an Android phone.
- Use the exact user-approved Quorix logo asset; never approximate or replace it.
- Dark Liquid Glass visual direction: real backdrop blur where supported, translucent layers, reflective edges, depth and accessible contrast. Gradient-only cards are insufficient.
- Chat Mode and Thinking selectors belong inside the composer, not the header.
- Wallpaper selection and controls belong in Settings > Appearance.
- Provider catalog is extensible; internal categories include core-model, inference, aggregator and media.
- BOSS orchestration must show real task/agent activity; never simulate execution.
- AI Review maximum is five rounds, with BOSS deciding when to stop.
- Android Device Control is native-only and permission-gated. Destructive actions require explicit confirmation and compatibility checks.
- Use free-first infrastructure, but never promise unlimited free API usage.

## Security
- Enforce authentication and ownership on protected operations.
- Keep provider keys server-side and use a secure secret-storage design.
- Respect provider terms and supported OAuth/third-party authorization paths.
- Validate uploads, commands and external integration scopes.
- Do not expose service-role keys to Android or web clients.

## Completion report
At the end of each task, report:
- What changed (file paths).
- What tests/builds actually ran and their results.
- What remains incomplete or unverified.
- Whether `docs/06_Plan.md` was updated.

Do not mark tasks complete based only on generated code or intended behavior.
