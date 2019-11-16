package org.bitcoin.tfw.lbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bitcoinj.core.Address;
import org.bitcoinj.params.RegTestParams;
import org.junit.After;
import org.junit.Before;

public class LTBCMainTestCase extends LTBCTestCase {

    final boolean startDeamon;

    public LTBCMainTestCase() {
        this(true);
    }

    public LTBCMainTestCase(boolean startDeamon) {
        this.startDeamon = startDeamon;
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

            if(startDeamon) {
                tbc.startDeamon();
                tbc.mine(101);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @After
    public void teardown() {
        tbc.stopDeaomn();
    }
}