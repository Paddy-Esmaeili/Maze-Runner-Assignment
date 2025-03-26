package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

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
