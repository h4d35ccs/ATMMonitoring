/**
 * 
 */
package test.com.ncr.ATMMonitoring.handler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ncr.ATMMonitoring.handler.QueueHandler;
import com.ncr.ATMMonitoring.handler.exception.QueueHandlerException;

/**
 * @author Otto Abreu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath:applicationContext.xml")
public class TestQueueHandler {

	@Autowired
	private QueueHandler qh;
	
	
	@After
	public void tearDown(){
		try {
			this.qh.destroy();
		} catch (QueueHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#loadQueue()}.
	 */
	@Test
	public void testLoadQueue() {
		try {
			qh.loadQueue();
			assertTrue(qh.isEmpty());
		} catch (QueueHandlerException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testLoadExistingQueue() {
		
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#add(java.lang.String)}.
	 */
	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAddNoIP() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#addAll(java.util.Collection)}.
	 */
	@Test
	public void testAddAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#removeAll()}.
	 */
	@Test
	public void testRemoveAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#removeElement(java.lang.String)}.
	 */
	@Test
	public void testRemoveElement() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ncr.ATMMonitoring.handler.QueueHandler#removeElements(java.util.Collection)}.
	 */
	@Test
	public void testRemoveElements() {
		fail("Not yet implemented");
	}

}
