package org.bitcoin.tfw.lbc;

import org.junit.Test;

public class TestCore {

    @Test
    public void testCore() {

        LocalTestBlockChain ltbc = new LocalTestBlockChain();

        ltbc.startDaemon();

        System.out.println("SSSSS");

        ltbc.setDefaultAddress();

        ltbc.mine(101);

        System.out.println("SSSSS");

        ltbc.stopDaemon();

        System.out.println("SSSSS");

    }
}
