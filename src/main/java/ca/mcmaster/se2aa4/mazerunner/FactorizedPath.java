package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

class FactorizedPath {
    private String path;

    public FactorizedPath(String path) {
        this.path = path;
    }

    public String factorizePath() {
        StringBuilder factorizedPath = new StringBuilder();
        int count = 1;
    
        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == path.charAt(i - 1)) {
                count++;
            } else {
                factorizedPath.append(count).append(path.charAt(i - 1));
                count = 1;
            }
        }

        factorizedPath.append(count).append(path.charAt(path.length() - 1));
    
        return factorizedPath.toString();
    }
}
