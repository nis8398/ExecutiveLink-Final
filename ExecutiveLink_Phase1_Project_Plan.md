ExecutiveLink
Phase 1 Project Plan — CEO / Managing Director and Prime Assistant
Prepared: 5 October 2026 | Status: Proposed build-ready plan
PHASE 1: One CEO/MD + one Prime Assistant
PHASE 2: Scalable organizational expansion

## 1. Executive summary
ExecutiveLink is a mobile-first communication and coordination app designed to reduce delays and missed updates between a CEO/Managing Director (CEO/MD) and their designated Prime Assistant. Phase 1 delivers a focused MVP for one executive and one Prime Assistant. The data model and authorization approach should support later expansion to additional assistants, department heads, staff reporting lines and multiple executives.
Phase 1 focuses on two-way communication, shared appointment coordination, actionable notifications, acknowledgement tracking, basic approvals, follow-ups and a daily briefing. Phase 2 features remain out of the initial interface until the first release is stable.
## 2. Goals and success measures
Reduce missed or delayed messages and meeting changes.
Show sent, provider-accepted where measurable, read, acknowledged and actioned states separately.
Synchronize the CEO agenda and Prime Assistant view.
Preserve messages, meetings and decisions across server restarts and device changes.
Protect executive information through authentication, least-privilege access and audit records.
Validate with one CEO/MD and one Prime Assistant before expansion.

## 3. Phase 1 scope
### Included in MVP
Secure sign-in, account recovery and invitation-based pairing for one CEO and one Prime Assistant.
Role-based workspace authorization; one executive and one designated Prime Assistant configured for the first release.
Two-way message inbox with urgent/important/routine categories, timestamps, read and acknowledgement states, and history.
Quick replies and actions such as acknowledge, approve, reject, request clarification or defer where applicable.
Shared calendar: create, update, reschedule and cancel appointments with time, timezone, location/link, purpose and notes.
Meeting confirmations, change notifications and configurable reminders (e.g. 24 hours and 15 minutes before).
In-app and push notifications where supported; deadline-based follow-up for unacknowledged urgent items.
Basic approval requests with decision, comment, actor and timestamp.
Daily briefing and end-of-day handover covering meetings, urgent messages, pending decisions and overdue follow-ups.
Basic tasks, audit events, backups, error logging and production monitoring.
### Excluded from Phase 1
Multiple supporting assistants or multiple executives in the active UI.
Department-head/staff reporting workflows and cross-department routing.
AI summaries, voice notes or automatic decision-making.
Full Google Calendar / Outlook integration unless essential and separately estimated.
WhatsApp/SMS automation unless a supported, authorized integration is separately scoped.
Complex enterprise approval chains, billing and public self-registration.
## 4. Users and permissions

Coordination priority is not the same as approval authority. The Prime Assistant does not automatically gain unrestricted access to confidential material or the power to approve every decision. Define permissions by action and data scope.
## 5. Core user workflows
### A. Send and acknowledge a message
Assistant composes a message, selects category and optional response deadline.
Backend checks authorization and persists the message before attempting notification.
Notification worker queues push/in-app notification and records processing status.
CEO opens the message; read state is recorded. CEO explicitly acknowledges or responds when action is required.
Assistant sees the status and response. Unacknowledged urgent items trigger configured follow-up after the deadline.
### B. Schedule or change a meeting
Assistant creates or edits an event with timezone, purpose, location/link and notes.
Backend persists the event and a notification outbox record.
CEO confirms, declines or requests a change.
Both users see the current event state; changes and cancellations remain auditable.
Old reminders are cancelled or recalculated after a time change.
### C. Request a decision
Assistant submits a request with context and due date.
CEO approves, rejects, defers or requests clarification.
Backend checks permission and records actor, timestamp and comment.
Assistant is notified and linked follow-up status is updated.
### D. Daily briefing
System gathers authorized upcoming meetings, urgent messages, pending approvals and overdue actions.
Prime Assistant reviews and edits the briefing.
CEO marks items for follow-up; unresolved items carry forward.
## 6. Recommended technical architecture
Reuse the existing Daymark React/TypeScript/Vite codebase where useful, after a repository audit confirms current dependencies, tests, build status and security gaps.

