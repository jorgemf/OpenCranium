package test.opencranium.cera;

import opencranium.Core;
import opencranium.cera.MissionLayer;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;

public class EmptyMissionLayer extends MissionLayer {

	public EmptyMissionLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(core, processorThreadPool);
	}

	@Override
	public void layerManageResult(Processable processable) {
		super.layerManageResult(processable);
	}

}
