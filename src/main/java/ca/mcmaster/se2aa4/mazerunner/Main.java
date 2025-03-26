package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

class Main {
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("i", true, "Path to the maze file");
        options.addOption("p", true, "Validating user's input path");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String inputFile = cmd.getOptionValue("i");
            String validInput = cmd.getOptionValue("p");

            if (inputFile == null) {
                throw new IllegalArgumentException("Error: -i flag was not detected");
            }

            Maze maze = new Maze(inputFile);

            PathGenerator generator = new RHRPathGenerator(maze);
            String canonicalPath = generator.findPath();

            FactorizedPath factorizedPath = new FactorizedPath(canonicalPath);
            String factorized = factorizedPath.factorizePath();
            System.out.println(factorized);

            if (validInput != null) {
                PathChecker validator = new PathValidator();
                if (validator.checkPath(maze, validInput)) {
                    System.out.println("correct path");
                } else {
                    System.out.println("incorrect path");
                }
            }
        } catch (Exception e) {
            System.out.println("Sorry, an error occurred!");
        }
    }
}


