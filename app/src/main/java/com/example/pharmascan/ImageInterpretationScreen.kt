//package com.example.pharmascan
//
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.requiredSize
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.ImageLoader
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import coil.request.SuccessResult
//import coil.size.Precision
//import com.example.pharmascan.util.UriSaver
//import kotlinx.coroutines.launch
//
//@Composable
//internal fun ImageInterpretationRoute(
//    viewModel: ImageInterpretationViewModel = viewModel(factory = GenerativeViewModelFactory)
//) {
//    val imageInterpretationUiState by viewModel.uiState.collectAsState()
//
//    val coroutineScope = rememberCoroutineScope()
//    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
//    val imageLoader = ImageLoader.Builder(LocalContext.current).build()
//
//    ImageInterpretationScreen(
//        uiState = imageInterpretationUiState,
//        onReasonClicked = { inputText, selectedItems ->
//            coroutineScope.launch {
//                val bitmaps = selectedItems.mapNotNull {
//                    val imageRequest = imageRequestBuilder
//                        .data(it)
//                        .size(size = 768)
//                        .precision(Precision.EXACT)
//                        .build()
//                    try {
//                        val result = imageLoader.execute(imageRequest)
//                        if (result is SuccessResult) {
//                            return@mapNotNull (result.drawable as BitmapDrawable).bitmap
//                        } else {
//                            return@mapNotNull null
//                        }
//                    } catch (e: Exception) {
//                        return@mapNotNull null
//                    }
//                }
//                viewModel.reason(inputText, bitmaps)
//            }
//        }
//    )
//}
//
//@Composable
//fun ImageInterpretationScreen(
//    uiState: ImageInterpretationUiState = ImageInterpretationUiState.Loading,
//    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> }
//) {
//    val textPrompt = "Get the name of the medicine, its symptoms, primary diagnosis, usage, and dosage from the input image in the following format. \n" +
//            "Example: ● Name\n" +
//            "● Symptoms: \n and so on." +
//            "Make sure to ask the person to visit the doctor if problem persists."
//    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf<Uri>() }
//
//    val pickMedia = rememberLauncherForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { imageUri ->
//        imageUri?.let {
//            imageUris.add(it)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .padding(all = 16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Image Upload Section
//        Button(
//            onClick = {
//                pickMedia.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            },
//            modifier = Modifier
//                .padding(all = 4.dp)
//                .align(Alignment.CenterHorizontally),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF9100),
//                contentColor = Color(0xFFFFFFFF)
//            )
//        ) {
//            Text("Upload Image")
//        }
//
//        LazyRow(
//            modifier = Modifier.padding(all = 8.dp)
//        ) {
//            items(imageUris) { imageUri ->
//                AsyncImage(
//                    model = imageUri,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .requiredSize(300.dp)
//                )
//            }
//        }
//
//        Button(
//            onClick = {
//                if (textPrompt.isNotBlank()) {
//                    onReasonClicked(textPrompt, imageUris.toList())
//                }
//            },
//            modifier = Modifier
//                .padding(all = 4.dp)
//                .align(Alignment.CenterHorizontally),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF9100),
//                contentColor = Color(0xFFFAFAFA)
//            )
//        ) {
//            Text("Submit")
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
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxWidth(),
//                    shape = MaterialTheme.shapes.large,
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0XFFFFE28C)
//                    )
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding(all = 16.dp)
//                            .fillMaxWidth()
//                    ) {
//                        // Display GenerativeModel output
//                        uiState.outputText?.let { outputText ->
//                            Text(
//                                text = "Medicine Information:",
//                                color = Color.Black,
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
//                            Text(
//                                text = outputText,
//                                color = Color.Black,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
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
//
//            else -> {}
//        }
//    }
//}
//
//@Composable
//@Preview(showSystemUi = true)
//fun ImageInterpretationScreenPreview() {
//    ImageInterpretationScreen()
//}
//--------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------

