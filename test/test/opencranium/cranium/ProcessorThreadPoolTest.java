package test.opencranium.cranium;

import junit.framework.TestCase;
import opencranium.cranium.CraniumException;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class ProcessorThreadPoolTest extends TestCase {

	@Test
	public void testConstructor() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		assertEquals(Runtime.getRuntime().availableProcessors() + 1, ptp.getNumberOfThreads());
		ptp = new ProcessorThreadPool(2);
		assertEquals(2, ptp.getNumberOfThreads());
		try {
			new ProcessorThreadPool(0);
			fail();
		} catch (CraniumException e) {
		}
		try {
			new ProcessorThreadPool(-10);
			fail();
		} catch (CraniumException e) {
		}
		new ProcessorThreadPool(2, 2, 2);
		try {
			new ProcessorThreadPool(2, 0, 2);
			fail();
		} catch (CraniumException e) {
		}
		try {
			new ProcessorThreadPool(2, -10, 2);
			fail();
		} catch (CraniumException e) {
		}
		try {
			new ProcessorThreadPool(2, 2, 0);
			fail();
		} catch (CraniumException e) {
		}
		try {
			new ProcessorThreadPool(2, 2, -10);
			fail();
		} catch (CraniumException e) {
		}
	}

	@Test
	public void testStartAll() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		ptp.startAll();
	}

	@Test
	public void testStopAll() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		ptp.startAll();
		ptp.stopAll();
	}

	@Test
	public void testPauseAll() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		try {
			ptp.pauseAll();
			fail();
		} catch (CraniumException e) {

		}
		ptp.startAll();
		ptp.resumeAll();
		ptp.pauseAll();
		ptp.stopAll();
		try {
			ptp.pauseAll();
			fail();
		} catch (CraniumException e) {

		}
	}

	@Test
	public void testResumeAll() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		try {
			ptp.resumeAll();
			fail();
		} catch (CraniumException e) {

		}
		ptp.startAll();
		ptp.resumeAll();
		ptp.stopAll();
		try {
			ptp.resumeAll();
			fail();
		} catch (CraniumException e) {

		}
	}

	@Test
	public void testExecutingDuring() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		try {
			ptp.executeDuring(0);
			fail();
		} catch (CraniumException e) {

		}
		ptp.startAll();
		ptp.executeDuring(0);
		try {
			ptp.executeDuring(-1);
			fail();
		} catch (CraniumException e) {

		}
		ptp.stopAll();
		try {
			ptp.executeDuring(0);
			fail();
		} catch (CraniumException e) {

		}
	}

	@Test
	public void testSetProcessorsThreads() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		ptp.setProcessorThreads(2);
		assertEquals(2, ptp.getNumberOfThreads());
		ptp.setProcessorThreads(4);
		assertEquals(4, ptp.getNumberOfThreads());
	}

	@Test
	public void testAddProcessor() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		Id id1 = IdManager.instance().getId("EmptyProcessor 1", EmptyProcessor.class);
		Id id2 = IdManager.instance().getId("EmptyProcessor 2", EmptyProcessor.class);

		EmptyProcessor ep1 = new EmptyProcessor(id1);
		EmptyProcessor ep2 = new EmptyProcessor(id2);
		assertTrue(ptp.addProcessor(ep1));
		assertFalse(ptp.addProcessor(ep1));
		assertTrue(ptp.addProcessor(ep2));
		assertFalse(ptp.addProcessor(ep2));
	}

	@Test
	public void testRemoveProcessor() {
		ProcessorThreadPool ptp = new ProcessorThreadPool();
		Id id1 = IdManager.instance().getId("EmptyProcessor 1", EmptyProcessor.class);
		Id id2 = IdManager.instance().getId("EmptyProcessor 2", EmptyProcessor.class);

		EmptyProcessor ep1 = new EmptyProcessor(id1);
		EmptyProcessor ep2 = new EmptyProcessor(id2);
		assertFalse(ptp.removeProcessor(ep2));
		assertTrue(ptp.addProcessor(ep1));
		assertTrue(ptp.addProcessor(ep2));
		assertTrue(ptp.removeProcessor(ep2));
		assertFalse(ptp.removeProcessor(ep2));
		assertTrue(ptp.removeProcessor(ep1));
	}

	@Test
	public void testGetNextProcessor() {
		ProcessorThreadPoolT ptp = new ProcessorThreadPoolT();
		Id id1 = IdManager.instance().getId("EmptyProcessor 1", EmptyProcessor.class);
		Id id2 = IdManager.instance().getId("EmptyProcessor 2", EmptyProcessor.class);
		EmptyProcessor ep1 = new EmptyProcessor(id1);
		EmptyProcessor ep2 = new EmptyProcessor(id2);
		try {
			ptp.executed(ep1);
			fail();
		} catch (CraniumException e) {
		}
		assertNull(ptp.getNextProcessor());
		assertTrue(ptp.addProcessor(ep1));
		assertTrue(ptp.addProcessor(ep2));
		assertNull(ptp.getNextProcessor());
		assertTrue(ep1.addProcessable(new EmptyProcessable(1, 1)));
		assertTrue(ep2.addProcessable(new EmptyProcessable(1, 1)));
		assertSame(ep1, ptp.getNextProcessor());
		assertSame(ep2, ptp.getNextProcessor());
		assertNull(ptp.getNextProcessor());
		ptp.executed(ep1);
		try {
			ptp.executed(ep1);
			fail();
		} catch (CraniumException e) {
		}
		ptp.executed(ep2);
		assertTrue(ep1.addProcessable(new EmptyProcessable(1, 1)));
		assertSame(ep1, ptp.getNextProcessor());
		ptp.executed(ep1);
		assertTrue(ep1.addProcessable(new EmptyProcessable(1, 1)));
		assertTrue(ep2.addProcessable(new EmptyProcessable(1, 1)));
		assertSame(ep2, ptp.getNextProcessor());
		assertSame(ep1, ptp.getNextProcessor());
		assertNull(ptp.getNextProcessor());
	}
}