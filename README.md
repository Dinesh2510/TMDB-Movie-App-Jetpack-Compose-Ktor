# 🎬 TMDB Compose Cinematics
#### Cinematic Android streaming client featuring a glassmorphic UI, powered by Jetpack Compose, Ktor 3.x, Koin, and Room.

[//]: # (### Next-gen Android streaming experience crafted with Jetpack Compose & Glassmorphism)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6+-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![MVVM Architecture](https://img.shields.io/badge/Architecture-MVVM-00C6FF?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/topic/architecture)
[![Ktor](https://img.shields.io/badge/Ktor-3.x-008080?style=for-the-badge&logo=ktor&logoColor=white)](https://ktor.io/)
[![Koin](https://img.shields.io/badge/Koin-DI-3B5998?style=for-the-badge&logo=koin&logoColor=white)](https://insert-koin.io/)
[![Room](https://img.shields.io/badge/Room-Database-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![DataStore](https://img.shields.io/badge/DataStore-Preferences-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture/datastore)
[![Coil 3](https://img.shields.io/badge/Coil-3.x-FF4081?style=for-the-badge&logo=android&logoColor=white)](https://coil-kt.github.io/coil/)
[![License](https://img.shields.io/badge/License-Apache%202.0-D22128?style=for-the-badge&logo=apache&logoColor=white)](LICENSE)

Welcome to the **TMDB Movie App v2.0**! A modern, high-performance, Netflix and Disney+ Hotstar–inspired cinematic Android application. Built using **Jetpack Compose**, **Ktor 3.x**, **Koin DI**, **Paging 3**, and **Room** local persistence, this app showcases advanced modern Android development patterns, glassmorphic UI components, dynamic video/trailer resolvers, and offline-first storage.

> 🆕 **This is the v2.0 successor to the original project:**
> **[TMDB Movie App (v1.0) — Jetpack Compose](https://github.com/Dinesh2510/TMDB-Movie-App-Jetpack-Compose)**
> That earlier repo used Retrofit + Hilt + a simpler UI. This v2.0 rewrite is a full architectural upgrade — same core idea, rebuilt from the ground up.

> 📢 **v2.0 Architectural Update:** This repository has been fully upgraded to leverage **Ktor 3.x Engine** (replacing Retrofit), **Koin DI** (replacing Hilt), **Coil 3**, and an updated glassmorphic UI system.

---

## 📸 Screenshots & UI Layouts

| Home Screen | Catalog / Top Rated | Video Selector Dialog |
| :---: | :---: | :---: |
| ![Home Screen](screenshots/1.png) | ![Top Rated](screenshots/2.png) | ![Video Dialog](screenshots/6.png) |

| Movie Details | Continue Watching | Wishlist |
| :---: | :---: | :---: |
| ![Movie Details](screenshots/3.png) | ![Search](screenshots/5.png) | ![Wishlist](screenshots/4.png) |

---

## ✨ Features & Highlights

### 🎬 Modern Cinematic UI & Glassmorphic Design
- **Glassmorphic Surface System** — translucent cards, dialogs, and segmented toggles built using dynamic gradients, custom border overlays, and haptic feedback.
- **Zomato-Inspired Floating Bottom Navigation**  — ultra-sleek, glassmorphic floating tab bar with active glow indicators, pill-shaped selections, and smooth micro-animations.
- **Hotstar/Prime Video-Style Carousels** — auto-scrolling hero banners featuring release badges, backdrop scrims, and quick-play actions.
- **Dual-Category Catalog** — dedicated "Top Rated" screen featuring a glassmorphic segmented tab bar to switch between TV Series and Movies seamlessly, with full pagination.

### ⚡ Networking & Dynamic Video Resolver
- **Ktor 3.x Engine** — asynchronous HTTP requests with strict status code validation (`expectSuccess = true`), fallback parsing for TMDB error payloads (e.g. status code 34 for 404s), and robust network recovery.
- **Trailers & Extras Selection** — real-time fetching of official YouTube trailers, teasers, resolution badges (1080p, 720p), and intent playback via a custom glassmorphic selection dialog.

### 💾 Offline-First Persistence & Pagination
- **Paging 3 Integration** — infinite scroll implementation for high-volume catalogs (Discover, Trending, Now Playing, Top Rated Movies & TV Shows).
- **Room Watchlist Database** — save favorite movies/shows with reactive Kotlin Flows and single-tap bookmark state toggles across feeds.
- **Continue Watching Tracker** — in-memory & Room DB persistence tracking exact playback progress fractions and remaining runtimes locally.

---

## 🛠️ Modern Tech Stack & Architecture

| Layer | Technology Used |
| :--- | :--- |
| **Language** | 100% [Kotlin](https://kotlinlang.org/) |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3, Glassmorphic Modifiers, Dynamic Gradients) |
| **Networking** | [Ktor Client 3.x](https://ktor.io/) (`CIO` Engine, Content Negotiation, Kotlinx Serialization) |
| **Dependency Injection** | [Koin DI](https://insert-koin.io/) |
| **Local Persistence** | [Room DB](https://developer.android.com/training/data-storage/room) & [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) |
| **Pagination** | [Android Jetpack Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) |
| **Image Loading** | [Coil 3](https://coil-kt.github.io/coil/) (asynchronous backdrop and poster rendering) |
| **Concurrency** | Kotlin Coroutines, `StateFlow`, and `SharedFlow` |

---

## 📁 Architecture Overview

The application follows **Clean Architecture** principles structured around **MVVM (Model-View-ViewModel)** and Unidirectional Data Flow (UDF):

```text
com.app.movieapp
 ├── data
 │    ├── local            # Room DB, DAOs, Watchlist & Progress Entities
 │    ├── remote           # Ktor ApiService, Paging Sources & Error Handling
 │    ├── repository       # Single source of truth repositories
 │    └── viewmodel        # MVVM Centralized ViewModels (Home, Details, TopRated)
 ├── graph                 # Compose Navigation Routes & Graph definitions
 ├── models                # DTOs, TMDB Responses & Serialized Models
 ├── screens               # Composables (Home, Details, Catalog, Dialogs, Profile)
 │    └── components       # Reusable Glassmorphic Cards, Buttons, and Loaders
 └── ui/theme              # Cinematic Color Palettes, Typography & Glass Modifiers
```

---

## 🔧 How to Run

**1. Clone the repository**
```bash
git clone https://github.com/Dinesh2510/TMDB-Movie-App-Jetpack-Compose-V2.git
```

**2. Open in Android Studio**
Open the project in Android Studio Jellyfish / Ladybug (2024.2+) or newer.

**3. Configure your TMDB API key**

Recommended (secure) way — add it to `local.properties` at the project root (this file is git-ignored and never committed):
```properties
TMDB_API_KEY=your_tmdb_api_key_here
```
Then read it into `BuildConfig` from `app/build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        val localProps = java.util.Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "TMDB_API_KEY", "\"${localProps.getProperty("TMDB_API_KEY")}\"")
    }
    buildFeatures { buildConfig = true }
}
```
And reference it in code via `BuildConfig.TMDB_API_KEY` instead of hardcoding it in `Constants.kt` — this keeps your key out of git history if you ever make the repo public.

**4. Build & run**
Sync Gradle dependencies and run the app on an Android device or emulator (API Level 24+).

---

## 🗺️ Roadmap

- [ ] Multiplatform expansion (iOS / Desktop / Web via Compose Multiplatform)
- [ ] Downloadable offline playback queue
- [ ] User profiles & personalized recommendations
- [ ] Dark/Light dynamic theming via Material You

---

## 🤝 Contributing

Contributions are always welcome! Feel free to open issues or submit pull requests to enhance features, add new API endpoints, or refine UI performance.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📜 License

This project is licensed under the **Apache License, Version 2.0**. See the [LICENSE](LICENSE) file for details.

This product uses the TMDB API but is not endorsed or certified by TMDB.

---
## 🔗 Related

- 🕰️ **v1.0 (previous version):** [TMDB-Movie-App-Jetpack-Compose](https://github.com/Dinesh2510/TMDB-Movie-App-Jetpack-Compose) — the original Retrofit + Hilt build this project evolved from.
---
## 🧑‍💻 Author

- GitHub: [@Dinesh2510](https://github.com/Dinesh2510)
- YouTube: [@pixeldesigndeveloper](https://www.youtube.com/@pixeldesigndeveloper)
- Website: [pixeldev.in](https://pixeldev.in/)
---
**Thanks for checking out the TMDB Movie App! Don't forget to ⭐ the repository if you find it helpful. 😊**

**Made with ❤️ by Dinesh Chavan** 