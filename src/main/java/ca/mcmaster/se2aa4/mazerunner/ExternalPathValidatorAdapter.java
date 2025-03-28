package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

// Adapter for ExternalPathValidator

class ExternalPathValidatorAdapter implements PathChecker {
    
    private final ExternalPathValidator externalValidator;

    public ExternalPathValidatorAdapter(ExternalPathValidator externalValidator) {
        this.externalValidator = externalValidator;
    }

    @Override
    public boolean checkPath(AbstractMaze maze, String path) {
        return externalValidator.checkPath();  
    }
}
