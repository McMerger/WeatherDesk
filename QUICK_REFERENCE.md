# WeatherDesk - Quick Reference Card

## 🚀 Quick Start

```bash
# 1. Configure API key
cp config.properties.template config.properties
# Edit config.properties and add your OpenWeatherMap API key

# 2. Run the app
./gradlew run
```

---

## 📁 Key Files

| File | Purpose |
|------|---------|
| `src/main/kotlin/com/weatherdesk/Main.kt` | Application entry point |
| `src/main/kotlin/com/weatherdesk/viewmodel/WeatherViewModel.kt` | Business logic |
| `src/main/kotlin/com/weatherdesk/service/OpenWeatherMapService.kt` | API integration |
| `src/main/kotlin/com/weatherdesk/repository/FirebaseRepository.kt` | Cloud storage |
| `src/main/resources/fxml/WeatherView.fxml` | UI layout |
| `config.properties` | API keys (create from template) |

---

## 🔑 Configuration

### Option 1: config.properties (Recommended)
```properties
openweathermap.api.key=YOUR_API_KEY_HERE
firebase.credentials.path=firebase-credentials.json
```

### Option 2: Environment Variables
```bash
export OPENWEATHERMAP_API_KEY="your_key_here"
export FIREBASE_CREDENTIALS_PATH="firebase-credentials.json"
```

---

## 🛠️ Common Commands

```bash
# Build project
./gradlew build

# Run application
./gradlew run

# Run tests
./gradlew test

# Create JAR
./gradlew jar

# Clean build
./gradlew clean build

# Run with debug logging
./gradlew run --info
```

---

## 📦 Project Structure

```
src/main/kotlin/com/weatherdesk/
├── Main.kt                      # Entry point
├── config/ConfigManager.kt      # Configuration
├── model/WeatherModels.kt       # Data classes
├── service/OpenWeatherMapService.kt  # API
├── repository/
│   ├── FirebaseRepository.kt    # Firebase
│   └── WeatherRepository.kt     # Repository
├── viewmodel/WeatherViewModel.kt     # ViewModel
└── view/WeatherController.kt    # UI controller

src/main/resources/
├── fxml/WeatherView.fxml        # Layout
├── styles/weather.css           # Styles
└── logback.xml                  # Logging
```

---

## 🎯 Feature Checklist

### Core Features
- [x] Fetch weather from OpenWeatherMap API
- [x] Search by city name
- [x] Search by coordinates (lat/lon)
- [x] Display current weather (temp, humidity, wind)
- [x] Show 5-day forecast
- [x] Weather condition icons
- [x] Unit conversion (°C/°F/K)
- [x] Wind speed units (km/h, mph, m/s)
- [x] Loading indicators
- [x] Error messages
- [x] Firebase cloud persistence
- [x] Save/load last location
- [x] Forecast rating system

---

## 🔍 Code Locations

### Input Validation
```kotlin
// City validation
WeatherViewModel.kt:87-96

// Coordinate validation
WeatherViewModel.kt:107-125
WeatherModels.kt:142-145
```

### API Integration
```kotlin
// Weather API calls
OpenWeatherMapService.kt:70-86

// API response parsing
OpenWeatherMapService.kt:130-159
```

### Firebase Operations
```kotlin
// Save preferences
FirebaseRepository.kt:57-78

// Load preferences
FirebaseRepository.kt:83-113

// Save ratings
FirebaseRepository.kt:118-137
```

### Unit Conversion
```kotlin
// Temperature conversion
WeatherModels.kt:13-17

// Wind speed conversion
WeatherModels.kt:35-39
```

---

## 🎨 UI Components

### FXML Elements
- `cityTextField` - City name input
- `latitudeTextField` - Latitude input
- `longitudeTextField` - Longitude input
- `coordinateModeToggle` - Switch input modes
- `loadingBox` - Loading indicator
- `weatherDataBox` - Weather display
- `forecastGrid` - 5-day forecast
- `ratingStars` - Rating system

