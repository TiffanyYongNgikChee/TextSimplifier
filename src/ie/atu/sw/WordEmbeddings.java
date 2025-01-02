package ie.atu.sw;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages word embeddings and Google-1000 common words embeddings storage.
 * Provides thread-safe storage and retrieval of word vectors using ConcurrentHashMap.
 * This class serves as the central repository for both general word embeddings and
 * the specialized Google-1000 most common words embeddings.
 *
 * @author Tiffany Yong Ngik Chee
 */
public class WordEmbeddings {
	/** Thread-safe map storing general word embeddings with words as keys and their vector representations as values. */
    private static final Map<String, double[]> wordEmbeddings = new ConcurrentHashMap<>();
    
    /** Thread-safe map storing Google-1000 word embeddings with words as keys and their vector representations as values. */
    private static final Map<String, double[]> google1000Embeddings = new ConcurrentHashMap<>();
    
    /** Flag indicating whether the general word embeddings file has been loaded. */
    private static boolean filespecified = false;
    
    /** Flag indicating whether the Google-1000 words file has been loaded. */
    private static boolean googlefilespecified = false;
    
    /**
     * Adds a word and its embedding vector to the general word embeddings map.
     * Sets the filespecified flag to true upon successful addition.
     * 
     * Time Complexity: O(1) average case for ConcurrentHashMap put operation
     *
     * @param word The word to be added
     * @param embedding The corresponding vector representation of the word
     */
    public static void addWordEmbedding(String word, double[] embedding) {
        wordEmbeddings.put(word, embedding);
        filespecified = true;
    }

    /**
     * Adds a word and its embedding vector to the Google-1000 embeddings map.
     * Sets the googlefilespecified flag to true upon successful addition.
     * 
     * Time Complexity: O(1) average case for ConcurrentHashMap put operation
     *
     * @param word The word from Google-1000 list to be added
     * @param embedding The corresponding vector representation of the word
     */
    public static void addGoogle1000Embedding(String word, double[] embedding) {
        google1000Embeddings.put(word, embedding);
        googlefilespecified = true;
    }

    /**
     * Retrieves the embedding vector for a given word from the general word embeddings map.
     * 
     * Time Complexity: O(1) average case for ConcurrentHashMap get operation
     * 
     * @param word The word whose embedding is to be retrieved
     * @return The embedding vector for the word, or null if the word is not found
     */
    public static double[] getEmbedding(String word) {
        return wordEmbeddings.get(word);
    }

    /**
     * Checks if the general word embeddings file has been loaded.
     *
     * Time Complexity: O(1) - constant time operation
     * @return true if the word embeddings file has been specified and loaded, false otherwise
     */
    public static boolean isFileSpecified() {
        return filespecified;
    }

    /**
     * Checks if the Google-1000 words file has been loaded.
     *
     * Time Complexity: O(1) - constant time operation
     * 
     * @return true if the Google-1000 file has been specified and loaded, false otherwise
     */
    public static boolean isGoogleFileSpecified() {
        return googlefilespecified;
    }

    /**
     * Provides access to the complete map of Google-1000 word embeddings.
     * 
     * Time Complexity: O(1) - returns reference to existing map
     *
     * @return Map containing Google-1000 words and their corresponding embedding vectors
     */
    public static Map<String, double[]> getGoogle1000Embeddings() {
        return google1000Embeddings;
    }
}
