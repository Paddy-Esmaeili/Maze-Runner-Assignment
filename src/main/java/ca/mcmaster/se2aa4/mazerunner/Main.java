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
    
        int entryX = -1, entryY = -1, exitX = -1, exitY = -1;
    
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][0] == ' ' || Character.isWhitespace(grid[i][0])) {
                entryX = i;
                entryY = 0;
            }
        }

        for (int i = 0; i < grid.length; i++) {
            int cols = grid[i].length - 1;
            char lastColumnChar = grid[i][cols];

            if (lastColumnChar == ' ' || Character.isWhitespace(lastColumnChar)) {
                exitX = i;
                exitY = grid[i].length - 1;
            }
        }
        
        if (entryX == -1 || entryY == -1) {
            throw new IllegalArgumentException("Maze must have valid entry points.");
        } 
        if (exitX == -1 || exitY == -1){
            throw new IllegalArgumentException("Maze must have valid exit points.");
        }
        
        return new Maze(grid, entryX, entryY, exitX, exitY);
    }

    private static boolean containsEmptySpace(char[] row) {
        for (char c : row) {
            if (c == ' ') {
                return true;
            }
        }
        return false;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getEntryX() {
        return entryX;
    }

    public int getEntryY() {
        return entryY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public boolean isWall(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[x].length && grid[x][y] == '#';
    }
    
}
