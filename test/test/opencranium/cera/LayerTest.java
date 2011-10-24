package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.cera.Layer.Type;
import opencranium.command.Action.Priority;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.data.Percept.AppraisalType;
import opencranium.data.Percept.Source;
import opencranium.util.ElementProcessingTime;
import opencranium.util.StatisticsManager;
import opencranium.util.Time;

import org.junit.Test;

import test.opencranium.cognitive.EmptyCognitiveFunction;
import test.opencranium.command.EmptyAction;
import test.opencranium.cranium.EmptyProcessable;
import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class LayerTest extends TestCase {

	@Test
	public void testLayerType() {
		EmptyLayer el;

		el = new EmptyLayer(Type.CORE_LAYER);
		assertTrue(el.isCoreLayer());
		assertFalse(el.isMissionLayer());
		assertFalse(el.isPhysicalLayer());
		assertFalse(el.isSensoryMotorLayer());

		el = new EmptyLayer(Type.MISSION_LAYER);
		assertFalse(el.isCoreLayer());
		assertTrue(el.isMissionLayer());
		assertFalse(el.isPhysicalLayer());
		assertFalse(el.isSensoryMotorLayer());

		el = new EmptyLayer(Type.PHYSICAL_LAYER);
		assertFalse(el.isCoreLayer());
		assertFalse(el.isMissionLayer());
		assertTrue(el.isPhysicalLayer());
		assertFalse(el.isSensoryMotorLayer());

		el = new EmptyLayer(Type.SENSORY_MOTOR_LAYER);
		assertFalse(el.isCoreLayer());
		assertFalse(el.isMissionLayer());
		assertFalse(el.isPhysicalLayer());
		assertTrue(el.isSensoryMotorLayer());
	}

	@Test
	public void testReset() {
		EmptyLayer el = new EmptyLayer();
		assertNull(el.lastResult);
		assertNull(el.lastSubmitted);
		Processable ep = new EmptyProcessable(0, 0);
		ep.setActivation(new Activation(Activation.MAX));
		el.submitProcessable(ep);
		assertSame(ep, el.lastSubmitted);
		el.manageResult(ep);
		assertSame(ep, el.lastResult);
		el.reset();
		assertNull(el.lastResult);
		assertNull(el.lastSubmitted);
	}

	@Test
	public void testActivationThreshold() {
		EmptyLayer el = new EmptyLayer();

		assertNotNull(el.getActivationThreshold());
		assertEquals(Activation.MIN, el.getActivationThreshold().getValue());

		Activation a1 = new Activation(Activation.MAX - 10);
		el.setActivationThreshold(a1);
		assertNotSame(a1, el.getActivationThreshold());
		assertEquals(a1.getValue(), el.getActivationThreshold().getValue());

		Processable ep = new EmptyProcessable(0, 0);
		ep.setActivation(new Activation(Activation.MIN));
		StatisticsManager.enableStatistics();

		assertEquals(0, ep.getId().getStadistics(el.getId()).getDiscardedTimes());
		assertNull(el.lastSubmitted);
		el.submitProcessable(ep);
		assertNull(el.lastSubmitted);
		assertEquals(1, ep.getId().getStadistics(el.getId()).getDiscardedTimes());

		StatisticsManager.disableStatistics();

		el.submitProcessable(ep);
		assertNull(el.lastSubmitted);
		assertEquals(1, ep.getId().getStadistics(el.getId()).getDiscardedTimes());

		StatisticsManager.enableStatistics();

		el.submitProcessable(ep);
		assertNull(el.lastSubmitted);
		assertEquals(2, ep.getId().getStadistics(el.getId()).getDiscardedTimes());

		Activation a2 = new Activation((Activation.MAX + Activation.MIN) / 2);
		el.setActivationThreshold(a2);
		ep.setActivation(a2);

		el.submitProcessable(ep);
		assertSame(ep, el.lastSubmitted);
		assertEquals(1, ep.getId().getStadistics(el.getId()).getProcessedTimes());

		Activation amin = new Activation(Activation.MIN);
		EmptyAction ea = new EmptyAction(Priority.HIGH);
		ea.setActivation(amin);
		el.lastSubmitted = null;
		el.submitProcessable(ea);
		assertSame(ea, el.lastSubmitted);

		EmptyPercept epercept = new EmptyPercept();
		epercept.setActivation(amin);
		epercept.setAppraisalType(AppraisalType.MATCH);
		el.lastSubmitted = null;
		el.submitProcessable(epercept);
		assertNull(el.lastSubmitted);
		epercept = new EmptyPercept();
		epercept.setAppraisalType(AppraisalType.MISSMATCH);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertSame(epercept, el.lastSubmitted);
		epercept = new EmptyPercept();
		epercept.setAppraisalType(AppraisalType.NOVELTY);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertSame(epercept, el.lastSubmitted);

		epercept = new EmptyPercept();
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertNull(el.lastSubmitted);
		epercept = new EmptyPercept();
		epercept.setSource(Source.COGNITIVE_FUNCTION);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertSame(epercept, el.lastSubmitted);

		epercept = new EmptyPercept();
		epercept.setSource(Source.MISSION_PROCESSOR);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertSame(epercept, el.lastSubmitted);

		epercept = new EmptyPercept();
		epercept.setSource(Source.PROPRIOCEPTIVE_SENSOR);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertNull(el.lastSubmitted);

		epercept = new EmptyPercept();
		epercept.setSource(Source.PHYSICAL_PROCESSOR);
		el.lastSubmitted = null;
		assertNull(el.lastSubmitted);
		el.submitProcessable(epercept);
		assertNull(el.lastSubmitted);
	}

	@Test
	public void testAverageActivation() {
		EmptyLayer el = new EmptyLayer();
		Activation a1 = new Activation((Activation.MAX + Activation.MIN) / 2);
		Activation a2 = new Activation(Activation.MAX - 1);
		Activation a3 = new Activation(Activation.MIN + 1);
		Processable ep = new EmptyProcessable(0, 0);
		el.setActivationThreshold(a1);

		Time tick1a = new Time(1, 10);
		Time tick1b = new Time(1, 15);
		Time tick2 = new Time(2, 20);
		Time tick3 = new Time(3, 30);
		Time tick4 = new Time(4, 40);

		StatisticsManager.enableStatistics();

		el.systemTick(tick1a);
		ep.setActivation(a2);
		el.submitProcessable(ep);
		el.submitProcessable(ep);
		ep.setActivation(a3);
		el.submitProcessable(ep);
		el.submitProcessable(ep);

		el.systemTick(tick1b);

		assertEquals(0, el.getLastNumberOfProcessableElementsSubmitted());
		assertEquals(0, el.getLastNumberOfTotalProcessableElementsSubmitted());
		assertEquals(Activation.MIN, el.getLastTickTotalAverageActivation().getValue());
		assertEquals(Activation.MIN, el.getLastTickAverageActivation().getValue());

		el.systemTick(tick2);

		assertEquals(2, el.getLastNumberOfProcessableElementsSubmitted());
		assertEquals(4, el.getLastNumberOfTotalProcessableElementsSubmitted());
		assertEquals(a1.getValue(), el.getLastTickTotalAverageActivation().getValue());
		assertEquals(a2.getValue(), el.getLastTickAverageActivation().getValue());

		StatisticsManager.disableStatistics();

		ep.setActivation(a2);
		el.submitProcessable(ep);
		el.submitProcessable(ep);
		ep.setActivation(a3);
		el.submitProcessable(ep);
		el.submitProcessable(ep);

		el.systemTick(tick3);

		assertEquals(2, el.getLastNumberOfProcessableElementsSubmitted());
		assertEquals(4, el.getLastNumberOfTotalProcessableElementsSubmitted());
		assertEquals(a1.getValue(), el.getLastTickTotalAverageActivation().getValue());
		assertEquals(a2.getValue(), el.getLastTickAverageActivation().getValue());

		StatisticsManager.enableStatistics();

		ep.setActivation(a2);
		el.submitProcessable(ep);
		ep.setActivation(a3);
		el.submitProcessable(ep);

		el.systemTick(tick4);

		assertEquals(1, el.getLastNumberOfProcessableElementsSubmitted());
		assertEquals(2, el.getLastNumberOfTotalProcessableElementsSubmitted());
		assertEquals(a1.getValue(), el.getLastTickTotalAverageActivation().getValue());
		assertEquals(a2.getValue(), el.getLastTickAverageActivation().getValue());

		el.reset();

		assertEquals(0, el.getLastNumberOfProcessableElementsSubmitted());
		assertEquals(0, el.getLastNumberOfTotalProcessableElementsSubmitted());
		assertEquals(Activation.MIN, el.getLastTickTotalAverageActivation().getValue());
		assertEquals(Activation.MIN, el.getLastTickAverageActivation().getValue());
	}

	@Test
	public void testCognitiveFunctions() {
		EmptyLayer el = new EmptyLayer();
		EmptyProcessable ep = new EmptyProcessable(0, 0);
		EmptyCognitiveFunction ecf = new EmptyCognitiveFunction();
		EmptyCognitiveFunction ecf2 = new EmptyCognitiveFunction();

		assertTrue(el.addCognitiveFunction(ecf));
		assertFalse(el.addCognitiveFunction(ecf));
		assertTrue(el.addCognitiveFunction(ecf2));
		assertTrue(el.removeCognitiveFunction(ecf2));

		Time tick1a = new Time(1, 10);
		Time tick1b = new Time(1, 15);
		Time tick2 = new Time(2, 20);
		Time tick3 = new Time(3, 30);
		Time tick4 = new Time(4, 40);

		EmptyCognitiveFunction.sleep = 50;
		StatisticsManager.enableStatistics();

		el.systemTick(tick1a);

		assertNull(el.lastResult);
		assertNull(el.lastSubmitted);
		el.manageResult(ep);
		assertSame(ep, el.lastResult);
		assertNull(el.lastSubmitted);

		ElementProcessingTime ept = ep.getId().getStadistics(ecf.getId());
		ElementProcessingTime ept2 = ecf.getId().getStadistics(ep.getId());

		assertEquals((double) 50 * 1000000, (double) ept.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(1, ept.getProcessedTimes());
		assertEquals((double) 50 * 1000000, (double) ept2.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(1, ept2.getProcessedTimes());

		el.systemTick(tick1b);

		assertEquals((double) 50 * 1000000, (double) ept.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(1, ept.getProcessedTimes());
		assertEquals((double) 50 * 1000000, (double) ept2.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(1, ept2.getProcessedTimes());

		el.systemTick(tick2);

		assertSame(ep, el.lastResult);
		assertNull(el.lastSubmitted);
		el.manageResult(ep);
		assertSame(ep, el.lastResult);
		assertNull(el.lastSubmitted);

		el.systemTick(tick3);

		assertEquals((double) 50 * 1000000, (double) ept.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(2, ept.getProcessedTimes());
		assertEquals((double) 50 * 1000000, (double) ept2.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(2, ept2.getProcessedTimes());

		StatisticsManager.disableStatistics();

		assertSame(ep, el.lastResult);
		assertNull(el.lastSubmitted);
		el.manageResult(ep);
		assertSame(ep, el.lastResult);
		assertNull(el.lastSubmitted);

		el.systemTick(tick4);

		assertEquals((double) 50 * 1000000, (double) ept.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(2, ept.getProcessedTimes());
		assertEquals((double) 50 * 1000000, (double) ept2.getAverageProcessingTime(), (double) 1 * 1000000);
		assertEquals(2, ept2.getProcessedTimes());

		StatisticsManager.enableStatistics();

	}

}