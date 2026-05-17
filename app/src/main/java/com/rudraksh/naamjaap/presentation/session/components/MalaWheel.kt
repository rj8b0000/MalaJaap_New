package com.rudraksh.naamjaap.presentation.session.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun MalaWheel(
    activeBeadIndex: Int,
    direction: Int,
    isPaused: Boolean,
    onSwipeForward: () -> Unit,
    onSwipeBackward: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    
    // Drag accumulation in pixels
    val dragOffset = remember { Animatable(0f) }
    val isAnimating = remember { mutableStateOf(false) }
    
    val dragThreshold = 100f // Explicit swipe threshold (ignores micro-touches/taps)
    val beadSpacingPx = 250f

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isPaused) {
                if (isPaused) return@pointerInput
                
                detectVerticalDragGestures(
                    onDragStart = { },
                    onDragEnd = {
                        // Snap back to 0 if the swipe was too short (didn't cross threshold)
                        if (isAnimating.value) return@detectVerticalDragGestures
                        coroutineScope.launch {
                            dragOffset.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(stiffness = 500f)
                            )
                        }
                    }
                ) { change, dragAmount ->
                    if (isAnimating.value) {
                        // Input Locked: ignore all gestures while snap animation is running
                        change.consume()
                        return@detectVerticalDragGestures
                    }
                    
                    change.consume()
                    coroutineScope.launch {
                        val newOffset = dragOffset.value + dragAmount
                        dragOffset.snapTo(newOffset)
                        
                        if (newOffset <= -dragThreshold) { 
                            // Swipe Up -> Forward
                            isAnimating.value = true
                            // Complete the physical animation to exactly one bead distance
                            dragOffset.animateTo(-beadSpacingPx, spring(stiffness = 400f))
                            // Fire exact 1 increment
                            onSwipeForward()
                            // Instantly reset visual offset as the logical index shifts
                            dragOffset.snapTo(0f)
                            isAnimating.value = false
                            
                        } else if (newOffset >= dragThreshold) { 
                            // Swipe Down -> Backward
                            isAnimating.value = true
                            dragOffset.animateTo(beadSpacingPx, spring(stiffness = 400f))
                            onSwipeBackward()
                            dragOffset.snapTo(0f)
                            isAnimating.value = false
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Render visible beads (virtualized range)
        for (i in -3..3) {
            // Swipe Up brings beads up. Bottom beads come to center.
            // i=1 (bottom) is the NEXT bead forward.
            val logicalIndex = activeBeadIndex + (direction * i)
            
            // 0 and 109 represent the Guru bead bounds
            val isGuruBead = logicalIndex == 109 || logicalIndex == 0
            
            // Do not render beads beyond the Guru bead to create a visual gap
            if (logicalIndex > 109 || logicalIndex < 0) continue

            // Calculate positional offset
            val yOffsetPx = (i * beadSpacingPx) + dragOffset.value
            
            // Calculate scale and alpha based on distance from center for a 3D wheel effect
            val normalizedDistance = abs(yOffsetPx / beadSpacingPx)
            val scale = (1f - (normalizedDistance * 0.15f)).coerceIn(0.5f, 1f)
            val alpha = (1f - (normalizedDistance * 0.25f)).coerceIn(0f, 1f)
            
            val isCenter = normalizedDistance < 0.5f
            val beadColor = when {
                isGuruBead -> MaterialTheme.colorScheme.error // Guru bead
                isCenter -> MaterialTheme.colorScheme.primary // Active highlighted bead
                else -> MaterialTheme.colorScheme.surfaceVariant // Standard bead
            }
            
            val size = if (isGuruBead) 70.dp else 50.dp

            Box(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = yOffsetPx.roundToInt()) }
                    .size(size)
                    .scale(scale)
                    .alpha(alpha)
                    .clip(CircleShape)
                    .background(beadColor),
                contentAlignment = Alignment.Center
            ) {}
        }
    }
}
