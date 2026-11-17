# Quorix — Dependency-Ordered Implementation Plan

**Status:** Living checklist  
**Rule:** Update this file in the same change set when a task is completed, blocked by a verified dependency, or its acceptance criteria change. Do not mark work complete without evidence.

## Phase 0 — Repository and architecture baseline
- [ ] Inspect repository tree, current branches and open PRs.
- [ ] Inspect Android, web, backend, Supabase migrations and CI workflows.
- [ ] Record actual framework/library versions from manifests.
- [ ] Map existing implementation to PRD, AppFlow, UI Spec, TRD and Schema.
- [ ] Identify duplicate/overlapping work before coding.
- [ ] Confirm exact approved Quorix logo asset is available in repository.
- [ ] Confirm environment variable names without recording secret values.

**Exit criteria:** Architecture map and gap list are based on current source, not assumptions.

## Phase 1 — Documentation and agent instructions
- [x] Create PRD.
- [x] Create AppFlow.
- [x] Create UI Specification.
- [x] Create Technical Requirements.
- [x] Create logical Schema specification.
- [x] Create this implementation plan.
- [x] Add Claude Code project instructions.
- [x] Add Cursor project rules.
- [ ] Review docs against current source and resolve discrepancies.

**Exit criteria:** Coding agents have a clear, consistent source of truth.

## Phase 2 — Build and CI baseline
- [ ] Run existing web lint/type/build/test commands.
- [ ] Run Android debug build and relevant tests.
- [ ] Verify GitHub Actions on the documentation branch and foundation branch.
- [ ] Record build commands and environment requirements.
- [ ] Fix baseline failures only in separately reviewed code changes.

**Exit criteria:** Reproducible build/test instructions and known baseline status.

## Phase 3 — Secure account and workspace foundation
- [ ] Review Supabase auth configuration.
- [ ] Review/apply safe workspace and ownership policies.
- [ ] Verify RLS with owner and unauthorized-user tests.
- [ ] Verify session handling on Android and web.
- [ ] Add account/session settings.

**Exit criteria:** A user can sign in and access only their own workspace data.

## Phase 4 — Provider Hub
- [ ] Audit existing provider registry and adapters.
- [ ] Define normalized provider/model metadata contract.
- [ ] Add official documentation URL and last-verified date.
- [ ] Add free-tier classification: permanent, limited, trial, paid, unknown.
- [ ] Review awesome-free-llm-apis as a discovery source only.
- [ ] Verify each candidate against official provider documentation.
- [ ] Implement/verify provider connection tests.
- [ ] Protect credentials server-side.
- [ ] Add UI for provider connection, model selection and status.
- [ ] Add adapter contract tests.

**Exit criteria:** At least one configured provider can pass a real connection test and complete a chat request without exposing its key.

## Phase 5 — AI Chat
- [ ] Connect chat UI to authenticated backend endpoint.
- [ ] Implement streaming and cancellation.
- [ ] Persist messages and conversation state.
- [ ] Add retry/regenerate/copy/edit.
- [ ] Add history, search, pin and archive.
- [ ] Add clear loading/error/rate-limit states.
- [ ] Test on Android phone-sized viewport.

**Exit criteria:** A user can complete and reopen a persisted chat.

## Phase 6 — BOSS Network
- [ ] Define task/agent contracts.
- [ ] Implement task planner and routing policy.
- [ ] Add provider/model eligibility checks.
- [ ] Implement single-agent fallback.
- [ ] Implement sequential delegation.
- [ ] Implement parallel execution with bounded concurrency.
- [ ] Add result synthesis and traceable task state.
- [ ] Add cancellation, timeout and safe retry.
- [ ] Add activity UI and tests.

**Exit criteria:** A BOSS task has a traceable plan, real agent runs and an evidence-based final result.

## Phase 7 — AI Review
- [ ] Define review artifact and finding schema.
- [ ] Implement severity/evidence/fix output.
- [ ] Enforce maximum five rounds.
- [ ] Let BOSS stop when further review is not useful.
- [ ] Add review report UI and tests.

## Phase 8 — Projects, Library and files
- [ ] Implement project CRUD and project instructions.
- [ ] Link conversations to projects.
- [ ] Implement secure file storage and metadata.
- [ ] Add upload validation and processing status.
- [ ] Add preview/download/delete controls.
- [ ] Add PDF/document extraction after validating supported formats.

## Phase 9 — Research Center
- [ ] Define search/retrieval interface.
- [ ] Implement source collection and citation tracking.
- [ ] Add URL/PDF analysis where supported.
- [ ] Add research progress and report export.
- [ ] Test source traceability and partial failures.

## Phase 10 — AI Builder
- [ ] Define supported project templates.
- [ ] Implement project file workspace.
- [ ] Add editor and safe execution environment.
- [ ] Add build/test logs and preview.
- [ ] Add ZIP export.
- [ ] Add GitHub integration with scoped authorization.
- [ ] Add deployment integration only after secret and permission design.

## Phase 11 — Media Studio
- [ ] Define image/audio/video capability contracts.
- [ ] Integrate one provider per supported capability.
- [ ] Add progress, output storage and export.
- [ ] Show cost/limits where known.
- [ ] Add integration tests.

## Phase 12 — Android Device Control
- [ ] Define native-only module boundary.
- [ ] Add device information view.
- [ ] Add permission/connection status.
- [ ] Evaluate ADB, Wireless Debugging and Shizuku support.
- [ ] Add command allowlist and confirmation UX.
- [ ] Add optional root-specific features only after safety review.
- [ ] Test supported Android versions/devices.
- [ ] Require explicit confirmation and compatibility checks for destructive actions.

## Phase 13 — Automations and integrations
- [ ] Define background job lifecycle.
- [ ] Add scheduler and job status.
- [ ] Add user-consented notifications.
- [ ] Add webhooks and external integrations with scoped permissions.
- [ ] Add audit logs and retry policy.

## Phase 14 — Visual polish and release
- [ ] Integrate exact approved logo and launcher asset.
- [ ] Implement and visually verify Liquid Glass surfaces (real blur where supported).
- [ ] Verify wallpaper controls under Settings > Appearance.
- [ ] Verify composer selectors are inside composer.
- [ ] Verify navigation, keyboard insets, accessibility and reduced motion.
- [ ] Run security review and secret scan.
- [ ] Run all CI checks and Android device smoke tests.
- [ ] Document signing and release procedure.
- [ ] Create release only after checks and manual acceptance pass.

## Change log
- 2026-11-17: Initial specification baseline and dependency-ordered plan created.
