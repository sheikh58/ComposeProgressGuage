package com.example.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.*
import com.example.scaleModifier
import kotlin.math.roundToInt

@Composable
fun DetailHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High-tech frosted back button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x15FFFFFF))
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                .clickable { onBack() }
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Gauges Dashboard",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = title.uppercase(),
                color = Color(0xFF22D3EE),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LabControlCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(Color(0x1AFFFFFF), Color(0x03FFFFFF))),
                shape = RoundedCornerShape(24.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x0AFFFFFF)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Configuration",
                    tint = Color(0xFF22D3EE),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title.uppercase(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun LabAnimateToggle(
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x08FFFFFF))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Dynamic Live Render",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (autoAnimate) "Oscillating automatically at 60 FPS" else "Sliders unlocked for custom laboratory tuning",
                color = Color(0xFF64748B),
                fontSize = 10.sp
            )
        }
        Switch(
            checked = autoAnimate,
            onCheckedChange = onAutoAnimateChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF22D3EE),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0x22FFFFFF)
            )
        )
    }
}

@Composable
fun DetailSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    accentColor: Color = Color(0xFF22D3EE),
    displayValue: String = "${(value * 100).toInt()}%"
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = label,
                color = if (enabled) Color.LightGray else Color.DarkGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = displayValue,
                color = if (enabled) accentColor else Color.DarkGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = Color(0x10FFFFFF),
                disabledThumbColor = Color.DarkGray,
                disabledActiveTrackColor = Color(0x05FFFFFF)
            )
        )
    }
}

/* ==========================================
   1. NEON CIRCLE DETAIL LAB SCREEN
   ========================================== */
@Composable
fun DetailCircleScreen(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    // Custom settings unique to this screen
    var strokeThickness by remember { mutableFloatStateOf(12f) }
    var selectedTheme by remember { mutableIntStateOf(0) } // 0 = Cyan, 1 = Violet, 2 = Cyber Emerald
    var showPercent by remember { mutableStateOf(true) }

    val startColor = when (selectedTheme) {
        1 -> Color(0xFF8A2387)
        2 -> Color(0xFF00FF87)
        else -> Color(0xFF00F2FE)
    }
    val endColor = when (selectedTheme) {
        1 -> Color(0xFFE94057)
        2 -> Color(0xFF60EFFF)
        else -> Color(0xFF4FACFE)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(
            title = "Neon Circle",
            subtitle = "Custom Radial Glow Labs",
            onBack = onBack
        )

        // Giant Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(Color(0x15FFFFFF), Color(0x02FFFFFF))),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0x06FFFFFF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                // Giant 220.dp Progress Ring
                GradientCircularProgress(
                    progress = progress,
                    modifier = Modifier.size(220.dp),
                    strokeWidth = strokeThickness.dp,
                    startColor = startColor,
                    endColor = endColor,
                    showPercentage = showPercent
                )
            }
        }

        // Custom parameters configuration card
        LabControlCard(title = "Indicator Configuration") {
            LabAnimateToggle(autoAnimate = autoAnimate, onAutoAnimateChange = onAutoAnimateChange)
            
            Spacer(modifier = Modifier.height(16.dp))

            DetailSlider(
                label = "Core Ring Progress Level",
                value = progress,
                onValueChange = onProgressChange,
                enabled = !autoAnimate,
                accentColor = startColor
            )

            DetailSlider(
                label = "Neon Ring Thickness",
                value = (strokeThickness - 6f) / 18f, // scale 6dp to 24dp
                onValueChange = { strokeThickness = 6f + it * 18f },
                accentColor = startColor,
                displayValue = "${strokeThickness.toInt()} dp"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Palette Theme Swapper
            Text(
                text = "NEON PALETTE CHROMATIC FOCUS",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("CYAN CORE", "VIOLET ECLIPSE", "EMERALD CYBER").forEachIndexed { index, name ->
                    val isActive = selectedTheme == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0x20FFFFFF) else Color(0x05FFFFFF))
                            .border(1.dp, if (isActive) startColor else Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { selectedTheme = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Percentage display toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x05FFFFFF))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Completed Readout Text",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = showPercent,
                    onCheckedChange = { showPercent = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = startColor
                    )
                )
            }
        }
    }
}

/* ==========================================
   2. ACTIVITY RINGS DETAIL LAB SCREEN
   ========================================== */
