package ie.atu.sw;

import java.io.*;

/**
* Provides file loading functionality with error handling and efficient buffered reading.
* Reads entire text files into memory as strings while maintaining line separators.
* 
* Space Complexity: O(N) where N is the size of the input file
*
* @author Tiffany Yong Ngik Chee
*/
public class FileLoader {
   /**
	* Reads an entire file into a String, preserving line separators.
	* Uses buffered reading for efficient file I/O operations.
	* 
	* Time Complexity: O(N) where N is the file size in characters
	* Space Complexity: O(N) for storing file contents
	* 
	* Key operations:
	* - File validation: O(1)
	* - Reading lines: O(N)
	* - String building: O(N)
	*
	* @param filePath Path to the file to be read
	* @return String containing entire file contents
	* @throws IOException If file doesn't exist, isn't readable, or has read errors
	*/
    public static String readFileToString(String filePath) throws IOException {
    	// O(1) file validation
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new IOException("File does not exist or cannot be read: " + filePath);
        }
        // Initialize StringBuilder for efficient string concatenation
        // Initial capacity based on file size could be an optimization
        StringBuilder fileContents = new StringBuilder();
        
        // O(N) reading operation where N is file size
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line, O(N) total operations
            while ((line = reader.readLine()) != null) {
                fileContents.append(line).append(System.lineSeparator());
            }
        }
        // O(N) operation for final string conversion and trim
        return fileContents.toString().trim(); // Trim to remove trailing newlines
    }

  
}
