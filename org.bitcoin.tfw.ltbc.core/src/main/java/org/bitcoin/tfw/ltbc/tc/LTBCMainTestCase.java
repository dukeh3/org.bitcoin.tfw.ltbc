package org.bitcoin.tfw.ltbc.tc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


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
    @BeforeEach
    public void Setup() {
        File f = new File("tmp");

        try {
            if (f.exists())
                delete(f);
            f.mkdir();

            if (startDaemon) {
                ltbc.startDaemon();
                ltbc.setDefaultAddress();
                ltbc.mine(101);
            } else {
                ltbc.setDefaultAddress();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    public void Teardown() {
        if (startDaemon) {
            ltbc.stopDaemon();
        }
    }
}