Avoid in-memory-only storage and full-state overwrite synchronization. Use durable records, scoped updates, concurrency/version checks and idempotent processing. Push delivery cannot guarantee a person has seen a notification.
## 7. Initial data model

Use UUID primary keys, foreign keys, timestamps and indexes for workspace membership, recipient status, event dates and due dates. Define retention, deletion and backup policies before production.
## 8. Initial API surface

Endpoint names are proposed, not claims about existing endpoints. Derive actor identity from the authenticated session; verify membership and resource-level permissions server-side; validate input, rate-limit sensitive actions and return safe errors.
## 9. Security, privacy and reliability
Authenticate every non-public endpoint; never trust client-supplied user_id, role or organization_id without server-side validation.
Enforce least privilege and workspace isolation; test cross-workspace access explicitly.
Use TLS, managed secrets, secure sessions, rate limits and protection against invitation replay/brute force.
Use expiring single-use invitations; allow session revocation and device removal.
Audit role changes, invitation acceptance, approval decisions and meeting changes; avoid unnecessary logging of message bodies/secrets.
Use idempotency and a transactional outbox or equivalent so committed updates do not silently lose notification jobs.
Retry failed notifications with backoff; expose errors for review and avoid duplicate alerts.
Encrypt backups where supported and test restore procedures.
Define retention, export/deletion, account deactivation and incident response.
Separate provider acceptance, device display, read status and human acknowledgement.
## 10. Delivery plan and milestones

Planning estimate: 8–10 weeks for a production-minded initial release with an experienced developer and design/testing support. Confirm after repository audit; integrations, native app packaging, enterprise reviews or limited team capacity may extend the schedule.
## 11. Testing and acceptance checklist
☐ CEO and Prime Assistant can sign in, sign out and recover access.
☐ Invitation is time-limited, single-use and validated server-side.
☐ Messages persist and sync; read and acknowledgement states are distinct.
☐ Urgent messages follow configured reminders without unlimited duplicates.
☐ Meeting creation, edits, rescheduling and cancellation update both views and leave an audit trail.
☐ Meeting time changes cancel or recalculate old reminders.
☐ Approval decisions require the authorized actor and preserve comments/timestamps.
☐ Server restart does not erase messages, meetings or decisions.
☐ Offline/reconnect does not silently overwrite newer server data or duplicate actions.
☐ Cross-workspace access tests fail safely; secrets and message content do not leak into logs.
☐ Push is tested on actual target devices, including denied permissions and unavailable devices.
☐ Backup restoration, monitoring alerts and rollback are rehearsed.
## 12. Phase 2 readiness by design
Include organization_id and ownership/relationship fields in business records from the start.
Make roles and permissions data-driven; do not hardcode one assistant into authorization logic.
Use executive_assistant_assignments with assignment_type PRIME or SUPPORT; enforce one active PRIME assignment per executive.
Support resource-level permissions and explicit direct-reporting exceptions later.
Keep notification preferences, routing and escalation rules configurable.
Maintain schema migrations and API versioning.
Do not build Phase 2 screens or complex workflows until the pilot proves Phase 1 is stable.
Phase 2 candidates: supporting assistants, department heads and staff reporting; multiple executives; configurable approval chains; Google Calendar/Outlook integration; document management; optional AI-assisted summaries with privacy controls.
## 13. Risks and mitigations

