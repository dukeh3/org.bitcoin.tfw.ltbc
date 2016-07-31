package org.bitcoin.tfw.lbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class LTBCMainTestCase extends LTBCTestCase {

	public LTBCMainTestCase() {
		super();
	}

	static protected void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	protected static void clearDir(File aroot) throws IOException {
		if (aroot.exists())
			delete(aroot);
		aroot.mkdir();
	}

	/**
	 * 
	 */
	@Before
	public void setup() {
		File f = new File("tmp");

		try {
			if (f.exists())
				delete(f);
			f.mkdir();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



		tbc.startDeamon();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tbc.mine(101);
	}

	@After
	public void teardown() {
		tbc.stopDeaomn();
	}
}