import static org.junit.Assert.*;

import org.junit.Test;

import se.h3.fs.bitcoin.ltbc.LocalTestBlockChain;


public class TestFile {

//	@Test
	public void test() {
		
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("bitcoin.conf").getPath());
		
		LocalTestBlockChain ltbc = new LocalTestBlockChain();
		
		ltbc.startDeamon();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ltbc.stopDeaomn();
	}

	
	
}
