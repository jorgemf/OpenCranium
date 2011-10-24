package test.opencranium.util;

import junit.framework.TestCase;
import opencranium.util.ElementProcessingTime;
import opencranium.util.Id;
import opencranium.util.IdManager;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class StatisticsIdTest extends TestCase {

	@Test
	public void testAddWhatever() {
		IdManager idManager = IdManager.instance();
		Id id1 = idManager.getId("id1", StatisticsIdTest.class);
		Id id2 = idManager.getId("id2", StatisticsIdTest.class);
		Id id3 = idManager.getId("id3", StatisticsIdTest.class);

		assertNotNull(id1.getStadistics(id2));

		id1.addCreated(id2);
		id1.addDiscarded(id2);
		id1.addPause(id2);
		id1.addProcessingTime(id2, 1234);
		id1.addProcessingTime(id3, 4567);

		ElementProcessingTime ept = id1.getStadistics(id2);

		assertNotNull(ept);
		assertSame(ept.getCreatedTimes(), 1);
		id1.addCreated(id2);
		assertSame(ept.getCreatedTimes(), 2);

		assertSame(ept.getDiscardedTimes(), 1);
		id1.addDiscarded(id2);
		assertSame(ept.getDiscardedTimes(), 2);

		assertSame(ept.getPausedTimes(), 1);
		id1.addPause(id2);
		assertSame(ept.getPausedTimes(), 2);

		assertEquals(ept.getProcessedTimes(), 1);
		assertEquals(ept.getProcessingNanoTime(), 1234l);

		id1.addProcessingTime(id2, 1);
		assertEquals(ept.getProcessedTimes(), 2);
		assertEquals(ept.getProcessingNanoTime(), 1235);

		ept = id1.getStadistics(id3);
		assertNotNull(ept);
		assertEquals(ept.getCreatedTimes(), 0);
		assertEquals(ept.getProcessingNanoTime(), 4567);
	}
}