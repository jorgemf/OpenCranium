package test.opencranium.cera;

import opencranium.Core;
import opencranium.cera.CoreLayer;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;

public class EmptyCoreLayer extends CoreLayer {

	public EmptyCoreLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(core, processorThreadPool);
	}

	@Override
	public void layerManageResult(Processable processable) {
		super.layerManageResult(processable);
	}
}
