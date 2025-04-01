//package com.example.pharmascan
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import com.example.pharmascan.ui.theme.PharmaScanTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            PharmaScanTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    ImageInterpretationRoute()
//                }
//            }
//        }
//    }
//}



//
//
//package com.example.pharmascan
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                AppNavigation()
//            }
//        }
//    }
//}
//
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "image_interpretation") {
//        composable("image_interpretation") {
//            ImageInterpretationRoute(navController = navController)
//        }
//        composable("chatbot") {
//            ChatbotRoute()
//        }
//    }
//}
//






package com.example.pharmascan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PharmaScanTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "image_interpretation") {
        composable("image_interpretation") {
            ImageInterpretationRoute(navController = navController)
        }
        composable("chatbot") {
            ChatbotRoute(navController = navController)
        }
    }
}


//// MainActivity.kt
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//
//class MainActivity : AppCompatActivity() {
//    private val viewModel: SymptomViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val symptomInput = findViewById<EditText>(R.id.symptomInput)
//        val submitButton = findViewById<Button>(R.id.submitButton)
//        val resultText = findViewById<TextView>(R.id.resultText)
//
//        submitButton.setOnClickListener {
//            val input = symptomInput.text.toString().trim()
//            if (input.isNotEmpty()) {
//                viewModel.fetchRemedies(input)
//            } else {
//                resultText.text = "Please enter symptoms"
//            }
//        }
//
//        viewModel.response.observe(this) { response ->
//            response?.let {
//                if (it.error != null) {
//                    resultText.text = it.error
//                } else {
//                    val result = StringBuilder()
//                    result.append("Condition: ${it.condition ?: "Unknown"}\n")
//                    result.append("Remedies:\n")
//                    it.remedies?.forEach { remedy ->
//                        result.append("- $remedy\n")
//                    }
//                    if (!it.warning.isNullOrEmpty()) {
//                        result.append("Warning: ${it.warning}")
//                    }
//                    resultText.text = result.toString()
//                }
//            }
//        }
//
//        viewModel.error.observe(this) { error ->
//            resultText.text = error
//        }
//    }
//}