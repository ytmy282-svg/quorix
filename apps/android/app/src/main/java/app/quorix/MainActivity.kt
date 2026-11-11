package app.quorix

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val Ink = Color(0xFF18201D)
private val Muted = Color(0xFF66716D)
private val Modes = listOf("Auto", "Normal Chat", "BOSS Mode", "Smart Routing", "Parallel", "Collaboration", "Sequential")
private val ThinkingLevels = listOf("Low", "Medium", "High")
private val GlassFill = Color.White.copy(alpha = 0.58f)
private val GlassStroke = Color.White.copy(alpha = 0.82f)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuorixApp() }
    }
}

@Composable
private fun GlassSurface(modifier: Modifier = Modifier, shape: RoundedCornerShape, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier.border(1.dp, GlassStroke, shape),
        shape = shape,
        color = GlassFill,
        shadowElevation = 12.dp,
        content = { Column(content = content) }
    )
}

@Composable
private fun QuorixApp() {
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("quorix_settings", 0) }
    var prompt by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Auto") }
    var thinking by remember { mutableStateOf("Medium") }
    var selectorOpen by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var settingsOpen by remember { mutableStateOf(false) }
    var lastPrompt by remember { mutableStateOf("") }
    var wallpaperUri by remember { mutableStateOf(preferences.getString("wallpaper_uri", null)?.let(Uri::parse)) }
    var attachmentName by remember { mutableStateOf<String?>(null) }

    val wallpaperPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            runCatching { context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            wallpaperUri = uri
            preferences.edit().putString("wallpaper_uri", uri.toString()).apply()
        }
    }
    val voiceInput = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) prompt = listOf(prompt, spoken).filter { it.isNotBlank() }.joinToString(" ")
        }
    }
    val attachmentPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) attachmentName = runCatching {
            context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
        }.getOrNull() ?: uri.lastPathSegment ?: "Attached file"
    }
    val wallpaperBitmap by produceState<android.graphics.Bitmap?>(initialValue = null, wallpaperUri) {
        value = wallpaperUri?.let { uri -> withContext(Dispatchers.IO) {
            runCatching { context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) } }.getOrNull()
        } }
    }

    MaterialTheme {
        Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFFEAF2EF), Color(0xFFF7F8F7), Color(0xFFE6EEEB))))) {
            if (wallpaperBitmap != null) {
                Image(wallpaperBitmap!!.asImageBitmap(), "Chat wallpaper", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.48f)))
            } else {
                // Soft ambient highlights behind the translucent glass surfaces.
                Box(Modifier.offset(x = (-70).dp, y = 180.dp).size(240.dp).blur(70.dp).background(Color(0xFFB9D9CE), CircleShape))
                Box(Modifier.align(Alignment.BottomEnd).offset(x = 65.dp, y = (-150).dp).size(260.dp).blur(80.dp).background(Color(0xFFD4DCEB), CircleShape))
            }

            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp)) {
                GlassSurface(Modifier.fillMaxWidth(), RoundedCornerShape(28.dp)) {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { menuOpen = true }) { Icon(Icons.Default.Menu, "Open menu", tint = Ink) }
                        Image(painter = painterResource(R.drawable.quorix_logo), contentDescription = "Quorix logo", modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        Text("Quorix", color = Ink, fontWeight = FontWeight.Bold, fontSize = 21.sp, modifier = Modifier.padding(start = 9.dp))
                        Spacer(Modifier.weight(1f))
                        Text("AI workspace", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(end = 12.dp))
                    }
                }

                Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    if (lastPrompt.isBlank()) {
                        Text("Hello, welcome to Quorix", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                        Text("What would you like to work on?", color = Muted, fontSize = 16.sp, modifier = Modifier.padding(top = 10.dp))
                        Row(Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Suggestion("Explain a topic") { prompt = "Explain a topic step by step" }
                            Suggestion("Write code") { prompt = "Help me write and review code" }
                        }
                    } else {
                        GlassSurface(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp)) {
                            Text(lastPrompt, color = Ink, modifier = Modifier.padding(18.dp))
                        }
                    }
                }

                GlassSurface(Modifier.fillMaxWidth(), RoundedCornerShape(32.dp)) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
                        if (attachmentName != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📎 $attachmentName", color = Muted, modifier = Modifier.weight(1f), maxLines = 1)
                                TextButton(onClick = { attachmentName = null }) { Text("Remove") }
                            }
                        }
                        BasicTextField(
                            value = prompt, onValueChange = { prompt = it },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 9.dp, vertical = 12.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Ink),
                            minLines = 1, maxLines = 5,
                            decorationBox = { inner -> Box {
                                if (prompt.isEmpty()) Text("Ask Quorix anything…", color = Muted)
                                inner()
                            } }
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { attachmentPicker.launch(arrayOf("*/*")) }) {
                                Icon(Icons.Default.Add, "Attach file", tint = Ink)
                            }
                            Box {
                                TextButton(onClick = { selectorOpen = true }) {
                                    Text("$mode · $thinking", color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Icon(Icons.Default.ArrowDropDown, null, tint = Ink)
                                }
                                DropdownMenu(expanded = selectorOpen, onDismissRequest = { selectorOpen = false }) {
                                    DropdownMenuItem(text = { Text("Mode") }, onClick = {}, enabled = false)
                                    Modes.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { mode = item }) }
                                    HorizontalDivider()
                                    DropdownMenuItem(text = { Text("Thinking level") }, onClick = {}, enabled = false)
                                    ThinkingLevels.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { thinking = item; selectorOpen = false }) }
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Quorix")
                                }
                                runCatching { voiceInput.launch(intent) }
                            }) { Icon(Icons.Default.Mic, "Voice input", tint = Ink) }
                            Surface(shape = CircleShape, color = Color(0xFF18201D)) {
                                IconButton(onClick = {
                                    if (prompt.isNotBlank() || attachmentName != null) {
                                        lastPrompt = prompt.trim().ifBlank { "Attachment: $attachmentName" }
                                        prompt = ""; attachmentName = null
                                    }
                                }) { Icon(Icons.Default.Send, "Send", tint = Color.White) }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(menuOpen, enter = slideInHorizontally { -it } + fadeIn(), exit = slideOutHorizontally { -it } + fadeOut()) {
                Box(Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize().background(Color(0x440C1713)).clickable { menuOpen = false })
                    GlassSurface(Modifier.fillMaxHeight().fillMaxWidth(0.84f), RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)) {
                        Column(Modifier.fillMaxSize().padding(22.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(R.drawable.quorix_logo), contentDescription = "Quorix logo", modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                                Text("Quorix", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink, modifier = Modifier.padding(start = 10.dp))
                            }
                            Text("Your AI workspace", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 20.dp))
                            listOf("New chat", "Chat history", "Projects", "Library", "BOSS Network", "AI Builder", "Research", "Media Studio", "Provider Hub").forEach { item ->
                                TextButton(onClick = { menuOpen = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text(item, color = Ink, modifier = Modifier.fillMaxWidth())
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = { menuOpen = false; settingsOpen = true }, modifier = Modifier.fillMaxWidth()) {
                                Text("Settings", color = Ink, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }

            if (settingsOpen) {
                AlertDialog(
                    onDismissRequest = { settingsOpen = false },
                    title = { Text("Settings") },
                    text = {
                        Column {
                            Text("Appearance", fontWeight = FontWeight.SemiBold)
                            Text("Chat wallpaper", color = Muted, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                            TextButton(onClick = { settingsOpen = false; wallpaperPicker.launch(arrayOf("image/*")) }) { Text("Choose from gallery") }
                            TextButton(onClick = {
                                wallpaperUri = null
                                preferences.edit().remove("wallpaper_uri").apply()
                            }) { Text("Reset wallpaper") }
                        }
                    },
                    confirmButton = { TextButton(onClick = { settingsOpen = false }) { Text("Done") } }
                )
            }
        }
    }
}

@Composable
private fun Suggestion(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick).border(1.dp, GlassStroke, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp), color = Color.White.copy(alpha = 0.55f), shadowElevation = 5.dp
    ) {
        Text(label, color = Ink, modifier = Modifier.padding(horizontal = 17.dp, vertical = 13.dp))
    }
}
