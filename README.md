# Sentence Simplifier using Word Embeddings and Virtual Threads (Java)

This project is a Java-based command-line application that simplifies English sentences by replacing complex words with simpler alternatives from Google's top 1000 common words. It uses pre-trained word embeddings and cosine similarity to preserve the meaning of the sentence. The system is designed with modular object-oriented principles and is compatible with Java's virtual threads (Project Loom) for scalable, responsive execution.

## Features

- Text simplification using semantic similarity
- Cosine similarity calculation between word vectors
- Integration with Google-1000 common words list
- Support for pre-trained word embedding files (e.g., GloVe-style)
- Console-based interactive user interface
- File input/output handling for batch processing
- Designed with virtual thread support for future concurrency

## Technologies Used

- Java 19+ (for virtual thread support)
- Object-Oriented Design
- Word Embeddings
- Cosine Similarity
- File I/O and Console Interaction

## Project Structure
src/
└── ie/atu/sw/
├── Runner.java # Main application entry point with menu and control logic
├── SentenceProcessor.java # Core text simplification logic
├── WordEmbeddings.java # Handles embedding dictionary loading and lookup
├── ConcreteFileLoader.java # Implements file loading for embedding files
├── Loader.java # Loads the Google-1000 word list
├── SaveToFile.java # Utility for writing simplified output to file
├── FileLoader.java # Reads input files as strings
└── ConsoleColour.java # Adds color formatting to console output

## How It Works

1. Load a word embedding file that maps words to vector representations.
2. Load a list of the top 1000 most common English words.
3. Input a sentence or load a file containing sentences to simplify.
4. Each word in the input sentence is compared with the 1000 common words using cosine similarity.
5. Words are replaced by their closest simpler equivalent if a match is found.
6. The simplified output is displayed and optionally written to a file.

## How to Run
```bash
javac --enable-preview --release 19 ie/atu/sw/*.java
java --enable-preview ie.atu.sw.Runner
```

### Requirements

- Java 19 or later (with preview features enabled)
