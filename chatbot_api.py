# import pandas as pd
# import re
# import unicodedata
# import nltk
# import json
# import logging
# from flask import Flask, request, jsonify
# from typing import List, Dict, Any

# # Configure logging
# logging.basicConfig(level=logging.DEBUG)
# logger = logging.getLogger(__name__)

# # Download NLTK resources
# try:
#     nltk.download('punkt', quiet=True)
#     nltk.download('stopwords', quiet=True)
# except Exception as e:
#     logger.error(f"NLTK download error: {e}")

# from nltk.stem import PorterStemmer
# from nltk.tokenize import word_tokenize
# from nltk.corpus import stopwords

# app = Flask(__name__)

# class SymptomRemedyMatcher:
#     def __init__(self, csv_path: str):
#         """
#         Initialize the matcher with symptom-remedy data
        
#         Args:
#             csv_path (str): Path to the CSV file with symptom data
#         """
#         self.df = pd.read_csv(csv_path, encoding='utf-8')
        
#         # Normalize column names to lowercase
#         self.df.columns = [col.strip().lower() for col in self.df.columns]
#         logger.info(f"Columns found: {self.df.columns.tolist()}")
        
#         # Normalize symptoms column
#         self.df['normalized_symptoms'] = self.df['symptoms'].apply(self.normalize_text)
        
#         # Initialize stemmer and stopwords
#         self.stemmer = PorterStemmer()
#         self.stop_words = set(stopwords.words('english'))

#     def normalize_text(self, text: str) -> str:
#         """
#         Normalize text by:
#         1. Converting to lowercase
#         2. Removing accents
#         3. Removing non-alphabetic characters
#         """
#         if not isinstance(text, str):
#             return ""
        
#         # Remove accents
#         text = unicodedata.normalize('NFKD', text).encode('ascii', 'ignore').decode('utf-8')
        
#         # Convert to lowercase and remove non-alphabetic characters
#         text = re.sub(r'[^a-z\s]', '', text.lower())
        
#         return text

#     def tokenize_and_stem(self, text: str) -> List[str]:
#         """
#         Tokenize text, remove stopwords, and stem
#         """
#         tokens = word_tokenize(text)
        
#         # Remove stopwords and stem
#         processed_tokens = [
#             self.stemmer.stem(token) 
#             for token in tokens 
#             if token not in self.stop_words
#         ]
        
#         return processed_tokens

#     def find_matching_symptoms(self, user_input: str) -> pd.DataFrame:
#         """
#         Find matching symptoms based on user input with multiple matching strategies
#         """
#         # Normalize and tokenize user input
#         normalized_input = self.normalize_text(user_input)
#         input_tokens = self.tokenize_and_stem(normalized_input)
        
#         # Strategy 1: Direct substring match
#         direct_match = self.df[
#             self.df['normalized_symptoms'].str.contains(normalized_input, case=False, na=False)
#         ]
        
#         if not direct_match.empty:
#             return direct_match
        
#         # Strategy 2: Token-based match
#         def token_match(symptoms: str) -> bool:
#             symptom_tokens = self.tokenize_and_stem(self.normalize_text(symptoms))
#             return any(token in symptom_tokens for token in input_tokens)
        
#         token_matched = self.df[self.df['normalized_symptoms'].apply(token_match)]
        
#         return token_matched

#     def parse_remedies(self, remedies: str) -> List[str]:
#         """
#         Parse remedies from string, handling different separators
#         """
#         if not isinstance(remedies, str):
#             return []
        
#         # Try different separators
#         separators = [',', '|', ';']
#         for sep in separators:
#             if sep in remedies:
#                 return [r.strip() for r in remedies.split(sep)]
        
#         return [remedies.strip()]

# # Global matcher instance
# try:
#     symptom_matcher = SymptomRemedyMatcher("symptom_remedy.csv")
# except Exception as e:
#     logger.error(f"Failed to initialize matcher: {e}")
#     symptom_matcher = None

# @app.route('/chat', methods=['POST'])
# def chat() -> Any:
#     """
#     Process user symptom input and return matching remedies
#     """
#     if symptom_matcher is None:
#         return jsonify({"error": "Service unavailable"}), 503
        
#     try:
#         # Log incoming request details
#         logger.debug(f"Received headers: {request.headers}")
#         logger.debug(f"Received data: {request.data}")
        
#         # Try multiple ways to parse JSON
#         try:
#             data = request.get_json(force=True)
#         except Exception as e:
#             logger.debug(f"get_json() failed: {e}")
#             try:
#                 data = json.loads(request.data.decode('utf-8'))
#             except Exception as e:
#                 logger.error(f"JSON parsing failed: {e}")
#                 return jsonify({"error": "Invalid JSON format"}), 400
        
#         # Extract message with fallback
#         user_input = data.get('message', '')
        
#         if not user_input:
#             return jsonify({"error": "Empty message"}), 400

#         # Find matching symptoms
#         matched = symptom_matcher.find_matching_symptoms(user_input)
        
#         if not matched.empty:
#             first_match = matched.iloc[0]
#             response = {
#                 "condition": first_match['condition'],
#                 "remedies": symptom_matcher.parse_remedies(first_match['remedies']),  # lowercase
#                 "warning": str(first_match.get('warning', ''))  # lowercase with fallback
#             }
#             return jsonify(response)
        
#         return jsonify({"error": "No matching remedies found"}), 404
    
