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

    // Commands 
    private final Command TURN_RIGHT;
    private final Command TURN_LEFT;
    private final Command GO_FORWARD;

    public PathGeneratorTemplate(AbstractMaze maze) {
        this.maze = maze.getGrid();
        this.x = maze.getEntryX();
        this.y = maze.getEntryY();
        this.exitX = maze.getExitX();
        this.exitY = maze.getExitY();
        this.path = new StringBuilder();
        this.visited = new HashSet<>();

        // Instantiating the commands
        this.TURN_RIGHT = new TurnRight(this);
        this.TURN_LEFT = new TurnLeft(this);
        this.GO_FORWARD = new GoForward(this);

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
       GO_FORWARD.execute();
    }

    protected void turnRight() {
        TURN_RIGHT.execute();
    }

    protected void turnLeft() {
        TURN_LEFT.execute();
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

    protected String getRightDirection(String currentDirection) {
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

    protected String getLeftDirection(String currentDirection) {
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
}
