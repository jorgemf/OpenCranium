package opencranium.cera;

import java.util.ArrayList;
import java.util.List;

import opencranium.Core;
import opencranium.cognitive.CognitiveFunction;
import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * Layer with the cognitive functions of the CERA architecture.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class CoreLayer extends Layer {

	/**
	 * ID of the layer. Static constant.
	 */
	public static final Id ID = IdManager.instance().getId("CoreLayer", CoreLayer.class);

	/**
	 * Pool of threads that execute the cognitive functions.
	 */
	private ProcessorThreadPool consciousCognitiveFuncionsProcessorPool;

	/**
	 * List of all the conscious cognitives functions in the architecture.
	 */
	private List<CognitiveFunction> consciousCognitiveFunctionsList;

	/**
	 * List of all the unconscious cognitives functions in the architecture.
	 */
	private List<CognitiveFunction> unconsciousCognitiveFunctionsList;

	/**
	 * Default constructor.
	 * 
	 * @param core
	 *            Core of the CERA architecture.
	 * @param processorThreadPool
	 *            Pool of threads which execute the skills.
	 */
	public CoreLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(ID, Type.CORE_LAYER, core);
		this.consciousCognitiveFuncionsProcessorPool = processorThreadPool;
		this.consciousCognitiveFunctionsList = new ArrayList<CognitiveFunction>();
		this.unconsciousCognitiveFunctionsList = new ArrayList<CognitiveFunction>();
	}

	/**
	 * Adds a conscious cognitive function to the layer. The cognitive functions
	 * added with this method are only called their method explicitProcess.
	 * 
	 * @param function
	 *            Cognitive function to add in the layer.
	 * @return True if the cognitive function was added, false otherwise.
	 * @see CognitiveFunction#explicitProcess(Processable, long)
	 */
	public boolean addConsciousCognitiveFunction(CognitiveFunction function) {
		boolean added = false;
		if (!this.consciousCognitiveFunctionsList.contains(function)) {
			added = this.consciousCognitiveFunctionsList.add(function);
			added &= this.consciousCognitiveFuncionsProcessorPool.addProcessor(function);
			if (!added) {
				this.consciousCognitiveFunctionsList.remove(function);
				this.consciousCognitiveFuncionsProcessorPool.removeProcessor(function);
			}
		}
		return added;
	}

	/**
	 * Removes a conscious cognitive function from the layer.
	 * 
	 * @param function
	 *            Cognitive function to remove from the layer.
	 * @return True if the cognitive function was added, false otherwise.
	 * @see CoreLayer#addConsciousCognitiveFunction(CognitiveFunction)
	 */
	public boolean removeConsciousCognitiveFunction(CognitiveFunction function) {
		boolean removed = this.consciousCognitiveFunctionsList.remove(function);
		removed &= this.consciousCognitiveFuncionsProcessorPool.removeProcessor(function);
		return removed;
	}

	/**
	 * Adds an unconscious cognitive function to the layers of the CERA
	 * architecture. The cognitive functions added with this method are only
	 * called their method implicitProcess.
	 * 
	 * @param function
	 *            Cognitive function to add in the layers.
	 * @return True if the cognitive function was added, false otherwise.
	 * @see CognitiveFunction#implicitProcess(Processable)
	 */
	public boolean addUnconsciousCognitiveFunction(CognitiveFunction function) {
		boolean added = false;
		if (!this.unconsciousCognitiveFunctionsList.contains(function)) {
			added = this.unconsciousCognitiveFunctionsList.add(function);
			added &= core.getSensoryMotorLayer().addCognitiveFunction(function);
			added &= core.getPhysicalLayer().addCognitiveFunction(function);
			added &= core.getMissionLayer().addCognitiveFunction(function);
			added &= core.getCoreLayer().addCognitiveFunction(function);
			if (!added) {
				this.unconsciousCognitiveFunctionsList.remove(function);
				core.getSensoryMotorLayer().removeCognitiveFunction(function);
				core.getPhysicalLayer().removeCognitiveFunction(function);
				core.getMissionLayer().removeCognitiveFunction(function);
				core.getCoreLayer().removeCognitiveFunction(function);
			}
		}
		return added;
	}

	/**
	 * Removes a unconscious cognitive function from the layers.
	 * 
	 * @param function
	 *            Cognitive function to remove from the layers.
	 * @return True if the cognitive function was added, false otherwise.
	 * @see CoreLayer#addUnconsciousCognitiveFunction(CognitiveFunction)
	 */
	public boolean removeUnconsciousCognitiveFunction(CognitiveFunction function) {
		boolean removed = this.unconsciousCognitiveFunctionsList.remove(function);
		removed &= core.getSensoryMotorLayer().removeCognitiveFunction(function);
		removed &= core.getPhysicalLayer().removeCognitiveFunction(function);
		removed &= core.getMissionLayer().removeCognitiveFunction(function);
		removed &= core.getCoreLayer().removeCognitiveFunction(function);
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cera.Layer#submitProcessable(opencranium.cranium.Processable)
	 */
	@Override
	public void layerSubmitProcessable(Processable processable) {
		for (CognitiveFunction function : this.consciousCognitiveFunctionsList) {
			function.addProcessable(processable);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#manageResult(opencranium.cranium.Processable)
	 */
	@Override
	protected void layerManageResult(Processable processable) {
		throw new CeraException("No results expected in Core Layer", this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		for (CognitiveFunction function : this.consciousCognitiveFunctionsList) {
			function.reset();
		}
		for (CognitiveFunction function : this.unconsciousCognitiveFunctionsList) {
			function.reset();
		}
	}

	@Override
	public boolean registerProcessor(Processor processor) {
		throw new CeraException("No processors allowed in core layer", this);
	}

}