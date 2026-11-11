package app.quorix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Ink = Color(0xFF202124)
private val Muted = Color(0xFF777777)
private val Glass = Color(0xF2FFFFFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuorixApp() }
    }
}

@Composable
private fun QuorixApp() {
    var prompt by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Auto") }
    var menuOpen by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF7F7F7)) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { menuOpen = !menuOpen }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open menu", tint = Ink)
                        }
                        Text("Quorix", color = Ink, fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { menuOpen = !menuOpen }) {
                            Text("$mode  ▾", color = Ink)
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Hello, welcome to Quorix", color = Ink,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium)
                        Text("What would you like to work on?", color = Muted,
                            modifier = Modifier.padding(top = 8.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        color = Glass,
                        shadowElevation = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                            OutlinedTextField(
                                value = prompt,
                                onValueChange = { prompt = it },
                                placeholder = { Text("Ask Quorix anything…") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                minLines = 1,
                                maxLines = 5
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { /* File picker integration follows */ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Attach file")
                                }
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { /* Voice input integration follows */ }) {
                                    Icon(Icons.Default.Mic, contentDescription = "Voice input")
                                }
                                IconButton(onClick = { /* Chat API integration follows */ }) {
                                    Icon(Icons.Default.Send, contentDescription = "Send")
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = menuOpen,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(0.82f),
                        shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                        color = Glass,
                        shadowElevation = 12.dp
                    ) {
                        Column(Modifier.padding(22.dp)) {
                            Text("Quorix", fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge, color = Ink)
                            listOf("New chat", "Chat history", "Projects", "Library",
                                "BOSS Network", "AI Builder", "Research", "Settings").forEach {
                                TextButton(onClick = { menuOpen = false }) {
                                    Text(it, color = Ink)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
