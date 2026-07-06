package com.example.components

import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

/**
 * 1. Gradient Circular Progress Ring with glowing neon edges, rounded stroke caps,
 * and a percentage in the center.
 */
@Composable
fun GradientCircularProgress(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = Color(0x12FFFFFF),
    startColor: Color = Color(0xFF00F2FE),
    endColor: Color = Color(0xFF4FACFE),
    showPercentage: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "CircularProgress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val minSize = min(width, height)
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (minSize - strokeWidthPx) / 2
            val center = Offset(width / 2, height / 2)

            // Draw track background
            drawCircle(
                color = trackColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidthPx)
            )

            val sweepAngle = animatedProgress * 360f

            // Multi-layered soft glow effect
            val glowCount = 4
            for (i in 1..glowCount) {
                val glowWidth = strokeWidthPx + (i * 4.dp.toPx())
                val glowAlpha = 0.12f / i
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(startColor, endColor, startColor),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = glowWidth, cap = StrokeCap.Round),
                    alpha = glowAlpha
                )
            }

            // Draw active progress ring
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(startColor, endColor, startColor),
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }

        if (showPercentage) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${(progress * 100).roundToInt()}%",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "COMPLETED",
                    color = Color.Gray,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

/**
 * 2. Apple Fitness-style Triple Activity Rings using vibrant gradient colors
 * with smooth spacing and subtle shadows.
 */
@Composable
fun TripleActivityRings(
    progressMove: Float,      // Outer Ring (Move - Pink/Magenta)
    progressExercise: Float,  // Middle Ring (Exercise - Neon Green/Lime)
    progressStand: Float,     // Inner Ring (Stand - Cyan/Deep Blue)
    modifier: Modifier = Modifier,
    ringWidth: Dp = 10.dp,
    spacing: Dp = 5.dp
) {
    val animatedMove by animateFloatAsState(targetValue = progressMove, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "MoveRing")
    val animatedExercise by animateFloatAsState(targetValue = progressExercise, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "ExerciseRing")
    val animatedStand by animateFloatAsState(targetValue = progressStand, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "StandRing")

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val minSize = min(width, height)
            val ringWidthPx = ringWidth.toPx()
            val spacingPx = spacing.toPx()
            val center = Offset(width / 2, height / 2)

            // Outer ring: Move
            val outerRadius = (minSize - ringWidthPx) / 2
            drawRing(
                center = center,
                radius = outerRadius,
                progress = animatedMove,
                ringWidthPx = ringWidthPx,
                trackColor = Color(0x1AFF1744),
                startColor = Color(0xFFFF1744),
                endColor = Color(0xFFFF8A00)
            )

            // Middle ring: Exercise
            val middleRadius = outerRadius - ringWidthPx - spacingPx
            drawRing(
                center = center,
                radius = middleRadius,
                progress = animatedExercise,
                ringWidthPx = ringWidthPx,
                trackColor = Color(0x1A00E676),
                startColor = Color(0xFF00E676),
                endColor = Color(0xFFB2FF59)
            )

            // Inner ring: Stand
            val innerRadius = middleRadius - ringWidthPx - spacingPx
            drawRing(
                center = center,
                radius = innerRadius,
                progress = animatedStand,
                ringWidthPx = ringWidthPx,
                trackColor = Color(0x1A00B0FF),
                startColor = Color(0xFF00B0FF),
                endColor = Color(0xFF00E5FF)
            )
        }
    }
}

private fun DrawScope.drawRing(
    center: Offset,
    radius: Float,
    progress: Float,
    ringWidthPx: Float,
    trackColor: Color,
    startColor: Color,
    endColor: Color
) {
    // 1. Draw background track
    drawCircle(
        color = trackColor,
        radius = radius,
        center = center,
        style = Stroke(width = ringWidthPx)
    )

    val sweepAngle = progress * 360f

    // 2. Multi-layered glow for each ring
    val glowCount = 2
    for (i in 1..glowCount) {
        val glowAlpha = 0.08f / i
        val glowWidth = ringWidthPx + (i * 4.dp.toPx())
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(startColor, endColor, startColor),
                center = center
            ),
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = glowWidth, cap = StrokeCap.Round),
            alpha = glowAlpha
        )
    }

    // 3. Draw active arc
    drawArc(
        brush = Brush.sweepGradient(
            colors = listOf(startColor, endColor, startColor),
            center = center
        ),
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = ringWidthPx, cap = StrokeCap.Round)
    )

    // 4. Overlap shadow to simulate a 3D overlay depth if ring overlaps
    if (progress > 1f) {
        val endAngleRad = Math.toRadians((sweepAngle - 90f).toDouble())
        val tipX = center.x + radius * cos(endAngleRad).toFloat()
        val tipY = center.y + radius * sin(endAngleRad).toFloat()
        
        drawCircle(
            color = Color.Black.copy(alpha = 0.4f),
            radius = ringWidthPx / 2f + 2.dp.toPx(),
            center = Offset(center.x, center.y - radius)
        )
    }
}

