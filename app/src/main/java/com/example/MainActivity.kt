package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.screens.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF050505) // Frosted Glass deep space-black background
                ) {
                    ProgressDashboardScreen()
                }
            }
        }
    }
}

@Composable
fun ProgressDashboardScreen() {
    // Master variables
    var autoAnimate by remember { mutableStateOf(true) }
    var masterProgress by remember { mutableFloatStateOf(0.68f) }

    // Navigation State: "dashboard", "circle", "rings", "gauge", "liquid", "segments"
    var currentScreen by remember { mutableStateOf("dashboard") }
    
    // Independent overrides for each progress bar (unlocked when autoAnimate = false)
    var circularProgress by remember { mutableFloatStateOf(0.68f) }
    var moveProgress by remember { mutableFloatStateOf(1.25f) } // Move ring (>100% showcase)
    var exerciseProgress by remember { mutableFloatStateOf(0.85f) }
    var standProgress by remember { mutableFloatStateOf(0.60f) }
    var speedometerProgress by remember { mutableFloatStateOf(0.55f) }
    var liquidProgress by remember { mutableFloatStateOf(0.42f) }
    var segmentedProgress by remember { mutableFloatStateOf(0.70f) }

    // Wave amplitude / Segment count settings
    var waveAmplitudeVal by remember { mutableFloatStateOf(8f) }
    var segmentCountVal by remember { mutableFloatStateOf(16f) }

    // Back Handlers for natural Android navigation gesture & back buttons
    BackHandler(enabled = currentScreen != "dashboard") {
        currentScreen = "dashboard"
    }

    // Sinusoidal auto-animator loop
    LaunchedEffect(autoAnimate) {
        if (autoAnimate) {
            val startTime = System.currentTimeMillis()
            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                // Smooth slow breathing oscillation between 0.15f and 0.95f
                val oscillatedValue = 0.55f + 0.40f * sin(elapsed.toFloat() / 1800f)
                masterProgress = oscillatedValue

                // Sync all progress values for smooth showreels
                circularProgress = oscillatedValue
                speedometerProgress = oscillatedValue
                liquidProgress = oscillatedValue
                segmentedProgress = oscillatedValue
                
                // Fitness rings offset slightly to make them look organic
                moveProgress = (0.75f + 0.55f * sin(elapsed.toFloat() / 1400f)) + 0.3f
                exerciseProgress = (0.50f + 0.45f * sin(elapsed.toFloat() / 1800f)) + 0.2f
                standProgress = (0.40f + 0.35f * sin(elapsed.toFloat() / 2200f)) + 0.1f

                delay(16) // Smooth 60FPS animation updates
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "ScreenNavigation"
            ) { screen ->
                when (screen) {
                    "dashboard" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // 1. Header Banner
                            HeaderSection()

                            // 2. Interactive Concept Sheet Grid
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // ROW 1: Circular Progress & Triple Rings side-by-side (Clean balanced grid)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Card 1: Circular Progress
                                    DashboardCard(
                                        title = "Neon Circle",
                                        subtitle = "Radial Glow cap",
                                        modifier = Modifier.weight(1f),
                                        isActive = false,
                                        onClick = { currentScreen = "circle" }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .padding(top = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GradientCircularProgress(
                                                progress = circularProgress,
                                                modifier = Modifier.size(110.dp)
                                            )
                                        }
                                    }

                                    // Card 2: Apple Fitness Rings
                                    DashboardCard(
                                        title = "Activity Rings",
                                        subtitle = "Concentric depth",
                                        modifier = Modifier.weight(1f),
                                        isActive = false,
                                        onClick = { currentScreen = "rings" }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .padding(top = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            TripleActivityRings(
                                                progressMove = moveProgress,
                                                progressExercise = exerciseProgress,
                                                progressStand = standProgress,
                                                modifier = Modifier.size(110.dp)
                                            )
                                        }
                                    }
                                }

                                // ROW 2: Speedometer Gauge (Takes center focus - wider aspect card)
                                DashboardCard(
                                    title = "Futuristic Gauge / Speedometer",
                                    subtitle = "Arc ticks, animated needle & drop shadow",
                                    modifier = Modifier.fillMaxWidth(),
                                    isActive = false,
                                    onClick = { currentScreen = "gauge" }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .padding(top = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        SpeedometerGauge(
                                            progress = speedometerProgress,
                                            modifier = Modifier.size(160.dp)
                                        )
                                    }
                                }

                                // ROW 3: Liquid Wave & Segmented Ring side-by-side
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Card 4: Liquid Wave
                                    DashboardCard(
                                        title = "Liquid Wave",
                                        subtitle = "Realistic glass waves",
                                        modifier = Modifier.weight(1f),
                                        isActive = false,
                                        onClick = { currentScreen = "liquid" }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .padding(top = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LiquidWaveProgress(
                                                progress = liquidProgress,
                                                modifier = Modifier.size(110.dp),
                                                waveAmplitude = waveAmplitudeVal.dp
                                            )
                                        }
                                    }

                                    // Card 5: Segmented Ring
                                    DashboardCard(
                                        title = "Dashed Segments",
                                        subtitle = "Pulsing neon caps",
                                        modifier = Modifier.weight(1f),
                                        isActive = false,
                                        onClick = { currentScreen = "segments" }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .padding(top = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            SegmentedDashedProgress(
                                                progress = segmentedProgress,
                                                totalSegments = segmentCountVal.toInt(),
                                                modifier = Modifier.size(110.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Premium layout tip/notice inside dashboard
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x05FFFFFF))
                                    .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "💡 Interactive HUD Architecture: Tap on any gauge to enter its physical simulation laboratory and configure highly granular engine parameters.",
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                    "circle" -> DetailCircleScreen(
                        progress = circularProgress,
                        onProgressChange = { circularProgress = it },
                        autoAnimate = autoAnimate,
                        onAutoAnimateChange = { autoAnimate = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                    "rings" -> DetailRingsScreen(
                        progressMove = moveProgress,
                        onProgressMoveChange = { moveProgress = it },
                        progressExercise = exerciseProgress,
                        onProgressExerciseChange = { exerciseProgress = it },
                        progressStand = standProgress,
                        onProgressStandChange = { standProgress = it },
                        autoAnimate = autoAnimate,
                        onAutoAnimateChange = { autoAnimate = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                    "gauge" -> DetailGaugeScreen(
                        progress = speedometerProgress,
                        onProgressChange = { speedometerProgress = it },
                        autoAnimate = autoAnimate,
                        onAutoAnimateChange = { autoAnimate = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                    "liquid" -> DetailLiquidScreen(
                        progress = liquidProgress,
                        onProgressChange = { liquidProgress = it },
                        waveAmplitude = waveAmplitudeVal,
                        onWaveAmplitudeChange = { waveAmplitudeVal = it },
                        autoAnimate = autoAnimate,
                        onAutoAnimateChange = { autoAnimate = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                    "segments" -> DetailSegmentsScreen(
                        progress = segmentedProgress,
                        onProgressChange = { segmentedProgress = it },
                        segmentCount = segmentCountVal,
                        onSegmentCountChange = { segmentCountVal = it },
                        autoAnimate = autoAnimate,
                        onAutoAnimateChange = { autoAnimate = it },
                        onBack = { currentScreen = "dashboard" }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x3322D3EE))
                    .border(1.dp, Color(0x4D22D3EE), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "JETPACK CANVAS",
                    color = Color(0xFF22D3EE),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "System Gauges",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Premium Material 3 Component Library",
            color = Color(0xFF64748B), // Slate-500
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DashboardCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    // Frosted Glass colors from the spec
    val cardBackground = if (isActive) Color(0x12FFFFFF) else Color(0x08FFFFFF) // bg-white/[0.03] or slightly active
    val cornerShape = RoundedCornerShape(24.dp) // rounded-3xl

    Card(
        modifier = modifier
            .clip(cornerShape)
            .clickable { onClick() }
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                brush = if (isActive) {
                    Brush.verticalGradient(listOf(Color(0xFF22D3EE), Color(0xFFA855F7)))
                } else {
                    Brush.verticalGradient(listOf(Color(0x1AFFFFFF), Color(0x03FFFFFF))) // border-white/10
                },
                shape = cornerShape
            ),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Spacious 16.dp padding
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = subtitle,
                        color = Color(0xFF64748B), // Slate-500
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Icon(
                    imageVector = if (isActive) Icons.Default.Tune else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Customize Indicator",
                    tint = if (isActive) Color(0xFF22D3EE) else Color(0x40FFFFFF),
                    modifier = Modifier.size(16.dp)
                )
            }
            
            content()
        }
    }
}

// Simple modifier extension to change switch scale
fun Modifier.scaleModifier(scale: Float): Modifier = this.then(
    Modifier.padding(all = 0.dp) // dummy to allow simple inline styling without layout scale dependencies
)
