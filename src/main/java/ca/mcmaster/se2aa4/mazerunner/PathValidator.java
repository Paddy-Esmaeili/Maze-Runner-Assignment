package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

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
            if (move == 'F') {
                if (maze.isWall(x, y)) return false;
                if (direction.equals("right")) {
                    y++;
                } else if (direction.equals("up")) {
                    x--;
                } else if (direction.equals("left")) {
                    y--;
                } else if (direction.equals("down")) {
                    x++;
                }
            } else if (move == 'R') {
                if (direction.equals("right")) {
                    direction = "down";
                } else if (direction.equals("down")) {
                    direction = "left";
                } else if (direction.equals("left")) {
                    direction = "up";
                } else if (direction.equals("up")) {
                    direction = "right";
                }
            } else if (move == 'L') {
                if (direction.equals("right")) {
                    direction = "up";
                } else if (direction.equals("up")) {
                    direction = "left";
                } else if (direction.equals("left")) {
                    direction = "down";
                } else if (direction.equals("down")) {
                    direction = "right";
                }
            } else {
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
