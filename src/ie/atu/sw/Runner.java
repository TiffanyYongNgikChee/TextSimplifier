package ie.atu.sw;

import java.io.*;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws Exception {
		
		Scanner scanner = new Scanner(System.in); //Initialise scanner to read user input
		int choice; //to store user choice for options 1-7
		boolean fileSpecified_wordembeddings = false; //flag used to indicate whether file has been specified or not
		boolean fileSpecified_google = false; //flag used to indicate whether google 1000 has been specified or not
		String inputFile = null;
		String sentenceInput = "";
		String outputFile = "./out.txt"; //default output file
		System.out.println(ConsoleColour.BLACK_BOLD);
		int algorithm_option = 1; //Default similarity algorithm option

	
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
		
		do {
				System.out.println(ConsoleColour.BLACK_BOLD);
				// Display menu and process user input
				System.out.println("-----------------------------------MENU-------------------------------------");
				System.out.println("	(1) Specify Embedding File						");
				System.out.println("	(2) Specify Google 1000 File");
	            System.out.println("	(3) Specify an Output File (default: ./out.txt)");
	            System.out.println("	(4) Specify an Input File");
				System.out.println("	(5) Execute, Analyse and Report from the input File 				");
				System.out.println("	(6) Quit								");
					
				// Prompt user to select an option
				System.out.println("Select Option [1-6]>								");
				choice = scanner.nextInt();
				System.out.println();
			
				if(choice == 6)
				{
					System.out.println(ConsoleColour.YELLOW_BACKGROUND);
					System.out.println("Exit.....");
					System.out.println("Save the result to File " + outputFile);
					System.out.println(ConsoleColour.RESET);
					System.out.println(ConsoleColour.BLACK_BOLD);
					break;
				}
				//Check if the file hasn't been specified yet
				else if(fileSpecified_wordembeddings == false && choice != 1)
				{
					System.out.println(ConsoleColour.RED_BACKGROUND);
					System.out.print("Sorry, please specify the file path in option 1 to enable access to functions 2-5");
					System.out.println(ConsoleColour.RESET);
					System.out.println(ConsoleColour.BLACK_BOLD);
					continue;
				}
				
				switch(choice)
				{
				case 1:
					//Parse a text file containing 59,602 word embeddings into a map data structure 
					//Prompt user to enter file path for word embeddings
					System.out.println("Enter the file path for word embeddings:");
					scanner.nextLine(); 
			        String filename = scanner.nextLine(); 
					loading(); // Display loading message
					Loader.loadWordEmbeddingsConcurrently(filename); // The file path is assumed to be relative to the current directory.
					fileSpecified_wordembeddings = Loader.checkFileSpecified(); //flag used to indicate whether file has been specified or not
					if(fileSpecified_wordembeddings == true)
					{
						System.out.println(ConsoleColour.GREEN_BACKGROUND); // Print success message in green background
						System.out.println("\nSuccessfully load the word-embeddings file...  \t");
					}
					else
					{
						System.out.println(ConsoleColour.RED_BACKGROUND); // Print failure message in red background
						System.out.println("Failed to load the word-embeddings file. Please check the file path and try again.");
					}
					System.out.println(ConsoleColour.RESET);
					break;
				case 2:
					//Prompt user to enter file path for word embeddings
					System.out.println("Enter the file path for word embeddings:");
					scanner.nextLine(); 
			        String filename2 = scanner.nextLine(); 
					loading(); // Display loading message
					Loader.loadGoogle1000Words(filename2); // The file path is assumed to be relative to the current directory.
					fileSpecified_google = Loader.checkGoogleFileSpecified(); //flag used to indicate whether file has been specified or not
					if(fileSpecified_google == true)
					{
						System.out.println(ConsoleColour.GREEN_BACKGROUND); // Print success message in green background
						System.out.println("\nSuccessfully load the google file...  \t");
					}
					else
					{
						System.out.println(ConsoleColour.RED_BACKGROUND); // Print failure message in red background
						System.out.println("Failed to load the google file. Please check the file path and try again.");
					}
					System.out.println(ConsoleColour.RESET);
					break;
				case 3:
					//Output the top n most similar words to a text file. 
					// Prompt the user to enter the file path for the output
			        System.out.println("Enter the file path for the output:");
			        scanner.nextLine();
			        outputFile = scanner.nextLine();
					break;
				case 4:
					String userInputFile;
					System.out.println("Enter the file path for the input text file");
					scanner.nextLine();
					userInputFile = scanner.nextLine();
			        File file = new File(userInputFile);

			        if (file.exists() && file.isFile() && file.canRead()) {
			        	System.out.println(ConsoleColour.GREEN_BACKGROUND); 
			        	inputFile = userInputFile;
			            System.out.println("File exists and is readable: " + userInputFile);
			            System.out.println(ConsoleColour.RESET);
			            // Proceed with processing the file
			            sentenceInput = Loader.readFileToString(userInputFile);
			            
			        } else {
			        	System.out.println(ConsoleColour.RED_BACKGROUND); 
			            System.out.println("The specified file does not exist or cannot be read. Please check the file path.");
			            System.out.println(ConsoleColour.RESET);
			        }
					break;
				case 5:
					if(fileSpecified_google==false) {
						continue;
					}
					else if(inputFile == null) {
						continue;
					}
					else {
						System.out.println("analysis....");				
						System.out.println(Loader.simplifySentence(sentenceInput));
						
						// Save the simplified sentence to the output file
				        Loader.appendToOutputFile(outputFile, Loader.simplifySentence(sentenceInput));
				        System.out.println("Simplified sentence saved to: " + outputFile);
					}
					break;
				case 6:
					System.out.println(ConsoleColour.YELLOW_BACKGROUND);
					System.out.println("Goodbye");					
					break;
				default:
					System.out.println(ConsoleColour.RED_BACKGROUND);
					System.out.println("This is not a number between 1 and 7, Please try again....");
					System.out.println(ConsoleColour.BLACK_BOLD);
					break;
				}
			}while(choice!=6);
		
	}
	public static void loading() throws Exception {
		//progress meter
		System.out.print(ConsoleColour.BLACK);	//Change the colour of the console text
		int size = 100;							//The size of the meter. 100 equates to 100%
		for (int i =0 ; i < size ; i++) {		//The loop equates to a sequence of processing steps
			printProgress(i + 1, size); 		//After each (some) steps, update the progress meter
			Thread.sleep(10);					//Slows things down so the animation is visible 
		}
	}
	
	public static void printProgress(int index, int total) {
		if (index > total) return;	//Out of range
        int size = 50; 				//Must be less than console width
	    char done = '█';			//Change to whatever you like.
	    char todo = '░';			//Change to whatever you like.
	    
	    //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;
        
        /*
         * A StringBuilder should be used for string concatenation inside a 
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.  
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
        	sb.append((i < completeLen) ? done : todo);
        }
        
        /*
         * The line feed escape character "\r" returns the cursor to the 
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");
        
        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }
}