/**
 * 3. Speedometer/Gauge Progress Indicator with a semi-circular arc,
 * colorful gradient scale, animated needle, and numeric progress value.
 */
@Composable
fun SpeedometerGauge(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    unit: String = "km/h",
    maxValue: Int = 220,
    startColor: Color = Color(0xFF00E676), // Green
    midColor: Color = Color(0xFFFFEB3B),   // Yellow
    endColor: Color = Color(0xFFFF1744)    // Red
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "Speedometer"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val minSize = min(width, height)
            val center = Offset(width / 2, height / 2)
            val startAngle = 150f
            val sweepAngle = 240f
            val maxRadius = minSize / 2f

            // 1. Draw outer gradient arc background (muted)
            val strokeWidthPx = 4.dp.toPx()
            val arcRadius = maxRadius - 16.dp.toPx()
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(startColor, midColor, endColor, endColor),
                    center = center
                ),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
                size = Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                alpha = 0.15f
            )

            // Draw active portion of the outer gradient arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(startColor, midColor, endColor),
                    center = center
                ),
                startAngle = startAngle,
                sweepAngle = animatedProgress * sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
                size = Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            // 2. Draw gauge scale ticks
            val totalTicks = 31
            val tickRadiusOuter = arcRadius - 8.dp.toPx()
            
            for (i in 0 until totalTicks) {
                val tickFraction = i.toFloat() / (totalTicks - 1)
                val tickAngle = startAngle + tickFraction * sweepAngle
                val angleRad = Math.toRadians(tickAngle.toDouble())
                val cosA = cos(angleRad).toFloat()
                val sinA = sin(angleRad).toFloat()

                val isActive = tickFraction <= animatedProgress
                val isMajor = i % 5 == 0

                val tickLength = if (isMajor) 10.dp.toPx() else 5.dp.toPx()
                val currentInnerRadius = tickRadiusOuter - tickLength

                // Tick color lerped along the active scale
                val tickColor = if (isActive) {
                    when {
                        tickFraction < 0.5f -> lerp(startColor, midColor, tickFraction * 2f)
                        else -> lerp(midColor, endColor, (tickFraction - 0.5f) * 2f)
                    }
                } else {
                    Color(0x22FFFFFF)
                }

                val startPoint = Offset(center.x + tickRadiusOuter * cosA, center.y + tickRadiusOuter * sinA)
                val endPoint = Offset(center.x + currentInnerRadius * cosA, center.y + currentInnerRadius * sinA)

                drawLine(
                    color = tickColor,
                    start = startPoint,
                    end = endPoint,
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.2.dp.toPx()
                )
            }

            // 3. Draw needle
            val needleAngle = startAngle + animatedProgress * sweepAngle
            val needleAngleRad = Math.toRadians(needleAngle.toDouble())
            val cosN = cos(needleAngleRad).toFloat()
            val sinN = sin(needleAngleRad).toFloat()
            
            val needleLength = arcRadius - 10.dp.toPx()
            val needleEndPoint = Offset(center.x + needleLength * cosN, center.y + needleLength * sinN)
            
            // Draw needle shadow
            val shadowOffset = 3.dp.toPx()
            drawLine(
                color = Color.Black.copy(alpha = 0.5f),
                start = Offset(center.x + shadowOffset, center.y + shadowOffset),
                end = needleEndPoint + Offset(shadowOffset, shadowOffset),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Current speed color for the needle
            val currentNeedleColor = when {
                animatedProgress < 0.5f -> lerp(startColor, midColor, animatedProgress * 2f)
                else -> lerp(midColor, endColor, (animatedProgress - 0.5f) * 2f)
            }

            // Draw glowing needle
            drawLine(
                color = currentNeedleColor,
                start = center,
                end = needleEndPoint,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw center metallic-style pivot
            drawCircle(
                color = Color(0xFF0C101B),
                radius = 16.dp.toPx(),
                center = center
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(currentNeedleColor, Color.Transparent),
                    center = center,
                    radius = 24.dp.toPx()
                ),
                radius = 24.dp.toPx(),
                center = center,
                alpha = 0.3f
            )
            drawCircle(
                color = currentNeedleColor,
                radius = 6.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
                center = center
            )
        }

        // Speed numeric readout
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * maxValue).roundToInt()}",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = unit.uppercase(),
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * 4. Liquid Wave Progress Bar where animated water fills a rounded container
 * with realistic waves, glassmorphism effects, and gradient lighting.
 */
