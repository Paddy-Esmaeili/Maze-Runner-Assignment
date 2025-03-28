package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

// This class allows for further extensibility. 
// Path validation logic for other structures that are not mazes (eg. graphs, charts, etc.) 

class ExternalPathValidator {

    protected String path;

    public ExternalPathValidator(String path) {
        this.path = path;
    }

    // checkPath() method implemented differently
    public boolean checkPath(){
        return true;
    }
}
