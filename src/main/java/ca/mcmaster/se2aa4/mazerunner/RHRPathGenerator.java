package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

class RHRPathGenerator extends PathGeneratorTemplate {
    private final Set<String> visited;

    public RHRPathGenerator(AbstractMaze maze) {
        super(maze);
        this.visited = new HashSet<>();
    }

    @Override
    protected void markVisitedPaths() {
        while (!isExit(x, y)) {
            String pos = x + "," + y;

            // Stuck in a loop
            if (visited.contains(pos) && wallOnRight() && wallOnFront() && wallOnLeft()) {
                break; 
            }

            visited.add(pos);

            // Implementing the right-hand rule
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
    }

    @Override
    protected void backTrack(){
        // This method stays abstract. We don't need it here. 
    }


    @Override
    protected boolean isValidMove(int x, int y, String direction) {
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
}
