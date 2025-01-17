package net.raphdf201.techapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Return a modifier with [fillMaxWidth] and [padding]
 * @param size the padding size (dp)
 */
fun dp(size: Int): Modifier {
    return Modifier.fillMaxWidth().padding(size.dp)
}