package ie.atu.sw;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
* Concrete implementation of file loader that processes embedding files using virtual threads.
* Uses parallel processing to efficiently load and parse large embedding files.
* 
* Space Complexity: O(N * D) where:
* - N is number of words in embedding file
* - D is dimension of embedding vectors
*
* @author Tiffany Yong Ngik Chee
*/
public class ConcreteFileLoader extends AbstractFileLoader {
	
	/**
	 * Loads and processes an embedding file using virtual threads for parallel processing.
	 * Each line is processed in a separate virtual thread for optimal performance.
	 * 
	 * Time Complexity: O(N * D) where:
	 * - N is number of lines in file
	 * - D is dimension of embedding vectors
	 * - Actual time reduced by parallel processing
	 * 
	 * Space Complexity: O(N * D) for storing embeddings
	 *
	 * @param filePath Path to the embedding file
	 * @throws IOException If there are errors reading the file
	 */
    @Override
    public void loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // O(N) operations distributed across virtual threads
        try (var executor = Executors.newVirtualThreadPerTaskExecutor();
             Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> executor.submit(() -> processLine(line)));
        }
    }
    
    /**
     * Processes a single line from the embedding file.
     * Parses the line into word and embedding vector components.
     * 
     * Time Complexity: O(D) where D is embedding dimension
     * Space Complexity: O(D) for storing single embedding
     *
     * @param line Single line from embedding file containing word and vector values
     */
    private void processLine(String line) {
        // Implement the line processing logic
    	// O(D) parsing operations
        String[] parts = line.split(", ");
        String word = parts[0];
        double[] embedding = new double[parts.length - 1];
        
        // O(D) loop for parsing vector values
        for (int i = 1; i < parts.length; i++) {
            embedding[i - 1] = Double.parseDouble(parts[i]);
        }
        // O(1) operation to store embedding
        WordEmbeddings.addWordEmbedding(word, embedding);
    }
}
