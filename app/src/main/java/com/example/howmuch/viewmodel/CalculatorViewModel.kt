package com.example.howmuch.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.example.howmuch.model.AppLanguage
import com.example.howmuch.model.CalculationResult
import com.example.howmuch.model.SalaryConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel to manage application state, calculations, and local persistence.
 *
 * Extends [AndroidViewModel] to leverage application context for accessing [SharedPreferences].
 * All state fields are exposed as read-only Flow wrappers ([StateFlow]) to guarantee UI safety.
 */
class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs: SharedPreferences =
        application.getSharedPreferences("howmuch_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Salary Configuration State
    private val _salaryConfig = MutableStateFlow(SalaryConfig())
    val salaryConfig: StateFlow<SalaryConfig> = _salaryConfig.asStateFlow()

    // App Language State
    private val _appLanguage = MutableStateFlow(AppLanguage.EN)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    // Form inputs state
    private val _itemName = MutableStateFlow("")
    val itemName: StateFlow<String> = _itemName.asStateFlow()

    private val _itemPriceStr = MutableStateFlow("")
    val itemPriceStr: StateFlow<String> = _itemPriceStr.asStateFlow()

    // Calculation History State
    private val _history = MutableStateFlow<List<CalculationResult>>(emptyList())
    val history: StateFlow<List<CalculationResult>> = _history.asStateFlow()

    init {
        loadSettings()
        loadLanguage()
        loadHistory()
    }

    // --- State Mutation Methods ---

    /**
     * Updates monthly salary configuration and persists it.
     */
    fun updateSalary(salary: Double) {
        _salaryConfig.update { it.copy(salary = salary) }
        saveSettings()
    }

    /**
     * Updates working days configuration and persists it.
     */
    fun updateWorkingDays(days: Int) {
        if (days in 1..31) {
            _salaryConfig.update { it.copy(workingDays = days) }
            saveSettings()
        }
    }

    /**
     * Updates working hours configuration and persists it.
     */
    fun updateWorkingHours(hours: Double) {
        if (hours in 0.1..24.0) {
            _salaryConfig.update { it.copy(workingHours = hours) }
            saveSettings()
        }
    }

    /**
     * Updates current item name input field.
     */
    fun updateItemName(name: String) {
        _itemName.value = name
    }

    /**
     * Updates current item price input text string.
     */
    fun updateItemPriceStr(priceStr: String) {
        // Filter input to allow only numeric characters and clean formatting
        val filtered = priceStr.filter { it.isDigit() || it == '.' }
        _itemPriceStr.value = filtered
    }

    /**
     * Computes the working duration for the current inputs, saves to history list,
     * and clears form inputs.
     *
     * @return True if computation succeeded, false otherwise (e.g. empty item name or invalid price).
     */
    fun calculateAndSave(): Boolean {
        val name = _itemName.value.trim()
        val price = _itemPriceStr.value.toDoubleOrNull() ?: 0.0
        val config = _salaryConfig.value

        if (name.isEmpty() || price <= 0.0 || config.salary <= 0.0 || config.workingDays <= 0) {
            return false
        }

        // Daily Wage = Monthly Salary / Total Working Days in Month
        val dailyWage = config.salary / config.workingDays
        // Hourly Wage = Daily Wage / Total Working Hours in a Day
        val hourlyWage = dailyWage / config.workingHours

        // Days required = Item Price / Daily Wage
        val workingDaysRequired = price / dailyWage
        // Hours required = Item Price / Hourly Wage
        val workingHoursRequired = price / hourlyWage
        // Percentage of monthly income
        val percentOfMonthly = (price / config.salary) * 100.0

        val result = CalculationResult(
            itemName = name,
            itemPrice = price,
            dailyWage = dailyWage,
            hourlyWage = hourlyWage,
            workingDaysRequired = workingDaysRequired,
            workingHoursRequired = workingHoursRequired,
            percentOfMonthly = percentOfMonthly
        )

        // Prepend new item to the history list
        _history.update { listOf(result) + it }
        saveHistory()

        // Reset item input fields
        _itemName.value = ""
        _itemPriceStr.value = ""
        return true
    }

    /**
     * Removes an item from calculation history by its unique identifier.
     */
    fun deleteHistoryItem(id: String) {
        _history.update { it.filter { item -> item.id != id } }
        saveHistory()
    }

    /**
     * Clears all items in the history list.
     */
    fun clearHistory() {
        _history.value = emptyList()
        saveHistory()
    }

    // --- Persistence Helper Methods ---

    private fun saveSettings() {
        val config = _salaryConfig.value
        sharedPrefs.edit()
            .putFloat("salary", config.salary.toFloat())
            .putInt("working_days", config.workingDays)
            .putFloat("working_hours", config.workingHours.toFloat())
            .apply()
    }

    private fun loadSettings() {
        val salary = sharedPrefs.getFloat("salary", 3000000.0f).toDouble()
        val days = sharedPrefs.getInt("working_days", 30)
        val hours = sharedPrefs.getFloat("working_hours", 8.0f).toDouble()
        _salaryConfig.value = SalaryConfig(salary, days, hours)
    }

    private fun saveHistory() {
        val json = gson.toJson(_history.value)
        sharedPrefs.edit()
            .putString("calculation_history", json)
            .apply()
    }

    private fun loadHistory() {
        val json = sharedPrefs.getString("calculation_history", null) ?: return
        try {
            val type = object : TypeToken<List<CalculationResult>>() {}.type
            val list: List<CalculationResult> = gson.fromJson(json, type)
            _history.value = list
        } catch (e: Exception) {
            _history.value = emptyList()
        }
    }

    /**
     * Updates application language and persists it.
     */
    fun updateLanguage(lang: AppLanguage) {
        _appLanguage.value = lang
        saveLanguage()
    }

    private fun saveLanguage() {
        sharedPrefs.edit()
            .putString("app_language", _appLanguage.value.name)
            .apply()
    }

    private fun loadLanguage() {
        val langStr = sharedPrefs.getString("app_language", AppLanguage.EN.name)
        val lang = try {
            AppLanguage.valueOf(langStr ?: AppLanguage.EN.name)
        } catch (e: Exception) {
            AppLanguage.EN
        }
        _appLanguage.value = lang
    }
}
