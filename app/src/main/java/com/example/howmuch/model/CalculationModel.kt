package com.example.howmuch.model

import java.util.UUID

/**
 * Configuration data for user's salary and working time setup.
 *
 * @property salary Monthly salary in user's currency.
 * @property workingDays Number of working days in a month (usually 20, 22, or 30).
 * @property workingHours Number of working hours in a single day (usually 8).
 */
data class SalaryConfig(
    val salary: Double = 3000000.0,
    val workingDays: Int = 30,
    val workingHours: Double = 8.0
)

/**
 * Representation of a calculated price conversion.
 *
 * @property id Unique identifier for this calculation.
 * @property itemName Name or description of the target item.
 * @property itemPrice Cost of the item.
 * @property dailyWage Computed daily wage.
 * @property hourlyWage Computed hourly wage.
 * @property workingDaysRequired Required duration in working days to purchase the item.
 * @property workingHoursRequired Required duration in working hours to purchase the item.
 * @property percentOfMonthly Percentage of the user's monthly salary that the item costs.
 * @property timestamp Epoch millisecond timestamp of when the calculation was made.
 */
data class CalculationResult(
    val id: String = UUID.randomUUID().toString(),
    val itemName: String,
    val itemPrice: Double,
    val dailyWage: Double,
    val hourlyWage: Double,
    val workingDaysRequired: Double,
    val workingHoursRequired: Double,
    val percentOfMonthly: Double,
    val timestamp: Long = System.currentTimeMillis()
)
