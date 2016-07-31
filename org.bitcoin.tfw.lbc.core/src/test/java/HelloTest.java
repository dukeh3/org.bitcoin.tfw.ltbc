import java.nio.file.Files;

import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.Test;

import se.h3.fs.bitcoin.ltbc.LTBCMainTestCase;

public class HelloTest extends LTBCMainTestCase {

//	@Test
	public void testName() throws Exception {
		
		WalletAppKit kit = new WalletAppKit(RegTestParams.get(), Files
				.createTempDirectory("wallet").toFile(), "dat");

		kit.startAsync().awaitRunning();

		kit.peerGroup().connectToLocalHost();

		Address fra = kit.wallet().freshReceiveAddress();

		System.out.println(fra);

		this.tbc.sendto(fra, 1);
		this.tbc.mine(6);

		System.out.println(kit.wallet().getBalance());
		
		
		
		
		
		
	}
}
