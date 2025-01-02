package ie.atu.sw;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main application runner for the Text Simplification System.
 * Provides a command-line interface for users to interact with the text simplification
 * functionality. The system requires word embeddings and Google-1000 common words files
 * to perform text simplification using virtual threads.
 *
 * The application follows a specific workflow:
 * 1. Load word embeddings file (required first)
 * 2. Load Google-1000 common words file
 * 3. Specify input/output files
 * 4. Process and simplify text
 *
 * @author Tiffany Yong Ngik Chee
 */
public class Runner {

    public static void main(String[] args) throws Exception {
    	
    	// Initialize scanner and variables
        Scanner scanner = new Scanner(System.in); 
        int choice; 
        boolean fileSpecified_wordembeddings = false; 
        boolean fileSpecified_google = false; 
        String inputFile = null;
        String sentenceInput = "";
        String outputFile = "./out.txt"; 
        System.out.println(ConsoleColour.BLACK_BOLD);
        
        System.out.println(ConsoleColour.BLACK_BOLD);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*          Object Oriented Programming Assignment          *");
        System.out.println("*            -Virtual Threaded Text Simplifier-            *");
        System.out.println("*                                                          *");
        System.out.println("*                Tiffany Yong Ngik Chee                    *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        
        // Main program loop - O(n) iterations where n is number of user interactions
        do {
            System.out.println(ConsoleColour.BLACK_BOLD);
            System.out.println("-----------------------------------MENU-------------------------------------");
            System.out.println("    (1) Specify Embedding File                        ");
            System.out.println("    (2) Specify Google 1000 File");
            System.out.println("    (3) Specify an Output File (default: ./out.txt)");
            System.out.println("    (4) Specify an Input File");
            System.out.println("    (5) Execute, Analyse and Report from the input File             ");
            System.out.println("    (6) Quit                                ");
            
            // Get user input - O(1)
            System.out.println("Select Option [1-6]>                                ");
            choice = scanner.nextInt();
            System.out.println();
            
            //Association
            AbstractFileLoader fileLoader = new ConcreteFileLoader();
            
            // Exit condition check - O(1)
            if (choice == 6) {
                System.out.println(ConsoleColour.YELLOW_BACKGROUND);
                System.out.println("Exit.....");
                System.out.println("Save the result to File " + outputFile);
                System.out.println(ConsoleColour.RESET);
                System.out.println(ConsoleColour.BLACK_BOLD);
                break;
            } 
            // Validation check - O(1)
            else if (!fileSpecified_wordembeddings && choice != 1) {
                System.out.println(ConsoleColour.RED_BACKGROUND);
                System.out.print("Sorry, please specify the file path in option 1 to enable access to functions 2-5");
                System.out.println(ConsoleColour.RESET);
                System.out.println(ConsoleColour.BLACK_BOLD);
                continue;
            }
            
            // Menu options processing 
            switch (choice) {
                case 1:// Load word embeddings file
                	// File loading - O(f) where f is file size
                    System.out.println("Enter the file path for word embeddings:");
                    scanner.nextLine();
                    String filename = scanner.nextLine();
                    loading();
                    fileLoader.loadFile(filename);
                    fileSpecified_wordembeddings = WordEmbeddings.isFileSpecified();
                    if (fileSpecified_wordembeddings) {
                        System.out.println(ConsoleColour.GREEN_BACKGROUND);
                        System.out.println("\nSuccessfully load the word-embeddings file...  \t");
                    } else {
                        System.out.println(ConsoleColour.RED_BACKGROUND);
                        System.out.println("Failed to load the word-embeddings file. Please check the file path and try again.");
                    }
                    System.out.println(ConsoleColour.RESET);
                    break;
                case 2:// Load Google 1000 file
                	// File loading - O(f) where f is file size
                    System.out.println("Enter the file path for word embeddings:");
                    scanner.nextLine();
                    String filename2 = scanner.nextLine();
                    loading();
                    Loader.loadGoogle1000Words(filename2);
                    fileSpecified_google = WordEmbeddings.isGoogleFileSpecified();
                    if (fileSpecified_google) {
                        System.out.println(ConsoleColour.GREEN_BACKGROUND);
                        System.out.println("\nSuccessfully load the google file...  \t");
                    } else {
                        System.out.println(ConsoleColour.RED_BACKGROUND);
                        System.out.println("Failed to load the google file. Please check the file path and try again.");
                    }
                    System.out.println(ConsoleColour.RESET);
                    break;
                case 3:// Specify output file
                	// O(1) operation
                    System.out.println("Enter the file path for the output:"); // output file specification
                    scanner.nextLine();
                    outputFile = scanner.nextLine();
                    break;
                case 4: // Specify input file
                	// File reading - O(f) where f is file size
                    System.out.println("Enter the file path for the input text file:");
                    scanner.nextLine();
                    inputFile = scanner.nextLine();
                    File file = new File(inputFile);
                    // ... input file handling ...
                    if (file.exists() && file.isFile() && file.canRead()) {
                        System.out.println(ConsoleColour.GREEN_BACKGROUND);
                        sentenceInput = FileLoader.readFileToString(inputFile);
                        System.out.println("File exists and is readable: " + inputFile);
                        System.out.println(ConsoleColour.RESET);
                    } else {
                        System.out.println(ConsoleColour.RED_BACKGROUND);
                        System.out.println("The specified file does not exist or cannot be read. Please check the file path.");
                        System.out.println(ConsoleColour.RESET);
                    }
                    break;
                case 5:// Process text
                	// Text processing - O(w * e) where:
                    // w is number of input words
                    // e is size of embedding dictionary
                    // text processing and saving 
                    if (!fileSpecified_google || inputFile == null) {
                        continue;
                    } else {
                        System.out.println("analysis....");
                        String simplifiedSentence = SentenceProcessor.simplifySentence(sentenceInput);
                        System.out.println(simplifiedSentence);

                        SaveToFile.appendToOutputFile(outputFile, simplifiedSentence);
                        System.out.println("Simplified sentence saved to: " + outputFile);
                    }
                    break;
                default:
                	// O(1) operation
                    System.out.println(ConsoleColour.RED_BACKGROUND);
                    System.out.println("This is not a number between 1 and 7, Please try again....");
                    System.out.println(ConsoleColour.BLACK_BOLD);
                    break;
            }
        } while (choice != 6);
    }
    
    /**
     * Displays a loading animation with a progress bar.
     *
     */
    public static void loading() throws Exception {
        System.out.print(ConsoleColour.BLACK);
        int size = 100;
        for (int i = 0; i < size; i++) {
            printProgress(i + 1, size);
            Thread.sleep(10);
        }
    }
    
    /**
     * Renders a progress bar in the console.
     *
     * @param index Current progress value
     * @param total Total steps for completion
     */
    public static void printProgress(int index, int total) {
        if (index > total) return;
        int size = 50;
        char done = '█';
        char todo = '░';

        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        System.out.print("\r" + sb + "] " + complete + "%");

        if (index == total) System.out.println("\n");
    }
}
