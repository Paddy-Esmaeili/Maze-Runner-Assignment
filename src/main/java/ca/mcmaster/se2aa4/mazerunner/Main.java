package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", true, "Path to the maze file");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String inputFile = cmd.getOptionValue("i");
            if (inputFile == null) {
                throw new IllegalArgumentException("Maze file path is required. Use -i <file_path>");
            }

            logger.info("**** Reading the maze from file: {}", inputFile);

            Maze maze = Maze.loadMazeFromFile(inputFile);
            PathGenerator generator = new PathGenerator();

            logger.info("**** Computing path");
            String canonicalPath = generator.findPath(maze);
            logger.info("Canonical Path: {}", canonicalPath);

        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
        logger.info("** End of Maze Runner");
    }
}

class Maze {
    private final char[][] grid;
    private final int entryX, entryY, exitX, exitY;

    public Maze(char[][] grid, int entryX, int entryY, int exitX, int exitY) {
        this.grid = grid;
        this.entryX = entryX;
        this.entryY = entryY;
        this.exitX = exitX;
        this.exitY = exitY;
    }

    public static Maze loadMazeFromFile(String inputFile) throws Exception {
        List<char[]> tempGrid = new ArrayList<>();
        int gridWidth = -1;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            boolean enteredMaze = false; 
    
            while ((line = reader.readLine()) != null) {
                line = line.stripTrailing(); 
    
                if (!enteredMaze && line.isEmpty()) {
                    continue;
                }
    
                if (!line.isEmpty()) {
                    enteredMaze = true; 
                }
    
                if (enteredMaze) {
                    if (gridWidth == -1) {
                        int firstHashIndex = line.indexOf('#');
                        int lastHashIndex = line.lastIndexOf('#');
                        if (firstHashIndex == -1 || lastHashIndex == -1) {
                            throw new IllegalArgumentException("Illegal bordering");
                        }
                        gridWidth = lastHashIndex - firstHashIndex + 1;     
                        line = line.substring(firstHashIndex, lastHashIndex + 1);
                    }
                    tempGrid.add(line.toCharArray());
                }
            }
        }
    
        char[][] grid = tempGrid.toArray(new char[0][]);
    
        if (containsEmptySpace(grid[0]) || containsEmptySpace(grid[grid.length - 1])) {
            throw new IllegalArgumentException("First and last rows of the maze cannot contain empty spaces.");
        }
    
    }
}