@Composable
fun DetailRingsScreen(
    progressMove: Float,
    onProgressMoveChange: (Float) -> Unit,
    progressExercise: Float,
    onProgressExerciseChange: (Float) -> Unit,
    progressStand: Float,
    onProgressStandChange: (Float) -> Unit,
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    var widthVal by remember { mutableFloatStateOf(10f) }
    var spacingVal by remember { mutableFloatStateOf(5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(
            title = "Activity Rings",
            subtitle = "Concentric Depth Calibration",
            onBack = onBack
        )

        // Giant Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(Color(0x15FFFFFF), Color(0x02FFFFFF))),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0x06FFFFFF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TripleActivityRings(
                        progressMove = progressMove,
                        progressExercise = progressExercise,
                        progressStand = progressStand,
                        modifier = Modifier.fillMaxSize(),
                        ringWidth = widthVal.dp,
                        spacing = spacingVal.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fitness Stats HUD readout
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x08FFFFFF))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("MOVE", color = Color(0xFFFF1744), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${(progressMove * 500).toInt()} / 500", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("KCAL", color = Color.Gray, fontSize = 8.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("EXERCISE", color = Color(0xFF00E676), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${(progressExercise * 30).toInt()} / 30", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("MIN", color = Color.Gray, fontSize = 8.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("STAND", color = Color(0xFF00B0FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${(progressStand * 12).toInt()} / 12", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("HR", color = Color.Gray, fontSize = 8.sp)
                    }
                }
            }
        }

        // Custom parameters configuration card
        LabControlCard(title = "Rings Configuration") {
            LabAnimateToggle(autoAnimate = autoAnimate, onAutoAnimateChange = onAutoAnimateChange)
            
            Spacer(modifier = Modifier.height(16.dp))

            DetailSlider(
                label = "Move Ring (Outer Pink)",
                value = progressMove / 1.5f,
                onValueChange = { onProgressMoveChange(it * 1.5f) },
                enabled = !autoAnimate,
                accentColor = Color(0xFFFF1744),
                displayValue = "${(progressMove * 100).toInt()}%"
            )

            DetailSlider(
                label = "Exercise Ring (Middle Green)",
                value = progressExercise / 1.5f,
                onValueChange = { onProgressExerciseChange(it * 1.5f) },
                enabled = !autoAnimate,
                accentColor = Color(0xFF00E676),
                displayValue = "${(progressExercise * 100).toInt()}%"
            )

            DetailSlider(
                label = "Stand Ring (Inner Blue)",
                value = progressStand / 1.5f,
                onValueChange = { onProgressStandChange(it * 1.5f) },
                enabled = !autoAnimate,
                accentColor = Color(0xFF00B0FF),
                displayValue = "${(progressStand * 100).toInt()}%"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Spacing / Width controls
            DetailSlider(
                label = "Ring Path Width",
                value = (widthVal - 6f) / 10f, // 6dp to 16dp
                onValueChange = { widthVal = 6f + it * 10f },
                accentColor = Color.White,
                displayValue = "${widthVal.toInt()} dp"
            )

            DetailSlider(
                label = "Concentric Ring Spacing",
                value = (spacingVal - 2f) / 10f, // 2dp to 12dp
                onValueChange = { spacingVal = 2f + it * 10f },
                accentColor = Color.LightGray,
                displayValue = "${spacingVal.toInt()} dp"
            )
        }
    }
}

/* ==========================================
   3. SPEEDOMETER GAUGE DETAIL LAB SCREEN
   ========================================== */
