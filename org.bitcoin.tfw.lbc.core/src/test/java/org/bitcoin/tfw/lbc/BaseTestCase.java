package org.bitcoin.tfw.lbc;
import java.nio.file.Files;

import org.bitcoin.tfw.lbc.LTBCMainTestCase;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.Assert;
import org.junit.Test;

public class BaseTestCase extends LTBCMainTestCase {

	@Test
	public void testReceive1BTC() throws Exception {
		
		WalletAppKit kit = new WalletAppKit(RegTestParams.get(), Files
				.createTempDirectory("wallet").toFile(), "dat");

		kit.startAsync().awaitRunning();

		kit.peerGroup().connectToLocalHost();
		
		Assert.assertEquals(Coin.ZERO, kit.wallet().getBalance());

		Address fra = kit.wallet().freshReceiveAddress();

		this.tbc.sendto(fra, 1);
		
		this.tbc.mine(6);

		// TODO fix race condition here
//		Assert.assertEquals(Coin.COIN, kit.wallet().getBalance());
	}
}
