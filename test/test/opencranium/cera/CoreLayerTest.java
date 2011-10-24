package test.opencranium.cera;

import junit.framework.TestCase;
import opencranium.Core;
import opencranium.cera.CoreLayer;
import opencranium.cera.Layer;
import opencranium.cranium.ProcessorThreadPool;

import org.junit.Test;

import test.opencranium.cognitive.EmptyCognitiveFunction;
import test.opencranium.cranium.EmptyProcessable;
import test.opencranium.data.EmptyPercept;

/**
 * @author Jorge Muñoz
 */
public class CoreLayerTest extends TestCase {

	@Test
	public void testBasic() {
		CoreLayer layer = Core.instance().getCoreLayer();

		assertSame(layer.getLayerType(), Layer.Type.CORE_LAYER);
		assertFalse(layer.isSensoryMotorLayer());
		assertFalse(layer.isPhysicalLayer());
		assertFalse(layer.isMissionLayer());
		assertTrue(layer.isCoreLayer());

		try {
			layer.registerProcessor(new EmptyCeraWorkspaceProcessor2());
			fail();
		} catch (Exception exception) {
		}

		EmptyCoreLayer ecl = new EmptyCoreLayer(Core.instance(), new ProcessorThreadPool());

		try {
			ecl.layerManageResult(new EmptyProcessable(0, 0));
			fail();
		} catch (Exception exception) {
		}
	}

	@Test
	public void testConsciousCognitiveFunctions() {
		CoreLayer layer = Core.instance().getCoreLayer();
		EmptyCognitiveFunction ecf = new EmptyCognitiveFunction();

		assertTrue(layer.addConsciousCognitiveFunction(ecf));
		assertFalse(layer.addConsciousCognitiveFunction(ecf));

		EmptyPercept ep = new EmptyPercept();

		assertFalse(ecf.isSomethingToProcess());
		layer.layerSubmitProcessable(ep);
		assertTrue(ecf.isSomethingToProcess());
		ecf.process(0);
		assertFalse(ecf.isSomethingToProcess());

		assertTrue(layer.removeConsciousCognitiveFunction(ecf));
		assertFalse(layer.removeConsciousCognitiveFunction(ecf));

		assertFalse(ecf.isSomethingToProcess());
		layer.layerSubmitProcessable(ep);
		assertFalse(ecf.isSomethingToProcess());

	}

	@Test
	public void testUnconsciousCognitiveFunctions() {
		CoreLayer layer = Core.instance().getCoreLayer();
		EmptyCognitiveFunction ecf = new EmptyCognitiveFunction();

		assertTrue(layer.addUnconsciousCognitiveFunction(ecf));
		assertFalse(layer.addUnconsciousCognitiveFunction(ecf));

		EmptyPercept ep = new EmptyPercept();

		assertFalse(ecf.isSomethingToProcess());
		layer.layerSubmitProcessable(ep);
		assertFalse(ecf.isSomethingToProcess());

		assertTrue(layer.removeUnconsciousCognitiveFunction(ecf));
		assertFalse(layer.removeUnconsciousCognitiveFunction(ecf));

	}

	@Test
	public void testThreads() {
		// TODO here!!!
	}

}