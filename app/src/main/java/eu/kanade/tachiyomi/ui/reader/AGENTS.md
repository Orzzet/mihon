# Gesture handling guidelines

This directory houses the reader overlay helpers and gesture plumbing.

- Prefer extracting shared gesture logic into small helper classes so that both paged and webtoon viewers can reuse the same functionality.
- Double-tap handlers should surface a single entry point (e.g., `onDoubleTap(context, image, firstTap, secondTap)`) to avoid duplicating request/overlay code.
- Keep network interactions cancellable. Use Kotlin coroutines with `viewScope.launch` and cancel the previous job when a new gesture arrives.
- Any UI surfaced from gestures must be dismissible and accessible: supply content descriptions and allow scrolling for long messages.
- When writing tests for gesture helpers, cover both success and failure branches (timeouts, HTTP errors) with coroutine `runTest`.

When modifying files in this directory, make sure unit tests cover:
1. The payload sent to the network layer.
2. The overlay visibility lifecycle (shown, updated, dismissed).
