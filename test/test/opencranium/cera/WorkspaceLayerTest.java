package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.cera.CeraException;
import opencranium.command.Action.Priority;
import opencranium.cranium.Activation;

import org.junit.Test;

import test.opencranium.command.EmptyAction;
import test.opencranium.cranium.EmptyProcessable;
import test.opencranium.cranium.EmptyProcessor;
import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class WorkspaceLayerTest extends TestCase {

	@Test
	public void testClass() {
		EmptyWorkspaceLayer ewl = new EmptyWorkspaceLayer();

		try {
			EmptyProcessable ep = new EmptyProcessable(0, 0);
			ep.setActivation(new Activation(Activation.MIN));
			ewl.submitProcessable(ep);
			fail();
		} catch (CeraException exception) {
		}
		ewl.submitProcessable(new EmptyAction(Priority.MED));

		EmptyCeraWorkspaceProcessor ecwp = new EmptyCeraWorkspaceProcessor();

		assertFalse(ewl.registerProcessor(ecwp));
		ecwp.nullLayer();
		assertTrue(ewl.registerProcessor(ecwp));
		ecwp.nullLayer();
		ecwp.setLayer(ewl);
		assertFalse(ewl.registerProcessor(ecwp));

		assertNull(ecwp.lastAction);
		assertNull(ecwp.lastPercept);
		ewl.submitProcessable(new EmptyAction(Priority.MED));
		assertNull(ecwp.lastAction);
		assertNull(ecwp.lastPercept);
		ewl.submitProcessable(new EmptyPercept());
		ecwp.process(0);
		assertNull(ecwp.lastAction);
		assertNotNull(ecwp.lastPercept);
		ewl.reset();
		assertNull(ecwp.lastAction);
		assertNull(ecwp.lastPercept);

		EmptyProcessor ep = new EmptyProcessor();
		try {
			ewl.registerProcessor(ep);
			fail();
		} catch (CeraException exception) {
		}

	}
}