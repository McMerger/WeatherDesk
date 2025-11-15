package com.weatherdesk.viewmodel

import com.weatherdesk.model.*
import com.weatherdesk.repository.WeatherRepository
import javafx.application.Platform
import javafx.beans.property.*
import kotlinx.coroutines.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * ViewModel for weather application following MVVM pattern
 * Manages UI state and business logic
 */
class WeatherViewModel(private val repository: WeatherRepository) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Observable properties for UI binding
    val currentWeather = SimpleObjectProperty<CurrentWeather?>()
    val forecast = SimpleListProperty<DailyForecast>()
    val isLoading = SimpleBooleanProperty(false)
    val errorMessage = SimpleStringProperty("")
    val successMessage = SimpleStringProperty("")

    // User preferences
    val temperatureUnit = SimpleObjectProperty<TemperatureUnit>(TemperatureUnit.CELSIUS)
    val windSpeedUnit = SimpleObjectProperty<WindSpeedUnit>(WindSpeedUnit.KILOMETERS_PER_HOUR)

    // Input validation
    val cityInput = SimpleStringProperty("")
    val latitudeInput = SimpleStringProperty("")
    val longitudeInput = SimpleStringProperty("")
    val isCoordinateMode = SimpleBooleanProperty(false)

    // Rating
    val currentRating = SimpleIntegerProperty(0)
    val averageRating = SimpleDoubleProperty(0.0)

    init {
    }


    /**
     * Fetch weather by city name
     */
    fun fetchWeatherByCity() {
        val city = cityInput.get()?.trim()

        if (city.isNullOrBlank()) {
            updateError("City name cannot be empty")
            return
        }

        if (!isValidCityName(city)) {
            updateError("Invalid city name. Please use letters, spaces, and hyphens only.")
            return
        }

        fetchWeather(LocationInput.City(city))
    }

    /**
     * Fetch weather by coordinates
     */
    fun fetchWeatherByCoordinates() {
        val latStr = latitudeInput.get()?.trim()
        val lonStr = longitudeInput.get()?.trim()

        if (latStr.isNullOrBlank() || lonStr.isNullOrBlank()) {
            updateError("Latitude and longitude cannot be empty")
            return
        }

        val lat = latStr.toDoubleOrNull()
        val lon = lonStr.toDoubleOrNull()

        if (lat == null || lon == null) {
            updateError("Invalid coordinates. Please enter valid numbers.")
            return
        }

        try {
            val location = LocationInput.Coordinates(lat, lon)
            fetchWeather(location)
        } catch (e: IllegalArgumentException) {
            updateError(e.message ?: "Invalid coordinates")
        }
    }

    /**
     * Core method to fetch weather data
     */
    private fun fetchWeather(location: LocationInput) {
        scope.launch {
            isLoading.set(true)
            errorMessage.set("")
            successMessage.set("")

            when (val result = repository.getWeather(location)) {
                is Result.Success -> {
                    currentWeather.set(result.data.current)
                    forecast.set(javafx.collections.FXCollections.observableArrayList(result.data.forecast))
                    updateSuccess("Weather data loaded successfully for ${result.data.current.city}")
                    loadAverageRating(result.data.current.city)
                    logger.info { "Weather fetched successfully for ${result.data.current.city}" }
                }
                is Result.Error -> {
                    updateError(result.message)
                    logger.error { "Failed to fetch weather: ${result.message}" }
                }
                is Result.Loading -> {
                    // Already handled by isLoading property
                }
            }

            isLoading.set(false)
        }
    }

    /**
     * Change temperature unit
     */
    fun setTemperatureUnit(unit: TemperatureUnit) {
        temperatureUnit.set(unit)

    }

    /**
     * Change wind speed unit
     */
    fun setWindSpeedUnit(unit: WindSpeedUnit) {
        windSpeedUnit.set(unit)

    }

    /**
     * Toggle between city and coordinate input modes
     */
    fun toggleInputMode() {
        isCoordinateMode.set(!isCoordinateMode.get())
        errorMessage.set("")
    }

    /**
     * Validate city name
     */
    private fun isValidCityName(city: String): Boolean {
        return city.matches(Regex("^[a-zA-Z\\s\\-,]+$"))
    }

    /**
     * Update error message
     */
    private fun updateError(message: String) {
        Platform.runLater {
            errorMessage.set(message)
            successMessage.set("")
        }
    }

    /**
     * Update success message
     */
    private fun updateSuccess(message: String) {
        Platform.runLater {
            successMessage.set(message)
            errorMessage.set("")
        }
    }

    /**
     * Clear messages
     */
    fun clearMessages() {
        errorMessage.set("")
        successMessage.set("")
    }


    /**
     * Cleanup when ViewModel is destroyed
     */
    fun onDestroy() {
        scope.cancel()
    }
}
