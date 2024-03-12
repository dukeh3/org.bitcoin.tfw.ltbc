package org.bitcoin.tfw.ltbc.tc;

import org.bitcoin.tfw.ltbc.LocalTestBlockChain;

public class LTBCTestCase {

	protected LocalTestBlockChain ltbc;

	public LTBCTestCase() {		
		super();
		
		ltbc = new LocalTestBlockChain();
	}

}