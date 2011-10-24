package test.opencranium.util;

import junit.framework.TestCase;
import opencranium.util.ElementProcessingTime;
import opencranium.util.Id;
import opencranium.util.IdManager;
import opencranium.util.Time;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class ElementProcessingTimeTest extends TestCase {

	@Test
	public void testConstructor() {
		Id id = IdManager.instance().getId("id1", this.getClass());
		ElementProcessingTime ept = new ElementProcessingTime(id);

		assertEquals(ept.getProcessingNanoTime(), 0);
		assertEquals(ept.getProcessedTimes(), 0);
		assertEquals(ept.getPausedTimes(), 0);
		assertEquals(ept.getGameTicks(), 0);
		assertEquals(ept.getAverageProcessingTime(), 0);
		assertEquals(ept.getAverageProcessingTimeWithPauses(), 0);

		ept.addProcessingTime(1053);
		ept.addProcessingTime(1053);

		assertEquals(ept.getProcessingNanoTime(), 1053 * 2);
		assertEquals(ept.getProcessedTimes(), 2);
		assertEquals(ept.getAverageProcessingTime(), 1053);
		assertEquals(ept.getAverageProcessingTimeWithPauses(), 1053);

		ept.addProcessingTime(100, new Time(1, 300));
		ept.addProcessingTime(100, new Time(2, 300));

		assertEquals(ept.getProcessedTimes(), 4);
		assertEquals(ept.getAverageProcessingTime(), (1053 * 2 + 200) / 4);
		assertEquals(ept.getAverageProcessingTimeWithPauses(), (1053 * 2 + 200) / 4);
		ept.paused();
		assertEquals(ept.getAverageProcessingTimeWithPauses(), (1053 * 2 + 200) / 5);

		assertEquals(ept.getGameTicks(), 2);

		ept.addProcessingTime(100, new Time(2, 400));

		assertEquals(ept.getProcessedTimes(), 5);
		assertEquals(ept.getAverageProcessingTime(), (1053 * 2 + 300) / 5);

	}

}