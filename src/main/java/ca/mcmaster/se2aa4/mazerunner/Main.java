package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("i", true, "Path to the maze file");
        options.addOption("p", true, "Validating user's input path");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String inputFile = cmd.getOptionValue("i");
            String validInput = cmd.getOptionValue("p");

            if (inputFile == null) {
                throw new IllegalArgumentException("Error: -i flag was not detected");
            }

            Maze maze = new Maze(inputFile);

            PathGenerator generator = new RHRPathGenerator(maze);
            String canonicalPath = generator.findPath();

            FactorizedPath factorizedPath = new FactorizedPath(canonicalPath);
            String factorized = factorizedPath.factorizePath();
            System.out.println(factorized);

            if (validInput != null) {
                PathChecker validator = new PathValidator();
                if (validator.checkPath(maze, validInput)) {
                    System.out.println("correct path");
                } else {
                    System.out.println("incorrect path");
                }
            }
        } catch (Exception e) {
            System.out.println("Sorry, an error occurred!");
        }
    }
}
abstract class AbstractMaze {
    protected char[][] grid;
    protected int entryX = -1, entryY = -1, exitX = -1, exitY = -1;    // Initialize the entry and exit points to -1 (Invalid value)

    public AbstractMaze(char[][] grid) {
        this.grid = grid;
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

    public boolean isWall(int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {     // Check for boundry cases
            return true;  
        }
        return grid[x][y] == '#';    // Check for actuall walls
    }
}

class Maze extends AbstractMaze {
    public Maze(String inputFile) throws Exception {
        super(loadGrid(inputFile));
        findEntryExit();
    }

    private static char[][] loadGrid(String inputFile) throws IOException {       // Save maze to a an array
        List<char[]> tempGrid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tempGrid.add(line.toCharArray());
            }
        }
        if (tempGrid.isEmpty()) {
            throw new IllegalArgumentException("Error: The inputted maze file is empty.");
        }
        return tempGrid.toArray(new char[0][]);
    }

    private void findEntryExit() {
        // Check for entry and exit points in the first and last column of the maze. 
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][0] == ' ' && entryX == -1) {
                entryX = i;
                entryY = 0;
            }

            if (grid[i][grid[0].length - 1] == ' '  && exitX == -1) {
                exitX = i;
                exitY = grid[0].length - 1;
            }
        }
    
        // Check if entry or exit was found
        if (entryX == -1) {
            throw new IllegalArgumentException("Maze must have a valid entry point on the left side.");
        }
        if (exitX == -1) {
            throw new IllegalArgumentException("Maze must have a valid exit point on the right side.");
        }
    }
}

interface PathGenerator {
    String findPath();
}

class RHRPathGenerator implements PathGenerator {
    private int x, y;
    private String direction = "right";     // Initializing the direction of the movement to East
    private final char[][] maze;
    public final StringBuilder path;
    private final Set<String> visited;     // Saving the coordinated of the visited cells
    private final int exitX, exitY;

    public RHRPathGenerator(AbstractMaze maze) {
        this.maze = maze.getGrid();  
        this.x = maze.getEntryX();
        this.y = maze.getEntryY();
        this.exitX = maze.getExitX();
        this.exitY = maze.getExitY();
        this.path = new StringBuilder();
        this.visited = new HashSet<>();  // Save the coordinates of the cells that were already visited
    }

    public String findPath() {
        while (!isExit(x, y)) {
            String pos = x + "," + y;

            // Stuck in a loop
            if (visited.contains(pos) && wallOnRight() && wallOnFront() && wallOnLeft()) {
                break; 
            }

            visited.add(pos);

            // Implementing the right hand rule
            if (!wallOnRight()) {
                turnRight();
                if (!wallOnFront()) {  
                    goForward();
                }
            } else if (!wallOnFront()) {
                goForward();
            } else {
                turnLeft();
            }
        }
        return path.toString();    // Return the canonical path as a string
    }


    private void goForward() {
        int newX = x, newY = y;
    
        switch (direction) {
            case "right" -> newY++;
            case "up" -> newX--;
            case "left" -> newY--;
            case "down" -> newX++;
        }
    
        if (!isWall(newX, newY)) { 
            x = newX;
            y = newY;
            path.append("F");
        }
    }

