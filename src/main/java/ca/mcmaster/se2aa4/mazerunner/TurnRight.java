package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

class TurnRight implements Command{
    
    private final PathGeneratorTemplate pathGenerator;
    private String prevDirection;

    public TurnRight(PathGeneratorTemplate pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    @Override
    public void execute() {
        prevDirection = pathGenerator.direction;
        pathGenerator.path.append("R");
        pathGenerator.direction = pathGenerator.getRightDirection(prevDirection);
    }

    @Override
    public void undo() {
        pathGenerator.direction = prevDirection;
        int len = pathGenerator.path.length();
        if (len > 0 && pathGenerator.path.charAt(len - 1) == 'R') {
            pathGenerator.path.deleteCharAt(len - 1);
        }
    }
}
