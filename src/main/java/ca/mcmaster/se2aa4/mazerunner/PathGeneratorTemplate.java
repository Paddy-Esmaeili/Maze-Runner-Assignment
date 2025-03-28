package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

abstract class PathGeneratorTemplate implements PathGenerator {
    protected int x, y;
    protected String direction = "right";     
    protected final char[][] maze;
    public final StringBuilder path;
    private final Set<String> visited;     
    private final int exitX, exitY;

    public PathGeneratorTemplate(AbstractMaze maze) {
        this.maze = maze.getGrid();
        this.x = maze.getEntryX();
        this.y = maze.getEntryY();
        this.exitX = maze.getExitX();
        this.exitY = maze.getExitY();
        this.path = new StringBuilder();
        this.visited = new HashSet<>();
    }

    public final String findPath(){
        markVisitedPaths();
        isValidMove(x, y, direction);
        backTrack();
        return path.toString();
    }

    // Hook methods
    protected abstract void markVisitedPaths();
    protected abstract void backTrack();
    protected abstract boolean isValidMove(int x, int y, String direction);

    protected void goForward() {
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

    protected void turnRight() {
        path.append("R");
        direction = getRightDirection(direction);
    }

    protected void turnLeft() {
        path.append("L");
        direction = getLeftDirection(direction);
    }

    protected boolean wallOnFront() {
        return !isValidMove(x, y, direction);
    }

    protected boolean wallOnRight() {
        return !isValidMove(x, y, getRightDirection(direction));
    }

    protected boolean wallOnLeft() {
        return !isValidMove(x, y, getLeftDirection(direction));
    }

    protected boolean isWall(int x, int y) {
        if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length) {
            return true; 
        }
        return maze[x][y] == '#';
    }

    protected boolean isExit(int x, int y) {
        return x == exitX && y == exitY;
    }

    private String getRightDirection(String currentDirection) {
        switch (currentDirection) {
            case "right": return "down";
            case "down": return "left";
            case "left": return "up";
            case "up": return "right";
            default: return currentDirection;
        }
    }

    private String getLeftDirection(String currentDirection) {
        switch (currentDirection) {
            case "right": return "up";
            case "up": return "left";
            case "left": return "down";
            case "down": return "right";
            default: return currentDirection;
        }
    }
}
