# Weather App
> Assignment 7 for Native Mobile Development Course (NMR 2215)

A native Android weather application built with **Kotlin** and **Jetpack Compose**. The app allows users to search for cities, view current weather conditions, and see a 7-day forecast. It supports **offline mode** by caching the last successful API response.

## Features
* **City Search:** Find cities by name using the Open-Meteo Geocoding API.
* **Current Weather:** Displays temperature, wind speed, and min/max temperatures.
* **7-Day Forecast:** Horizontal scrollable list showing daily forecast.
* **Offline Support:** Automatically saves downloaded data. If there is no internet connection, the app displays the cached weather with an "Offline Mode" warning.
* **MVVM Architecture:** Clean separation of concerns between UI, Business Logic, and Data.

## Tech Stack
* **Language:** Kotlin
* **UI:** Jetpack Compose (Material3)
* **Networking:** Retrofit + OkHttp
* **JSON Parsing:** Kotlinx Serialization
* **Local Storage:** Jetpack DataStore (Preferences)
* **Concurrency:** Coroutines & Flow

## Architecture
The application follows the **MVVM (Model-View-ViewModel)** architectural pattern + Repository pattern:

1.  **Data Layer (`data/`)**:
    * `api/`: Defines Retrofit interfaces and HTTP client.
    * `model/`: Data classes annotated with `@Serializable` for parsing JSON.
    * `local/`: `WeatherCache` class that handles saving/retrieving JSON strings using DataStore.
    * `repository/`: `WeatherRepository` decides source of data (API vs Cache).
2.  **Domain/Logic (`viewmodel/`)**:
    * `WeatherViewModel`: Manages UI state (`isLoading`, `weatherData`, `isOffline`), handles exceptions, and communicates with the Repository.
3.  **UI Layer (`ui/`)**:
    * `WeatherScreen`: A composable function that renders the UI based on ViewModel state.

## API Reference
The app uses the free **Open-Meteo API** (no API key required).

* **Geocoding (Search):**
  `GET https://geocoding-api.open-meteo.com/v1/search?name={city}&count=10`
* **Weather Data:**
  `GET https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true&daily=temperature_2m_max,temperature_2m_min`

## Caching Strategy (Offline Mode)
To fulfill the offline requirement, the app uses **DataStore Preferences**:
1.  When data is successfully fetched from the API, the full JSON response object is serialized into a String and saved to DataStore with the key `last_weather_json`.
2.  If the network request fails (e.g., `UnknownHostException`), the Repository catches the exception and attempts to read the `last_weather_json` from DataStore.
3.  If cached data exists, it is returned to the UI with an `isOffline = true` flag.

## How to Run
1.  Clone this repository.
2.  Open the project in **Android Studio**.
3.  Sync Gradle project to download dependencies.
4.  Run the app on an Emulator or a physical device (Internet permission required).
5.  **To test offline mode:** Load a city, then enable "Airplane Mode" on the device/emulator and restart the app.

## Requirements Checklist
- [x] Search functionality
- [x] Current weather display
- [x] Forecast (7-day daily)
- [x] Offline/Cache implementation
- [x] MVVM Architecture
- [x] Error handling (Network timeouts, empty search)

---
*Student: Noyan Inayatulla*