    private void turnRight() {
        path.append("R");
        direction = switch (direction) {
            case "right" -> "down";
            case "down" -> "left";
            case "left" -> "up";
            case "up" -> "right";
            default -> direction;
        };
    }

    private void turnLeft() {
        path.append("L");
        direction = switch (direction) {
            case "right" -> "up";
            case "up" -> "left";
            case "left" -> "down";
            case "down" -> "right";
            default -> direction;
        };
    }

    private boolean wallOnFront() {
        return !isValidMove(x, y, direction);
    }

    private boolean wallOnRight() {
        return !isValidMove(x, y, getRightDirection(direction));
    }

    private boolean wallOnLeft() {
        return !isValidMove(x, y, getLeftDirection(direction));
    }

    private boolean isValidMove(int x, int y, String direction) {
        return switch (direction) {
            case "right" -> !isWall(x, y + 1);
            case "up" -> !isWall(x - 1, y);
            case "left" -> !isWall(x, y - 1);
            case "down" -> !isWall(x + 1, y);
            default -> false;
        };
    }

    private String getRightDirection(String currentDirection) {
        return switch (currentDirection) {
            case "right" -> "down";
            case "down" -> "left";
            case "left" -> "up";
            case "up" -> "right";
            default -> currentDirection;
        };
    }

    private String getLeftDirection(String currentDirection) {
        return switch (currentDirection) {
            case "right" -> "up";
            case "up" -> "left";
            case "left" -> "down";
            case "down" -> "right";
            default -> currentDirection;
        };
    }

    public boolean isWall(int x, int y) {
        return maze[x][y] == '#';
    }

    private boolean isExit(int x, int y) {
        return x == exitX && y == exitY;
    }
}

class FactorizedPath {
    private String path;

    public FactorizedPath(String path) {
        this.path = path;
    }

    public String factorizePath() {
        StringBuilder factorizedPath = new StringBuilder();
        int count = 1;
    
        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == path.charAt(i - 1)) {
                count++;
            } else {
                factorizedPath.append(count).append(path.charAt(i - 1));
                count = 1;
            }
        }

        factorizedPath.append(count).append(path.charAt(path.length() - 1));
    
        return factorizedPath.toString();
    }
}

interface PathChecker {
    boolean checkPath(AbstractMaze maze, String path);
}

class PathValidator implements PathChecker {
    public boolean checkPath(AbstractMaze maze, String path) {

        // Convert to canonical form if the path is factorized
        for (int i = 0; i < path.length(); i++) {
            if (Character.isDigit(path.charAt(i))) {
                path = deconstructFactorizedPath(path);
            }
        }

        int x = maze.getEntryX();
        int y = maze.getEntryY();
        String direction = "right";

        for (char move : path.toCharArray()) {
            switch (move) {
                case 'F': {
                    if (maze.isWall(x, y)) return false;
                    switch (direction) {
                        case "right" -> y++;
                        case "up" -> x--;
                        case "left" -> y--;
                        case "down" -> x++;
                    }
                    break;
                }
                case 'R':
                    direction = switch (direction) {
                        case "right" -> "down";
                        case "down" -> "left";
                        case "left" -> "up";
                        case "up" -> "right";
                        default -> direction;
                    };
                    break;
                case 'L':
                    direction = switch (direction) {
                        case "right" -> "up";
                        case "up" -> "left";
                        case "left" -> "down";
                        case "down" -> "right";
                        default -> direction;
                    };
                    break;
                default:
                    return false;
            }
        }
        return x == maze.getExitX() && y == maze.getExitY();
    }

    // Expanding the inputted factorized path by the user
    private String deconstructFactorizedPath(String path) {
        StringBuilder expandedPath = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char currentChar = path.charAt(i);
            if (Character.isDigit(currentChar)) {
                int repeatCount = Character.getNumericValue(currentChar);
                if (i + 1 < path.length()) {
                    char nextChar = path.charAt(i + 1);
                    expandedPath.append(String.valueOf(nextChar).repeat(repeatCount));
                    i++; 
                }
            } else {
                expandedPath.append(currentChar);
            }
        }
        return expandedPath.toString();
    }
}
