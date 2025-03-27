package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class UserPathValidityTest {

    // TEST #5 

    @Test
    void testValidPath() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Mock test file to test the code
        File tempMaze = File.createTempFile("maze", ".txt");
        try (FileWriter writer = new FileWriter(tempMaze)) {
            writer.write(
                "#####\n" +
                "     \n" +
                "#####\n"
            );
        }

        String[] args = {"-i", tempMaze.getAbsolutePath(), "-p", "FFFF"};
        Main.main(args);

        System.setOut(System.out);

        String output = outputStream.toString().trim();
        assertTrue(output.contains("correct path"));
    }

    // TEST #6

    @Test
    void testInvalidPath() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

       // Mock maze file to test the code 
        File tempMaze = File.createTempFile("maze", ".txt");
        try (FileWriter writer = new FileWriter(tempMaze)) {
            writer.write(
                "#####\n" +
                "     \n" +
                "#####\n"
            );
        }

        String[] args = {"-i", tempMaze.getAbsolutePath(), "-p", "FFRFF"};
        Main.main(args);

        System.setOut(System.out);

        String output = outputStream.toString().trim();
        assertTrue(output.contains("incorrect path"));
    }
}