@Composable
fun DetailGaugeScreen(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    var selectedUnit by remember { mutableStateOf("km/h") }
    var selectedMaxSpeed by remember { mutableIntStateOf(220) }
    var scaleRedLine by remember { mutableFloatStateOf(0.85f) } // Redline limit percentage

    val currentSpeedVal = (progress * selectedMaxSpeed).roundToInt()
    val isRedline = progress >= scaleRedLine

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(
            title = "Speedometer Gauge",
            subtitle = "Dynamic Mechanical Cockpit",
            onBack = onBack
        )

        // Giant Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(Color(0x15FFFFFF), Color(0x02FFFFFF))),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0x06FFFFFF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Giant Speedometer with mechanical spring needle
                    SpeedometerGauge(
                        progress = progress,
                        modifier = Modifier.fillMaxSize(),
                        unit = selectedUnit,
                        maxValue = selectedMaxSpeed,
                        startColor = Color(0xFF00FF87),
                        midColor = Color(0xFFFFEB3B),
                        endColor = Color(0xFFFF1744)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // High warning HUD panel
                AnimatedVisibility(visible = isRedline) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x22FF1744))
                            .border(1.dp, Color(0xAAFF1744), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "⚠ GAUGE LIMIT WARNING - OVERHEAT EXCEEDED",
                            color = Color(0xFFFF1744),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Custom parameters configuration card
        LabControlCard(title = "Gauge Configuration") {
            LabAnimateToggle(autoAnimate = autoAnimate, onAutoAnimateChange = onAutoAnimateChange)
            
            Spacer(modifier = Modifier.height(16.dp))

            DetailSlider(
                label = "Mechanical Velocity Needle",
                value = progress,
                onValueChange = onProgressChange,
                enabled = !autoAnimate,
                accentColor = if (isRedline) Color(0xFFFF1744) else Color(0xFF00FF87),
                displayValue = "$currentSpeedVal $selectedUnit"
            )

            DetailSlider(
                label = "Alert Safety Redline Threshold",
                value = scaleRedLine,
                onValueChange = { scaleRedLine = it },
                accentColor = Color(0xFFFFEB3B),
                displayValue = "${(scaleRedLine * 100).toInt()}%"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Unit configuration row
            Text(
                text = "SPEEDOMETER SYSTEM UNIT",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("km/h", "mph", "rpm", "% load").forEach { unit ->
                    val isActive = selectedUnit == unit
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0x20FFFFFF) else Color(0x05FFFFFF))
                            .border(1.dp, if (isActive) Color(0xFFFFEB3B) else Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                            .clickable {
                                selectedUnit = unit
                                // Adjust max values depending on physical unit chosen
                                selectedMaxSpeed = when (unit) {
                                    "mph" -> 140
                                    "rpm" -> 8000
                                    "% load" -> 100
                                    else -> 220
                                }
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unit.uppercase(),
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gauge limit readout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Active Max Readout Limit:",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = "$selectedMaxSpeed ${selectedUnit.uppercase()}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/* ==========================================
   4. LIQUID WAVE PROGRESS DETAIL LAB SCREEN
   ========================================== */
@Composable
fun DetailLiquidScreen(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    waveAmplitude: Float,
    onWaveAmplitudeChange: (Float) -> Unit,
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    var selectedShape by remember { mutableIntStateOf(0) } // 0 = Rounded Box, 1 = Circle, 2 = Cyber Rectangle
    var frequencyVal by remember { mutableFloatStateOf(1.5f) }
    var fluidTheme by remember { mutableIntStateOf(0) } // 0 = Classic Blue, 1 = Toxic Sludge, 2 = Gold Oil

    val containerShape = when (selectedShape) {
        1 -> RoundedCornerShape(110.dp) // Perfect circle inside size 220.dp
        2 -> RoundedCornerShape(4.dp)   // Cyber rigid edges
        else -> RoundedCornerShape(32.dp) // Large modern rounded box
    }

    val primaryColor = when (fluidTheme) {
        1 -> Color(0xFF00FF87)
        2 -> Color(0xFFF2C94C)
        else -> Color(0xFF0072FF)
    }

    val secondaryColor = when (fluidTheme) {
        1 -> Color(0xFF38EF7D)
        2 -> Color(0xFFF2994A)
        else -> Color(0xFF00C6FF)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(
            title = "Liquid Wave",
            subtitle = "Parallax Fluidic Simulations",
            onBack = onBack
        )

        // Giant Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(Color(0x15FFFFFF), Color(0x02FFFFFF))),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0x06FFFFFF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                LiquidWaveProgress(
                    progress = progress,
                    modifier = Modifier.size(220.dp),
                    containerShape = containerShape,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    waveAmplitude = waveAmplitude.dp,
                    waveFrequency = frequencyVal
                )
            }
        }

        // Custom parameters configuration card
        LabControlCard(title = "Fluidic Wave Simulator Settings") {
            LabAnimateToggle(autoAnimate = autoAnimate, onAutoAnimateChange = onAutoAnimateChange)
            
            Spacer(modifier = Modifier.height(16.dp))

            DetailSlider(
                label = "Water Line Level Fill",
                value = progress,
                onValueChange = onProgressChange,
                enabled = !autoAnimate,
                accentColor = secondaryColor
            )

            DetailSlider(
                label = "Wave Height Amplitude",
                value = waveAmplitude / 25f, // range 0dp to 25dp
                onValueChange = { onWaveAmplitudeChange(it * 25f) },
                accentColor = secondaryColor,
                displayValue = "${waveAmplitude.toInt()} dp"
            )

            DetailSlider(
                label = "Sinusoidal Wave Frequency",
                value = (frequencyVal - 0.5f) / 2.5f, // range 0.5f to 3.0f
                onValueChange = { frequencyVal = 0.5f + it * 2.5f },
                accentColor = secondaryColor,
                displayValue = String.format("%.2f Hz", frequencyVal)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Capsule/Shape Selector
            Text(
                text = "GLASS CONTAINER PROFILE",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("CAPSULE", "CIRCLE RETAIN", "RIGID HEX").forEachIndexed { index, name ->
                    val isActive = selectedShape == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0x20FFFFFF) else Color(0x05FFFFFF))
                            .border(1.dp, if (isActive) secondaryColor else Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { selectedShape = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fluid Color selectors
            Text(
                text = "FLUID CHEMICAL COLOR THEME",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("CLASSIC OCEAN", "BIO TOXIN", "GOLDEN SLICK").forEachIndexed { index, name ->
                    val isActive = fluidTheme == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0x20FFFFFF) else Color(0x05FFFFFF))
                            .border(1.dp, if (isActive) secondaryColor else Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { fluidTheme = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/* ==========================================
   5. DASHED SEGMENTS DETAIL LAB SCREEN
   ========================================== */
@Composable
fun DetailSegmentsScreen(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    segmentCount: Float,
    onSegmentCountChange: (Float) -> Unit,
    autoAnimate: Boolean,
    onAutoAnimateChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    var gapAngleVal by remember { mutableFloatStateOf(6f) }
    var segmentTheme by remember { mutableIntStateOf(0) } // 0 = Lime, 1 = Plasma Cyan, 2 = Punk Pink
    var segmentWidthVal by remember { mutableFloatStateOf(10f) }

    val activeColor = when (segmentTheme) {
        1 -> Color(0xFF00F5FF)
        2 -> Color(0xFFFF007F)
        else -> Color(0xFFCFFF3E)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(
            title = "Dashed Segments",
            subtitle = "Pulsing Discretized Arrays",
            onBack = onBack
        )

        // Giant Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(Color(0x15FFFFFF), Color(0x02FFFFFF))),
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0x06FFFFFF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                SegmentedDashedProgress(
                    progress = progress,
                    modifier = Modifier.size(220.dp),
                    totalSegments = segmentCount.toInt(),
                    segmentSpacingAngle = gapAngleVal,
                    strokeWidth = segmentWidthVal.dp,
                    activeColor = activeColor
                )
            }
        }

        // Custom parameters configuration card
        LabControlCard(title = "Dashed Ring Segment Settings") {
            LabAnimateToggle(autoAnimate = autoAnimate, onAutoAnimateChange = onAutoAnimateChange)
            
            Spacer(modifier = Modifier.height(16.dp))

            DetailSlider(
                label = "Discretized Progress Level",
                value = progress,
                onValueChange = onProgressChange,
                enabled = !autoAnimate,
                accentColor = activeColor
            )

            DetailSlider(
                label = "Total Segments Count",
                value = (segmentCount - 8f) / 32f, // range 8 to 40
                onValueChange = { onSegmentCountChange(8f + it * 32f) },
                accentColor = activeColor,
                displayValue = "${segmentCount.toInt()} cells"
            )

            DetailSlider(
                label = "Cell Angle Spacing Gap",
                value = (gapAngleVal - 2f) / 13f, // range 2 to 15 degrees
                onValueChange = { gapAngleVal = 2f + it * 13f },
                accentColor = activeColor,
                displayValue = String.format("%.1f° gap", gapAngleVal)
            )

            DetailSlider(
                label = "Dashed Segment Stroke Size",
                value = (segmentWidthVal - 6f) / 14f, // range 6dp to 20dp
                onValueChange = { segmentWidthVal = 6f + it * 14f },
                accentColor = activeColor,
                displayValue = "${segmentWidthVal.toInt()} dp"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Color palette themes
            Text(
                text = "ARRAY CELL CHROMATIC ENERGY",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("NEON LIME", "PLASMA CYAN", "PUNK PINK").forEachIndexed { index, name ->
                    val isActive = segmentTheme == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0x20FFFFFF) else Color(0x05FFFFFF))
                            .border(1.dp, if (isActive) activeColor else Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { segmentTheme = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = if (isActive) Color.White else Color.Gray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
