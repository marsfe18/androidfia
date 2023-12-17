package com.polstat.luthfiani.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeTopBar(pageNumber: Int, gantiPage: (Int) -> Unit) {
    TabRow(selectedTabIndex = pageNumber) {
        Tab(
            text = { Text("Semua Utasan") },
            selected = pageNumber == 0,
            onClick = { gantiPage(0) }
        )
        Tab(
            text = { Text("Utasanku") },
            selected = pageNumber == 1,
            onClick = { gantiPage(1) }
        )
    }
}