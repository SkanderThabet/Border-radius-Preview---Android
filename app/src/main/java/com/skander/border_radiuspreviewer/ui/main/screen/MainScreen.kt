package com.skander.border_radiuspreviewer.ui.main.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skander.border_radiuspreviewer.domain.model.Corner
import com.skander.border_radiuspreviewer.domain.model.Corner.All.displayText
import com.skander.border_radiuspreviewer.domain.model.CornerRadius
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
        ChipGroupReflowSample(
            selectedCorners,
            viewModel::toggleCorner,
            viewModel::resetRadius
        )
        BodyContent(cornerRadius)
        FooterContent(selectedCorners, viewModel, modifier, cornerRadius)
    }
}

@Composable
private fun FooterContent(
    selectedCorners: Set<Corner>,
    viewModel: CornerRadiusViewModel,
    modifier: Modifier,
    cornerRadius: CornerRadius
) {
    CornerControls(
        corners = selectedCorners,
        onCornerClick = viewModel::toggleCorner,
        modifier = modifier
    )
    AnimatedVisibility(
        visible = selectedCorners.isNotEmpty(),
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(
                durationMillis = 300,
                easing = EaseOutQuart
            )
        ) + fadeIn(
            animationSpec = tween(300)
        ) + expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(
                durationMillis = 200,
                easing = EaseInQuart
            )
        ) + fadeOut(
            animationSpec = tween(200)
        ) + shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(200)
        )
    ) {
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
    ) }

}

@Composable
private fun BodyContent(cornerRadius: CornerRadius) {
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
            ),
        ) { }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipGroupReflowSample(
    corners: Set<Corner>,
    onChipCornerClick: (Corner) -> Unit,
    onResetClick: () -> Unit,
) {
    Column {
        FlowRow(
            Modifier.fillMaxWidth(1f).wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.Center,
        ) {
            setOf(Corner.TopStart, Corner.TopEnd, Corner.BottomStart, Corner.BottomEnd, Corner.All)
                .forEach { corner ->
                    InputChip(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .align(alignment = Alignment.CenterVertically),
                        label = { Text(text = corner.displayText()) },
                        onClick = { onChipCornerClick(corner) },
                        selected = corners.contains(corner)
                    )
                }
            ElevatedSuggestionChipSample(onResetClick = onResetClick)
        }
    }
}

@Composable
fun ElevatedSuggestionChipSample(onResetClick: () -> Unit,) {
    ElevatedSuggestionChip(
        onClick = onResetClick,
        label = { Text("Reset") },
        icon = { Icon(Icons.Filled.Refresh, tint = MaterialTheme.colorScheme.primary, contentDescription = "Localized Message") }
    )
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
                                Corner.TopEnd, Corner.BottomEnd -> 24.dp
                                Corner.TopStart, Corner.BottomStart -> (-24).dp
                                else -> 0.dp
                            },
                            y = when (corner) {
                                Corner.BottomStart, Corner.BottomEnd -> 24.dp
                                Corner.TopStart, Corner.TopEnd -> (-24).dp
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
        SliderWithCustomThumbSample(
            value = value,
            onValueChange = onValueChange,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithCustomThumbSample(
    value : Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbSize = DpSize(20.dp, 20.dp)
    val trackHeight = 4.dp
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Slider(
            modifier = Modifier.semantics { contentDescription = "Localized Description" }
                .requiredSizeIn(minWidth = thumbSize.width, minHeight = trackHeight),
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            interactionSource = interactionSource,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            thumb = {
                Label(
                    label = {
                        PlainTooltip(modifier = Modifier.sizeIn(45.dp, 25.dp).wrapContentWidth()) {
                            Text("%.2f".format(value))
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    SliderDefaults.Thumb(interactionSource = interactionSource, modifier =  Modifier.size(thumbSize)
                        .shadow(1.dp, CircleShape, clip = false)
                        .indication(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = false, radius = 20.dp)
                        ))
                }
            },
            track = {
                SliderDefaults.Track(
                    sliderState = it,
                    modifier = Modifier.height(trackHeight),
                    thumbTrackGapSize = 0.dp,
                    trackInsideCornerSize = 0.dp,
                    drawStopIndicator = null
                )
            }
        )
    }
}