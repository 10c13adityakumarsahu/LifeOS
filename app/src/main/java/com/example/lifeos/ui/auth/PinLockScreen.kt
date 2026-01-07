package com.example.lifeos.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PinLockScreen(
    isSettingUp: Boolean, // True if creating a new PIN
    onPinCorrect: () -> Unit,
    onPinSet: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    
    // Shake Animation State
    val shakeOffset = remember { Animatable(0f) }
    
    // Entrance Animations
    val contentAlpha = remember { Animatable(0f) }
    val contentScale = remember { Animatable(0.95f) }
    
    LaunchedEffect(Unit) {
        launch {
            contentAlpha.animateTo(1f, tween(800, easing = LinearOutSlowInEasing))
        }
        launch {
            contentScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
        }
    }

    // For Setup
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(if (isSettingUp) 1 else 0) }

    val title = when {
        isSettingUp && step == 1 -> "Create Your PIN"
        isSettingUp && step == 2 -> "Confirm Your PIN"
        else -> "Welcome Back"
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
            .alpha(contentAlpha.value)
            .scale(contentScale.value)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = shakeOffset.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated LifeOS Banner
            val infiniteTransition = rememberInfiniteTransition()
            val bannerPulse by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            
            val glowAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.7f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(bannerPulse)
            ) {
                Text(
                    "LifeOS", 
                    style = MaterialTheme.typography.displayMedium, 
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alpha(contentAlpha.value)
                )
                // Subtle glow effect
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha), Color.Transparent)
                            )
                        )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    title, 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    letterSpacing = 2.sp
                )
            }
            
            Text(
                if (isSettingUp) "Secure your personal data" else "Ecosystem of Efficiency",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // PIN Dots with Animation
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isActive = index < pin.length
                    val scale by animateFloatAsState(
                        targetValue = if (isActive) 1.25f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                    )
                    val color by animateColorAsState(
                        targetValue = if (isActive) MaterialTheme.colorScheme.primary 
                                     else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        animationSpec = tween(300)
                    )

                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (!isActive) Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                                else Modifier
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            AnimatedVisibility(
                visible = error.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        error, 
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Modern Keypad
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("CLR", "0", "DEL")
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                rows.forEachIndexed { rowIndex, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.offset(y = (20 * rowIndex).dp).alpha(contentAlpha.value) // Very subtle layered entrance feel if we animated this, but keeping it simple for now
                    ) {
                        row.forEach { key ->
                            KeypadButton(
                                text = key,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    handleKeyPress(
                                        key = key,
                                        currentPin = pin,
                                        isSettingUp = isSettingUp,
                                        step = step,
                                        confirmPin = confirmPin,
                                        onPinUpdate = { pin = it },
                                        onError = { msg ->
                                            error = msg
                                            scope.launch {
                                                repeat(4) {
                                                    shakeOffset.animateTo(12f, tween(60))
                                                    shakeOffset.animateTo(-12f, tween(60))
                                                }
                                                shakeOffset.animateTo(0f, tween(60))
                                                delay(2500)
                                                error = ""
                                            }
                                        },
                                        onStepUpdate = { step = it },
                                        onConfirmPinUpdate = { confirmPin = it },
                                        onComplete = { finalPin ->
                                            if (isSettingUp) {
                                                onPinSet(finalPin)
                                            } else {
                                                onPinSet(finalPin)
                                                // Clear if parent doesn't navigate away
                                                scope.launch {
                                                    delay(200)
                                                    pin = ""
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(76.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (text == "CLR" || text == "DEL") Color.Transparent
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material.ripple.rememberRipple(bounded = false, radius = 38.dp),
                onClick = { 
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (text == "DEL") {
            Icon(
                Icons.Filled.Backspace, 
                contentDescription = "Delete", 
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Text(
                text = text,
                style = if (text == "CLR") MaterialTheme.typography.labelLarge else MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (text == "CLR") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                letterSpacing = if (text == "CLR") 1.sp else 0.sp
            )
        }
    }
}

private fun handleKeyPress(
    key: String,
    currentPin: String,
    isSettingUp: Boolean,
    step: Int,
    confirmPin: String,
    onPinUpdate: (String) -> Unit,
    onError: (String) -> Unit,
    onStepUpdate: (Int) -> Unit,
    onConfirmPinUpdate: (String) -> Unit,
    onComplete: (String) -> Unit
) {
    val MAX_PIN_LENGTH = 4
    when (key) {
        "DEL" -> {
            if (currentPin.isNotEmpty()) onPinUpdate(currentPin.dropLast(1))
        }
        "CLR" -> {
            onPinUpdate("")
        }
        else -> {
            if (currentPin.length < MAX_PIN_LENGTH) {
                val newPin = currentPin + key
                onPinUpdate(newPin)
                
                if (newPin.length == MAX_PIN_LENGTH) {
                    if (isSettingUp) {
                        if (step == 1) {
                            onConfirmPinUpdate(newPin)
                            onPinUpdate("")
                            onStepUpdate(2)
                        } else if (step == 2) {
                            if (newPin == confirmPin) {
                                onComplete(newPin)
                            } else {
                                onError("PINs do not match. Start over.")
                                onPinUpdate("")
                                onStepUpdate(1)
                            }
                        }
                    } else {
                        onComplete(newPin)
                    }
                }
            }
        }
    }
}
