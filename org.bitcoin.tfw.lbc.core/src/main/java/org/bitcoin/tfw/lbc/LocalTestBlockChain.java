package org.bitcoin.tfw.lbc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.params.RegTestParams;

public class LocalTestBlockChain {
    private static final Logger logger = LogManager.getLogger();

    class SystemProfile {
        String deamon;
        String cli;

        public SystemProfile(String deamon, String cli) {
            super();
            this.deamon = deamon;
            this.cli = cli;
        }
    }

    SystemProfile profile;
    Process deamon;

    public LocalTestBlockChain() {
        // Create a temporary directory to store stuff

        try {
            final String BITCOIN_VERSION = "0.18.1";

            Path td = Files.createTempDirectory("lbct");

            final String cn = "bitcoin.conf";
            Path cp = td.resolve(cn);

            String os = System.getProperty("os.name");
            String archProp = System.getProperty("os.arch");

            String conf = " -datadir=" + td + " -conf=" + cp + " -regtest ";
            String dfn = null;
            String cfn = null;

            Path ar = null;

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

            Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(cn), cp,
                    StandardCopyOption.REPLACE_EXISTING);

            for (String s : new String[]{dfn, cfn}) {
                Path path = td.resolve(s);
                Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(ar.resolve(s).toString()),
                        path, StandardCopyOption.REPLACE_EXISTING);

                path.toFile().setExecutable(true);
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

    public void startDeamon() {
        try {
            keepAlive = true;
            deamon = Runtime.getRuntime().exec(profile.deamon);

            dist = new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(deamon.getInputStream()));

                    while (keepAlive)
                        logger.debug(br.readLine());

                } catch (IOException e) {
                    logger.warn(e);
                }
            });
            dist.start();

            dest = new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(deamon.getErrorStream()));

                    while (keepAlive)
                        logger.warn(br.readLine());

                } catch (IOException e) {
                    logger.warn(e);
                }
            });
            dest.start();


            new Thread(() -> {
                try {
                    if (!deamon.isAlive()) {
                        logger.warn("Deamon is live");
                    }


                    int ret = deamon.waitFor();
                    if (keepAlive) {
                        throw new RuntimeException("Deamon dead while keepAlive set to true, exitcode: " + ret);
                    }


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();

            address = LegacyAddress.fromBase58(RegTestParams.get(), getNewAddress());
            logger.debug("Default address set to: " + address);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopDeaomn() {
        keepAlive = false;

        dist.interrupt();
        dest.interrupt();

        deamon.destroy();

        try {
            deamon.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class FailedCall extends RuntimeException {
        final int result;
        final byte[] stfderr;

        public FailedCall(int result, byte[] stfderr) {
            this.result = result;
            this.stfderr = stfderr;
        }
    }

    byte[] cli_call(String call) {
        try {
            String command = profile.cli + "-rpcwait " + call;
            logger.debug(command);
            Process cli = Runtime.getRuntime().exec(command);

            int result = cli.waitFor();

            if (result != 0) {
                throw new FailedCall(result, cli.getErrorStream().readAllBytes());
            }

            return cli.getInputStream().readAllBytes();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Address address;

    public void mine(int blocks) {
        mineto(blocks, address);
    }

    public void mineto(int blocks, Address address) {
        cli_call("generatetoaddress " + blocks + " " + address);
    }

    public void sendto(String addr, double val) {
        cli_call("sendtoaddress " + addr + " " + val);
    }

    public void sendto(Address addr, double val) {
        cli_call("sendtoaddress " + addr + " " + val);
    }

    public String getNewAddress() {
        return new String(cli_call("getnewaddress test legacy")).trim();
    }


}
