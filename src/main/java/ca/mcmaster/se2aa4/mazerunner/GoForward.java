package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

class GoForward implements Command{
    private final PathGeneratorTemplate pathGenerator;
    private int prevX, prevY;

    public GoForward(PathGeneratorTemplate pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    @Override
    public void execute() {
        prevX = pathGenerator.x;
        prevY = pathGenerator.y;

        int newX = pathGenerator.x, newY = pathGenerator.y;

        if (pathGenerator.direction.equals("right")) {
            newY++;
        } else if (pathGenerator.direction.equals("up")) {
            newX--;
        } else if (pathGenerator.direction.equals("left")) {
            newY--;
        } else if (pathGenerator.direction.equals("down")) {
            newX++;
        }

        if (!pathGenerator.isWall(newX, newY)) {
            pathGenerator.x = newX;
            pathGenerator.y = newY;
            pathGenerator.path.append("F");
        }
    }

    @Override
    public void undo() {
        pathGenerator.x = prevX;
        pathGenerator.y = prevY;
        int len = pathGenerator.path.length();
        if (len > 0 && pathGenerator.path.charAt(len - 1) == 'F') {
            pathGenerator.path.deleteCharAt(len - 1);
        }
    }
}
