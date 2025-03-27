package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

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
    
        if (direction.equals("right")) {
            newY++;
        } else if (direction.equals("up")) {
            newX--;
        } else if (direction.equals("left")) {
            newY--;
        } else if (direction.equals("down")) {
            newX++;
        }
    
        if (!isWall(newX, newY)) { 
            x = newX;
            y = newY;
            path.append("F");
        }
    }

    private void turnRight() {
        path.append("R");
        if (direction.equals("right")) {
            direction = "down";
        } else if (direction.equals("down")) {
            direction = "left";
        } else if (direction.equals("left")) {
            direction = "up";
        } else if (direction.equals("up")) {
            direction = "right";
        }
    }

    private void turnLeft() {
        path.append("L");
        if (direction.equals("right")) {
            direction = "up";
        } else if (direction.equals("up")) {
            direction = "left";
        } else if (direction.equals("left")) {
            direction = "down";
        } else if (direction.equals("down")) {
            direction = "right";
        }
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
        if (direction.equals("right")) {
            return !isWall(x, y + 1);
        } else if (direction.equals("up")) {
            return !isWall(x - 1, y);
        } else if (direction.equals("left")) {
            return !isWall(x, y - 1);
        } else if (direction.equals("down")) {
            return !isWall(x + 1, y);
        }
        return false;
    }

    private String getRightDirection(String currentDirection) {
        if (currentDirection.equals("right")) {
            return "down";
        } else if (currentDirection.equals("down")) {
            return "left";
        } else if (currentDirection.equals("left")) {
            return "up";
        } else if (currentDirection.equals("up")) {
            return "right";
        }
        return currentDirection;
    }

    private String getLeftDirection(String currentDirection) {
        if (currentDirection.equals("right")) {
            return "up";
        } else if (currentDirection.equals("up")) {
            return "left";
        } else if (currentDirection.equals("left")) {
            return "down";
        } else if (currentDirection.equals("down")) {
            return "right";
        }
        return currentDirection;
    }

    public boolean isWall(int x, int y) {

        if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length) {
            return true; 
        }
        return maze[x][y] == '#';
        
    }

    private boolean isExit(int x, int y) {
        return x == exitX && y == exitY;
    }
}
