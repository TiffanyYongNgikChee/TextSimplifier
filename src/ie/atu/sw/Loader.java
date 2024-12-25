package ie.atu.sw;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Loader {
	
	// Concurrent map to store GloVe word embeddings: word -> vector
    private static Map<String, double[]> wordEmbeddings = new ConcurrentHashMap<>();
    private static Map<String, double[]> google1000Embeddings = new ConcurrentHashMap<>();
    //private static ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    static boolean filespecified = false; //check if the file specified (word-embeddings.txt)
    static boolean googlefilespecified = false; //check if the file specified (google-1000.txt)
    private static String inputSentence = "";
    
    /**
     * Loads the GloVe embeddings file into memory.
     * @param filePath The path to the GloVe embeddings file.
     */
	public static void loadWordEmbeddings(String filePath) {
		
		System.out.println("\nLoading word embeddings from: " + filePath);
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            // Read the embeddings file line by line
            while ((line = br.readLine()) != null) {
            	//Split each line into parts seperated by ', '
            	//running time: o(1)
                String[] parts = line.split(", ");
                //Extract the word from the first part
                String word = parts[0];
                //Create an array to store the embedding vector
                double[] embedding = new double[parts.length - 1];
                //Parse and store each value of the embedding vector
                //running time: o(n^2) - loop iteration and parsing and assigning
                for (int i = 1; i < parts.length; i++) {
                    embedding[i - 1] = Double.parseDouble(parts[i]);
                }
                //Add the word and its embedding vector to the map
                //running time: o(1) - adding each word to the map
                wordEmbeddings.put(word, embedding);
                filespecified = true;
            }
        } catch (Exception e) {
            e.printStackTrace(); //if an exception occurs during file reading or parsing
        }
		
    }
	
	//To indicate whether file has been specified or not
	public static boolean checkFileSpecified()
	{
		//running time: o(1) - no loops
		return filespecified;
	}
	
	public static void loadGoogle1000Words(String filePath) {
		
        System.out.println("Loading Google-1000 words and their embeddings from: " + filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;

            while ((word = br.readLine()) != null) {
                word = word.trim().toLowerCase();; // Remove any extra spaces
                double[] embedding = wordEmbeddings.get(word); // Get the embedding
                if (embedding != null) {
                    google1000Embeddings.put(word, embedding); // Add to the ConcurrentHashMap
                } else {
                    System.out.println("No embedding found for Google-1000 word: " + word);
                }
            }
            googlefilespecified = true;

            System.out.println("Google-1000 words loaded successfully. Total size: " + google1000Embeddings.size());
        } catch (IOException e) {
            System.err.println("Error loading Google-1000 words: " + e.getMessage());
        }
		
    }
	
	//To indicate whether google 1000 file has been specified or not
	public static boolean checkGoogleFileSpecified()
	{
		//running time: o(1) - no loops
		return googlefilespecified;
	}
	public static void createOutputFile(String filename) throws IOException {
    	//running time: o(1) - no loops
    	//create file to write to the specified 'filename'
        PrintWriter outputFile = new PrintWriter(filename);
        //print header
        outputFile.println("					Result of Simplifying Text");
        outputFile.println("------------------------------------------------------------------");
        //close the file
        outputFile.close();
    }
	
	/**
     * Reads the contents of a file and returns it as a single string.
     *
     * @param filePath the path to the input file
     * @return the file contents as a string
     * @throws IOException if an I/O error occurs
     */
    public static String readFileToString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new IOException("File does not exist or cannot be read: " + filePath);
        }

        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line).append(System.lineSeparator());
            }
        }
        inputSentence = fileContents.toString().trim(); // Trim to remove trailing newlines
        return inputSentence;
    }
    
    /**
	 * Method to compute cosine similarity between two vectors
	 * 
	 * @param vec1 - first vector
	 * @param vec2 - second vector
	 */
    private static double cosineSimilarity(double[] vec1, double[] vec2) {
    	//running time: o(n), methods iterate over each element of the vectors once to perform calculation
        double dotProduct = 0.0;
        double normVec1 = 0.0;
        double normVec2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normVec1 += Math.pow(vec1[i], 2);
            normVec2 += Math.pow(vec2[i], 2);
        }

        double similarity = dotProduct / (Math.sqrt(normVec1) * Math.sqrt(normVec2));
        return similarity;
    }
    
    /**
     * Simplifies the input sentence by replacing words with their most similar counterparts
     * from the Google-1000 word list based on cosine similarity.
     *
     * @param inputSentence The sentence to simplify.
     * @return The simplified sentence.
     */
    public static String simplifySentence(String inputSentence) {
        if (inputSentence == null || inputSentence.isEmpty()) {
            return ""; // If input is empty, return an empty string
        }

        // Split the sentence into words
        String[] words = inputSentence.split("\\s+"); // Split by whitespace
        StringBuilder simplifiedSentence = new StringBuilder();

        // Process each word
        for (String word : words) {
            String simplifiedWord = simplifyWord(word.toLowerCase());
            simplifiedSentence.append(simplifiedWord).append(" ");
        }

        // Return the simplified sentence as a string
        return simplifiedSentence.toString().trim();
    }

    /**
     * Finds the most similar word from the Google-1000 word list for the given word.
     *
     * @param word The word to simplify.
     * @return The most similar word from the Google-1000 list or the original word if no match is found.
     */
    private static String simplifyWord(String word) {
        if (!wordEmbeddings.containsKey(word)) {
            return word; // If the word has no embedding, return it as is
        }

        double[] targetEmbedding = wordEmbeddings.get(word);
        String bestMatch = word;
        double highestSimilarity = -1.0;

        // Compare with each Google-1000 word
        for (Map.Entry<String, double[]> entry : google1000Embeddings.entrySet()) {
            double similarity = cosineSimilarity(targetEmbedding, entry.getValue());
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = entry.getKey();
            }
        }

        return bestMatch;
    }
    
    /**
     * Appends the simplified text to the output file. If the file does not exist,
     * it creates one with the required header.
     *
     * @param filename The name of the output file.
     * @param result   The text to append to the file.
     * @throws IOException if an I/O error occurs.
     */
    public static void appendToOutputFile(String filename, String result) throws IOException {
        File file = new File(filename);

        // Check if the file exists; if not, create it with a header
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("                    Result of Simplifying Text");
                writer.println("------------------------------------------------------------------");
            }
        }

        // Append the result to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) { // 'true' enables appending
            writer.println(result);
            writer.println(); // Add an extra newline for separation
        }
    }


}
