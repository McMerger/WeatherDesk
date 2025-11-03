# WeatherDesk - Kotlin Desktop Weather Application

A professional desktop weather application built with Kotlin, JavaFX, and Firebase, fetching real-time weather data from OpenWeatherMap API.

## Technology Stack

- **Language**: Kotlin 1.9.22
- **UI Framework**: JavaFX 21
- **Architecture**: MVVM (Model-View-ViewModel)
- **API**: OpenWeatherMap API
- **Backend**: Firebase Firestore
- **HTTP Client**: Ktor
- **Build Tool**: Gradle

## Features

### Core Functionality
- ✅ **Real-time Weather Data**: Fetch current weather from OpenWeatherMap API
- ✅ **Dual Input Methods**: Search by city name OR geographic coordinates (latitude/longitude)
- ✅ **5-Day Forecast**: Multi-day weather forecast with daily high/low temperatures
- ✅ **Unit Conversion**: Toggle between Celsius, Fahrenheit, and Kelvin
- ✅ **Weather Icons**: Visual weather condition indicators
- ✅ **Cloud Persistence**: Store last-used location in Firebase Firestore
- ✅ **Rating System**: Rate forecast accuracy and store ratings in Firebase
- ✅ **Responsive UI**: Clean, modern JavaFX interface with loading states
- ✅ **Error Handling**: Comprehensive error messages and validation

### Architecture Highlights
- **MVVM Pattern**: Clean separation between View (FXML), ViewModel, and Model
- **Repository Pattern**: Abstracted data access layer
- **Kotlin Coroutines**: Async/await patterns for non-blocking API calls
- **OOP Design**: Data classes, sealed classes, enums
- **Functional Programming**: Extension functions, lambda expressions
- **Dependency Injection**: Manual DI with clear service boundaries

## Project Structure

```
src/main/kotlin/com/weatherdesk/
├── Main.kt                          # Application entry point
├── config/
│   └── ConfigManager.kt             # Configuration and API key management
├── model/
│   └── WeatherModels.kt             # Data models (CurrentWeather, DailyForecast, etc.)
├── service/
│   └── OpenWeatherMapService.kt     # OpenWeatherMap API integration
├── repository/
│   ├── FirebaseRepository.kt        # Firebase Firestore operations
│   └── WeatherRepository.kt         # Weather data repository
├── viewmodel/
│   └── WeatherViewModel.kt          # MVVM ViewModel with business logic
└── view/
    └── WeatherController.kt         # JavaFX controller

src/main/resources/
├── fxml/
│   └── WeatherView.fxml             # JavaFX FXML layout
└── styles/
    └── weather.css                  # Application styles
```

## Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Gradle**: 8.x (or use included wrapper)
- **OpenWeatherMap API Key**: [Get free key here](https://openweathermap.org/api)
- **Firebase Project** (Optional): [Firebase Console](https://console.firebase.google.com/)

## Setup Instructions

### 1. Clone and Build

```bash
# Navigate to project directory
cd studio

# Build the project
./gradlew build
```

### 2. Configure API Keys

**Option A: Using config.properties (Recommended)**

```bash
# Copy template
cp config.properties.template config.properties

# Edit config.properties and add your API key
openweathermap.api.key=YOUR_ACTUAL_API_KEY
```

**Option B: Using Environment Variables**

```bash
export OPENWEATHERMAP_API_KEY="your_api_key_here"
```

### 3. Configure Firebase (Optional but Recommended)

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Firestore Database
3. Download service account credentials:
   - Go to Project Settings → Service Accounts
   - Click "Generate New Private Key"
   - Save as `firebase-credentials.json` in project root
4. Update `config.properties`:
   ```properties
   firebase.credentials.path=firebase-credentials.json
   ```

**Note**: The app works without Firebase using local storage, but cloud persistence will be disabled.

### 4. Run the Application

```bash
# Using Gradle
./gradlew run

# Or build and run JAR
./gradlew jar
java -jar build/libs/weatherdesk-1.0.0.jar
```

## Usage Guide

### Searching by City Name
1. Ensure "Use Coordinates" toggle is OFF
2. Enter city name (e.g., "London", "New York", "Tokyo")
3. Click "Search"

### Searching by Coordinates
1. Click "Use Coordinates" toggle
2. Enter latitude (-90 to 90) and longitude (-180 to 180)
3. Click "Search by Coordinates"

### Changing Temperature Units
- Use the dropdown in the weather card to switch between °C, °F, and K
- Selection is saved to your preferences

### Rating Forecasts
- After viewing weather data, click stars to rate accuracy (1-5)
- Click "Submit Rating" to save to Firebase
- Average ratings are displayed for each city

## Input Validation

The application includes comprehensive validation:

### City Name Validation
- ✅ Cannot be empty
- ✅ Must contain only letters, spaces, hyphens, and commas
- ✅ Examples: "New York", "São Paulo", "Saint-Denis"

### Coordinate Validation
- ✅ Latitude: Must be between -90 and 90
- ✅ Longitude: Must be between -180 and 180
- ✅ Must be valid decimal numbers
- ✅ Example: Lat: 51.5074, Lon: -0.1278 (London)

## API Integration

### OpenWeatherMap API
- **Current Weather Endpoint**: `/data/2.5/weather`
- **Forecast Endpoint**: `/data/2.5/forecast`
- **Units**: Metric (Celsius, m/s)
- **Rate Limits**: Free tier allows 60 calls/minute

### Firebase Firestore Collections

**users** collection:
```json
{
  "lastSearchedCity": "London",
  "lastSearchedLatitude": 51.5074,
  "lastSearchedLongitude": -0.1278,
  "preferredTempUnit": "CELSIUS",
  "preferredWindUnit": "KILOMETERS_PER_HOUR",
  "updatedAt": 1234567890
}
```

**ratings** collection:
```json
{
  "city": "London",
  "rating": 5,
  "date": "2024-01-15",
  "userId": "default_user",
  "timestamp": 1234567890
}
```

## Code Patterns Demonstrated

### Object-Oriented Programming (OOP)
- **Data Classes**: Immutable models with automatic equals/hashCode/toString
- **Sealed Classes**: Type-safe Result wrapper
- **Enums**: TemperatureUnit, WindSpeedUnit, WeatherCondition
- **Interfaces**: Clean abstraction layers
- **Encapsulation**: Private fields with public methods

### Functional Programming
- **Extension Functions**: `convertFromCelsius()`, `getFormattedTemperature()`
- **Higher-Order Functions**: `map`, `filter`, `groupBy`
- **Lambda Expressions**: Event handlers, collection operations
- **Immutability**: Data classes with `copy()` for modifications

### Procedural Elements
- **Sequential Logic**: API response parsing, data transformation
- **Validation Functions**: Input checking, range validation

## Error Handling

The application handles errors at multiple levels:

1. **Configuration Errors**: Missing API keys with user-friendly messages
2. **Network Errors**: API failures, timeout handling
3. **Validation Errors**: Invalid city names, coordinate ranges
4. **Firebase Errors**: Graceful degradation to local storage
5. **Parsing Errors**: Malformed API responses

## Design Patterns Used

1. **MVVM**: Separation of UI, business logic, and data
2. **Repository Pattern**: Abstracted data access
3. **Singleton**: ConfigManager for global configuration
4. **Observer Pattern**: JavaFX property bindings
5. **Strategy Pattern**: LocationInput sealed class for polymorphic search
6. **Factory Pattern**: Weather icon creation based on condition

## Testing

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport
```

## Assignment Rubric Compliance

### ✅ Problem Statement
Desktop weather application with real-time data, dual input methods, and cloud persistence.

### ✅ Features Completeness
- [x] Fetch weather from OpenWeatherMap API
- [x] City name input with validation
- [x] Coordinate input with validation
- [x] Current weather display (temp, humidity, wind)
- [x] 5-day forecast with icons
- [x] Weather condition icons
- [x] Unit conversion (Celsius, Fahrenheit, Kelvin)
- [x] Loading indicators
- [x] Error feedback with alerts
- [x] Firebase Firestore integration
- [x] Store last-used location
- [x] Forecast rating system
- [x] Responsive JavaFX UI

### ✅ Technology Requirements
- [x] Kotlin programming language
- [x] JavaFX UI framework
- [x] Firebase backend (Firestore)
- [x] OpenWeatherMap API integration
- [x] Secure API key management

### ✅ Code Quality
- [x] MVVM architecture
- [x] OOP patterns (classes, inheritance, encapsulation)
- [x] Functional programming (lambdas, extensions)
- [x] Procedural code where appropriate
- [x] Proper error handling
- [x] Logging and monitoring
- [x] Code documentation

### ✅ UI/UX
- [x] JavaFX components (VBox, HBox, GridPane, etc.)
- [x] FXML layouts (not web HTML/CSS)
- [x] Clean, readable design
- [x] Loading states with ProgressIndicator
- [x] Error dialogs
- [x] Responsive layout

## Troubleshooting

### API Key Issues
**Error**: "OpenWeatherMap API key not found"
- **Solution**: Ensure `config.properties` exists with valid `openweathermap.api.key`

### Firebase Connection Issues
**Warning**: "Firebase: Not Connected"
- **Solution**: Check `firebase-credentials.json` exists and is valid
- **Note**: App works without Firebase using local storage

### Build Errors
**Error**: "Cannot resolve JavaFX modules"
- **Solution**: Ensure JDK 17+ is installed and JAVA_HOME is set correctly

### Runtime Errors
**Error**: Network timeout
- **Solution**: Check internet connection and firewall settings
- **Note**: OpenWeatherMap API requires internet access

## Future Enhancements

- [ ] Multi-user authentication
- [ ] Historical weather data charts
- [ ] Weather alerts and notifications
- [ ] Dark mode theme
- [ ] Multiple location favorites
- [ ] Export weather data to CSV
- [ ] Offline mode with cached data
- [ ] Internationalization (i18n)

## License

Educational project for demonstration purposes.

## Credits

- **Weather Data**: [OpenWeatherMap](https://openweathermap.org/)
- **Backend**: [Firebase](https://firebase.google.com/)
- **Framework**: [JavaFX](https://openjfx.io/)
- **Language**: [Kotlin](https://kotlinlang.org/)

---

**Built with ❤️ using Kotlin, JavaFX, and Firebase**
