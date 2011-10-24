package test.opencranium.cranium;

import junit.framework.TestCase;
import opencranium.util.ElementProcessingTime;
import opencranium.util.StatisticsManager;

import org.junit.Test;

import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class ProcessorTest extends TestCase {

	@Test
	public void testProcessor() {
		EmptyProcessor ep = new EmptyProcessor();
		StatisticsManager.enableStatistics();

		assertFalse(ep.processedNothing);
		assertFalse(ep.processedSomething);
		ep.process(0);
		assertTrue(ep.processedNothing);
		assertFalse(ep.processedSomething);
		ElementProcessingTime ept = ep.getElementProcessingTime();
		assertEquals(ept.getProcessedTimes(), 1);
		assertEquals(ept.getAverageProcessingTime(), 1 * 1000000, 1 * 1000000);
		ep.addProcessable(new EmptyPercept());
		ep.processedNothing = false;
		ep.process(0);
		assertFalse(ep.processedNothing);
		assertTrue(ep.processedSomething);
		assertEquals(2, ept.getProcessedTimes());
		assertEquals(10 * 1000000, ept.getProcessingNanoTime(), 1 * 1000000);
		assertEquals(5 * 1000000, ept.getAverageProcessingTime(), 1 * 1000000);

		ElementProcessingTime ept2 = EmptyPercept.ID.getStadistics(EmptyProcessor.ID);
		assertEquals(1, ept2.getProcessedTimes());
		assertEquals(1 * 10000000, ept2.getAverageProcessingTime(), 1 * 1000000);

		StatisticsManager.disableStatistics();
		ep.process(0);
		assertEquals(ept.getProcessedTimes(), 2);
	}

	@Test
	public void testUpdatedProcessable() {
		/*
		 * Order of the elements is not important to this test, all have the
		 * same sorting value.
		 */
		EmptyProcessable ep1 = new EmptyProcessable(101, 1);
		EmptyProcessable ep2 = new EmptyProcessable(100, 2);
		EmptyProcessable ep3 = new EmptyProcessable(100, 3);
		EmptyProcessable ep4 = new EmptyProcessable(99, 4);

		EmptyProcessor ep = new EmptyProcessor();
		ep.addProcessable(ep1);
		ep.addProcessable(ep2);
		ep.addProcessable(ep4);

		ep.process(0);
		assertSame(ep1, ep.lastProcessable);
		ep.process(0);
		assertSame(ep2, ep.lastProcessable);
		ep.process(0);
		assertSame(ep4, ep.lastProcessable);

		ep.addProcessable(ep1);
		ep.addProcessable(ep2);
		ep.addProcessable(ep4);
		ep.addProcessable(ep3);

		ep.process(0);
		assertSame(ep1, ep.lastProcessable);
		ep.process(0);
		assertSame(ep4, ep.lastProcessable);
		ep.process(0);
		assertSame(ep3, ep.lastProcessable);
	}

}