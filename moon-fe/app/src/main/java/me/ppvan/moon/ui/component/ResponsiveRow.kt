package me.ppvan.moon.ui.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

@Composable
fun ResponsiveRow(content: LazyGridScope.(ResponsiveGridData) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val cols = (maxWidth.value / 200).roundToInt()
        val gridState = rememberLazyGridState()
        val responsiveGridData = ResponsiveGridData(columnsCount = cols)

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(cols),
        ) {
            content(responsiveGridData)
        }
    }
}