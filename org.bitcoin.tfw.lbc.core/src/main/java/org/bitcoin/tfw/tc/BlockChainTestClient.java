package org.bitcoin.tfw.tc;

import java.io.IOException;
import java.nio.file.Files;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;

public class BlockChainTestClient {
	public WalletAppKit kit;
	
	public BlockChainTestClient() {
//		try {
//
//			NetworkParameters np = RegTestParams.get();
//			kit = new WalletAppKit(np, Files.createTempDirectory("wallet").toFile(), "dat");
////			kit.startAsync().awaitRunning();
//			kit.peerGroup().connectToLocalHost();
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
	}
}
