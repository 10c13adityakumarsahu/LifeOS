# LifeOS - Personal Life Operating System

LifeOS is a privacy-focused, offline-first Android application designed to help you manage your daily life. It integrates task management, finance tracking, and health monitoring into a single, cohesive interface.

## 🚀 How to Use the App

### 1. Dashboard
The **Dashboard** is your command center.
- **Do Now**: Shows tasks that are critical or due very soon.
- **Daily Summary**: A quick card showing your current water intake status.
- **Navigation**: Use the bottom bar to switch between Tasks, Finance, Fitness, and Settings.

### 2. Task Management
Keep track of what needs to get done.
- **Add Task**: Tap the `+` Floating Action Button (FAB) at the bottom right.
- **Task Details**: Enter a Title, Description, choose a Priority (Critical, High, Medium, Low), and set a Deadline (in hours).
- **Prioritization**: Tasks are automatically sorted based on priority and deadline in the Dashboard.

### 3. Finance Manager
Take control of your money.
- **Transactions**: Log expenses by tapping `+`.
- **People / Lending**: Track money you've **Lent** or **Borrowed**. Select "Lend" or "Borrow" transaction types to keep a record of who owes you. The "People" tab filters these specifically.
- **Goals**: Switch to the **Goals** tab to create saving targets (e.g., "New Laptop"). Tracker your progress by adding amounts to specific goals.

### 4. Fitness & Health
Stay healthy while you work.
- **Hydration**: Log water intake using quick-add buttons (+150ml, +250ml, +500ml).
- **Progress**: A visual progress bar shows how close you are to your daily goal (2500ml).
- **History**: View a list of today's water logs.

### 5. Settings & Reminders
Customize LifeOS to fit your schedule.
- **Water Reminders**: Enable notifications to remind you to drink water. Use the slider to adjust the interval (e.g., every 30 mins).
- **Sedentary Reminders**: Get nudged to move around if you've been sitting too long.

---

## 🔒 Privacy & Security

**LifeOS is designed with a strict "Offline-First" philosophy.**

*   **No Internet Required**: The app functions 100% offline.
*   **No Accounts / Logins**: You do not need to create an account, provide an email, or sign in with Google/Facebook.
*   **Data Ownership**: All your data (tasks, finances, health logs) is stored locally on your device in a secure SQLite database (via Android Room).
*   **No Tracking**: We do not collect analytics, user behavior, or personal data. Your business is your business.
*   **Data Persistence**: If you uninstall the app, your data is removed (unless you use Android's system-level backup). We recommend backing up manually if needed in future updates.

---

## 🛠️ Technical Details

This project is built using modern Android development standards.

### Technology Stack
*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose (Material 3 Design System)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Database**: Room (SQLite abstraction layer)
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Dependency Injection**: Manual DI (AppContainer pattern)
*   **Background Work**: WorkManager (for scheduled reminders)
*   **Widgets**: Android App Widgets support

### Project Structure
The codebase is organized by layer and feature:

```
com.example.lifeos
├── data                # Data Layer
│   ├── local           # Room Database, DAOs, Entities
│   ├── manager         # Notification Helper
│   ├── repository      # Repositories (SSOT)
│   ├── worker          # Background Workers
│   └── AppContainer.kt # Dependency Injection
├── ui                  # UI Layer (Compose)
│   ├── components      # Reusable UI (TaskCard, BottomNav)
│   ├── dashboard       # Dashboard Screen & VM
│   ├── finance         # Finance Screen & VM
│   ├── fitness         # Fitness Screen & VM
│   ├── navigation      # NavHost & Destinations
│   ├── settings        # Settings Screen & VM
│   ├── tasks           # Tasks Screen & VM
│   └── theme           # App Theme & Colors
└── widget              # Home Screen Widgets
```

### Build & Run
1.  Open the project in **Android Studio**.
2.  Ensure you have **JDK 17** or higher configured.
3.  Sync Gradle files.
4.  Run on an Emulator or Physical Device (API Level 26+ recommended).
