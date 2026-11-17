# Quorix — Application Flow and Screen State Specification

**Status:** UX/interaction baseline  
**Primary target:** Android phone  
**Design direction:** Dark Liquid Glass using the approved Quorix logo and supplied reference screenshots

## 1. Global navigation

Primary destinations:
1. Chat
2. BOSS
3. Projects
4. Builder
5. Research
6. Media
7. Providers
8. Library
9. Device Control (Android native capability; show only when available)
10. Automations
11. Settings

Use a floating translucent bottom navigation capsule on the main mobile surface. The selected tab receives a restrained lens/highlight treatment. A left slide-in glass sidebar provides profile, new chat, history/search and access to secondary modules. Avoid placing every destination in the bottom bar if it harms mobile usability.

## 2. App entry and authentication

### Signed out
- Show Quorix brand mark and wordmark.
- Actions: Sign in, Create account.
- Show loading, validation, network failure and authentication error states.
- Do not expose private workspace data before authentication.

### Signed in
- Load user profile, workspace, settings and provider connection metadata.
- If onboarding is incomplete, show a guided setup for workspace and first provider.
- If no provider is connected, show an empty state with a clear Add Provider action.

## 3. Chat screen

### Header
- Sidebar control.
- Exact approved Quorix logo and wordmark.
- Profile/avatar entry.
- Do not put Mode or Thinking selectors in the header.

### Conversation body
- Empty state with example prompts and New Chat action.
- User and assistant message states.
- Streaming indicator and Stop action.
- Tool/agent activity when applicable.
- Code blocks, markdown, citations and attachment previews.
- Error state with Retry; never silently discard a prompt.
- Long conversations must remain scrollable and usable on a phone.

### Composer
- Large rounded Liquid Glass capsule anchored above the keyboard.
- Attachment (+), multiline message input, microphone/voice action and Send.
- Mode selector and Thinking selector inside the composer.
- Preserve draft text when navigating away or recovering from an interruption.
- Disable Send for empty input; show sending/generating state.
- Support keyboard insets and small screens.

### Chat actions
- Edit prompt, copy, regenerate, stop, share.
- Rename, pin, archive, lock and delete conversation.
- Link conversation to a project.
- Confirm destructive delete/lock operations where appropriate.

### Chat modes
- Auto
- Normal Chat
- BOSS Mode
- Smart Routing
- Parallel
- Collaboration
- Sequential

### Thinking levels
- Low, Medium, High.
- Only offer levels supported by the selected provider/model; explain unavailable options.

## 4. BOSS workspace

- Task input and mode selection.
- Optional preferred providers/agents.
- BOSS plan preview where applicable.
- Agent/task activity list.
- States: draft, queued, planning, running, waiting, synthesizing, completed, failed, cancelled.
- Show each agent's assigned task and provider/model without exposing secrets.
- Show final synthesis and source outputs.
- Allow cancellation and retry when safe.
- If only single-model routing is implemented, label it accurately; do not display simulated agent execution as real work.

## 5. Provider Hub

Provider list is unified, with internal category metadata:
- Core model
- Inference
- Aggregator
- Media

Provider detail screen:
- Name, category, documentation URL and verification date.
- Connection method: API key or explicitly supported OAuth/authorization.
- Secret input masked by default.
- Save, test, edit, disconnect.
- Model list with capabilities and availability.
- Usage/rate-limit information only when supplied by provider or measured.
- States: not connected, testing, connected, invalid key, rate limited, unavailable, unsupported.

Do not display a provider as “free” without current evidence. Distinguish permanent free tier, limited free tier, trial credit and paid-only.

## 6. Projects and Library

### Projects
- Project list, create, rename, archive and delete.
- Project detail with instructions, linked chats, tasks and files.
- Project-scoped search.

### Library
- File list and folders.
- Upload, preview, download, rename and delete.
- Processing states for uploaded files.
- Clear unsupported-format and size-limit errors.
- Keep generated artifacts distinct from source uploads.

## 7. Builder

- Project creation wizard.
- Project type: website, Android app, bot, Python/Node app, API or dashboard.
- File tree, editor, terminal, preview, test output and build logs.
- Clear distinction between generated code and executed/tested code.
- Export ZIP and GitHub actions only after permission/confirmation.
- Mobile layout must prioritize file navigation and readable editor controls.

## 8. Research Center

- Research question input.
- Scope/depth options.
- Sources and citations panel.
- Progress and tool activity.
- Results report with source links.
- States: queued, searching, reading, synthesizing, completed, partial, failed.
- Clearly label inaccessible sources and uncertain findings.

## 9. Media Studio

- Choose media type: image, video, audio or voice.
- Provider/model selection based on capability.
- Prompt and supported input controls.
- Generation progress, cancel when supported, result preview and export.
- Show provider limits and estimated cost when known.
- Do not show unavailable generation controls as functional.

## 10. Android Device Control

Only available in the native Android application and only after permission checks.
- Device overview.
- Connection method/status (ADB, Wireless Debugging, USB OTG, Shizuku, optional root).
- Command/action confirmation.
- Output/log viewer.
- Permission denied, unsupported device, disconnected and command failed states.
- Destructive operations require a separate confirmation that identifies the exact action and target.

## 11. Automations and notifications

- Automation list, create, pause, resume and delete.
- Schedule/trigger summary, last run and next run.
- Job detail with logs and retry where safe.
- Notifications for completion, failure and provider issues.
- Require user consent for recurring jobs and external integrations.

## 12. Settings

Sections:
- Account and sessions
- Appearance
- Provider/privacy preferences
- Notifications
- Storage and data export/delete
- Security and permissions
- About/version

Appearance:
- Dark default.
- Wallpaper selection from gallery.
- Preview, apply, reset.
- Brightness/dim overlay, blur and overlay intensity.
- Maintain readable text contrast.

## 13. Global state and error rules

Every asynchronous screen must account for:
- Initial loading
- Empty
- Ready
- Submitting
- Running
- Partial success
- Failed
- Offline/network unavailable
- Permission denied
- Rate limited
- Cancelled

All actions need visible feedback. Never show a success state before the server/device operation confirms success. Preserve drafts and retry context when possible.
