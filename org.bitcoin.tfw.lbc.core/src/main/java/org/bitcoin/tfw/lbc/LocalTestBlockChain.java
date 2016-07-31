package org.bitcoin.tfw.lbc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bitcoinj.core.Address;

public class LocalTestBlockChain {

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
			Path td = Files.createTempDirectory("lbct");

			final String cn = "bitcoin.conf";
			Path cp = td.resolve(cn);

			String os = System.getProperty("os.name");
			String archProp = System.getProperty("os.arch");

			String conf = " -datadir=" + td + " -conf=" + cp + " -regtest ";
			String dfn = null;
			String cfn = null;

			Path ar = null;

			if (os.startsWith("Windows")) {
				ar = Paths.get("bin", "bitcoin-0.9.3-win").resolve(archProp);
				dfn = "bitcoind.exe";
				cfn = "bitcoin-cli.exe";

			} else if (os.startsWith("Linux")) {

				ar = Paths.get("bin", "bitcoin-0.9.3-linux").resolve(archProp);
				dfn = "bitcoind";
				cfn = "bitcoin-cli";
			}

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

			for (String s : new String[] { dfn, cfn }) {
				Path path = td.resolve(s);
				Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(ar.resolve(s).toString()),
						path, StandardCopyOption.REPLACE_EXISTING);

				path.toFile().setExecutable(true);
			}

			profile = new SystemProfile(td.resolve(dfn) + conf, td.resolve(cfn) + conf);

			System.out.println(td);

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
			e.printStackTrace();
		}
	}

	private boolean keepAlive = false;

	public void startDeamon() {
		try {
			keepAlive = true;
			deamon = Runtime.getRuntime().exec(profile.deamon);

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						if (!deamon.isAlive()) {
							System.out.println("Deamon is live");
						}
						
						int ret = deamon.waitFor();
						if(keepAlive) {
							throw new RuntimeException("Deamon dead while keepAlive set to true, exitcode: " + ret);
						}
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
				}
			}).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopDeaomn() {
		keepAlive = false;
		deamon.destroy();
		try {
			deamon.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void cli_call(String call) {
		try {
			String command = profile.cli + "-rpcwait " + call;
			System.out.println(command);
			Process cli = Runtime.getRuntime().exec(command);

			cli.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mine(int blocks) {
		cli_call("setgenerate true " + blocks);
	}

	public void sendto(String addr, double val) {
		cli_call("sendtoaddress " + addr + " " + val);
	}

	public void sendto(Address addr, double val) {
		cli_call("sendtoaddress " + addr + " " + val);
	}

}
