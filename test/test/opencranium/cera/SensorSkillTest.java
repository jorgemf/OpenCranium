package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.cera.CeraException;
import opencranium.cranium.Processable;

import org.junit.Test;

import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class SensorSkillTest extends TestCase {

	@Test
	public void testSensorSkill() {
		EmptySensorSkill esk = new EmptySensorSkill();
		try {
			esk.addProcessable(null);
			fail();
		} catch (CeraException e) {
		}
		assertNull(esk.getSortTermMemory());
		EmptyPercept ep = new EmptyPercept();
		esk.sense = ep;
		Processable[] p = esk.execute(0);
		assertNull(p);
		esk.senseUpdated = true;
		p = esk.execute(0);
		assertSame(ep, p[0]);
		esk.sense = new EmptyPercept();
		assertNotSame(ep, esk.sense);
		p = esk.execute(0);
		assertSame(ep, p[0]);
		esk.senseUpdated = true;
		p = esk.execute(0);
		assertNotSame(ep, p[0]);
	}
}