@Composable
fun LiquidWaveProgress(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    containerShape: RoundedCornerShape = RoundedCornerShape(24.dp),
    primaryColor: Color = Color(0xFF0072FF),
    secondaryColor: Color = Color(0xFF00C6FF),
    waveAmplitude: Dp = 8.dp,
    waveFrequency: Float = 1.5f
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "LiquidProgress"
    )

    // Infinite animators for horizontal waving movement (creating parallax waves)
    val infiniteTransition = rememberInfiniteTransition(label = "WavesTransition")
    val waveOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WaveOffset1"
    )

    val waveOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WaveOffset2"
    )

    Box(
        modifier = modifier
            .clip(containerShape)
            .background(Color(0xFF0B0E17))
            .border(
                1.5.dp, 
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.18f), Color.White.copy(alpha = 0.02f))), 
                containerShape
            ),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val waveAmplitudePx = with(density) { waveAmplitude.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Water level height calculation
            val fillHeight = animatedProgress * height
            val waterLevelY = height - fillHeight

            // Back wave path (semi-transparent, shifted offset for depth)
            val pathBack = Path().apply {
                moveTo(0f, height)
                lineTo(0f, waterLevelY)
                
                val waveLength = width * 1.3f
                for (x in 0..width.toInt()) {
                    val angle = (x.toFloat() / waveLength) * 2 * PI.toFloat() * waveFrequency + waveOffset1
                    val y = waterLevelY + sin(angle) * waveAmplitudePx
                    lineTo(x.toFloat(), y)
                }
                
                lineTo(width, height)
                close()
            }

            // Front wave path (solid)
            val pathFront = Path().apply {
                moveTo(0f, height)
                lineTo(0f, waterLevelY)
                
                val waveLength = width * 0.9f
                for (x in 0..width.toInt()) {
                    val angle = (x.toFloat() / waveLength) * 2 * PI.toFloat() * waveFrequency - waveOffset2
                    val y = waterLevelY + cos(angle) * (waveAmplitudePx * 0.75f)
                    lineTo(x.toFloat(), y)
                }
                
                lineTo(width, height)
                close()
            }

            // Draw back wave (Background)
            drawPath(
                path = pathBack,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.45f),
                        primaryColor.copy(alpha = 0.2f)
                    )
                )
            )

            // Draw front wave (Foreground)
            drawPath(
                path = pathFront,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        secondaryColor,
                        primaryColor
                    )
                )
            )

            // Glass specular highlight reflection arc on top
            drawArc(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent)
                ),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                size = Size(width - 8.dp.toPx(), height - 8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Overlay text percentage
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).roundToInt()}%",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(1.5f, 1.5f),
                        blurRadius = 3f
                    )
                )
            )
            Text(
                text = "H2O LEVEL",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )
        }
    }
}

/**
 * 5. Segmented Dashed Progress Ring with evenly spaced rounded segments,
 * active segments glowing while inactive segments remain muted.
 */
@Composable
fun SegmentedDashedProgress(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier,
    totalSegments: Int = 16,
    segmentSpacingAngle: Float = 6f, // Gap in degrees
    strokeWidth: Dp = 10.dp,
    activeColor: Color = Color(0xFFCFFF3E), // Neon Lime/Yellow
    inactiveColor: Color = Color(0x12FFFFFF) // Muted Dark Grey
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "SegmentedProgress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val minSize = min(width, height)
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (minSize - strokeWidthPx) / 2
            val center = Offset(width / 2, height / 2)

            val segmentAngleSweep = (360f / totalSegments) - segmentSpacingAngle

            for (i in 0 until totalSegments) {
                // Align start from top (-90 degrees)
                val startAngle = -90f + i * (360f / totalSegments) + (segmentSpacingAngle / 2f)
                val isActive = (i.toFloat() / totalSegments) < animatedProgress

                val segmentColor = if (isActive) activeColor else inactiveColor

                // Draw base segment
                drawArc(
                    color = segmentColor,
                    startAngle = startAngle,
                    sweepAngle = segmentAngleSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )

                // Draw soft glowing back-layers if segment is active
                if (isActive) {
                    val glowLayers = 3
                    for (g in 1..glowLayers) {
                        val glowWidth = strokeWidthPx + (g * 4.dp.toPx())
                        val glowAlpha = 0.15f / g
                        drawArc(
                            color = activeColor,
                            startAngle = startAngle,
                            sweepAngle = segmentAngleSweep,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = glowWidth, cap = StrokeCap.Round),
                            alpha = glowAlpha
                        )
                    }
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).roundToInt()}%",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "SEGMENTS",
                color = Color.Gray,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}
