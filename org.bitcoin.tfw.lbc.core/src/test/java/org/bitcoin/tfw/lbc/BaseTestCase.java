package org.bitcoin.tfw.lbc;

import java.nio.file.Files;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.Assert;
import org.junit.Test;

public class BaseTestCase extends LTBCMainTestCase {

    @Test
    public void testReceive1BTC() throws Exception {

        WalletAppKit kit = new WalletAppKit(RegTestParams.get(), Files
                .createTempDirectory("wallet").toFile(), "dat");


        kit.connectToLocalHost();
        kit.startAsync().awaitRunning();


        final Address fra = kit.wallet().freshReceiveAddress();
        final int coinsToSend = 1;

        Assert.assertEquals(Coin.ZERO, kit.wallet().getBalance());

        tbc.sendTo(fra, coinsToSend);
        this.tbc.mine(6);

        // We wait for 1 second here
        Thread.sleep(1000);

        // TODO fix race condition here
		Assert.assertEquals(Coin.valueOf(coinsToSend,0), kit.wallet().getBalance());
    }
}
