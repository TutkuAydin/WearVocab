# ⌚ WearVocab: Wear OS Integrated Vocabulary Master

WearVocab is a modern language learning ecosystem built with **Jetpack Compose**.
It bridges the gap between a feature-rich mobile management tool and a glanceable,
action-oriented **Wear OS** experience.

---

## 🚀 Key Features

- 📱 **Mobile:** Advanced word management with **Free Dictionary API** integration,
  automated data fetching, and swipe-to-action list management.
- ⌚ **Wear OS:** Glanceable word cards, short learning sessions, and optimized
  wrist-first interactions.
---

## 🏗 Architecture

The project combines **MVI (Intent-based)** state management with **MVVM** to ensure
predictable state flow and scalable UI logic.

- **Room Persistence:** Local database as the single source of truth.
- **Retrofit & Coroutines:** Asynchronous networking for complex dictionary data.
- **Modular UI Components:** Reusable composables such as `WordCard` shared across screens.

### Phone–Watch Separation

- **Mobile app:** Owns data fetching, persistence, and synchronization.
- **Wear OS app:** Focuses purely on short, action-based learning interactions.

Device communication is handled using the **Wear OS Data Layer API**, with a clear
separation between persistent state and one-time user actions.

---

## ⌚ Wear OS UX Optimization

Designing for the wrist requires a different mindset. WearVocab applies:

- **ScalingLazyColumn:** Fluid scrolling optimized for circular screens.
- **Audio Feedback:** Solves small-screen constraints by shifting learning to sound.
- **Minimalist UI:** Uses `CompactButton` and `TitleCard` to prioritize content
  over navigation depth.

Most design decisions favor **ergonomics and speed** over visual complexity.

---

## 🛠 Tech Stack

- **UI:** Jetpack Compose (Mobile & Wear)
- **Database:** Room
- **Networking:** Retrofit
- **Async:** Kotlin Coroutines & Flow
- **Wear OS:** Data Layer API

---

## 🎓 Academic / Article Focus

This project serves as a reference for:

1. Integrating third-party REST APIs into a local persistence layer.
2. Designing state-driven UIs using MVI on Android.
3. Building efficient, battery-conscious experiences for **Wear OS**.

https://github.com/user-attachments/assets/a5aa1709-f8a2-454e-b4b1-421ca8aacaee

https://github.com/user-attachments/assets/96d3376a-87f2-41f2-8fab-5c40755051e9
