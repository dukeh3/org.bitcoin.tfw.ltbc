package org.bitcoin.tfw.ltbc.junit;

import org.bitcoin.tfw.ltbc.tc.LTBCMainTestCase;
//import org.bitcoinj.core.Address;
//import org.bitcoinj.core.Coin;
import org.bitcoinj.base.Address;
import org.bitcoinj.base.Coin;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;

public class TestMainTestCase extends LTBCMainTestCase {

    @Test
    public void testReceive1BTC() throws Exception {

        WalletAppKit kit = new WalletAppKit(RegTestParams.get(), Files
                .createTempDirectory("wallet").toFile(), "dat");

        kit.connectToLocalHost();
        kit.startAsync().awaitRunning();

        final Address fra = kit.wallet().freshReceiveAddress();
        final int coinsToSend = 1;

        Assertions.assertEquals(Coin.ZERO, kit.wallet().getBalance());

        ltbc.sendTo(fra, coinsToSend);
        ltbc.mine(6);

        // We wait for 1 second here
        Thread.sleep(1000);

        // TODO fix race condition here
		Assertions.assertEquals(Coin.valueOf(coinsToSend,0), kit.wallet().getBalance());
    }
}