## 14. Governance and next actions
Audit the Daymark repository: reusable UI, sync endpoints, dependency health, tests and security gaps.
Confirm target devices, identity provider, notification requirements and whether calendar integration is mandatory for launch.
Approve Phase 1 requirements and screen wireframes.
Create migrations, environment configuration and automated authorization tests before feature expansion.
Implement message and meeting workflows end-to-end from UI through database to notifications.
Pilot with one CEO/MD and one Prime Assistant; measure notification processing, acknowledgement and meeting-change reliability.
Release only after acceptance checks, backup/restore and support readiness.
The uploaded Daymark project is a starting codebase, not yet a verified production-ready foundation. Audit it before estimating refactoring effort or declaring features complete.
## Appendix A. Suggested initial backlog

## Tables

| Measure | Proposed acceptance target |
| --- | --- |
| Online synchronization | 95th percentile under 3 seconds in agreed test conditions. |
| Notification processing | 99% of eligible jobs accepted by the push provider within 60 seconds under normal conditions; this is not proof of human receipt. |
| Meeting changes | Every authorized change is persisted and creates a traceable notification event. |
| Persistence | Records survive application/server restart. |
| Access control | Automated tests confirm users cannot access another workspace. |
| Pilot usability | Both users complete core workflows without developer assistance. |

| Role | Phase 1 permissions |
| --- | --- |
| CEO / Managing Director | Access own workspace; message; view agenda; confirm/decline/request meeting changes; respond to approvals; manage own notification preferences. |
| Prime Assistant | Manage CEO calendar; send messages and briefings; create follow-ups and approval requests; track acknowledgements; coordinate assigned items. |
| Platform administrator | Operate service with tightly controlled, audited privileged access; no routine access to message content. |
| Future Phase 2 roles | Supporting Assistant, Department Head, Staff Member and Organization Administrator; reserved for later implementation. |

| Layer | Recommendation |
| --- | --- |
| Client | React + TypeScript + Vite responsive Progressive Web App (PWA). Validate iOS/Android browser push and installation early. |
| Backend | Node.js API using the existing server framework where appropriate; authenticated endpoints, server-side authorization and input validation. |
| Authentication | Managed identity provider such as Supabase Auth or equivalent; invitation-based pairing and account recovery. |
| Database | PostgreSQL (managed Supabase is an option); organization/workspace scoping and RLS if using Supabase. |
| Live updates | Supabase Realtime or WebSockets; database remains the source of truth. |
| Notifications | Durable outbox/queue with retries; Web Push initially, platform-specific push if native apps are introduced. |
| Files | Private object storage for optional attachments with access checks and expiring links. |
| Operations | Separate dev/staging/production environments, managed secrets, backups, monitoring, error tracking and recovery documentation. |

| Entity | Purpose / key fields |
| --- | --- |
| organizations | id, name, status, created_at; future workspace boundary. |
| users / profiles | auth user id, display_name, status, created_at. |
| organization_memberships | organization_id, user_id, role, status, joined_at. |
| executive_assistant_assignments | organization_id, executive_user_id, assistant_user_id, assignment_type, active dates; supports PRIME and future SUPPORT. |
| conversations / members | Workspace-scoped conversations and authorized participants. |
| messages | sender, conversation, body, category, timestamps, linked entity, response deadline. |
| message_receipts | message_id, user_id, read_at, acknowledged_at, response_state; delivery time only if measurable. |
| calendar_events | owner, title, description, start/end, timezone, location/link, status, version, audit timestamps. |
| event_participants | event_id, user_id, response status and timestamp. |
| approval_requests | requester, approver, details, due date, status, decision comment/time. |
| tasks | owner, creator, due date, status and optional linked entity. |
| notification_jobs | recipient, event type, schedule, attempts, status, provider id and last error. |
| audit_events | actor, action, entity, timestamp and minimized metadata. |

