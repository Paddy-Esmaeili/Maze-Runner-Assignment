package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


class MazeTest {

    public static File createFile(String content, File directory) throws IOException {
        File inputFile = new File(directory, "mockMaze.txt");
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(content);
            writer.flush();
        }
        return inputFile;
    }

    // TEST #1

    @Test
    void fileExists(@TempDir File tempDir) throws Exception {
        File inputFile = createFile(
            "#####\n" +
            "     \n" +
            "#####", tempDir
        );
        assertNotNull(inputFile);
        assertTrue(inputFile.exists());
    }

    // TEST #2

    @Test
    void fileValidity(@TempDir File tempDir) {

        // Case 1: No valid entry or exit points 
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            File invalidMaze = createFile(
                "#####\n" +
                "#####\n" +
                "#####", tempDir
            );
            new Maze(invalidMaze.getAbsolutePath());
        });
        assertEquals("Maze must have a valid entry point on the left side.", exception1.getMessage());

        // Case 2: No valid exit points 
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            File noEntryPoints = createFile(
                "#####\n" +
                "#    \n" +
                "#####", tempDir
            );
            new Maze(noEntryPoints.getAbsolutePath());
        });
        assertEquals("Maze must have a valid entry point on the left side.", exception2.getMessage());

        // Case 3: No valid entry points 
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            File noExitPoints = createFile(
                "#####\n" +
                "    #\n" +
                "#####", tempDir
            );
            new Maze(noExitPoints.getAbsolutePath());
        });
        assertEquals("Maze must have a valid exit point on the right side.", exception3.getMessage());

        // Case 4: Empty maze file 
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            File emptyMaze = createFile("", tempDir);
            new Maze(emptyMaze.getAbsolutePath());
        });
        assertEquals("Error: The inputted maze file is empty.", exception4.getMessage());
    }

    // TEST #3

    @Test
    void findEntryCoordinates(@TempDir File tempDir) throws Exception {

        File mockMaze = createFile(
            "#####\n" +
            "     \n" +
            "#####", tempDir
        );
    
        Maze maze = new Maze(mockMaze.getAbsolutePath());
    
        maze.findEntryExit(); 
    
        assertEquals(1, maze.entryX); 
        assertEquals(0, maze.entryY);
    }

    // TEST #4

    @Test
    void findExitCoordinates(@TempDir File tempDir) throws Exception {

        File mockMaze = createFile(
            "#####\n" +
            "     \n" +
            "#####", tempDir
        );
    
        Maze maze = new Maze(mockMaze.getAbsolutePath());
    
        maze.findEntryExit(); 
    
        assertEquals(1, maze.exitX); 
        assertEquals(4, maze.exitY);
    }

}
