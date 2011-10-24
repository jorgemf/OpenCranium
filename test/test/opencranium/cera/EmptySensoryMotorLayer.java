package test.opencranium.cera;

import opencranium.Core;
import opencranium.cera.SensoryMotorLayer;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;

public class EmptySensoryMotorLayer extends SensoryMotorLayer {

	public EmptySensoryMotorLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(core, processorThreadPool);
	}

	@Override
	public void layerManageResult(Processable processable) {
		super.layerManageResult(processable);
	}

}
