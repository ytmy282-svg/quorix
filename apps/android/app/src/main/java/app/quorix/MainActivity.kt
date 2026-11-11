package app.quorix

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val Ink = Color(0xFF202124)
private val Muted = Color(0xFF777777)
private val Glass = Color(0xF2FFFFFF)
private val Page = Color(0xFFF8F9FA)
private val Modes = listOf("Auto", "Normal Chat", "BOSS Mode", "Smart Routing", "Parallel", "Collaboration", "Sequential")
private val ThinkingLevels = listOf("Low", "Medium", "High")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuorixApp() }
    }
}

@Composable
private fun QuorixApp() {
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("quorix_settings", 0) }
    var prompt by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Auto") }
    var thinking by remember { mutableStateOf("Medium") }
    var modeMenuOpen by remember { mutableStateOf(false) }
    var thinkingMenuOpen by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var lastPrompt by remember { mutableStateOf("") }
    var wallpaperUri by remember { mutableStateOf(preferences.getString("wallpaper_uri", null)?.let(Uri::parse)) }
    var attachmentName by remember { mutableStateOf<String?>(null) }
    val lowMemoryProfile = remember {
        Build.SUPPORTED_32_BIT_ABIS.isNotEmpty() && Build.SUPPORTED_64_BIT_ABIS.isEmpty()
    }

    val wallpaperPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            wallpaperUri = uri
            preferences.edit().putString("wallpaper_uri", uri.toString()).apply()
        }
    }
    val attachmentPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            attachmentName = runCatching {
                context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
                    ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
            }.getOrNull() ?: uri.lastPathSegment ?: "Attached file"
        }
    }
    val wallpaperBitmap by produceState<android.graphics.Bitmap?>(initialValue = null, wallpaperUri) {
        value = wallpaperUri?.let { uri ->
            withContext(Dispatchers.IO) {
                runCatching { context.contentResolver.openInputStream(uri)?.use { stream -> BitmapFactory.decodeStream(stream) } }.getOrNull()
            }
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Page) {
            Box(Modifier.fillMaxSize()) {
                if (wallpaperBitmap != null) {
                    Image(
                        bitmap = wallpaperBitmap!!.asImageBitmap(),
                        contentDescription = "Chat wallpaper",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.58f)))
                }
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { menuOpen = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open menu", tint = Ink)
                            }
                            Text("Quorix", color = Ink, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.weight(1f))
                            Box {
                                TextButton(onClick = { modeMenuOpen = true }) { Text("$mode  ▾", color = Ink) }
                                DropdownMenu(expanded = modeMenuOpen, onDismissRequest = { modeMenuOpen = false }) {
                                    Modes.forEach { item ->
                                        DropdownMenuItem(text = { Text(item) }, onClick = { mode = item; modeMenuOpen = false })
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
                                TextButton(onClick = { thinkingMenuOpen = true }) { Text("$thinking  ▾", color = Muted, fontSize = 12.sp) }
                                DropdownMenu(expanded = thinkingMenuOpen, onDismissRequest = { thinkingMenuOpen = false }) {
                                    ThinkingLevels.forEach { item ->
                                        DropdownMenuItem(text = { Text(item) }, onClick = { thinking = item; thinkingMenuOpen = false })
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
                        Text("Hello, welcome to Quorix", color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
                        Text("What would you like to work on?", color = Muted, modifier = Modifier.padding(top = 8.dp))
                        if (lastPrompt.isNotBlank()) {
                            Surface(modifier = Modifier.fillMaxWidth().padding(top = 28.dp), color = Glass, shape = RoundedCornerShape(20.dp)) {
                                Text(lastPrompt, color = Ink, modifier = Modifier.padding(16.dp))
                            }
                        } else {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Suggestion("Explain a topic") { prompt = "Explain a topic step by step" }
                                Suggestion("Write code") { prompt = "Help me write and review code" }
                            }
                        }
                    }

                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = Glass, shadowElevation = 5.dp) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                            if (attachmentName != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("📎 $attachmentName", color = Muted, modifier = Modifier.weight(1f), maxLines = 1)
                                    TextButton(onClick = { attachmentName = null }) { Text("Remove") }
                                }
                            }
                            BasicTextField(
                                value = prompt,
                                onValueChange = { prompt = it },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Ink),
                                minLines = 1,
                                maxLines = 5,
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (prompt.isEmpty()) Text("Ask Quorix anything…", color = Muted)
                                        innerTextField()
                                    }
                                }
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { attachmentPicker.launch(arrayOf("*/*")) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Attach file", tint = Ink)
                                }
                                TextButton(onClick = { wallpaperPicker.launch(arrayOf("image/*")) }) { Text("Wallpaper", color = Muted, fontSize = 11.sp) }
                                if (wallpaperUri != null) {
                                    TextButton(onClick = {
                                        wallpaperUri = null
                                        preferences.edit().remove("wallpaper_uri").apply()
                                    }) { Text("Reset", color = Muted, fontSize = 11.sp) }
                                }
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { /* Voice input integration follows */ }) {
                                    Icon(Icons.Default.Mic, contentDescription = "Voice input", tint = Ink)
                                }
                                Surface(shape = CircleShape, color = Ink) {
                                    IconButton(onClick = {
                                        if (prompt.isNotBlank() || attachmentName != null) {
                                            lastPrompt = prompt.trim().ifBlank { "Attachment: $attachmentName" }
                                            prompt = ""
                                            attachmentName = null
                                        }
                                    }) {
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
                        Box(Modifier.fillMaxSize().background(Color(0x55000000)).clickable { menuOpen = false })
                        Surface(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.84f),
                            shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                            color = Glass,
                            shadowElevation = 12.dp
                        ) {
                            Column(Modifier.padding(22.dp)) {
                                Text("Quorix", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = Ink)
                                Spacer(Modifier.size(18.dp))
                                listOf("New chat", "Chat history", "Projects", "Library", "BOSS Network", "AI Builder", "Research", "Media Studio", "Provider Hub", "Settings").forEach { item ->
                                    TextButton(onClick = { menuOpen = false }) { Text(item, color = Ink) }
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
    Surface(modifier = Modifier.clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), color = Glass, shadowElevation = 1.dp) {
        Text(label, color = Ink, modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp))
    }
}
