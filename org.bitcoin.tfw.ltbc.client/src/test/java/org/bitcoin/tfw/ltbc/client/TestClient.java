package org.bitcoin.tfw.ltbc.client;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.bitcoin.tfw.ltbc.client.RetrofitCallHelper.safeCall;

public class TestClient {

    static public WalletAppKit createStartedWalletAppkit() {
        try {
            WalletAppKit wak = new WalletAppKit(RegTestParams.get(), Files
                    .createTempDirectory("wallet").toFile(), "dat");

            wak.connectToLocalHost().startAsync().awaitRunning();
            return wak;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testScenarion1() throws InterruptedException {
        String url = "http://localhost:18080";
        DaemonControllerClient daemon = new DaemonControllerClient(url);
        ControllerClient controller = new ControllerClient(url);

        safeCall(daemon.avatar.start());
        Assertions.assertTrue(safeCall(daemon.avatar.getStatus()));

        WalletAppKit wak = createStartedWalletAppkit();
        Assertions.assertEquals(Coin.ZERO.value, wak.wallet().getBalance().value);

        final Address address = wak.wallet().freshReceiveAddress();
        final Coin amount = Coin.valueOf(1, 0);

        safeCall(controller.avatar.sendTo(amount.toBtc().floatValue(), address.toString()));
        safeCall(controller.avatar.mine(6));

        Thread.sleep(1000);
        Assertions.assertEquals(amount.value, wak.wallet().getBalance().value);

        safeCall(daemon.avatar.stop());
        Assertions.assertFalse(safeCall(daemon.avatar.getStatus()));
    }
}
