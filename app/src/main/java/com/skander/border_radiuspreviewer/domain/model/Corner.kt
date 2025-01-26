package com.skander.border_radiuspreviewer.domain.model

sealed class Corner {
    data object TopStart : Corner()
    data object TopEnd : Corner()
    data object BottomStart : Corner()
    data object BottomEnd : Corner()
    data object All : Corner()

    fun displayText(): String = when(this) {
        TopStart -> "Top Start"
        TopEnd -> "Top End"
        BottomStart -> "Bottom Start"
        BottomEnd -> "Bottom End"
        All -> "All"
    }
}