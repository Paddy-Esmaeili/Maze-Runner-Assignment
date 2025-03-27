package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class PathGenerationTest {

    // TEST #8

    @Test
    void testFindPathSimple() {
        AbstractMaze mockMaze = new MockMaze1(1, 0, 1, 4);
        RHRPathGenerator generator = new RHRPathGenerator(mockMaze);

        String path = generator.findPath();
        assertEquals("FFFF", path, "Expected simple path: 'FFFF'");
    }

    // TEST #9 

    @Test
    void testFindPathComplex() {
        AbstractMaze mockMaze = new MockMaze2(1, 0, 3, 4);
        RHRPathGenerator generator = new RHRPathGenerator(mockMaze);

        String path = generator.findPath();
        assertEquals("FRFLFRFLFF", path);
    }


    // TEST #10 - Path has a valid entry and exit point but the program needs to return and exit from the entry points. 

    @Test
    void testEdgeCase(){
        AbstractMaze mockMaze = new MockMaze3(1, 0, 3, 4);
        RHRPathGenerator generator = new RHRPathGenerator(mockMaze);

        String path = generator.findPath();
        assertEquals("FRFLFLLFRFLF", path);
    }


    // Mock class for a simple straight-line maze
    private static class MockMaze1 extends AbstractMaze {
        private final int entryX, entryY, exitX, exitY;

        public MockMaze1(int entryX, int entryY, int exitX, int exitY) {
            super(new char[][]{
                {'#', '#', '#', '#', '#'},
                {' ', ' ', ' ', ' ', ' '}, 
                {'#', '#', '#', '#', '#'}
            });
            this.entryX = entryX;
            this.entryY = entryY;
            this.exitX = exitX;
            this.exitY = exitY;
        }

        @Override public int getEntryX() { return entryX; }
        @Override public int getEntryY() { return entryY; }
        @Override public int getExitX() { return exitX; }
        @Override public int getExitY() { return exitY; }
        @Override public boolean isWall(int x, int y) { return grid[x][y] == '#'; }
    }

    // Mock class for a more complex maze with turns
    private static class MockMaze2 extends AbstractMaze {
        private final int entryX, entryY, exitX, exitY;

        public MockMaze2(int entryX, int entryY, int exitX, int exitY) {
            super(new char[][]{
                {'#', '#', '#', '#', '#'},
                {' ', ' ', '#', '#', '#'}, 
                {'#', ' ', ' ', '#', '#'},
                {'#', '#', ' ', ' ', ' '}       
            });
            this.entryX = entryX;
            this.entryY = entryY;
            this.exitX = exitX;
            this.exitY = exitY;
        }

        @Override public int getEntryX() { return entryX; }
        @Override public int getEntryY() { return entryY; }
        @Override public int getExitX() { return exitX; }
        @Override public int getExitY() { return exitY; }
        @Override public boolean isWall(int x, int y) { return grid[x][y] == '#'; }
    }

    private static class MockMaze3 extends AbstractMaze {
        private final int entryX, entryY, exitX, exitY;

        // No valid path from entry to exit points.
        // The entry points need to be used as exit points. 
        // The program needs to backtrack. 

        public MockMaze3(int entryX, int entryY, int exitX, int exitY) {
            super(new char[][]{
                {'#', '#', '#', '#', '#'},
                {' ', ' ', '#', '#', '#'},      
                {'#', ' ', ' ', '#', ' '},
                {'#', '#', '#', '#', '#'}      
            });
            this.entryX = entryX;
            this.entryY = entryY;
            this.exitX = exitX;
            this.exitY = exitY;
        }

        @Override public int getEntryX() { return entryX; }
        @Override public int getEntryY() { return entryY; }
        @Override public int getExitX() { return exitX; }
        @Override public int getExitY() { return exitY; }
        @Override public boolean isWall(int x, int y) { return grid[x][y] == '#'; }
    }
}
