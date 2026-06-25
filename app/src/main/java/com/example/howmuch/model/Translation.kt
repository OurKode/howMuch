package com.example.howmuch.model

enum class AppLanguage {
    EN, ID
}

data class Dictionary(
    val salaryConfigTitle: String,
    val monthlyIncome: String,
    val workingDays: String,
    val workingHours: String,
    val calculatorTitle: String,
    val itemName: String,
    val costOfItem: String,
    val saveCalculation: String,
    val realTimePreview: String,
    val requiresWorkingFor: String,
    val daysLabel: String,
    val hoursLabel: String,
    val percentOfMonthly: String,
    val dailyWage: String,
    val hourlyWage: String,
    val historyTitle: String,
    val clearAll: String,
    val noHistory: String,
    val emptyHistoryDesc: String,
    val removeLabel: String,
    val daysShort: String,
    val hrsShort: String,
    val unnamedItem: String,
    val appSubtitle: String
)

val EnglishDictionary = Dictionary(
    salaryConfigTitle = "SALARY CONFIGURATION",
    monthlyIncome = "Monthly Income (Rp)",
    workingDays = "Working Days/Month",
    workingHours = "Working Hours/Day",
    calculatorTitle = "CALCULATOR",
    itemName = "Item Name",
    costOfItem = "Cost of Item (Rp)",
    saveCalculation = "Save Calculation",
    realTimePreview = "REAL-TIME PREVIEW",
    requiresWorkingFor = "Requires working for",
    daysLabel = "days",
    hoursLabel = "hours",
    percentOfMonthly = "of monthly",
    dailyWage = "Daily wage: Rp",
    hourlyWage = "Hourly: Rp",
    historyTitle = "CALCULATION HISTORY",
    clearAll = "CLEAR ALL",
    noHistory = "No history items",
    emptyHistoryDesc = "Your saved item calculations will appear here.",
    removeLabel = "Remove",
    daysShort = "days",
    hrsShort = "hrs",
    unnamedItem = "unnamed item",
    appSubtitle = "Translate prices into the duration of work required to buy them."
)

val IndonesianDictionary = Dictionary(
    salaryConfigTitle = "KONFIGURASI GAJI",
    monthlyIncome = "Pendapatan Bulanan (Rp)",
    workingDays = "Hari Kerja/Bulan",
    workingHours = "Jam Kerja/Hari",
    calculatorTitle = "KALKULATOR",
    itemName = "Nama Barang",
    costOfItem = "Harga Barang (Rp)",
    saveCalculation = "Simpan Kalkulasi",
    realTimePreview = "PRATINJAU LANGSUNG",
    requiresWorkingFor = "Membutuhkan kerja selama",
    daysLabel = "hari",
    hoursLabel = "jam",
    percentOfMonthly = "dari gaji bulanan",
    dailyWage = "Gaji harian: Rp",
    hourlyWage = "Gaji per jam: Rp",
    historyTitle = "RIWAYAT KALKULASI",
    clearAll = "HAPUS SEMUA",
    noHistory = "Tidak ada riwayat",
    emptyHistoryDesc = "Kalkulasi barang yang Anda simpan akan muncul di sini.",
    removeLabel = "Hapus",
    daysShort = "hari",
    hrsShort = "jam",
    unnamedItem = "barang tanpa nama",
    appSubtitle = "Terjemahkan harga barang menjadi durasi kerja yang dibutuhkan untuk membelinya."
)
