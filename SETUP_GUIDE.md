# WeatherDesk Setup Guide

Complete step-by-step guide to set up and run the Kotlin desktop weather application.

## Quick Start (5 minutes)

### 1. Get OpenWeatherMap API Key (Free)

1. Visit [OpenWeatherMap Sign Up](https://home.openweathermap.org/users/sign_up)
2. Create a free account
3. Navigate to API Keys section
4. Copy your API key (starts with your default key)
5. **Important**: It may take 10 minutes for the key to activate

### 2. Configure the Application

```bash
# Copy the template
cp config.properties.template config.properties

# Edit config.properties
# Replace YOUR_API_KEY_HERE with your actual API key
```

Example `config.properties`:
```properties
openweathermap.api.key=abc123def456ghi789jkl012mno345pq
firebase.credentials.path=firebase-credentials.json
```

### 3. Run the Application

```bash
# Make sure you have JDK 17+ installed
java -version

# Run with Gradle
./gradlew run
```

That's it! The app will launch and you can start searching for weather.

---

## Detailed Setup

### Prerequisites

#### Required:
- **Java Development Kit (JDK) 17 or higher**
  - Download: [Adoptium OpenJDK](https://adoptium.net/)
  - Verify: `java -version` should show 17+

#### Recommended:
- **Gradle 8.x** (or use included wrapper `./gradlew`)
- **Git** (for version control)

### Installation Steps

#### Step 1: Verify Java Installation

```bash
# Check Java version (must be 17+)
java -version

# Check JAVA_HOME is set
echo $JAVA_HOME  # Unix/Mac
echo %JAVA_HOME%  # Windows
```

If not installed:
- **Ubuntu/Debian**: `sudo apt install openjdk-17-jdk`
- **Mac**: `brew install openjdk@17`
- **Windows**: Download from [Adoptium](https://adoptium.net/)

#### Step 2: Build the Project

```bash
# Navigate to project directory
cd studio

# Download dependencies and build
./gradlew build

# First build may take 2-5 minutes
```

#### Step 3: Configure API Keys

##### Option A: config.properties (Recommended)

```bash
# Copy template
cp config.properties.template config.properties

# Edit with your favorite editor
nano config.properties  # or vim, code, etc.
```

Add your API key:
```properties
openweathermap.api.key=YOUR_ACTUAL_KEY_HERE
```

##### Option B: Environment Variables

```bash
# Unix/Mac - Add to ~/.bashrc or ~/.zshrc
export OPENWEATHERMAP_API_KEY="your_key_here"

# Windows - PowerShell
$env:OPENWEATHERMAP_API_KEY="your_key_here"

# Windows - Command Prompt
set OPENWEATHERMAP_API_KEY=your_key_here
```

#### Step 4: Run the Application

```bash
# Using Gradle wrapper (recommended)
./gradlew run

# Or build a JAR and run it
./gradlew jar
java -jar build/libs/weatherdesk-1.0.0.jar
```

---

## Firebase Setup (Optional)

Firebase enables cloud persistence for:
- Last searched location across devices
- Forecast ratings storage
- User preferences sync

### Step 1: Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add Project"
3. Enter project name: "WeatherDesk" (or your choice)
4. Disable Google Analytics (optional for this project)
5. Click "Create Project"

### Step 2: Enable Firestore Database

1. In Firebase Console, click "Firestore Database"
2. Click "Create Database"
3. Select "Start in test mode" (for development)
4. Choose a location close to you
5. Click "Enable"

### Step 3: Generate Service Account Credentials

1. Click gear icon → "Project Settings"
2. Go to "Service Accounts" tab
3. Click "Generate New Private Key"
4. Click "Generate Key" (downloads JSON file)
5. **IMPORTANT**: Keep this file secure, it contains secrets!

### Step 4: Configure Application

```bash
# Move downloaded file to project root
mv ~/Downloads/weatherdesk-*.json firebase-credentials.json

# Update config.properties
echo "firebase.credentials.path=firebase-credentials.json" >> config.properties
```

### Step 5: Test Firebase Connection

1. Run the application
2. Check footer: Should show "Firebase: Connected ✓"
3. Search for a city
4. Restart app - last search should load automatically

---

## Firestore Security Rules (Production)

If deploying to production, update Firestore rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection - authenticated users only
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    // Ratings collection - authenticated users can write, anyone can read
    match /ratings/{ratingId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

---

## Running Without Firebase

The application works perfectly without Firebase using browser localStorage fallback:

1. Don't create `config.properties` entry for Firebase
2. Don't download credentials file
3. App will show: "Firebase: Not Connected (using local storage)"
4. Features that still work:
   - ✅ All weather fetching
   - ✅ Last location saved locally
   - ✅ Unit preferences saved locally
   - ❌ Cloud sync across devices
   - ❌ Rating persistence

---

## Troubleshooting

### Problem: "OpenWeatherMap API key not found"

**Solution**:
```bash
# Check config.properties exists
ls -la config.properties

# Check it contains the key
cat config.properties | grep openweathermap.api.key

# Ensure no spaces around = sign
# WRONG: openweathermap.api.key = abc123
# RIGHT: openweathermap.api.key=abc123
```

### Problem: "401 Unauthorized" from API

**Solution**:
- API key takes 10-15 minutes to activate after creation
- Wait a bit and try again
- Verify key is correct (copy-paste from OpenWeatherMap dashboard)

### Problem: JavaFX modules not found

**Solution**:
```bash
# Ensure JDK 17+ is installed
java -version

# Clean and rebuild
./gradlew clean build --refresh-dependencies
```

### Problem: Firebase connection fails

**Solution**:
1. Verify `firebase-credentials.json` exists in project root
2. Check file is valid JSON: `cat firebase-credentials.json | python -m json.tool`
3. Ensure Firebase project has Firestore enabled
4. Check file permissions: `chmod 600 firebase-credentials.json`

### Problem: "Module not found" errors

**Solution**:
```bash
# Delete Gradle cache and rebuild
rm -rf ~/.gradle/caches
./gradlew clean build --refresh-dependencies
```

### Problem: Application window doesn't appear

**Solution**:
1. Check logs in `logs/weatherdesk.log`
2. Ensure display is available (for headless servers, use Xvfb)
3. Try running with: `./gradlew run --info` for detailed output

---

## Development Setup

### IDE Setup (IntelliJ IDEA Recommended)

1. **Install IntelliJ IDEA**
   - Download: [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/)

2. **Open Project**
   - File → Open → Select `studio` directory
   - Wait for Gradle sync to complete

3. **Run Configuration**
   - Run → Edit Configurations → Add New → Application
   - Main class: `com.weatherdesk.MainKt`
   - Module: `weatherdesk.main`
   - Working directory: `$PROJECT_DIR$`

4. **Enable Kotlin Plugin**
   - IntelliJ IDEA comes with Kotlin support built-in
   - Verify: Settings → Plugins → Kotlin is enabled

### Debugging

```bash
# Run with debug logging
./gradlew run --debug

# Or set log level in src/main/resources/logback.xml
<root level="DEBUG">
```

### Building Distribution

```bash
# Create executable JAR with all dependencies
./gradlew jar

# JAR location
ls -lh build/libs/weatherdesk-1.0.0.jar

# Run standalone
java -jar build/libs/weatherdesk-1.0.0.jar
```

---

## Testing the Application

### Manual Testing Checklist

#### ✅ City Search
- [ ] Search for "London" - should return UK city
- [ ] Search for "New York" - should return US city
- [ ] Search for "Tokyo" - should return Japan city
- [ ] Try invalid city "XYZ12345" - should show error

#### ✅ Coordinate Search
- [ ] Toggle to coordinate mode
- [ ] Enter: Lat 51.5074, Lon -0.1278 (London)
- [ ] Should display London weather
- [ ] Try invalid coordinates (lat: 100) - should show error

#### ✅ Unit Conversion
- [ ] Change temperature unit to Fahrenheit
- [ ] Temperature should update (e.g., 20°C → 68°F)
- [ ] Change to Kelvin (20°C → 293K)
- [ ] Restart app - should remember preference

#### ✅ Forecast
- [ ] Verify 5-day forecast displays
- [ ] Each day shows high/low temps
- [ ] Weather icons match conditions

#### ✅ Rating System
- [ ] Click 5 stars
- [ ] Submit rating
- [ ] Should show success message
- [ ] If Firebase connected, rating persists

#### ✅ Error Handling
- [ ] Disconnect internet - should show error
- [ ] Enter empty city name - should show validation error
- [ ] Enter special characters - should validate

### Automated Tests

```bash
# Run unit tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

---

## Performance Tips

### Optimize Startup Time

1. **Pre-compile Kotlin**: `./gradlew compileKotlin`
2. **Use JAR distribution**: Faster than Gradle run
3. **Cache API responses**: Implement local caching for repeated queries

### Reduce API Calls

```kotlin
// Cache results for 10 minutes per city
// Modify WeatherRepository to add caching
private val cache = mutableMapOf<String, Pair<WeatherData, Long>>()
```

---

## Deployment

### Desktop Distribution

#### macOS App Bundle
```bash
# Use jpackage (JDK 14+)
jpackage --input build/libs \
  --name WeatherDesk \
  --main-jar weatherdesk-1.0.0.jar \
  --main-class com.weatherdesk.MainKt \
  --type dmg
```

#### Windows Executable
```bash
jpackage --input build/libs \
  --name WeatherDesk \
  --main-jar weatherdesk-1.0.0.jar \
  --main-class com.weatherdesk.MainKt \
  --type exe \
  --win-console
```

#### Linux Package
```bash
jpackage --input build/libs \
  --name WeatherDesk \
  --main-jar weatherdesk-1.0.0.jar \
  --main-class com.weatherdesk.MainKt \
  --type deb
```

---

## Security Best Practices

### 1. API Key Management

✅ **DO**:
- Store in `config.properties` (gitignored)
- Use environment variables in production
- Rotate keys periodically

❌ **DON'T**:
- Commit API keys to Git
- Hard-code keys in source files
- Share keys publicly

### 2. Firebase Credentials

✅ **DO**:
- Keep `firebase-credentials.json` secure
- Set file permissions: `chmod 600 firebase-credentials.json`
- Use service accounts with minimal permissions

❌ **DON'T**:
- Commit credentials to version control
- Use production credentials in development
- Share credentials across projects

### 3. Firestore Security

- Update security rules before production
- Implement user authentication
- Validate all data server-side

---

## Getting Help

### Resources
- **OpenWeatherMap API Docs**: https://openweathermap.org/api
- **Firebase Docs**: https://firebase.google.com/docs
- **Kotlin Docs**: https://kotlinlang.org/docs/
- **JavaFX Docs**: https://openjfx.io/

### Common Issues
- Check `logs/weatherdesk.log` for errors
- Enable debug logging for detailed info
- Verify all prerequisites are installed

### Support
- GitHub Issues: Create detailed bug reports
- Stack Overflow: Tag with `kotlin`, `javafx`, `firebase`

---

**Happy Weather Tracking! ☁️🌤️⛈️**
