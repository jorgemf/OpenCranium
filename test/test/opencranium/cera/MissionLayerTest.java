package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.Core;
import opencranium.cera.CeraException;
import opencranium.cera.Layer;
import opencranium.cera.MissionLayer;
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
public class MissionLayerTest extends TestCase {

	@Test
	public void testBasic() {
		MissionLayer layer = Core.instance().getMissionLayer();

		assertSame(layer.getLayerType(), Layer.Type.MISSION_LAYER);
		assertFalse(layer.isSensoryMotorLayer());
		assertFalse(layer.isPhysicalLayer());
		assertTrue(layer.isMissionLayer());
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
		EmptyMissionLayer eml = new EmptyMissionLayer(Core.instance(), new ProcessorThreadPool());

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
			eml.layerManageResult(new EmptyProcessable(1, 1));
			fail();
		} catch (CeraException exception) {
		}

		EmptyAction.TYPE = opencranium.command.Action.Type.SIMPLE;
		EmptyAction ea = new EmptyAction(Priority.HIGH);

		// Simple action

		try {
			eml.layerManageResult(ea);
			fail();
		} catch (CeraException exception) {
		}

		// complex action

		EmptyAction.TYPE = opencranium.command.Action.Type.COMPLEX;
		ea = new EmptyAction(Priority.HIGH);

		eml.layerManageResult(ea);

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ea, ecwpP.lastAction);
		assertNull(ecwpP.lastPercept);
		assertFalse(ecwpP.isSomethingToProcess());

		assertTrue(ecwpM.isSomethingToProcess());
		ecwpM.process(0);
		assertSame(ea, ecwpM.lastAction);
		assertNull(ecwpM.lastPercept);
		assertFalse(ecwpM.isSomethingToProcess());

		assertTrue(ecf.isSomethingToProcess());
		ecf.process(0);
		assertSame(ea, ecf.lastExplicit);
		assertNull(ecf.lastImplicit);
		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();

		// single percept

		EmptyPercept.TYPE = Type.SINGLE_PERCEPT;
		EmptyPercept ep = new EmptyPercept();

		try {
			eml.layerManageResult(ep);
			fail();
		} catch (CeraException exception) {
		}

		// complex percept

		EmptyPercept.TYPE = Type.COMPLEX_PERCEPT;
		ep = new EmptyPercept();

		try {
			eml.layerManageResult(ep);
			fail();
		} catch (CeraException exception) {
		}

		// mission percept

		EmptyPercept.TYPE = Type.MISSION_PERCEPT;
		ep = new EmptyPercept();

		eml.layerManageResult(ep);

		assertFalse(ecwpP.isSomethingToProcess());

		assertTrue(ecwpM.isSomethingToProcess());
		ecwpM.process(0);
		assertSame(ep, ecwpM.lastPercept);
		assertNull(ecwpM.lastAction);
		assertFalse(ecwpM.isSomethingToProcess());

		assertTrue(ecf.isSomethingToProcess());
		ecf.process(0);
		assertSame(ep, ecf.lastExplicit);
		assertNull(ecf.lastImplicit);
		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();
	}

}