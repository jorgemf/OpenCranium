package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.data.Percept;
import opencranium.data.Percept.MemoryType;
import opencranium.data.Percept.Nature;
import opencranium.data.Percept.Source;

import org.junit.Test;

import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class CeraWorkspaceProcessorTest extends TestCase {

	@Test
	public void testManageResults() {
		EmptyCeraWorkspaceProcessor ecwp = new EmptyCeraWorkspaceProcessor();

		ecwp.addProcessable(new EmptyPercept());
		ecwp.addProcessable(new EmptyPercept());
		ecwp.addProcessable(new EmptyPercept());

		ecwp.process(0);
		assertTrue(ecwp.lastProcessable instanceof EmptyPercept);
		Percept p = (Percept) ecwp.lastProcessable;

		assertSame(Source.MISSION_PROCESSOR, p.getSource());
		assertSame(Nature.UNKNOWN, p.getNature());
		assertSame(MemoryType.SHORT_TERM, p.getMemoryType());

		ecwp.process(0);
		p = (Percept) ecwp.lastProcessable;

		assertSame(Source.MISSION_PROCESSOR, p.getSource());
		assertSame(Nature.INVENTED, p.getNature());
		assertSame(MemoryType.SHORT_TERM, p.getMemoryType());

		ecwp.process(0);
		p = (Percept) ecwp.lastProcessable;

		assertSame(Source.MISSION_PROCESSOR, p.getSource());
		assertSame(Nature.REAL, p.getNature());
		assertSame(MemoryType.SHORT_TERM, p.getMemoryType());
	}

}
