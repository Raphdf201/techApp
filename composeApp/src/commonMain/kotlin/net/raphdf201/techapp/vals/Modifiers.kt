package net.raphdf201.techapp.vals

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * [Modifier] with attributes :
 * - [fillMaxWidth]
 */
fun modifier(): Modifier {
    return Modifier.fillMaxWidth()
}

/**
 * [Modifier] with attributes :
 * - [fillMaxWidth]
 * - [padding]
 */
fun modifier(paddingSize: Int): Modifier {
    return Modifier.fillMaxWidth().padding(paddingSize.dp)
}

/**
 * [Modifier] with attributes :
 * - [fillMaxWidth]
 * - [padding]
 * - [border]
 */
fun modifier(paddingSize: Int, borderWidth: Int, borderColor: Color): Modifier {
    return Modifier.fillMaxWidth().padding(paddingSize.dp)
        .border(width = borderWidth.dp, borderColor)
}
