# Quorix — UI and Visual Design Specification

**Status:** Visual baseline  
**Brand:** Quorix  
**Primary platform:** Android  
**Visual direction:** Dark Liquid Glass, green-and-black identity

## 1. Design references

Use the three user-supplied reference screenshots as behavioral/visual guidance:
1. Account switcher: dark translucent rounded overlay, backdrop blur, visible underlying content, profile rows and soft reflective edges.
2. Music/library screen: dark canvas, artwork cards, floating frosted-glass bottom navigation capsule, selected-tab lens/highlight and floating mini-player/task panel.
3. Minimal login: near-black background, centered brand mark/wordmark and a restrained primary action.

Do not copy third-party branding or layouts literally. Use the patterns to create a distinct Quorix interface.

## 2. Brand rules

- Use the exact user-approved green-and-black Quorix logo asset from the project owner.
- Do not redraw, approximate, recolor or replace the logo without approval.
- Use the same approved mark in the app header, splash/login and launcher icon where technically appropriate.
- Wordmark: Quorix.
- Dark/near-black is the default appearance.
- Green is the primary accent; use sparingly for active states, primary actions and status.
- Do not use excessive neon glow or generic enterprise-dashboard styling.

The exact logo asset path must be confirmed in the repository before implementation. Do not invent a path.

## 3. Liquid Glass requirements

A surface qualifies as Liquid Glass only when it has meaningful layered treatment:
- Real backdrop blur where platform/browser support allows.
- Translucent fill that lets background content remain visible.
- Fine reflective edge/highlight.
- Layered shadow/depth and separation from background.
- Subtle light/refraction-like gradient treatment, not gradient-only cards.
- Smooth state transitions and restrained motion.

Provide graceful fallbacks on devices/browsers without backdrop blur. The fallback must remain legible and visually layered.

## 4. Color tokens

Initial design tokens (implementation may refine after visual review):
- Background: near-black, approximately #070A08.
- Surface: dark green-black, approximately #101713.
- Elevated surface: approximately #17211B.
- Primary accent: Quorix green, use the exact logo-derived value once sampled from the approved asset.
- Primary text: approximately #F2F5F2.
- Secondary text: approximately #A5B0A8.
- Divider/edge: low-opacity white/green.
- Error: accessible red.
- Warning: accessible amber.
- Success: accessible green.

Do not hard-code a guessed brand green as final; sample the approved logo and centralize tokens.

## 5. Layout and navigation

- Mobile-first layouts, including narrow Android screens.
- Floating glass bottom navigation capsule with selected-tab lens effect.
- Sidebar slides in from left with blur and subtle shadow.
- Sidebar includes profile, New Chat, history/search and module destinations.
- Main content remains scrollable and respects system bars and keyboard insets.
- Floating task status panel may sit above bottom navigation when background work is active.
- Avoid overcrowded bottom navigation; use sidebar for secondary modules.

## 6. Chat visual specification

Header:
- Sidebar action, exact Quorix mark/wordmark, profile/avatar.
- No Mode or Thinking selectors in header.

Composer:
- Large rounded glass capsule near bottom.
- Attachment (+), multiline text field, microphone/voice and send controls.
- Mode and Thinking dropdown controls inside composer.
- Keyboard-aware, expands for multiline input and remains reachable.
- Disabled, focused, sending, recording and generating states.

Messages:
- Clear user/assistant distinction.
- Comfortable line length and readable typography.
- Code blocks with horizontal scrolling/copy.
- Citation/source links visually distinct.
- Tool/agent activity compact and expandable.
- Loading, partial, error and retry states.

## 7. Typography and spacing

- Use platform-native readable typography and a consistent type scale.
- Body text must remain legible on a 5–6 inch phone.
- Prefer 4dp-based spacing increments.
- Use consistent corner radii; large radii for glass capsules, moderate radii for cards and controls.
- Touch targets should be comfortable and accessible.
- Avoid tiny labels and low-contrast secondary text.

Exact font family, dp scale and component dimensions must be verified against the current Android/web code and then centralized as tokens. Do not introduce arbitrary one-off values.

## 8. Motion and accessibility

- Use short, restrained transitions for sidebar, selection lens and surface state changes.
- Respect reduced-motion preferences.
- Preserve sufficient contrast over custom wallpapers.
- Support screen readers, content descriptions, keyboard focus and scalable text.
- Never communicate status through color alone.

## 9. Wallpaper settings

Wallpaper controls belong in Settings > Appearance, not in the chat composer:
- Choose from gallery.
- Preview and apply.
- Reset to default.
- Blur amount.
- Brightness/dim overlay.
- Overlay intensity.
- Ensure all text and controls remain readable.

## 10. Component states

Every reusable control must define default, pressed, focused, disabled, loading and error states where relevant. Glass effects must not obscure control boundaries or focus indicators.

## 11. Validation

Review screenshots on a real Android phone at narrow width and on web. Verify blur is real where claimed, wallpaper contrast, keyboard behavior, navigation reachability, touch targets, reduced motion and low-memory behavior. A visual implementation is not accepted solely because it uses transparency or gradients.
