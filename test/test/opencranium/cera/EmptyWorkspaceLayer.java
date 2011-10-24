package test.opencranium.cera;

import opencranium.Core;
import opencranium.cera.Layer;
import opencranium.cera.WorkspaceLayer;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

public class EmptyWorkspaceLayer extends WorkspaceLayer {

	public final static Id ID = IdManager.instance().getId("EmptyWorkspaceLayer", EmptyWorkspaceLayer.class);

	public EmptyWorkspaceLayer() {
		super(ID, Layer.Type.SENSORY_MOTOR_LAYER, new ProcessorThreadPool(), Core.instance());
	}

	@Override
	protected void layerManageResult(Processable processable) {
	}

}
