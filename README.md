# MediRoot - AI-Powered Health Assistant 🩺💡

![imagescan](https://github.com/user-attachments/assets/df8ffcfd-492a-447c-9175-53d9424d6717)    ![chatbot](https://github.com/user-attachments/assets/79236e2a-7e6a-46cc-8985-7f5c22f0af9f)


MediRoot is a mobile health assistant that combines OCR technology and NLP-powered symptom analysis to provide instant medical insights from prescriptions, reports, or user-described symptoms.

## ✨ Key Features
- 📸 Medical Text Extraction** - Scan medicine labels/reports using OCR (Gemini API)
- 💬 Symptom Analysis Chatbot** - Get AI-powered health guidance
- 🔊 Voice Output** - Hear diagnoses & recommendations via TTS
- 📊 Health Literacy Tools** - Simplified medical information

## 🛠️ Technology Stack
| Component       | Technologies               |
|-----------------|----------------------------|
| **Mobile App**  | Kotlin, Android SDK        |
| **Backend**     | Python, Flask              |
| **AI/ML**       | Gemini API, NLTK, NLP      |
| **OCR**         | Google Vision API          |
| **TTS**         | Android Text-to-Speech     |

## 🚀 Getting Started
### Prerequisites
- Android Studio (for development)
- Python 3.8+ (for backend)
- Google Cloud API key (for Gemini/Vision)

### Installation
```bash
# Clone repo
git clone https://github.com/ya5h8/MediRoot.git

# Android App
cd Android_App
Open in Android Studio

# Backend
cd Backend
pip install -r requirements.txt
```
## 📱 How to Use MediRoot

### 🖼️ Image Submission Flow
1. **Tap "Upload Image" button**  
   - Only accesses device gallery (no camera permission needed)

2. **Select medicine image**  
   - Choose clear photos of:  
     ✓ Medicine labels  
     ✓ Prescription slips  
     ✓ Medical reports  

3. **Press "Submit"**  
   - Processes image via Gemini OCR  
   - Typical extraction time: 3-5 seconds  

4. **View Results**  
   - Extracted text appears in this format:  
     ```plaintext
     [Medicine Name]: Paracetamol  
     [Dosage]: 500mg every 6 hours  
     [Warnings]: Do not exceed 4g/day  
     ```

### 💬 Symptom Chatbot
1. **Type symptoms** in text box  
   *(Example: "headache and fever since yesterday")*  

2. **Hit "Enter"** to get:  
   - 🩺 **Possible Condition**  
   - 💊 **Suggested Remedies**  
   - ⚠️ **When to See Doctor**  

### 🔊 Voice Features (Optional)
- Tap **"Speak"** to hear:  
  - Extracted medicine details  
  - Diagnosis explanations  

📌 **Best Practices:**  
- Use well-lit, high-resolution images  
- Crop to focus on relevant text  
- Supported formats: JPG, PNG (max 5MB)

## 👥 Team
- **[Yash Gawande](https://github.com/ya5h8)** 
- **[Vaishnavi Chauhan](https://github.com/Vaishnavi276)**
- **[Radhika Gawande](https://github.com/Radhika300904)** 
- **[Radhika Chavan]**   
- **[Tejshree Bambal]** 


  


    
