# ExecutiveLink (Android)

**ExecutiveLink** is a native Android communication and coordination platform engineered for a CEO / Managing Director and their Prime Assistant. Built with modern Kotlin and Jetpack Compose, it establishes an accountable, closed-loop workspace across messaging, formal approvals, shared scheduling, daily executive briefings, and immutable audit logs.

---

## Key Features

1. **Role-Based Workspace (CEO & Prime Assistant)**
   - Seamless perspective toggle between **CEO / Managing Director (Sarah Jenkins)** and **Prime Assistant (Marcus Vance)** directly from the top bar.
   - Tailored views, actions, and authorizations based on the active role.

2. **Actionable Inbox & Directive Messaging**
   - Two-way communication with priority tiers: **Urgent SLA**, **Important**, and **Routine**.
   - Distinct lifecycle states: `Sent`, `Delivered`, `Read`, `Acknowledged`, and `Actioned`.
   - Live SLA response countdown timers (e.g. 15m, 30m, 1h, 2h) with overdue escalation badges.
   - 1-tap **Acknowledge** action for the Executive with instant status feedback.

3. **Executive Approvals Board**
   - High-stakes decision cards across budgets/expenditures, contracts, and schedule exceptions.
   - Comprehensive context: Financial impact, justification notes, requestor, and deadline.
   - Decisive CEO actions: **Approve**, **Reject**, or **Request Clarification** with directive notes.

4. **Shared Executive Calendar & Rescheduling**
   - Two-way synchronized agenda of today's executive engagements, board meetings, and telepresence sessions.
   - Rapid appointment rescheduling with mandatory business rationale and audit alerts.
   - Executive 1-tap confirmation workflow.

5. **Daily Briefing & End-of-Day Handover**
   - Morning KPI scorecard: Scheduled engagements, pending approvals, and open urgent directives.
   - Strategic priorities checklist and formal Executive Sign-Off mechanism.

6. **Immutable Cryptographic Audit Trail**
   - Complete event ledger tracking all dispatches, acknowledgements, approval decisions, calendar shifts, and briefing reviews.
   - SHA-256 cryptographic hash signatures and precise timestamps for regulatory compliance.

---

## Technical Stack & Architecture

- **Platform**: Android SDK 36 (minSdk 26, targetSdk 36)
- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose with Material 3 Design System
- **Build System**: Gradle 9.3.1 with Android Gradle Plugin 9.1.1
- **Persistence**: Room Database with KSP (Kotlin Symbol Processing) and TypeConverters
- **Architecture**: Clean MVVM (Model-View-ViewModel) with Kotlin Coroutines and reactive StateFlow
- **Assets**: Custom adaptive launcher icon and executive briefing hero visual assets
