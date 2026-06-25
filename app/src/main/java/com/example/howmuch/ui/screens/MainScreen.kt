package com.example.howmuch.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.howmuch.model.CalculationResult
import com.example.howmuch.ui.theme.MinimalistColors
import com.example.howmuch.viewmodel.CalculatorViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Main application screen.
 * Displays a clean, grid-based Bento layout containing salary details setup,
 * target item input, real-time calculations preview, and a history list.
 */
@Composable
fun MainScreen(viewModel: CalculatorViewModel) {
    val salaryConfig by viewModel.salaryConfig.collectAsState()
    val itemName by viewModel.itemName.collectAsState()
    val itemPriceStr by viewModel.itemPriceStr.collectAsState()
    val history by viewModel.history.collectAsState()

    // Real-time wage conversions
    val currentPrice = itemPriceStr.toDoubleOrNull() ?: 0.0
    val dailyWage = if (salaryConfig.workingDays > 0) salaryConfig.salary / salaryConfig.workingDays else 0.0
    val hourlyWage = if (salaryConfig.workingHours > 0) dailyWage / salaryConfig.workingHours else 0.0
    val requiredDays = if (dailyWage > 0) currentPrice / dailyWage else 0.0
    val requiredHours = if (hourlyWage > 0) currentPrice / hourlyWage else 0.0
    val percentOfMonthly = if (salaryConfig.salary > 0) (currentPrice / salaryConfig.salary) * 100.0 else 0.0

    Scaffold(
        containerColor = MinimalistColors.CanvasBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                HeaderSection()
            }

            // Bento Card 1: Salary configuration
            item {
                SalaryConfigCard(
                    salary = salaryConfig.salary,
                    workingDays = salaryConfig.workingDays,
                    workingHours = salaryConfig.workingHours,
                    onSalaryChange = { viewModel.updateSalary(it) },
                    onDaysChange = { viewModel.updateWorkingDays(it) },
                    onHoursChange = { viewModel.updateWorkingHours(it) }
                )
            }

            // Bento Card 2: Calculator inputs
            item {
                CalculatorInputCard(
                    itemName = itemName,
                    itemPriceStr = itemPriceStr,
                    onItemNameChange = { viewModel.updateItemName(it) },
                    onItemPriceChange = { viewModel.updateItemPriceStr(it) },
                    onCalculate = { viewModel.calculateAndSave() },
                    isCalculateEnabled = itemName.trim().isNotEmpty() && currentPrice > 0.0
                )
            }

            // Real-time Result Preview Card (visible when price is entered)
            item {
                AnimatedVisibility(
                    visible = currentPrice > 0.0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ResultPreviewCard(
                        itemName = itemName.ifEmpty { "unnamed item" },
                        itemPrice = currentPrice,
                        requiredDays = requiredDays,
                        requiredHours = requiredHours,
                        percentOfMonthly = percentOfMonthly,
                        dailyWage = dailyWage,
                        hourlyWage = hourlyWage
                    )
                }
            }

            // Bento Card 3: History List
            item {
                HistoryHeader(
                    onClearAll = { viewModel.clearHistory() },
                    showClearBtn = history.isNotEmpty()
                )
            }

            if (history.isEmpty()) {
                item {
                    EmptyHistoryCard()
                }
            } else {
                items(history, key = { it.id }) { item ->
                    HistoryItemCard(
                        item = item,
                        onDelete = { viewModel.deleteHistoryItem(item.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column {
        Text(
            text = "HowMuch?",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Translate prices into the duration of work required to buy them.",
            style = MaterialTheme.typography.bodyMedium,
            color = MinimalistColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MinimalistColors.BorderColor, thickness = 1.dp)
    }
}

@Composable
fun SalaryConfigCard(
    salary: Double,
    workingDays: Int,
    workingHours: Double,
    onSalaryChange: (Double) -> Unit,
    onDaysChange: (Int) -> Unit,
    onHoursChange: (Double) -> Unit
) {
    BentoCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SALARY CONFIGURATION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MinimalistColors.TextSecondary
            )

            // Monthly income input
            MinimalistInputField(
                label = "Monthly Income (Rp)",
                value = formatNumberPlain(salary),
                onValueChange = { onSalaryChange(it.toDoubleOrNull() ?: 0.0) },
                placeholder = "3.000.000",
                keyboardType = KeyboardType.Number
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Working days input
                Box(modifier = Modifier.weight(1f)) {
                    MinimalistInputField(
                        label = "Working Days/Month",
                        value = workingDays.toString(),
                        onValueChange = { onDaysChange(it.toIntOrNull() ?: 0) },
                        placeholder = "30",
                        keyboardType = KeyboardType.Number
                    )
                }
                // Working hours input
                Box(modifier = Modifier.weight(1f)) {
                    MinimalistInputField(
                        label = "Working Hours/Day",
                        value = formatNumberPlain(workingHours),
                        onValueChange = { onHoursChange(it.toDoubleOrNull() ?: 0.0) },
                        placeholder = "8",
                        keyboardType = KeyboardType.Number
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorInputCard(
    itemName: String,
    itemPriceStr: String,
    onItemNameChange: (String) -> Unit,
    onItemPriceChange: (String) -> Unit,
    onCalculate: () -> Unit,
    isCalculateEnabled: Boolean
) {
    BentoCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "CALCULATOR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MinimalistColors.TextSecondary
            )

            MinimalistInputField(
                label = "Item Name",
                value = itemName,
                onValueChange = onItemNameChange,
                placeholder = "e.g., Mechanical Keyboard",
                keyboardType = KeyboardType.Text
            )

            MinimalistInputField(
                label = "Cost of Item (Rp)",
                value = itemPriceStr,
                onValueChange = onItemPriceChange,
                placeholder = "300.000",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(4.dp))

            MinimalistButton(
                onClick = onCalculate,
                text = "Save Calculation",
                enabled = isCalculateEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ResultPreviewCard(
    itemName: String,
    itemPrice: Double,
    requiredDays: Double,
    requiredHours: Double,
    percentOfMonthly: Double,
    dailyWage: Double,
    hourlyWage: Double
) {
    BentoCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "REAL-TIME PREVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MinimalistColors.TextSecondary
                    )
                    Text(
                        text = itemName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MinimalistColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = "Rp ${formatCurrency(itemPrice)}",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MinimalistColors.TextPrimary
                )
            }

            // Days required text (Large Display)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Requires working for",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MinimalistColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatDecimals(requiredDays),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        lineHeight = 40.sp,
                        color = MinimalistColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "days",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = MinimalistColors.TextSecondary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }

            // Info Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Hours Badge (Pale Green)
                StatusBadge(
                    label = "${formatDecimals(requiredHours)} hours",
                    bgColor = MinimalistColors.PaleGreenBg,
                    textColor = MinimalistColors.PaleGreenText,
                    modifier = Modifier.weight(1f)
                )

                // Percent of income Badge (Pale Red)
                StatusBadge(
                    label = "${formatDecimals(percentOfMonthly)}% of monthly",
                    bgColor = MinimalistColors.PaleRedBg,
                    textColor = MinimalistColors.PaleRedText,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(color = MinimalistColors.BorderColor, thickness = 1.dp)

            // Wage conversion details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Daily wage: Rp ${formatCurrency(dailyWage)}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MinimalistColors.TextSecondary
                )
                Text(
                    text = "Hourly: Rp ${formatCurrency(hourlyWage)}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MinimalistColors.TextSecondary
                )
            }
        }
    }
}

@Composable
fun HistoryHeader(
    onClearAll: () -> Unit,
    showClearBtn: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "CALCULATION HISTORY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MinimalistColors.TextSecondary
        )
        if (showClearBtn) {
            Text(
                text = "CLEAR ALL",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MinimalistColors.PaleRedText,
                modifier = Modifier
                    .clickable { onClearAll() }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun EmptyHistoryCard() {
    BentoCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No history items",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MinimalistColors.TextSecondary
            )
            Text(
                text = "Your saved item calculations will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MinimalistColors.TextSecondary
            )
        }
    }
}

@Composable
fun HistoryItemCard(
    item: CalculationResult,
    onDelete: () -> Unit
) {
    BentoCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.itemName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatTime(item.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MinimalistColors.TextSecondary
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Rp ${formatCurrency(item.itemPrice)}",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MinimalistColors.TextPrimary
                    )
                    
                    Text(
                        text = "Remove",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MinimalistColors.PaleRedText,
                        modifier = Modifier
                            .clickable { onDelete() }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            // Results Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusBadge(
                    label = "${formatDecimals(item.workingDaysRequired)} days",
                    bgColor = MinimalistColors.PaleGreenBg,
                    textColor = MinimalistColors.PaleGreenText,
                    modifier = Modifier.weight(1f)
                )
                
                StatusBadge(
                    label = "${formatDecimals(item.workingHoursRequired)} hrs",
                    bgColor = MinimalistColors.PaleBlueBg,
                    textColor = MinimalistColors.PaleBlueText,
                    modifier = Modifier.weight(1f)
                )

                StatusBadge(
                    label = "${formatDecimals(item.percentOfMonthly)}%",
                    bgColor = MinimalistColors.PaleRedBg,
                    textColor = MinimalistColors.PaleRedText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// --- Custom Minimalist UI Widgets ---

@Composable
fun BentoCard(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MinimalistColors.CardBg, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = MinimalistColors.BorderColor, shape = RoundedCornerShape(8.dp))
    ) {
        content()
    }
}

@Composable
fun MinimalistInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MinimalistColors.TextPrimary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { 
                Text(
                    text = placeholder,
                    fontFamily = if (keyboardType == KeyboardType.Number) FontFamily.Monospace else FontFamily.SansSerif,
                    color = MinimalistColors.TextSecondary.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = if (keyboardType == KeyboardType.Number) FontFamily.Monospace else FontFamily.SansSerif
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MinimalistColors.CanvasBg,
                unfocusedContainerColor = MinimalistColors.CanvasBg,
                focusedBorderColor = MinimalistColors.TextPrimary,
                unfocusedBorderColor = MinimalistColors.BorderColor,
                cursorColor = MinimalistColors.TextPrimary
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MinimalistButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Scale on press animation for micro-interaction tactile feedback
    val scale = if (isPressed) 0.98f else 1f

    val containerColor = if (enabled) MinimalistColors.TextPrimary else MinimalistColors.BorderColor
    val contentColor = if (enabled) Color.White else MinimalistColors.TextSecondary

    Box(
        modifier = modifier
            .scale(scale)
            .height(48.dp)
            .background(color = containerColor, shape = RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No default ripple to keep the flat editorial look
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun StatusBadge(
    label: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(9999.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

// --- Data Formatting Helpers ---

fun formatNumberPlain(value: Double): String {
    if (value == 0.0) return ""
    return if (value % 1.0 == 0.0) {
        value.toLong().toString()
    } else {
        value.toString()
    }
}

fun formatCurrency(value: Double): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(value).replace(',', '.')
}

fun formatDecimals(value: Double): String {
    val df = DecimalFormat("#.##")
    return df.format(value)
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
