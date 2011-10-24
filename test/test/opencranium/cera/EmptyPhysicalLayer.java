package test.opencranium.cera;

import opencranium.Core;
import opencranium.cera.PhysicalLayer;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;

public class EmptyPhysicalLayer extends PhysicalLayer {

	public EmptyPhysicalLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(core, processorThreadPool);
	}

	@Override
	public void layerManageResult(Processable processable) {
		super.layerManageResult(processable);
	}

}
