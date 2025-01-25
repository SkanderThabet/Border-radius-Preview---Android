package com.skander.border_radiuspreviewer.domain.model

sealed class Corner {
    data object TopStart : Corner()
    data object TopEnd : Corner()
    data object BottomStart : Corner()
    data object BottomEnd : Corner()
    data object All : Corner()
}