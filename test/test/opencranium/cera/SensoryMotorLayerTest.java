package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.Core;
import opencranium.cera.CeraException;
import opencranium.cera.Layer;
import opencranium.cera.SensoryMotorLayer;
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
public class SensoryMotorLayerTest extends TestCase {

	@Test
	public void testSkills() {
		SensoryMotorLayer layer = Core.instance().getSensoryMotorLayer();

		assertSame(layer.getLayerType(), Layer.Type.SENSORY_MOTOR_LAYER);
		assertTrue(layer.isSensoryMotorLayer());
		assertFalse(layer.isPhysicalLayer());
		assertFalse(layer.isMissionLayer());
		assertFalse(layer.isCoreLayer());

		EmptyMotorSkill ems = new EmptyMotorSkill();

		assertTrue(layer.registerAgentSkill(ems));
		assertFalse(layer.registerAgentSkill(ems));

		try {
			layer.registerProcessor(null);
			fail();
		} catch (CeraException exception) {
		}
	}

	@Test
	public void testManageResults() {
		EmptySensoryMotorLayer esml = new EmptySensoryMotorLayer(Core.instance(), new ProcessorThreadPool());

		Id id1 = IdManager.instance().getId("EmptyCeraWorkspaceProcessor1", EmptyCeraWorkspaceProcessor.class);
		EmptyCeraWorkspaceProcessor2 ecwpP = new EmptyCeraWorkspaceProcessor2(id1);
		Id id2 = IdManager.instance().getId("EmptyCeraWorkspaceProcessor2", EmptyCeraWorkspaceProcessor.class);
		EmptyCeraWorkspaceProcessor2 ecwpM = new EmptyCeraWorkspaceProcessor2(id2);
		EmptyCognitiveFunction ecf = new EmptyCognitiveFunction();

		try {
			esml.layerManageResult(new EmptyProcessable(1, 1));
			fail();
		} catch (CeraException exception) {
		}

		EmptyAction ea = new EmptyAction(Priority.HIGH);

		try {
			esml.layerManageResult(ea);
			fail();
		} catch (CeraException exception) {
		}

		assertTrue(Core.instance().getPhysicalLayer().registerProcessor(ecwpP));
		assertTrue(Core.instance().getMissionLayer().registerProcessor(ecwpM));
		assertTrue(Core.instance().getCoreLayer().addConsciousCognitiveFunction(ecf));

		// single percept

		EmptyPercept.TYPE = Type.SINGLE_PERCEPT;
		EmptyPercept ep = new EmptyPercept();

		esml.layerManageResult(ep);

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ep, ecwpP.lastPercept);
		assertNull(ecwpP.lastAction);

		assertFalse(ecwpM.isSomethingToProcess());
		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();

		// complex percept

		EmptyPercept.TYPE = Type.COMPLEX_PERCEPT;
		ep = new EmptyPercept();

		esml.layerManageResult(ep);

		assertTrue(ecwpP.isSomethingToProcess());
		ecwpP.process(0);
		assertSame(ep, ecwpP.lastPercept);
		assertNull(ecwpP.lastAction);

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

		assertTrue(ecwpM.isSomethingToProcess());

		ecwpM.process(0);
		assertSame(ep, ecwpM.lastPercept);
		assertNull(ecwpM.lastAction);

		assertFalse(ecf.isSomethingToProcess());

		ecwpP.reset();
		ecwpM.reset();
		ecf.reset();
	}
}