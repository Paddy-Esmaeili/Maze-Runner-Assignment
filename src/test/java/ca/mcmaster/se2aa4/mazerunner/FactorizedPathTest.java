package ca.mcmaster.se2aa4.mazerunner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class FactorizedPathTest{

    // TEST #7
 
    @Test
    void testFactorizedPathGeneration(){
        FactorizedPath factorized = new FactorizedPath("FFF");
        assertEquals("3F", factorized.factorizePath());

        FactorizedPath factorized2 = new FactorizedPath("FFFFRRLL");
        assertEquals("4F2R2L", factorized2.factorizePath());

        FactorizedPath factorized3 = new FactorizedPath("F");
        assertEquals("1F", factorized3.factorizePath());

        FactorizedPath factorized4 = new FactorizedPath("FFFRRRRFFFF");
        assertEquals("3F4R4F", factorized4.factorizePath());

    }
}
