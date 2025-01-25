package com.skander.border_radiuspreviewer.ui.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skander.border_radiuspreviewer.domain.model.Corner
import com.skander.border_radiuspreviewer.ui.main.viewModel.CornerRadiusViewModel

@Composable
fun CornerRadiusPreviewer(
    modifier: Modifier = Modifier,
    viewModel: CornerRadiusViewModel = hiltViewModel()
) {
    val cornerRadius by viewModel.cornerRadius.collectAsStateWithLifecycle()

    val selectedCorners by viewModel.selectedCorners.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(
                    topStart = cornerRadius.topStart.dp,
                    topEnd = cornerRadius.topEnd.dp,
                    bottomStart = cornerRadius.bottomStart.dp,
                    bottomEnd = cornerRadius.bottomEnd.dp
                )
            ) { }
            
        }
        CornerControls(
            corners = selectedCorners,
            onCornerClick = viewModel::toggleCorner,
            modifier = modifier
        )

        if (selectedCorners.isNotEmpty()) {
            RadiusSlider(
                value = when {
                    selectedCorners.contains(Corner.All) -> cornerRadius.topStart
                    selectedCorners.isNotEmpty() -> when (selectedCorners.first()) {
                        Corner.TopStart -> cornerRadius.topStart
                        Corner.TopEnd -> cornerRadius.topEnd
                        Corner.BottomStart -> cornerRadius.bottomStart
                        Corner.BottomEnd -> cornerRadius.bottomEnd
                        Corner.All -> cornerRadius.topStart
                    }
                    else -> 0f
                },
                onValueChange = { value ->
                    selectedCorners.forEach { corner ->
                        viewModel.updateRadius(corner, value)
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun CornerControls(
    corners: Set<Corner>,
    onCornerClick: (Corner) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        setOf(Corner.TopStart, Corner.TopEnd, Corner.BottomStart, Corner.BottomEnd, Corner.All)
            .forEach { corner ->
                IconButton(
                    onClick = { onCornerClick(corner) },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(
                            when (corner) {
                                Corner.TopStart -> Alignment.TopStart
                                Corner.TopEnd -> Alignment.TopEnd
                                Corner.BottomStart -> Alignment.BottomStart
                                Corner.BottomEnd -> Alignment.BottomEnd
                                Corner.All -> Alignment.Center
                            }
                        )
                        .offset(
                            x = when (corner) {
                                Corner.TopEnd, Corner.BottomEnd -> (-16).dp
                                Corner.TopStart, Corner.BottomStart -> 16.dp
                                else -> 0.dp
                            },
                            y = when (corner) {
                                Corner.BottomStart, Corner.BottomEnd -> (-16).dp
                                Corner.TopStart, Corner.TopEnd -> 16.dp
                                else -> 0.dp
                            }
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (corners.contains(corner)) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
    }
}

@Composable
fun RadiusSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = value.toInt().toString(),
            onValueChange = {
                onValueChange(it.toFloatOrNull() ?: value)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Radius") }
        )
    }
}