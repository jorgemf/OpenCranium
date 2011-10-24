package test.opencranium.cera;

import opencranium.cera.Layer;
import opencranium.cognitive.CognitiveFunction;
import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * @author Jorge Muñoz
 */
public class EmptyLayer extends Layer {

	public Processable lastResult;

	public Processable lastSubmitted;

	public static final Id ID = IdManager.instance().getId("EmptyLayer", EmptyLayer.class);

	public EmptyLayer() {
		super(ID, Type.MISSION_LAYER, null);
	}

	public EmptyLayer(Type layerType) {
		super(ID, layerType, null);
	}

	@Override
	protected void layerSubmitProcessable(Processable processable) {
		this.lastSubmitted = processable;
	}

	@Override
	public void reset() {
		super.reset();
		this.lastResult = null;
		this.lastSubmitted = null;
	}

	@Override
	protected void layerManageResult(Processable processable) {
		this.lastResult = processable;
	}

	@Override
	public void manageResult(Processable processable) {
		super.manageResult(processable);
	}

	@Override
	public boolean addCognitiveFunction(CognitiveFunction function) {
		return super.addCognitiveFunction(function);
	}

	@Override
	public boolean removeCognitiveFunction(CognitiveFunction function) {
		return super.removeCognitiveFunction(function);
	}

	@Override
	public boolean registerProcessor(Processor processor) {
		return false;
	}

}