//
//
//package com.example.pharmascan
//
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.requiredSize
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.ImageLoader
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import coil.request.SuccessResult
//import coil.size.Precision
//import com.example.pharmascan.util.UriSaver
//import kotlinx.coroutines.launch
//
//@Composable
//internal fun ImageInterpretationRoute(
//    viewModel: ImageInterpretationViewModel = viewModel(factory = GenerativeViewModelFactory)
//) {
//    val imageInterpretationUiState by viewModel.uiState.collectAsState()
//
//    val coroutineScope = rememberCoroutineScope()
//    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
//    val imageLoader = ImageLoader.Builder(LocalContext.current).build()
//
//    ImageInterpretationScreen(
//        uiState = imageInterpretationUiState,
//        onReasonClicked = { inputText, selectedItems ->
//            coroutineScope.launch {
//                val bitmaps = selectedItems.mapNotNull {
//                    val imageRequest = imageRequestBuilder
//                        .data(it)
//                        .size(size = 768)
//                        .precision(Precision.EXACT)
//                        .build()
//                    try {
//                        val result = imageLoader.execute(imageRequest)
//                        if (result is SuccessResult) {
//                            return@mapNotNull (result.drawable as BitmapDrawable).bitmap
//                        } else {
//                            return@mapNotNull null
//                        }
//                    } catch (e: Exception) {
//                        return@mapNotNull null
//                    }
//                }
//                viewModel.reason(inputText, bitmaps)
//            }
//        }
//    )
//}
//
//@Composable
//fun ImageInterpretationScreen(
//    uiState: ImageInterpretationUiState = ImageInterpretationUiState.Loading,
//    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> }
//) {
//    val textPrompt = "Get the name of the medicine, its symptoms, primary diagnosis, usage, and dosage from the input image in the following format. \n" +
//            "Example: ● Name\n" +
//            "● Symptoms: \n and so on." +
//            "Make sure to ask the person to visit the doctor if problem persists."
//    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf<Uri>() }
//
//    val pickMedia = rememberLauncherForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { imageUri ->
//        imageUri?.let {
//            imageUris.add(it)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .padding(all = 16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Image Upload Section
//        Button(
//            onClick = {
//                pickMedia.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            },
//            modifier = Modifier
//                .padding(all = 4.dp)
//                .align(Alignment.CenterHorizontally),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF9100),
//                contentColor = Color(0xFFFFFFFF)
//            )
//        ) {
//            Text("Upload Image")
//        }
//
//        LazyRow(
//            modifier = Modifier.padding(all = 8.dp)
//        ) {
//            items(imageUris) { imageUri ->
//                AsyncImage(
//                    model = imageUri,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .requiredSize(300.dp)
//                )
//            }
//        }
//
//        Button(
//            onClick = {
//                if (textPrompt.isNotBlank()) {
//                    onReasonClicked(textPrompt, imageUris.toList())
//                }
//            },
//            modifier = Modifier
//                .padding(all = 4.dp)
//                .align(Alignment.CenterHorizontally),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF9100),
//                contentColor = Color(0xFFFAFAFA)
//            )
//        ) {
//            Text("Submit")
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
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxWidth(),
//                    shape = MaterialTheme.shapes.large,
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0XFFFFE28C)
//                    )
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding(all = 16.dp)
//                            .fillMaxWidth()
//                    ) {
//                        // Display GenerativeModel output
//                        uiState.outputText?.let { outputText ->
//                            Text(
//                                text = "Medicine Information:",
//                                color = Color.Black,
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
//                            Text(
//                                text = outputText,
//                                color = Color.Black,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
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
//
//            else -> {}
//        }
//    }
//}
//
//@Composable
//@Preview(showSystemUi = true)
//fun ImageInterpretationScreenPreview() {
//    ImageInterpretationScreen()
//}
package com.example.pharmascan

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.example.pharmascan.util.UriSaver
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
internal fun ImageInterpretationRoute(
    navController: NavController,
    viewModel: ImageInterpretationViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val imageInterpretationUiState by viewModel.uiState.collectAsState()

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

    val coroutineScope = rememberCoroutineScope()
    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    ImageInterpretationScreen(
        uiState = imageInterpretationUiState,
        onReasonClicked = { inputText, selectedItems ->
            coroutineScope.launch {
                val bitmaps = selectedItems.mapNotNull {
                    val imageRequest = imageRequestBuilder
                        .data(it)
                        .size(size = 768)
                        .precision(Precision.EXACT)
                        .build()
                    try {
                        val result = imageLoader.execute(imageRequest)
                        if (result is SuccessResult) {
                            return@mapNotNull (result.drawable as BitmapDrawable).bitmap
                        } else {
                            return@mapNotNull null
                        }
                    } catch (e: Exception) {
                        return@mapNotNull null
                    }
                }
                viewModel.reason(inputText, bitmaps)
            }
        },
        onChatbotClicked = {
            navController.navigate("chatbot")
        },
        onLearnMoreClicked = { medicineName ->
            navController.navigate("chatbot")
            viewModel.queryChatbot("Tell me more about $medicineName")
        },
        onSpeakClicked = { text, pitch, speed ->
            if (isTtsInitialized) {
                if (text.isNotBlank()) {
                    textToSpeech?.setPitch(pitch)
                    textToSpeech?.setSpeechRate(speed)
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
fun ImageInterpretationScreen(
    uiState: ImageInterpretationUiState = ImageInterpretationUiState.Loading,
    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> },
    onChatbotClicked: () -> Unit = {},
    onLearnMoreClicked: (String) -> Unit = {},
    onSpeakClicked: (String, Float, Float) -> Unit = { _, _, _ -> }
) {
    val defaultPrompt = "Extract the name of the medicine from the input image, then provide the following information about the medicine in the specified format. If any details are not available on the image, use your knowledge to provide accurate information based on the medicine's name:\n" +
            "● Name: [The name of the medicine]\n" +
            "● Used For: [The conditions or symptoms the medicine is intended to treat, e.g., fever, pain, headache]\n" +
            "● Primary Use: [The primary medical condition or purpose the medicine is prescribed for, e.g., fever relief, pain relief]\n" +
            "● Usage: [How to take the medicine, e.g., with water, after food]\n" +
            "● Dosage: [The recommended dosage, e.g., 500 mg every 4-6 hours, including frequency and maximum daily limit]\n" +
            "● Precautions: [Important precautions to take while using the medicine, e.g., avoid alcohol, do not exceed recommended dose]\n" +
            "● Side Effects: [Common side effects of the medicine, e.g., nausea, rash]\n" +
            "● Storage: [How to store the medicine, e.g., store in a cool, dry place]\n" +
            "Ensure all information is accurate and relevant to the medicine's intended use. At the end, add: 'Please visit a doctor if the problem persists.'"

    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf<Uri>() }
    var pitch by remember { mutableStateOf(1f) } // Default pitch: 1.0
    var speed by remember { mutableStateOf(1f) } // Default speed: 1.0

    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground()
            .verticalScroll(rememberScrollState())
    ) {
        // Tabs
        TabRow(
            selectedTabIndex = 0,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Tab(
                selected = true,
                onClick = { /* Already on this screen */ },
                text = { Text("Image Scan") }
            )
            Tab(
                selected = false,
                onClick = onChatbotClicked,
                text = { Text("Chatbot") }
            )
        }

        // Title
        Text(
            text = "Image Scan",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Image Upload Section
        Button(
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF1E90FF)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Upload Image")
        }

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(imageUris) { imageUri ->
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .requiredSize(200.dp)
                )
            }
        }

        // Pitch Slider
        Text(
            text = "Choose the Pitch",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Slider(
            value = pitch,
            onValueChange = { pitch = it },
            valueRange = 0.5f..2.0f, // Pitch range: 0.5 (low) to 2.0 (high)
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        // Speed Slider
        Text(
            text = "Choose the Speed",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Slider(
            value = speed,
            onValueChange = { speed = it },
            valueRange = 0.5f..2.0f, // Speed range: 0.5 (slow) to 2.0 (fast)
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        // Submit Button with Validation
        Button(
            onClick = {
                if (imageUris.isEmpty()) {
                    Toast.makeText(context, "Please upload a photo", Toast.LENGTH_SHORT).show()
                } else {
                    onReasonClicked(defaultPrompt, imageUris.toList())
                }
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
            Text("Submit")
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
                            text = "Medicine Information:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = uiState.outputText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Note: This information may not be 100% accurate. Please consult a doctor or pharmacist for confirmation.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        // Speak Button
                        Button(
                            onClick = {
                                onSpeakClicked(uiState.outputText, pitch, speed)
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
                        // Learn More Button
                        Button(
                            onClick = {
                                val medicineName = uiState.outputText.split("\n")[0].replace("● Name: ", "")
                                onLearnMoreClicked(medicineName)
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
                            Text("Learn More")
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

@Composable
@Preview(showSystemUi = true)
fun ImageInterpretationScreenPreview() {
    PharmaScanTheme {
        ImageInterpretationScreen()
    }
}