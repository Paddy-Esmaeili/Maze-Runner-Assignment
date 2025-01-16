package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", true, "path to the mazeRunner.txt file");

        CommandLineParser parser = new DefaultParser();
    
        try {
            CommandLine cmd = parser.parse(options, args);
            if(!cmd.hasOption("i")){
                logger.error("Error: Missing the -i flag.");
                return;
            } 
            String inputFile = cmd.getOptionValue("i");
            logger.info("**** Reading the maze from file: {}", inputFile);

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        System.out.print("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        System.out.print("PASS ");
                    }
                }
                logger.debug(mazeLine.toString());
            }
        } catch(Exception e) {
            logger.error("/!\\ An error has occured /!\\", e);
        }
        logger.info("**** Computing path");
        logger.warn("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}
