package UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutTab() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("About application", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Subject: Principles of programming languages", fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Author: Danijel Tomić", fontSize = 16.sp, color = Color.DarkGray)
    }
}
