package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.cera.CeraException;
import opencranium.command.Action.Priority;
import opencranium.command.Action.Type;

import org.junit.Test;

import test.opencranium.command.EmptyAction;

/**
 * @author Jorge Muñoz
 */
public class MotorSkillTest extends TestCase {

	@Test
	public void testMotorSkill() {
		EmptyMotorSkill ems = new EmptyMotorSkill(EmptyAction.ID);

		EmptyAction.TYPE = Type.COMPLEX;
		EmptyAction ea = new EmptyAction(Priority.MED);
		ems.addProcessable(ea);

		try {
			ems.process(0);
			fail();
		} catch (CeraException e) {

		}
		assertNull(ems.getSortTermMemory());

		EmptyAction.TYPE = Type.SIMPLE;
		ea = new EmptyAction(Priority.MED);
		ems.addProcessable(ea);
		ems.process(0);
		assertSame(ea, ems.getSortTermMemory()[0]);

	}
}