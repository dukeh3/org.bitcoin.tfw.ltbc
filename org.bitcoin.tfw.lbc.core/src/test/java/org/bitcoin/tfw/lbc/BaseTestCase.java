package org.bitcoin.tfw.lbc;

import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;

import org.bitcoin.tfw.lbc.LTBCMainTestCase;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;
import org.junit.Assert;
import org.junit.Test;

public class BaseTestCase extends LTBCMainTestCase {

    @Test
    public void testReceive1BTC() throws Exception {

        WalletAppKit kit = new WalletAppKit(RegTestParams.get(), Files
                .createTempDirectory("wallet").toFile(), "dat");


        kit.startAsync().awaitRunning();
        kit.peerGroup().connectToLocalHost();

        final Address fra = kit.wallet().freshReceiveAddress();
        final int coinsToSend = 1;

        Assert.assertEquals(Coin.ZERO, kit.wallet().getBalance());

        tbc.sendto(fra, coinsToSend);
        this.tbc.mine(6);

        // We wait for 1 second here
        Thread.sleep(1000);

        // TODO fix race condition here
		Assert.assertEquals(Coin.valueOf(coinsToSend,0), kit.wallet().getBalance());
    }
}
