package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

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
