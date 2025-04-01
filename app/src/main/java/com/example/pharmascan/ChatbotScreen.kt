//package com.example.pharmascan
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun ChatbotRoute(
//    viewModel: ImageInterpretationViewModel = viewModel(factory = GenerativeViewModelFactory)
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    ChatbotScreen(
//        uiState = uiState,
//        onEnterClicked = { symptoms ->
//            viewModel.querySymptoms(symptoms)
//        }
//    )
//}
//
//@Composable
//fun ChatbotScreen(
//    uiState: ImageInterpretationUiState,
//    onEnterClicked: (String) -> Unit
//) {
//    var symptomsInput by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .padding(all = 16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Text(
//            text = "Chatbot: Enter Your Symptoms",
//            color = Color.Black,
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        TextField(
//            value = symptomsInput,
//            onValueChange = { symptomsInput = it },
//            label = { Text("Symptoms (e.g., headache, fever)") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp)
//        )
//
//        Button(
//            onClick = {
//                onEnterClicked(symptomsInput)
//            },
//            modifier = Modifier
//                .padding(all = 4.dp)
//                .align(Alignment.CenterHorizontally),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF9100),
//                contentColor = Color(0xFFFAFAFA)
//            )
//        ) {
//            Text("Enter")
//        }
//
//        // Display Results
//        when (uiState) {
//            ImageInterpretationUiState.Initial -> {
//                // Nothing is shown
//            }
//
//            ImageInterpretationUiState.Loading -> {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .padding(all = 8.dp)
//                        .align(Alignment.CenterHorizontally)
//                ) {
//                    CircularProgressIndicator(color = Color(0XFFFFE28C))
//                }
//            }
//
//            is ImageInterpretationUiState.Success -> {
//                uiState.chatbotResponse?.let { chatbotResponse ->
//                    Card(
//                        modifier = Modifier
//                            .padding(vertical = 16.dp)
//                            .fillMaxWidth(),
//                        shape = MaterialTheme.shapes.large,
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color(0XFFFFE28C)
//                        )
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .padding(all = 16.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Text(
//                                text = "Chatbot Response:",
//                                color = Color.Black,
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(top = 8.dp)
//                            )
//                            Text(
//                                text = "● Condition: ${chatbotResponse.condition}",
//                                color = Color.Black
//                            )
//                            Text(
//                                text = "● Remedies: ${chatbotResponse.remedies.joinToString(", ")}",
//                                color = Color.Black
//                            )
//                            if (chatbotResponse.warning.isNotEmpty()) {
//                                Text(
//                                    text = "● Warning: ${chatbotResponse.warning}",
//                                    color = Color.Black
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            is ImageInterpretationUiState.Error -> {
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxWidth(),
//                    shape = MaterialTheme.shapes.large,
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0XFFFFE28C)
//                    )
//                ) {
//                    Text(
//                        text = uiState.errorMessage,
//                        color = Color(0XFFFF3D00),
//                        modifier = Modifier.padding(all = 16.dp)
//                    )
//                }
//            }
//        }
//    }
//}
package com.example.pharmascan

import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout   .fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun ChatbotRoute(
    navController: NavController,
    viewModel: ImageInterpretationViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()

    // Initialize TextToSpeech
    val context = LocalContext.current
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsInitialized by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported: ${Locale.US}")
                    Toast.makeText(context, "TTS language not supported", Toast.LENGTH_SHORT).show()
                } else {
                    isTtsInitialized = true
                    Log.d("TTS", "TextToSpeech initialized successfully")
                }
            } else {
                Log.e("TTS", "TextToSpeech initialization failed with status: $status")
                Toast.makeText(context, "TTS initialization failed", Toast.LENGTH_SHORT).show()
            }
        }
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            Log.d("TTS", "TextToSpeech shut down")
        }
    }

    ChatbotScreen(
        uiState = uiState,
        onEnterClicked = { symptoms ->
            viewModel.queryChatbot(symptoms)
        },
        onImageScanClicked = {
            navController.navigate("image_interpretation")
        },
        onSpeakClicked = { text ->
            if (isTtsInitialized) {
                if (text.isNotBlank()) {
                    textToSpeech?.setPitch(1f) // Default pitch
                    textToSpeech?.setSpeechRate(1f) // Default speed
                    val result = textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    if (result == TextToSpeech.SUCCESS) {
                        Log.d("TTS", "Speaking: $text")
                    } else {
                        Log.e("TTS", "Failed to speak: $result")
                        Toast.makeText(context, "Failed to speak", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("TTS", "Text to speak is empty")
                    Toast.makeText(context, "No text to speak", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.w("TTS", "TextToSpeech not initialized yet")
                Toast.makeText(context, "Text-to-Speech not ready", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
fun ChatbotScreen(
    uiState: ImageInterpretationUiState,
    onEnterClicked: (String) -> Unit,
    onImageScanClicked: () -> Unit,
    onSpeakClicked: (String) -> Unit = { _ -> }
) {
    var symptomsInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground()
            .verticalScroll(rememberScrollState())
    ) {
        // Tabs
        TabRow(
            selectedTabIndex = 1,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Tab(
                selected = false,
                onClick = onImageScanClicked,
                text = { Text("Image Scan") }
            )
            Tab(
                selected = true,
                onClick = { /* Already on this screen */ },
                text = { Text("Chatbot") }
            )
        }

        // Title
        Text(
            text = "Chatbot",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Text Input for Symptoms
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE6F0FA)
            )
        ) {
            TextField(
                value = symptomsInput,
                onValueChange = { symptomsInput = it },
                label = { Text("Enter Your Symptoms or Question") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        // Enter Button
        Button(
            onClick = {
                onEnterClicked(symptomsInput)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF1E90FF)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Enter")
        }

        // Display Results
        when (uiState) {
            ImageInterpretationUiState.Initial -> {
                // Nothing is shown
            }

            ImageInterpretationUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is ImageInterpretationUiState.Success -> {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0XFFFFE28C)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Chatbot Response:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = uiState.outputText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = {
                                onSpeakClicked(uiState.outputText)
                            },
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF1E90FF)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Speak")
                        }
                    }
                }
            }

            is ImageInterpretationUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0XFFFFE28C)
                    )
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = Color(0XFFFF3D00),
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }
            }
        }
    }
}