package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.Core;
import opencranium.cera.CeraException;
import opencranium.cera.Layer;
import opencranium.cera.PhysicalLayer;
import opencranium.command.Action.Priority;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.data.Percept.Type;
import opencranium.util.Id;
import opencranium.util.IdManager;

import org.junit.Test;

import test.opencranium.cognitive.EmptyCognitiveFunction;
import test.opencranium.command.EmptyAction;
import test.opencranium.cranium.EmptyProcessable;
import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class PhysicalLayerTest extends TestCase {

	@Test
	public void testBasic() {
		PhysicalLayer layer = Core.instance().getPhysicalLayer();

		assertSame(layer.getLayerType(), Layer.Type.PHYSICAL_LAYER);
		assertFalse(layer.isSensoryMotorLayer());
		assertTrue(layer.isPhysicalLayer());
		assertFalse(layer.isMissionLayer());
		assertFalse(layer.isCoreLayer());

		EmptyCeraWorkspaceProcessor2 ecwp = new EmptyCeraWorkspaceProcessor2();

		assertTrue(layer.registerProcessor(ecwp));
		assertFalse(layer.registerProcessor(ecwp));

		try {
			layer.registerProcessor(null);
			fail();
		} catch (Exception exception) {
		}
	}

	@Test
	public void testManageResults() {
		PhysicalLayer esml = new EmptyPhysicalLayer(Core.instance(), new ProcessorThreadPool());

		Id id1 = IdManager.instance().getId("EmptyCeraWorkspaceProcessor1", EmptyCeraWorkspaceProcessor.class);
		EmptyCeraWorkspaceProcessor2 ecwpP = new EmptyCeraWorkspaceProcessor2(id1);
		Id id2 = IdManager.instance().getId("EmptyCeraWorkspaceProcessor2", EmptyCeraWorkspaceProcessor.class);
		EmptyCeraWorkspaceProcessor2 ecwpM = new EmptyCeraWorkspaceProcessor2(id2);
		EmptyCognitiveFunction ecf = new EmptyCognitiveFunction();
		EmptyMotorSkill2 ems = new EmptyMotorSkill2(EmptyAction.ID);

		assertTrue(Core.instance().getSensoryMotorLayer().registerAgentSkill(ems));
		assertTrue(Core.instance().getPhysicalLayer().registerProcessor(ecwpP));
		assertTrue(Core.instance().getMissionLayer().registerProcessor(ecwpM));
		assertTrue(Core.instance().getCoreLayer().addConsciousCognitiveFunction(ecf));

		try {
			esml.layerManageResult(new EmptyProcessable(1, 1));
			fail();
		} catch (CeraException exception) {
		}

		EmptyAction.TYPE = opencranium.command.Action.Type.SIMPLE;
		EmptyAction ea = new EmptyAction(Priority.HIGH);

		esml.layerManageResult(ea);

		// Simple action

		assertTrue(ems.isSomethingToProcess());
		ems.process(0);
		assertSame(ea, ems.lastAction);
		assertFalse(ems.isSomethingToProcess());

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ea, ecwpP.lastAction);
		assertNull(ecwpP.lastPercept);
		assertFalse(ecwpP.isSomethingToProcess());

		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();

		// complex action

		EmptyAction.TYPE = opencranium.command.Action.Type.COMPLEX;
		ea = new EmptyAction(Priority.HIGH);

		try {
			esml.layerManageResult(ea);
			fail();
		} catch (CeraException exception) {
		}

		// single percept

		EmptyPercept.TYPE = Type.SINGLE_PERCEPT;
		EmptyPercept ep = new EmptyPercept();

		try {
			esml.layerManageResult(ep);
			fail();
		} catch (CeraException exception) {
		}

		// complex percept

		EmptyPercept.TYPE = Type.COMPLEX_PERCEPT;
		ep = new EmptyPercept();

		esml.layerManageResult(ep);

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ep, ecwpP.lastPercept);
		assertNull(ecwpP.lastAction);
		assertFalse(ecwpP.isSomethingToProcess());

		assertTrue(ecwpM.isSomethingToProcess());
		ecwpM.process(0);
		assertSame(ep, ecwpM.lastPercept);
		assertNull(ecwpM.lastAction);
		assertFalse(ecwpM.isSomethingToProcess());

		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();

		// mission percept

		EmptyPercept.TYPE = Type.MISSION_PERCEPT;
		ep = new EmptyPercept();

		esml.layerManageResult(ep);

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ep, ecwpP.lastPercept);
		assertNull(ecwpP.lastAction);
		assertFalse(ecwpP.isSomethingToProcess());

		assertTrue(ecwpM.isSomethingToProcess());
		ecwpM.process(0);
		assertSame(ep, ecwpM.lastPercept);
		assertNull(ecwpM.lastAction);
		assertFalse(ecwpM.isSomethingToProcess());

		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();
	}

}