# Patch notes

## Changes

- Grouped the title/description search alternatives so the archive and status filters apply to every result, in the H2 query and Oracle reference.
- The task hook clears prior errors on each request, exits loading after both success and failure, and aborts/ignores superseded requests.
- Search and status changes return pagination to page 1.
- Added a repository regression test covering a wrong-status title match and an archived description match.

## Verification

- `cd frontend; npm run build` — passed.
- `cd backend; .\mvnw.cmd test` — passed (1 regression test, no failures). It verifies that title and description matches with the wrong status or archived flag are excluded, while valid matches from both fields are returned.
- Live API check of `/api/tasks?q=fix&status=DONE&page=1&pageSize=10` — passed; returned rows had the requested status and were not archived.
- Browser checks on the configured local app — passed: DONE filtering showed DONE-only rows; changing status from page 2 returned matching results from the first page; changing search from page 2 returned the matching first-page result; a slow request superseded by a newer search did not replace the newer result; a failed fetch displayed an error and ended loading, and the next request cleared the old error and entered loading.
- `git diff --check` — passed.
- Oracle PL/SQL is a reference artifact and was not executed.

## Tradeoffs and remaining risks

Aborting replaced fetches avoids unnecessary work; a request may still reach the server before cancellation, but its response cannot replace newer UI state. Search still sends a request per keystroke. There is no frontend test runner configured, so frontend behavior was checked interactively in the browser rather than with automated tests. The Oracle SQL is a reference artifact and is not executed by the local H2 app, so production-dialect behavior remains unverified.

No unrelated changes were made. Handwritten explanations are intentionally not included.
