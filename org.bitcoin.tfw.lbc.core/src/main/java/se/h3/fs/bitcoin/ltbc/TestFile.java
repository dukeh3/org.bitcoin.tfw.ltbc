package se.h3.fs.bitcoin.ltbc;
import static org.junit.Assert.*;

import org.junit.Test;


public class TestFile {

	@Test
	public void test() {
		
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("bitcoin.conf").getPath());
	}

}
