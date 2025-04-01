//package com.example.pharmascan
//
//import android.graphics.Bitmap
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.content
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class ImageInterpretationViewModel(
//    private val generativeModel: GenerativeModel
//) : ViewModel() {
//
//    private val _uiState: MutableStateFlow<ImageInterpretationUiState> =
//        MutableStateFlow(ImageInterpretationUiState.Initial)
//    val uiState: StateFlow<ImageInterpretationUiState> =
//        _uiState.asStateFlow()
//
//    fun reason(
//        userInput: String,
//        selectedImages: List<Bitmap>
//    ) {
//        _uiState.value = ImageInterpretationUiState.Loading
//        val prompt = "Look at the image(s), and then answer the following question: $userInput"
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val inputContent = content {
//                    for (bitmap in selectedImages) {
//                        image(bitmap)
//                    }
//                    text(prompt)
//                }
//
//                var outputContent = ""
//
//                generativeModel.generateContentStream(inputContent)
//                    .collect { response ->
//                        outputContent += response.text
//                        _uiState.value = ImageInterpretationUiState.Success(outputContent)
//                    }
//            } catch (e: Exception) {
//                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "")
//            }
//        }
//    }
//}










//
//import android.graphics.Bitmap
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.content
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//import org.json.JSONObject
//
//// Updated UI state to include chatbot response for both image processing and symptom input
//sealed class ImageInterpretationUiState {
//    object Initial : ImageInterpretationUiState()
//    object Loading : ImageInterpretationUiState()
//    data class Success(
//        val outputText: String? = null, // From GenerativeModel (image processing)
//        val chatbotResponse: ChatbotResponse? = null // From Flask chatbot (image or symptom input)
//    ) : ImageInterpretationUiState()
//    data class Error(val errorMessage: String) : ImageInterpretationUiState()
//}
//
//// Data class to hold chatbot response
//data class ChatbotResponse(
//    val condition: String,
//    val remedies: List<String>,
//    val warning: String
//)
//
//class ImageInterpretationViewModel(
//    private val generativeModel: GenerativeModel
//) : ViewModel() {
//
//    private val _uiState: MutableStateFlow<ImageInterpretationUiState> =
//        MutableStateFlow(ImageInterpretationUiState.Initial)
//    val uiState: StateFlow<ImageInterpretationUiState> = _uiState.asStateFlow()
//
//    private val client = OkHttpClient()
//
//    // Existing function for image processing
//    fun reason(
//        userInput: String,
//        selectedImages: List<Bitmap>
//    ) {
//        _uiState.value = ImageInterpretationUiState.Loading
//        val prompt = "Look at the image(s), and then answer the following question: $userInput"
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val inputContent = content {
//                    for (bitmap in selectedImages) {
//                        image(bitmap)
//                    }
//                    text(prompt)
//                }
//
//                var outputContent = ""
//
//                // Process image with GenerativeModel
//                generativeModel.generateContentStream(inputContent)
//                    .collect { response ->
//                        outputContent += response.text
//                        // Update UI with GenerativeModel output
//                        _uiState.value = ImageInterpretationUiState.Success(outputText = outputContent)
//
//                        // Extract symptoms from the output and query the chatbot
//                        val symptoms = extractSymptoms(outputContent)
//                        if (symptoms.isNotEmpty()) {
//                            val chatbotResponse = queryChatbot(symptoms)
//                            // Update UI with both GenerativeModel output and chatbot response
//                            _uiState.value = ImageInterpretationUiState.Success(
//                                outputText = outputContent,
//                                chatbotResponse = chatbotResponse
//                            )
//                        }
//                    }
//            } catch (e: Exception) {
//                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "")
//            }
//        }
//    }
//
//    // New function to handle manual symptom input
//    fun querySymptoms(symptoms: String) {
//        if (symptoms.isBlank()) {
//            _uiState.value = ImageInterpretationUiState.Error("Please enter symptoms")
//            return
//        }
//
//        _uiState.value = ImageInterpretationUiState.Loading
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val chatbotResponse = queryChatbot(symptoms)
//                if (chatbotResponse != null) {
//                    _uiState.value = ImageInterpretationUiState.Success(
//                        outputText = null, // No image processing output
//                        chatbotResponse = chatbotResponse
//                    )
//                } else {
//                    _uiState.value = ImageInterpretationUiState.Error("No matching remedies found")
//                }
//            } catch (e: Exception) {
//                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "Failed to query chatbot")
//            }
//        }
//    }
//
//    private fun extractSymptoms(outputText: String): String {
//        // Simple extraction: Look for the "Symptoms" field in the output
//        val symptomsLine = outputText.split("\n").find { it.startsWith("● Symptoms:") }
//        return if (symptomsLine != null) {
//            symptomsLine.removePrefix("● Symptoms:").trim()
//        } else {
//            ""
//        }
//    }
//
//    private suspend fun queryChatbot(symptoms: String): ChatbotResponse? {
//        return try {
//            val json = JSONObject().apply {
//                put("message", symptoms)
//            }.toString()
//
//            val requestBody = json.toRequestBody("application/json".toMediaType())
//            val request = Request.Builder()
//                .url("http://10.0.2.2:5000/chat") // Use 10.0.2.2 for emulator to access localhost
//                .post(requestBody)
//                .build()
//
//            val response = client.newCall(request).execute()
//            if (response.isSuccessful) {
//                val responseBody = response.body?.string()
//                val jsonResponse = JSONObject(responseBody ?: "{}")
//                if (jsonResponse.has("error")) {
//                    null // Chatbot returned an error (e.g., "No matching remedies found")
//                } else {
//                    ChatbotResponse(
//                        condition = jsonResponse.getString("condition"),
//                        remedies = jsonResponse.getJSONArray("remedies").let { array ->
//                            List(array.length()) { array.getString(it) }
//                        },
//                        warning = jsonResponse.getString("warning")
//                    )
//                }
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//}




package com.example.pharmascan

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageInterpretationViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<ImageInterpretationUiState> =
        MutableStateFlow(ImageInterpretationUiState.Initial)
    val uiState: StateFlow<ImageInterpretationUiState> = _uiState.asStateFlow()

    // For image interpretation
    fun reason(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = ImageInterpretationUiState.Loading
        val prompt = "Look at the image(s), and then answer the following question: $userInput"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (bitmap in selectedImages) {
                        image(bitmap)
                    }
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = ImageInterpretationUiState.Success(outputText = outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    // For chatbot text queries
    fun queryChatbot(userInput: String) {
        if (userInput.isBlank()) {
            _uiState.value = ImageInterpretationUiState.Error("Please enter a valid query")
            return
        }

        _uiState.value = ImageInterpretationUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prompt = """
                    You are a medical chatbot. Based on the user's input, provide a response that includes:
                    - The likely condition (if applicable)
                    - Suggested remedies or advice
                    - A warning to consult a doctor if the problem persists or worsens
                    Format the response as follows:
                    ● Condition: [Condition Name]
                    ● Remedies: [Remedy 1], [Remedy 2], ...
                    ● Warning: [Warning Message]
                    User input: $userInput
                """

                val inputContent = content {
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = ImageInterpretationUiState.Success(outputText = outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "Failed to get a response from the chatbot")
            }
        }
    }
}