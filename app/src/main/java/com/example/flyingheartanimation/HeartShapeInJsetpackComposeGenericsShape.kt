package com.example.flyingheartanimation

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable

@Composable
fun heart(): GenericShape {
    return GenericShape { size, _ ->
        heartPath(size = size)
    }
}