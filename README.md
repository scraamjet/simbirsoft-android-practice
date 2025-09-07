# Simbirsoft Android Practice

This project was created as part of a **practical training at Simbirsoft**, where I worked under the guidance of a mentor.  
It demonstrates a production-style Android application with a modular architecture, modern UI approaches, and clean architecture principles.

---

## 🛠️ Technologies & Tools

- **Language:** Kotlin  
- **Architecture:** Multi-module, Clean Architecture, MVI + MVVM (Effect, State, Event)  
- **Dependency Injection:** Dagger 2  
- **UI:**
  - Jetpack Compose — used in `Auth` module and partially in `News` (only `NewsFragment`)  
  - XML Layouts — used in all other modules (`ConstraintLayout`, `LinearLayout`, etc.)  
  - `RecyclerView` + `ListAdapter`  
  - `SearchView`  
  - `TabLayout` + `ViewPager`  
- **Multithreading:** Coroutines + Flow  
- **Networking:** Retrofit  
- **Data storage:**
  - Room (local database)  
  - DataStore Preferences  
- **Image loading:** Coil  
- **Background work:** WorkManager (used for donation notifications)  

---

## 📐 Project Architecture

The project follows **Clean Architecture** with strict separation of layers:


- **Data Layer**
  - **Retrofit** — network requests
  - **Room** — local database
  - **DataStore Preferences** — key-value storage  
- **Domain Layer**
  - **Repositories** — abstraction between data and domain
  - **UseCases / Interactors** — business logic  
- **Presentation Layer**
  - **ViewModels** — state management with MVI + MVVM (Effect, State, Event)
  - **Fragments / Features** — user interface implemented with **XML** or **Compose**

---

## 🌐 Data Flow

1. **Network fetch on app start** — data is loaded once from the backend using a `Service`.  
   - Backend repository: [simbirsoft-django-practice](https://github.com/scraamjet/simbirsoft-django-practice)  
   - Backend stack: **Python / Django**
2. **Persistence** — the retrieved data is stored in **Room**.  
3. **Single source of truth** — the app reads data only from the **local database** after initialization.  
   - This ensures **offline access**, faster UI rendering, and consistent state management.  

---

## 📱 Features

The application is divided into independent **feature modules**.  

- **Authentication (Auth module)**  
  - Implemented with Jetpack Compose  
  - User login and authentication flow  

- **News**  
  - **News list** — displays news according to selected filters (Compose for list UI)  
  - **News detail** — detailed news screen with text and images (XML)  

- **Profile**  
  - Ability to **change or remove profile picture**  
  - Profile information management  

- **Search**  
  - Search by **title** and **description**  
  - (Filter module is not used here)  

- **Filter**  
  - Dedicated screen to choose **categories of news**  
  - Filters affect what is displayed in the News list  

- **Help**  
  - Displays a list of **help categories**  

- **Donations**  
  - Dialog screen allowing users to donate a chosen amount  
  - Integrated with **WorkManager** for notifications  

## 🔀 Branches

Some branches demonstrate alternative approaches and specific implementations:

- **`rxjava` branch**  
  - Multithreading: **RxJava** (cold and hot observables)  

- **`concurrency` branch**  
  - Multithreading: **Executors**, **Service**, **Handler/Looper**  

- **`data_structures` branch**  
  - Local storage: **SharedPreferences**  
  - Configuration changes handled via **Bundle**  

- **`fragments` branch**  
  - Fragments are created using the **`newInstance()` factory method**  
  - Configuration changes handled via **Bundle**
