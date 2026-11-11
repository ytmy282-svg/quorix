package app.quorix

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF202124)
private val Muted = Color(0xFF777777)
private val Glass = Color(0xF2FFFFFF)
private val Page = Color(0xFFF8F9FA)
private val Modes = listOf(
    "Auto", "Normal Chat", "BOSS Mode", "Smart Routing",
    "Parallel", "Collaboration", "Sequential"
)
private val ThinkingLevels = listOf("Low", "Medium", "High")

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
    var thinking by remember { mutableStateOf("Medium") }
    var modeMenuOpen by remember { mutableStateOf(false) }
    var thinkingMenuOpen by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var lastPrompt by remember { mutableStateOf("") }
    val lowMemoryProfile = remember {
        Build.SUPPORTED_32_BIT_ABIS.isNotEmpty() && Build.SUPPORTED_64_BIT_ABIS.isEmpty()
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Page) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { menuOpen = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open menu", tint = Ink)
                            }
                            Text(
                                "Quorix", color = Ink, fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.weight(1f))
                            Box {
                                TextButton(onClick = { modeMenuOpen = true }) {
                                    Text("$mode  ▾", color = Ink)
                                }
                                DropdownMenu(
                                    expanded = modeMenuOpen,
                                    onDismissRequest = { modeMenuOpen = false }
                                ) {
                                    Modes.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = { mode = item; modeMenuOpen = false }
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Thinking", color = Muted, style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.width(4.dp))
                            Box {
                                TextButton(onClick = { thinkingMenuOpen = true }) {
                                    Text("$thinking  ▾", color = Muted, fontSize = 12.sp)
                                }
                                DropdownMenu(
                                    expanded = thinkingMenuOpen,
                                    onDismissRequest = { thinkingMenuOpen = false }
                                ) {
                                    ThinkingLevels.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = { thinking = item; thinkingMenuOpen = false }
                                        )
                                    }
                                }
                            }
                            if (lowMemoryProfile) {
                                Spacer(Modifier.weight(1f))
                                Text("Lite mode", color = Muted, fontSize = 11.sp)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Hello, welcome to Quorix", color = Ink,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "What would you like to work on?", color = Muted,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (lastPrompt.isNotBlank()) {
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(top = 28.dp),
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(lastPrompt, color = Ink, modifier = Modifier.padding(16.dp))
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Suggestion("Explain a topic") { prompt = "Explain a topic step by step" }
                                Suggestion("Write code") { prompt = "Help me write and review code" }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = Glass,
                        shadowElevation = 5.dp
                    ) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                            BasicTextField(
                                value = prompt,
                                onValueChange = { prompt = it },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Ink),
                                minLines = 1,
                                maxLines = 5,
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (prompt.isEmpty()) {
                                            Text("Ask Quorix anything…", color = Muted)
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { /* Native document and photo picker integration pending */ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Attach file", tint = Ink)
                                }
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { /* Voice input integration pending */ }) {
                                    Icon(Icons.Default.Mic, contentDescription = "Voice input", tint = Ink)
                                }
                                Surface(shape = CircleShape, color = Ink) {
                                    IconButton(
                                        onClick = {
                                            if (prompt.isNotBlank()) {
                                                lastPrompt = prompt.trim()
                                                prompt = ""
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                                    }
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
                    Box(Modifier.fillMaxSize()) {
                        Box(
                            Modifier.fillMaxSize().background(Color(0x55000000))
                                .clickable { menuOpen = false }
                        )
                        Surface(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.84f),
                            shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                            color = Glass,
                            shadowElevation = 12.dp
                        ) {
                            Column(Modifier.padding(22.dp)) {
                                Text(
                                    "Quorix", fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleLarge, color = Ink
                                )
                                Spacer(Modifier.size(18.dp))
                                listOf(
                                    "New chat", "Chat history", "Projects", "Library",
                                    "BOSS Network", "AI Builder", "Research", "Media Studio",
                                    "Provider Hub", "Settings"
                                ).forEach { item ->
                                    TextButton(onClick = { menuOpen = false }) {
                                        Text(item, color = Ink)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Suggestion(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Text(label, color = Ink, modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp))
    }
}