| Method / endpoint (proposed) | Purpose |
| --- | --- |
| POST /api/invitations | Create short-lived invitation. |
| POST /api/invitations/accept | Accept invitation after authentication. |
| GET /api/me/workspace | Return authorized workspace and role. |
| GET /api/conversations | List visible conversations. |
| POST /api/conversations/:id/messages | Persist a message and enqueue notification. |
| POST /api/messages/:id/receipt | Record read or acknowledgement action. |
| GET /api/calendar/events | List authorized events by date range. |
| POST /api/calendar/events | Create event and notification record. |
| PATCH /api/calendar/events/:id | Update event with conflict checks and notify participants. |
| POST /api/calendar/events/:id/respond | Confirm, decline or request change. |
| POST /api/approvals | Create approval request. |
| POST /api/approvals/:id/decision | Record authorized decision. |
| GET /api/briefing/today | Return authorized daily briefing. |
| GET /api/notifications | List visible notification status. |

| Timing (indicative) | Workstream | Exit criteria |
| --- | --- | --- |
| Week 1 | Repository audit, requirements, user journeys and UX prototype | Scope approved; risks and reuse opportunities documented. |
| Weeks 2–3 | Authentication, workspace model, database, authorization and environments | Two users securely access only their workspace; persistence and migrations work. |
| Weeks 4–5 | Messages, receipts, calendar, event responses and approvals | End-to-end workflows pass integration tests. |
| Week 6 | Push, reminder jobs, acknowledgement follow-up and briefing | Retry, cancellation and failure paths tested on target devices. |
| Weeks 7–8 | Security, accessibility, offline/reconnect, device testing and pilot fixes | No open critical defects; acceptance checklist passed. |
| Weeks 9–10 contingency | Production hardening, restore drill, monitoring, onboarding and release | Release approved after pilot and readiness review. |

| Risk | Mitigation |
| --- | --- |
| Push not shown or not seen | Track provider status separately from read/acknowledgement; use pending queue and deadline-based follow-up. |
| Confidential data exposure | Authenticated APIs, least privilege, workspace isolation, secure invitations, audit logs and security testing. |
| Lost or duplicate updates | Persistent database, transactional outbox, idempotency and retries. |
| Conflicting calendar edits | Versioning, optimistic concurrency and conflict prompts. |
| Scope creep | Limit Phase 1 to one CEO and one Prime Assistant; change-control advanced integrations. |
| Mobile browser differences | Prototype push early; test supported OS/browser combinations; consider native client if required. |
| Phase 2 redesign | Use organization-scoped data, extensible assignments and permission policies from the beginning. |

| ID | Priority | Task | Acceptance summary |
| --- | --- | --- | --- |
| AUTH-01 | P0 | Sign-in and account recovery | Authenticated identity; no shared demo credentials. |
| AUTH-02 | P0 | Invitation and pair CEO with Prime Assistant | Expiring single-use invitation; workspace/role checked server-side. |
| DATA-01 | P0 | PostgreSQL schema and migrations | Workspace-scoped records; backup strategy documented. |
| SEC-01 | P0 | Authorization and workspace isolation | Unauthorized requests denied; negative tests included. |
| MSG-01 | P0 | Send/list messages | Persisted, paginated inbox with sender and timestamps. |
| MSG-02 | P0 | Read and acknowledgement receipts | States are distinct and synchronized. |
| NTF-01 | P0 | Notification outbox/worker | Retryable jobs, idempotency and error visibility. |
| NTF-02 | P0 | Push and in-app notifications | Permission-aware alerts link to relevant record. |
| CAL-01 | P0 | Create/update shared events | Timezone-aware records and conflict checks. |
| CAL-02 | P0 | Confirm, decline, reschedule, cancel | Traceable updates reach participants. |
| CAL-03 | P0 | Scheduled reminders | Old reminders recalculated after changes. |
| APR-01 | P1 | Approval request and decision history | Authorized decisions with comments/timestamps. |
| BRF-01 | P1 | Daily briefing | Shows authorized meetings, urgent messages and actions. |
| TASK-01 | P1 | Follow-up tasks and deadlines | Tasks link to messages/events and appear in overdue view. |
| OPS-01 | P0 | Monitoring and backup/restore | Alerts and recovery procedure documented. |
| QA-01 | P0 | Device, security and workflow tests | Pilot passes agreed acceptance checklist. |
