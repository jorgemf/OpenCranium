package test.opencranium.cranium;

import java.util.TreeSet;

import junit.framework.TestCase;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.cranium.Workspace;
import opencranium.cranium.WorkspaceProcessor;
import opencranium.util.Id;
import opencranium.util.IdManager;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class WorkspaceTest extends TestCase {

	@Test
	public void testRegisterProcessors() {
		Workspace w = new Workspace(new ProcessorThreadPool());
		EmptyWorkspaceProcessor ewp1 = new EmptyWorkspaceProcessor();
		EmptyWorkspaceProcessor ewp2 = new EmptyWorkspaceProcessor();
		assertTrue(w.registerProcessor(ewp1));
		assertFalse(w.registerProcessor(ewp1));
		assertFalse(w.registerProcessor(ewp2));
		Id id3 = IdManager.instance().getId("EmptyWorkspaceProcessor2", WorkspaceProcessor.class);
		EmptyWorkspaceProcessor ewp3 = new EmptyWorkspaceProcessor(id3);
		assertTrue(w.registerProcessor(ewp3));
		assertFalse(w.registerProcessor(ewp3));
	}

	@Test
	public void testUnregisterProcessors() {
		Workspace w = new Workspace(new ProcessorThreadPool());
		EmptyWorkspaceProcessor ewp1 = new EmptyWorkspaceProcessor();
		EmptyWorkspaceProcessor ewp2 = new EmptyWorkspaceProcessor();
		assertFalse(w.unregisterProcessor(ewp1));
		assertTrue(w.registerProcessor(ewp1));
		assertFalse(w.registerProcessor(ewp1));
		assertFalse(w.registerProcessor(ewp2));
		assertTrue(w.unregisterProcessor(ewp1));
		assertTrue(w.registerProcessor(ewp1));
		Id id3 = IdManager.instance().getId("EmptyWorkspaceProcessor2", WorkspaceProcessor.class);
		EmptyWorkspaceProcessor ewp3 = new EmptyWorkspaceProcessor(id3);
		assertTrue(w.registerProcessor(ewp3));
		assertFalse(w.registerProcessor(ewp3));
		assertTrue(w.unregisterProcessor(ewp1));
		assertTrue(w.unregisterProcessor(ewp3));
		assertFalse(w.unregisterProcessor(ewp1));
		assertFalse(w.unregisterProcessor(ewp3));
	}

	@Test
	public void testSubmitProcessable() {
		Workspace w = new Workspace(new ProcessorThreadPool());
		EmptyWorkspaceProcessor ewp1 = new EmptyWorkspaceProcessor();
		assertTrue(w.registerProcessor(ewp1));

		Id id1 = IdManager.instance().getId("Input id 1", WorkspaceProcessor.class);
		Id id2 = IdManager.instance().getId("Input id 2", WorkspaceProcessor.class);
		Id id3 = IdManager.instance().getId("Input id 3", WorkspaceProcessor.class);
		Id id4 = IdManager.instance().getId("Input id 4", WorkspaceProcessor.class);
		Id id5 = IdManager.instance().getId("Input id 5", WorkspaceProcessor.class);
		TreeSet<Id> inputSet = new TreeSet<Id>();
		inputSet.add(id1);
		inputSet.add(id4);
		inputSet.add(id5);
		Id idw = IdManager.instance().getId("EmptyWorkspaceProcessor2", WorkspaceProcessor.class);
		EmptyWorkspaceProcessor ewp2 = new EmptyWorkspaceProcessor(idw);
		ewp2.setInputId(inputSet);
		assertTrue(w.registerProcessor(ewp2));

		EmptyProcessable ep1 = new EmptyProcessable(id1, 5, 5);
		EmptyProcessable ep2 = new EmptyProcessable(id2, 4, 4);
		EmptyProcessable ep3 = new EmptyProcessable(id3, 3, 3);
		EmptyProcessable ep4 = new EmptyProcessable(id4, 2, 2);
		EmptyProcessable ep5 = new EmptyProcessable(id5, 1, 1);

		w.submitProcessable(ep1);
		w.submitProcessable(ep2);
		w.submitProcessable(ep3);
		w.submitProcessable(ep4);
		w.submitProcessable(ep5);

		ewp1.process(0);
		assertSame(ep1, ewp1.lastProcessable);
		ewp1.process(0);
		assertSame(ep2, ewp1.lastProcessable);
		ewp1.process(0);
		assertSame(ep3, ewp1.lastProcessable);

		ewp2.process(0);
		assertSame(ep1, ewp2.lastProcessable);
		ewp2.process(0);
		assertSame(ep4, ewp2.lastProcessable);
		ewp2.process(0);
		assertSame(ep5, ewp2.lastProcessable);

	}

}