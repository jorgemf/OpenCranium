package test.opencranium.cranium;

import junit.framework.TestCase;
import opencranium.cranium.CraniumException;
import opencranium.cranium.ProcessorThread;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class ProcessorThreadTest extends TestCase {

	@Test
	public void testConstructor() {
		ProcessorThread pt = new ProcessorThread();
		assertTrue(pt.isInitialized());
		assertFalse(pt.isStarted());
		assertFalse(pt.isKilling());
		assertFalse(pt.isPaused());
		assertFalse(pt.isPausing());
		assertFalse(pt.isRunning());
		assertNull(pt.getProcessorThreads());
	}

	@Test
	public void testStart() {
		ProcessorThread pt = new ProcessorThread();
		pt.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		assertTrue(pt.isStarted());
		assertTrue(pt.isPaused());
		assertFalse(pt.isPausing());
		assertFalse(pt.isRunning());
		assertFalse(pt.isKilling());
		try {
			pt.start();
			fail();
		} catch (CraniumException ce) {
		}
	}

	@Test
	public void testKill() {
		ProcessorThread pt = new ProcessorThread();
		pt.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		assertTrue(pt.isPaused());
		pt.kill();
		assertTrue(pt.isKilling());
		pt.resume(0);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		assertFalse(pt.isStarted());
		assertFalse(pt.isKilling());
		assertFalse(pt.isPaused());
		assertFalse(pt.isPausing());
	}

	@Test
	public void testResume() {
		ProcessorThread pt = new ProcessorThread();
		pt.start();
		EmptyProcessor2 ep = new EmptyProcessor2(200);
		assertTrue(ep.addProcessable(new EmptyProcessable(0, 0)));
		assertFalse(pt.isRunning());
		pt.setCurrentProcess(ep);
		try {
			pt.setCurrentProcess(ep);
			fail();
		} catch (CraniumException e) {
		}
		assertTrue(pt.isRunning());
		assertSame(0, ep.processedSomething);
		assertSame(0, ep.processedNothing);
		pt.resume(0);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		try {
			Thread.sleep(210);
		} catch (InterruptedException e) {
		}
		assertSame(1, ep.processedSomething);
		assertSame(0, ep.processedNothing);
		assertFalse(pt.isPaused());
		pt.pause();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		assertTrue(pt.isPaused());
		assertSame(1, ep.processedSomething);
		assertSame(1, ep.processedNothing);
	}

}
