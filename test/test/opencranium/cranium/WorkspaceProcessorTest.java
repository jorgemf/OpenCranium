package test.opencranium.cranium;

import junit.framework.TestCase;
import opencranium.command.Action;
import opencranium.cranium.CraniumException;

import org.junit.Test;

import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class WorkspaceProcessorTest extends TestCase {

	@Test
	public void testConstructor() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();
		assertEquals(5, ewp.getInputTypes().size());
		assertEquals(5, ewp.getOutputTypes().size());
		assertSame(EmptyWorkspaceProcessor.ID, ewp.getId());
	}

	@Test
	public void testAddProcessable() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();

		try {
			assertTrue(ewp.addProcessable(new EmptyProcessable(0, 0)));
			fail();
		} catch (CraniumException e) {
			// everything ok
		} catch (NullPointerException e) {
			// process with null ID
			fail();
		}

		assertTrue(ewp.addProcessable(new EmptyAction(Action.Priority.MED)));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
	}

	@Test
	public void testOutputType() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		ewp.emptyOutputTypes();
		try {
			ewp.process(0);
			fail();
		} catch (CraniumException e) {
		}
	}

	@Test
	public void testProcessProcessable() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();

		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyAction(Action.Priority.MED)));
		ewp.process(0);
		assertTrue(ewp.lastResult instanceof EmptyAction);
		ewp.process(0);
		assertTrue(ewp.lastResult instanceof EmptyAction);
	}

	@Test
	public void testPurgeList() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();

		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyAction(Action.Priority.MED)));
		assertTrue(ewp.addProcessable(new EmptyAction(Action.Priority.MED)));

		ewp.purgeProcessableList(5);
		for (int i = 0; i < 5; i++) {
			ewp.process(0);
			assertTrue(ewp.lastResult instanceof EmptyAction);
		}
		try {
			ewp.process(0);
			// nothing remaining to process
			fail();
		} catch (CraniumException e) {
		}

	}

	@Test
	public void testReset() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();

		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.addProcessable(new EmptyPercept()));

		ewp.process(0);
		assertTrue(ewp.lastResult instanceof EmptyAction);
		assertEquals(1, ewp.memory);
		ewp.reset();
		assertEquals(0, ewp.memory);
		try {
			ewp.process(0);
			// nothing remaining to process
			fail();
		} catch (CraniumException e) {
		}
	}

	@Test
	public void testDisbledInput() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();
		ewp.setInputEnabled(false);

		assertFalse(ewp.isInputEnabled());
		assertFalse(ewp.addProcessable(new EmptyPercept()));
		assertFalse(ewp.isSomethingToProcess());
	}

	@Test
	public void testDisbledOutput() {
		EmptyWorkspaceProcessor ewp = new EmptyWorkspaceProcessor();
		ewp.setOutputEnabled(false);

		assertFalse(ewp.isOutputEnabled());
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.isSomethingToProcess());

		ewp.lastResult = null;
		ewp.process(0);
		assertNull(ewp.lastResult);

		ewp.setOutputEnabled(true);
		assertTrue(ewp.isOutputEnabled());
		assertTrue(ewp.addProcessable(new EmptyPercept()));
		assertTrue(ewp.isSomethingToProcess());
		ewp.process(0);
		assertNotNull(ewp.lastResult);
	}

}