package ie.atu.sw;

import java.io.*;

/**
* Handles file output operations for the text simplification results.
* Provides methods to create and append to output files with proper formatting.
* 
* Space Complexity: O(N) where N is the size of output text
*
* @author Tiffany Yong Ngik Chee
*/
public class SaveToFile {

	/**
	* Creates a new output file with header information.
	* Initializes the file with title and separator line.
	* 
	* Time Complexity: O(1) - constant time operations
	* Space Complexity: O(1) - uses constant extra space
	*
	* @param filename Name of the output file to create
	* @throws IOException If there are errors creating or writing to the file
	*/
    public static void createOutputFile(String filename) throws IOException {
    	// O(1) operation to create and write header
        try (PrintWriter outputFile = new PrintWriter(filename)) {
            outputFile.println("                    Result of Simplifying Text");
            outputFile.println("------------------------------------------------------------------");
        }
    }
    /**
     * Appends simplified text results to the output file.
     * Creates the file with headers if it doesn't exist.
     * 
     * Time Complexity: O(N) where N is length of result string
     * Space Complexity: O(N) for buffering the output
     *
     * @param filename Name of the output file
     * @param result Simplified text to append to the file
     * @throws IOException If there are errors writing to the file
     */
    public static void appendToOutputFile(String filename, String result) throws IOException {
        File file = new File(filename);
        // O(1) file existence check and creation if needed
        if (!file.exists()) {
            createOutputFile(filename);
        }
        // O(N) write operation where N is result length
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(result);
            writer.println();
        }
    }
}
