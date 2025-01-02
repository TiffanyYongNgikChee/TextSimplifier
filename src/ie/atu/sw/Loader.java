package ie.atu.sw;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
* Handles loading of word embeddings and Google-1000 common words using virtual threads.
* Provides concurrent file processing capabilities for efficient data loading.
* 
* Space Complexity: O(N + M) where:
* - N is total size of word embeddings
* - M is size of Google-1000 words data
*
* @author Tiffany Yong Ngik Chee
*/
public class Loader {
	
	/**
	* Loads word embeddings from a file using virtual threads for concurrent processing.
	* Each line is processed in its own virtual thread for optimal performance.
	* 
	* Time Complexity: O(N * D) where:
	* - N is number of words in embedding file
	* - D is dimension of embedding vectors
	* @param filePath Path to the word embeddings file
	* @throws IOException If there are errors reading the file
	*/
    public static void loadWordEmbeddings(String filePath) throws IOException {
        System.out.println("\nLoading word embeddings from: " + filePath);
        Path path = Paths.get(filePath);
        // O(N) operations distributed across virtual threads
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        	Files.lines(path).forEach(line ->
        		executor.submit(() -> {
        			// O(D) parsing operations per line
        			String[] parts = line.split(", ");
        			String word = parts[0]; 
        			double[] embedding = new double[parts.length - 1]; 
        			for (int i = 1; i < parts.length; i++) { 
        				embedding[i - 1] = Double.parseDouble(parts[i]); } 
        			// O(1) operation to store embedding
        			WordEmbeddings.addWordEmbedding(word, embedding);
        			}) 
        		); 
        	}
        			
    }
    
    /**
     * Loads Google-1000 common words and their corresponding embeddings using virtual threads.
     * Matches each word with its embedding from previously loaded word embeddings.
     * 
     * Time Complexity: O(M * log N) where:
     * - M is number of Google-1000 words
     * - N is size of word embeddings dictionary (for lookup)
     * 
     * @param filePath Path to the Google-1000 words file
     */
    public static void loadGoogle1000Words(String filePath) {
	    System.out.println("Loading Google-1000 words and their embeddings from: " + filePath);

	    // O(M) operations distributed across virtual threads
	    try (var executor = Executors.newVirtualThreadPerTaskExecutor();
	         Stream<String> lines = Files.lines(Paths.get(filePath))) {
	        
	        lines.forEach(line -> executor.submit(() -> {
	            String word = line.trim().toLowerCase(); // Normalize the word
	            // O(log N) lookup in word embeddings
	            double[] embedding = WordEmbeddings.getEmbedding(word); // Attempt to get the embedding from loaded wordEmbeddings
	            if (embedding != null) {
	            	// O(1) operation to store embedding
	            	WordEmbeddings.addGoogle1000Embedding(word, embedding); // Add the word and its embedding to the concurrent map
	            } else {
	                System.out.println("No embedding found for Google-1000 word: " + word);
	            }
	        }));
	    } catch (IOException e) {
	        System.err.println("Error loading Google-1000 words: " + e.getMessage());
	    }
	    System.out.println("Google-1000 words loaded successfully. Total size: " + WordEmbeddings.getGoogle1000Embeddings().size());
	}
}
	