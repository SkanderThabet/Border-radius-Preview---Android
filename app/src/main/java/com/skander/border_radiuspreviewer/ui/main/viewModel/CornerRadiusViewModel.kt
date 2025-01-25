package com.skander.border_radiuspreviewer.ui.main.viewModel

import androidx.lifecycle.ViewModel
import com.skander.border_radiuspreviewer.domain.model.Corner
import com.skander.border_radiuspreviewer.domain.model.CornerRadius
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CornerRadiusViewModel @Inject constructor() : ViewModel() {

    private val _selectedCorners = MutableStateFlow<Set<Corner>>(emptySet())
    val selectedCorners = _selectedCorners.asStateFlow();

    private val _cornerRadius = MutableStateFlow(CornerRadius())
    val cornerRadius = _cornerRadius.asStateFlow()

    fun updateRadius(corner: Corner, value: Float) {
        _cornerRadius.update { current ->
            when(corner) {
                is Corner.TopStart -> current.copy(topStart = value)
                is Corner.TopEnd -> current.copy(topEnd = value)
                is Corner.BottomStart -> current.copy(bottomStart = value)
                is Corner.BottomEnd -> current.copy(bottomEnd = value)
                is Corner.All -> current.copy(
                    topStart = value,
                    topEnd = value,
                    bottomStart = value,
                    bottomEnd = value
                )
            }
        }
    }

    fun toggleCorner(corner: Corner) {
        _selectedCorners.update { current ->
            when {
                corner == Corner.All && current.contains(Corner.All) -> emptySet()
                corner == Corner.All -> setOf(Corner.All)
                current.contains(Corner.All) -> setOf(corner)
                current.contains(corner) -> current - corner
                else -> current + corner
            }
        }
    }

}