### Controller Methods
- `searchByCity()` - Handle city search
- `searchByCoordinates()` - Handle coord search
- `changeTempUnit()` - Change temperature unit
- `submitRating()` - Submit forecast rating
- `toggleInputMode()` - Switch input modes

---

## 🐛 Troubleshooting

### Problem: API key not found
```bash
# Check config file exists
cat config.properties

# Verify API key format (no spaces around =)
openweathermap.api.key=abc123xyz789
```

### Problem: Build fails
```bash
# Clean and rebuild
./gradlew clean build --refresh-dependencies

# Check Java version (need 17+)
java -version
```

### Problem: Firebase not connecting
```bash
# Check credentials file exists
ls -la firebase-credentials.json

# Verify it's valid JSON
cat firebase-credentials.json | python -m json.tool
```

### Problem: City not found
- Wait 10-15 minutes if API key is newly created
- Try full city name: "London, UK" instead of "London"
- Check spelling

---

## 📊 Architecture Diagram

```
View (FXML)
    ↓
Controller
    ↓
ViewModel
    ↓
Repository
    ↓
Service / Firebase
```

---

## 🔒 Security Checklist

- [ ] `config.properties` is in `.gitignore`
- [ ] `firebase-credentials.json` is in `.gitignore`
- [ ] API key not hardcoded in source files
- [ ] No credentials in Git history
- [ ] File permissions set correctly (chmod 600)

---

## 📚 Documentation Files

1. **KOTLIN_README.md** - Complete documentation
2. **SETUP_GUIDE.md** - Detailed setup steps
3. **VALIDATION_CHECKLIST.md** - Requirements validation
4. **PROJECT_SUMMARY.md** - Project overview
5. **QUICK_REFERENCE.md** - This file

---

## 🧪 Testing Examples

### City Search
```
Input: "London"
Expected: London, UK weather with 5-day forecast
```

### Coordinate Search
```
Input: Lat=51.5074, Lon=-0.1278
Expected: London, UK weather
```

### Invalid Input
```
Input: City="!!!"
Expected: Validation error message
```

### Unit Conversion
```
Action: Change unit to Fahrenheit
Expected: 20°C → 68°F
```

---

## 📞 API Endpoints

### OpenWeatherMap
- **Current**: `https://api.openweathermap.org/data/2.5/weather`
- **Forecast**: `https://api.openweathermap.org/data/2.5/forecast`
- **Docs**: https://openweathermap.org/api

### Firebase
- **Collections**: `users`, `ratings`
- **Console**: https://console.firebase.google.com/

---

## 🎓 Key Concepts

### MVVM Pattern
- **Model**: Data classes (`WeatherModels.kt`)
- **View**: FXML + Controller
- **ViewModel**: Business logic + state

### Repository Pattern
- Abstracts data sources
- Coordinates API + Firebase
- Single source of truth

### Dependency Injection
- Manual DI in `Main.kt`
- Services injected into repositories
- Repositories injected into ViewModel

---

## 💡 Tips

1. **API Key**: Get free key from OpenWeatherMap (takes 10 min to activate)
2. **Firebase**: Optional but recommended for cloud features
3. **Logging**: Check `logs/weatherdesk.log` for debugging
4. **Units**: Preferences saved automatically
5. **Last Location**: Auto-loads on startup if available

---

## 🌐 Useful Links

- **OpenWeatherMap Sign Up**: https://home.openweathermap.org/users/sign_up
- **Firebase Console**: https://console.firebase.google.com/
- **Kotlin Docs**: https://kotlinlang.org/docs/
- **JavaFX Docs**: https://openjfx.io/

---

## 📝 Version Info

- **Version**: 1.0.0
- **Kotlin**: 1.9.22
- **JavaFX**: 21
- **JDK**: 17+
- **Gradle**: 8.x

---

**Quick Questions?**

- How to run? → `./gradlew run`
- Where's the API key? → `config.properties`
- Build fails? → `./gradlew clean build`
- Need help? → See `SETUP_GUIDE.md`
