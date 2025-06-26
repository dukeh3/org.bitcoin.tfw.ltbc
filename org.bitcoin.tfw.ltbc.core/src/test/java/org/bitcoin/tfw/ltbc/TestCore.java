package org.bitcoin.tfw.ltbc;

import org.bitcoin.tfw.ltbc.tc.LTBCMainTestCase;
import org.bitcoinj.base.Address;
import org.bitcoinj.base.Coin;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestCore extends LTBCMainTestCase {
    @Test
    void name() throws Exception {

        // Set up a wallet
        Path tmpDir = Files.createTempDirectory("test_");
        WalletAppKit kit = new WalletAppKit(RegTestParams.get(), tmpDir.toFile(), "test");
        kit.connectToLocalHost();
        kit.startAsync().awaitRunning();

        // Make sure the wallet is empty
        Assertions.assertEquals(Coin.valueOf(0), kit.wallet().getBalance());

        Address address = kit.wallet().freshReceiveAddress();

        ltbc.sendTo(address.toString(), 1.0);
        ltbc.mine(6);

        // Wait for the wallet to sync up.
        Thread.sleep(1000);

        // Make sure we got the coins
        Assertions.assertEquals(Coin.valueOf(1, 0), kit.wallet().getBalance());
    }
}