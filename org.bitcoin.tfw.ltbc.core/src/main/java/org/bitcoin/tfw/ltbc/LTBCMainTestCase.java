package org.bitcoin.tfw.ltbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class LTBCMainTestCase extends LTBCTestCase {

    final boolean startDaemon;

    public LTBCMainTestCase() {
        this(true);
    }

    public LTBCMainTestCase(boolean startDaemon) {
        this.startDaemon = startDaemon;
    }

    static protected void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    protected static void clearDir(File aroot) throws IOException {
        if (aroot.exists())
            delete(aroot);
        aroot.mkdir();
    }


    /**
     *
     */
    @Before
    public void setup() {
        File f = new File("tmp");

        try {
            if (f.exists())
                delete(f);
            f.mkdir();

            if (startDaemon) {
                tbc.startDaemon();
                tbc.setDefaultAddress();
                tbc.mine(101);
            } else {
                tbc.setDefaultAddress();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void teardown() {
        if (startDaemon) {
            tbc.stopDaemon();
        }
    }
}