#     except Exception as e:
#         logger.error(f"Unexpected error: {e}", exc_info=True)
#         return jsonify({"error": "Internal server error"}), 500

# if __name__ == '__main__':
#     app.run(host='0.0.0.0', port=5000, debug=True)
import pandas as pd
import re
import unicodedata
import nltk
import json
import logging
from flask import Flask, request, jsonify
from typing import List, Dict, Any

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Download NLTK resources
try:
    nltk.download('punkt', quiet=True)
    nltk.download('stopwords', quiet=True)
except Exception as e:
    logger.error(f"NLTK download error: {e}")

from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords

app = Flask(__name__)

class SymptomRemedyMatcher:
    def __init__(self, csv_path: str):
        """
        Initialize the matcher with symptom-remedy data
        
        Args:
            csv_path (str): Path to the CSV file with symptom data
        """
        self.df = pd.read_csv(csv_path, encoding='utf-8')
        
        # Normalize column names
        self.df.columns = [self.normalize_text(col) for col in self.df.columns]
        
        # Normalize symptoms column
        self.df['normalized_symptoms'] = self.df['symptoms'].apply(self.normalize_text)
        
        # Initialize stemmer and stopwords
        self.stemmer = PorterStemmer()
        self.stop_words = set(stopwords.words('english'))

    def normalize_text(self, text: str) -> str:
        """
        Normalize text by:
        1. Converting to lowercase
        2. Removing accents
        3. Removing non-alphabetic characters
        """
        if not isinstance(text, str):
            return ""
        
        # Remove accents
        text = unicodedata.normalize('NFKD', text).encode('ascii', 'ignore').decode('utf-8')
        
        # Convert to lowercase and remove non-alphabetic characters
        text = re.sub(r'[^a-z\s]', '', text.lower())
        
        return text

    def tokenize_and_stem(self, text: str) -> List[str]:
        """
        Tokenize text, remove stopwords, and stem
        """
        tokens = word_tokenize(text)
        
        # Remove stopwords and stem
        processed_tokens = [
            self.stemmer.stem(token) 
            for token in tokens 
            if token not in self.stop_words
        ]
        
        return processed_tokens

    def find_matching_symptoms(self, user_input: str) -> pd.DataFrame:
        """
        Find matching symptoms based on user input with multiple matching strategies
        """
        # Normalize and tokenize user input
        normalized_input = self.normalize_text(user_input)
        input_tokens = self.tokenize_and_stem(normalized_input)

        # Log intermediate steps for debugging
        logger.debug(f"User input: {user_input}")
        logger.debug(f"Normalized input: {normalized_input}")
        logger.debug(f"Input tokens after stemming: {input_tokens}")

        # Strategy 1: Token-based substring match
        def substring_match(symptoms: str) -> bool:
            normalized_symptom = self.normalize_text(symptoms)
            # Check if any stemmed input token exists in the normalized symptom
            return any(token in normalized_symptom for token in input_tokens)

        direct_match = self.df[self.df['normalized_symptoms'].apply(substring_match)]
        
        logger.debug(f"Direct match result: {direct_match}")

        if not direct_match.empty:
            return direct_match

        # Strategy 2: Token-based match (fallback)
        def token_match(symptoms: str) -> bool:
            symptom_tokens = self.tokenize_and_stem(self.normalize_text(symptoms))
            return any(token in symptom_tokens for token in input_tokens)

        token_matched = self.df[self.df['normalized_symptoms'].apply(token_match)]
        
        logger.debug(f"Token match result: {token_matched}")

        return token_matched

    def parse_remedies(self, remedies: str) -> List[str]:
        """
        Parse remedies from string, handling different separators
        """
        if not isinstance(remedies, str):
            return []
        
        # Try different separators
        separators = [',', '|', ';']
        for sep in separators:
            if sep in remedies:
                return [r.strip() for r in remedies.split(sep)]
        
        return [remedies.strip()]

# Global matcher instance
symptom_matcher = SymptomRemedyMatcher("symptom_remedy.csv")

@app.route('/chat', methods=['POST'])
def chat() -> Any:
    """
    Process user symptom input and return matching remedies
    """
    try:
        # Log incoming request details
        logger.debug(f"Received headers: {request.headers}")
        logger.debug(f"Received data: {request.data}")
        
        # Try multiple ways to parse JSON
        try:
            # Try get_json() first
            data = request.get_json(force=True)
        except Exception as e:
            logger.debug(f"get_json() failed: {e}")
            # Fallback to manual parsing
            try:
                data = json.loads(request.data.decode('utf-8'))
            except Exception as e:
                logger.error(f"JSON parsing failed: {e}")
                return jsonify({"error": f"Invalid JSON: {str(e)}"}), 400
        
        # Extract message with fallback
        user_input = data.get('message', '')
        
        if not user_input:
            return jsonify({"error": "Empty message"}), 400

        # Find matching symptoms
        matched = symptom_matcher.find_matching_symptoms(user_input)
    
        
        if not matched.empty:
            first_match = matched.iloc[0]
            response = {
                "condition": first_match['condition'],
                "remedies": symptom_matcher.parse_remedies(first_match['remedies']),
                "warning": first_match['warning'] if first_match['warning'] != '-' else ''
            }
            return jsonify(response)
        
        return jsonify({"error": "No matching remedies found"}), 404
    
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)