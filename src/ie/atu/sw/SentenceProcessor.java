package ie.atu.sw;

import java.util.*;

/**
* Processes sentences for text simplification using word embeddings and cosine similarity.
* Provides functionality to convert complex words to simpler alternatives from Google-1000 common words.
*
* Space Complexity: O(N) where N is the length of input sentence
* 
* @author Tiffany Yong Ngik Chee 
*/
public class SentenceProcessor {
	
	/**
	    * Calculates cosine similarity between two word embedding vectors.
	    * 
	    * Time Complexity: O(n) where n is the length of vectors
	    * Space Complexity: O(1) - uses constant extra space
	    *
	    * @param vec1 First word embedding vector
	    * @param vec2 Second word embedding vector
	    * @return Cosine similarity score between -1 and 1
	    */
    public static double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0;
        double normVec1 = 0.0;
        double normVec2 = 0.0;
        
        // O(n) loop where n is vector length
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normVec1 += Math.pow(vec1[i], 2);
            normVec2 += Math.pow(vec2[i], 2);
        }

        return dotProduct / (Math.sqrt(normVec1) * Math.sqrt(normVec2));
    }
    
    /**
     * Simplifies an input sentence by replacing complex words with simpler alternatives.
     * 
     * Time Complexity: O(w * (e + v)) where:
     * - w is number of words in input sentence
     * - e is size of embedding dictionary
     * - v is dimension of word vectors
     * 
     * @param inputSentence The sentence to be simplified
     * @return Simplified version of the input sentence
     */
    public static String simplifySentence(String inputSentence) {
        if (inputSentence == null || inputSentence.isEmpty()) {
            return ""; // If input is empty, return an empty string
        }
        
        // O(w) splitting operation where w is number of words
        String[] words = inputSentence.split("\\s+");
        StringBuilder simplifiedSentence = new StringBuilder();
        
        // O(w) loop for each word
        for (String word : words) {
            String simplifiedWord = simplifyWord(word.toLowerCase());
            simplifiedSentence.append(simplifiedWord).append(" ");
        }

        return simplifiedSentence.toString().trim();
    }

    /**
     * Finds the most similar word from Google-1000 common words for a given input word.
     * 
     * Time Complexity: O(e * v) where:
     * - e is number of words in Google-1000 embeddings
     * - v is dimension of word vectors
     * 
     * @param word The word to be simplified
     * @return The most similar word from Google-1000 list or original word if no embedding found
     */
    private static String simplifyWord(String word) {
    	// O(1) lookup in embedding dictionary
        double[] targetEmbedding = WordEmbeddings.getEmbedding(word);
        if (targetEmbedding == null) {
            return word;
        }

        String bestMatch = word;
        double highestSimilarity = -1.0;
        
        // O(e * v) loop where:
        // e is number of Google-1000 words
        // v is vector dimension (for cosine similarity calculation)
        for (Map.Entry<String, double[]> entry : WordEmbeddings.getGoogle1000Embeddings().entrySet()) {
            double similarity = cosineSimilarity(targetEmbedding, entry.getValue());
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = entry.getKey();
            }
        }

        return bestMatch;
    }
}
