package ie.atu.sw;

import java.io.IOException;

/**
* Abstract base class for file loading operations.
* Defines the contract for file loading implementations.
* 
* @author Tiffany Yong Ngik Chee
*/
public abstract class AbstractFileLoader {
	/**
	 * Loads and processes a file from the given path.
	 *
	 * @param filePath Path to the file to be loaded
	 * @throws IOException If there are errors reading the file
	 */
    public abstract void loadFile(String filePath) throws IOException;
}
