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
        options.addOption("p", true, "Validating user's input path");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String inputFile = cmd.getOptionValue("i");
            String validInput = cmd.getOptionValue("p");

            if (inputFile == null) {
                throw new IllegalArgumentException("Error: i flag was not detected");
            }

            Maze maze = Maze.loadMazeFromFile(inputFile);
            logger.info("**** Reading the maze from file: {}", inputFile);

            SimplePathGenerator simpleGenerator = new SimplePathGenerator();
            String canonicalPath = simpleGenerator.findPath(maze);

            if (validInput != null) {
                logger.info("**** Validating path");
                PathValidator validator = new PathValidator();
                if (validator.checkPath(maze, validInput)) {
                    logger.info("correct path");
                } else {
                    logger.info("incorrect path");
                }
            }

            logger.info("**** Computing path");
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

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tempGrid.add(line.toCharArray());
            }
        }

        char[][] grid = tempGrid.toArray(new char[0][]);
        int entryX = -1, entryY = -1, exitX = -1, exitY = -1;

        //detect entry points
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][0] == ' ') {
                entryX = i;
                entryY = 0;
                break;
            }
        }
        
        //detect exit points
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][grid[i].length-1] == ' ') {
                exitX = i;
                exitY = grid.length-1;
            }
        }

        if (entryX == -1 || entryY == -1) {
            throw new IllegalArgumentException("Maze must have a valid entry point on the left side.");
        }
        if (exitX == -1 || exitY == -1) {
            throw new IllegalArgumentException("Maze must have a valid exit point on the right side.");
        }

        return new Maze(grid, entryX, entryY, exitX, exitY);
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

    //check where the walls are
    public boolean isWall(int x, int y) {
        return grid[x][y] == '#';
    }

    //check if it reached the exit point
    public boolean isExit(int x, int y) {
        return x == exitX && y == exitY;
    }
}

class SimplePathGenerator {
    public String findPath(Maze maze) {
        StringBuilder path = new StringBuilder();
        boolean[][] visited = new boolean[maze.getGrid().length][maze.getGrid()[0].length];   //cells that were visited before

        int x = maze.getEntryX();
        int y = maze.getEntryY();

        if (findPathRecursive(maze, x, y, visited, path)) {
            return path.toString();
        } else {
            System.out.println("Error: No possible paths were found.");
            return "";    //return an empty string as the path
        }
    }

    private boolean findPathRecursive(Maze maze, int x, int y, boolean[][] visited, StringBuilder path) {
        if (maze.isExit(x, y)) {
            return true;
        }

        visited[x][y] = true;

        // Moving East
        if (y + 1 < maze.getGrid()[x].length && !maze.isWall(x, y + 1) && !visited[x][y + 1]) {
            path.append("F");
            if (findPathRecursive(maze, x, y + 1, visited, path)) {
                return true;
            }

            path.deleteCharAt(path.length() - 1);
        }

        //Moving South
        if (x + 1 < maze.getGrid().length && !maze.isWall(x + 1, y) && !visited[x + 1][y]) {
            path.append("R");
            if (findPathRecursive(maze, x + 1, y, visited, path)) {
                return true;
            }
            path.deleteCharAt(path.length() - 1);
        }

        // Moving North
        if (x - 1 >= 0 && !maze.isWall(x - 1, y) && !visited[x - 1][y]) {
            path.append("L");
            if (findPathRecursive(maze, x - 1, y, visited, path)) {
                return true;
            }
            path.deleteCharAt(path.length() - 1);
        }

        visited[x][y] = false;
        return false;
    }
}
class PathValidator {
    public boolean checkPath(Maze maze, String path) {
        int x = maze.getEntryX();
        int y = maze.getEntryY();

        for (char move : path.toCharArray()) {
            //The path cannot contain any characters other than F, R, and L
            if (move != 'F' && move != 'R' && move != 'L') {
                return false; 
            }

            // Move based on the user's input
            switch (move) {
                case 'F': // Move forward 
                    if (y + 1 < maze.getGrid()[x].length && !maze.isWall(x, y + 1)) {
                        y++;
                    } else {
                        return false; // Hits a wall
                    }
                    break;
                case 'R': // Move right 
                    if (x + 1 < maze.getGrid().length && !maze.isWall(x + 1, y)) {
                        x++;
                    } else {
                        return false; // Hits a wall
                    }
                    break;
                case 'L': // Move left 
                    if (x - 1 >= 0 && !maze.isWall(x - 1, y)) {
                        x--;
                    } else {
                        return false; // Hits a wall
                    }
                    break;
                default:
                    return false;   
            }
        }
        
        //return true if the path hits no walls
        return true;
    }
}
