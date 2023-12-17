package com.polstat.luthfiani.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomInputField(
    textValue: String,
    onTextChange: (String) -> Unit,
    onAttachClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .height(60.dp), // Apply padding based on the keyboard's height
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Field
        TextField(
            value = textValue,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f) // Takes up the remaining space
                .background(Color.White, RoundedCornerShape(12.dp)),
            placeholder = { Text("Type a comment...") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Attach File Button
        IconButton(onClick = onAttachClick) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach File",
                tint = Color.Gray
            )
        }

        // Send Comment Button
        IconButton(onClick = onSendClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send Comment",
                tint = Color.Gray
            )
        }
    }
}