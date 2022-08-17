package org.bitcoin.tfw.lbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.params.RegTestParams;

public class LocalTestBlockChain {
    private static final Logger logger = LogManager.getLogger();

    final String BITCOIN_VERSION = "0.18.1";
    // final String BITCOIN_VERSION = "0.19.1";
    // final String BITCOIN_VERSION = "0.21.2";
    // final String BITCOIN_VERSION = "23.0";

    static class SystemProfile {
        String daemon;
        String cli;

        public SystemProfile(String daemon, String cli) {
            super();
            this.daemon = daemon;
            this.cli = cli;
        }
    }

    SystemProfile profile;
    Process daemon;

    public LocalTestBlockChain() {
        // Create a temporary directory to store stuff

        try {
            Path td = Files.createTempDirectory("lbct");

            final String cn = "bitcoin.conf";
            Path cp = td.resolve(cn);

            String os = System.getProperty("os.name");
            String archProp = System.getProperty("os.arch");

            String conf = " -datadir=" + td + " -conf=" + cp + " -regtest ";
            String dfn;
            String cfn;

            Path ar;

            if (os.startsWith("Linux")) {

                ar = Paths.get("bin", "bitcoin-" + BITCOIN_VERSION + "-linux").resolve(archProp);
                dfn = "bitcoind";
                cfn = "bitcoin-cli";
            } else throw new RuntimeException("Unsupported OS");
//            else if (os.startsWith("Windows")) {
//                ar = Paths.get("bin", "bitcoin-0.9.3-win").resolve(archProp);
//                dfn = "bitcoind.exe";
//                cfn = "bitcoin-cli.exe";
//            }

            // else if (os.startsWith("Mac")) {
            // int arch = archProp.equals("amd64")
            // || archProp.equals("x86_64") ? 64 : 32;
            // profile = new SystemProfile("bin/bitcoin-0.9.3-osx/"
            // + arch + "/bitcoind " + conf,
            // "bin/bitcoin-0.9.3-osx/" + arch + "/bitcoin-cli "
            // + conf);
            // }

            Files.copy(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(cn)), cp,
                    StandardCopyOption.REPLACE_EXISTING);

            for (String s : new String[]{dfn, cfn}) {
                Path path = td.resolve(s);
                Files.copy(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(ar.resolve(s).toString())),
                        path, StandardCopyOption.REPLACE_EXISTING);

                if (!path.toFile().setExecutable(true)) throw new RuntimeException("Setting Executable failed");
            }

            profile = new SystemProfile(td.resolve(dfn) + conf, td.resolve(cfn) + conf);

            logger.debug("Using tempdir: " + td);

            // Files.copy(Thread.currentThread().getContextClassLoader()
            // .getResourceAsStream(profile.deamon),
            // td.resolve(profile.deamon),
            // StandardCopyOption.REPLACE_EXISTING);
            //
            // Files.copy(Thread.currentThread().getContextClassLoader()
            // .getResourceAsStream("bitcoin.conf"),
            // td.resolve("bitcoin.conf"),
            // StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean keepAlive = false;

    private Thread dist;
    private Thread dest;

    public boolean isDaemonAlive() {
        return daemon != null && daemon.isAlive();
    }

    public void startDaemon() {
        try {
            keepAlive = true;
            daemon = Runtime.getRuntime().exec(profile.daemon);

            dist = new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(daemon.getInputStream()));

                    while (keepAlive)
                        logger.debug(br.readLine());

                } catch (IOException e) {
                    logger.warn(e);
                }
            });
            dist.start();

            dest = new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(daemon.getErrorStream()));

                    while (keepAlive)
                        logger.warn(br.readLine());

                } catch (IOException e) {
                    logger.warn(e);
                }
            });
            dest.start();


            new Thread(() -> {
                try {
                    if (!daemon.isAlive()) {
                        logger.warn("Daemon is live");
                    }

                    int ret = daemon.waitFor();
                    if (keepAlive) {
                        throw new RuntimeException("Daemon dead while keepAlive set to true, exitcode: " + ret);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setDefaultAddress() {
        // TODO: This should be activated with v 23.0
//            createWallet("test_wallet");
        defaultAddress = LegacyAddress.fromBase58(RegTestParams.get(), getNewAddress());
        logger.debug("Default address set to: " + defaultAddress);
    }

    public void stopDaemon() {
        keepAlive = false;

        dist.interrupt();
        dest.interrupt();

        daemon.destroy();

        try {
            daemon.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class FailedCall extends RuntimeException {
        final int result;
        final byte[] stderr;

        public FailedCall(int result, byte[] stderr) {
            this.result = result;
            this.stderr = stderr;
        }
    }

    byte[] cliCall(String call) {
        try {
            String command = profile.cli + "-rpcwait " + call;
            logger.debug(command);
            Process cli = Runtime.getRuntime().exec(command);

            int result = cli.waitFor();

            if (result != 0) {
                logger.warn("CLI command failed with result: " + result);
                throw new FailedCall(result, cli.getErrorStream().readAllBytes());
            }

            return cli.getInputStream().readAllBytes();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Address defaultAddress;

    public void createWallet(String name) {
        cliCall("createwallet " + name);
    }

    public void mine(int blocks) {
        mineTo(blocks, defaultAddress);
    }

    public void mineTo(int blocks, Address address) {
        cliCall("generatetoaddress " + blocks + " " + address);
    }

    public void sendTo(String address, double val) {
        cliCall("sendtoaddress " + address + " " + val);
    }

    public void sendTo(Address address, double val) {
        cliCall("sendtoaddress " + address + " " + val);
    }

    //TODO This could be the balrog
    public String getNewAddress() {
        return new String(cliCall("getnewaddress test legacy")).trim();
    }

}
