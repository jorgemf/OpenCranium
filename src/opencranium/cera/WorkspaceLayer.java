package opencranium.cera;

import java.util.ArrayList;
import java.util.List;

import opencranium.Core;
import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.cranium.Workspace;
import opencranium.data.Percept;
import opencranium.util.Id;

/**
 * This class extends the functionality of a layer including a workspace where
 * the CeraWorkspaceProcessor can be executed.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class WorkspaceLayer extends Layer {

	/**
	 * Workspace of the layer.
	 */
	private Workspace workspace;

	/**
	 * List of CeraWorkspaceProcessors that this layer contains.
	 */
	private List<CeraWorkspaceProcessor> processorsSet;

	/**
	 * 
	 * Default constructor.
	 * 
	 * @param id
	 *            Id of the layer
	 * @param layerType
	 *            The type of the layer
	 * @param processorThreadPool
	 *            The ThreadPool used in the workspace to execute the
	 *            processors.
	 * @param core
	 *            The core of the layers
	 */
	protected WorkspaceLayer(Id id, Type layerType, ProcessorThreadPool processorThreadPool, Core core) {
		super(id, layerType, core);
		this.workspace = new Workspace(processorThreadPool);
		this.processorsSet = new ArrayList<CeraWorkspaceProcessor>();
	}

	@Override
	public boolean registerProcessor(Processor processor) {
		boolean added = false;
		if (processor instanceof CeraWorkspaceProcessor) {
			added = registerProcessor((CeraWorkspaceProcessor) processor);
		} else {
			throw new CeraException("Only CeraWorkspaceProcessor are allowed in Cera layers", this);
		}
		return added;
	}

	/**
	 * Register a processor into the layer.
	 * 
	 * @param processor
	 *            The CeraWorkspaceProcessor to register.
	 * @return true if the processor was added, false otherwise.
	 */
	public boolean registerProcessor(CeraWorkspaceProcessor processor) {
		boolean added = false;
		if (processor.getLayer() == null) {
			if (this.workspace.registerProcessor(processor)) {
				processor.setWorkspace(this.workspace);
				processor.setLayer(this);
				added = this.processorsSet.add(processor);
			}
		}
		return added;
	}

	/**
	 * Submits a processable into the layer. If the processable is not an Action
	 * or a Percept an exception is thrown.
	 * 
	 * @param processable
	 *            The processable element to process by the processors in the
	 *            layer. It must be an Action or a Percept.
	 */
	public void layerSubmitProcessable(Processable processable) {
		if (processable instanceof Action || processable instanceof Percept) {
			this.workspace.submitProcessable(processable);
		} else {
			throw new CeraException("CERA only support Processable of types Action and Percept", this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		for (CeraWorkspaceProcessor processor : processorsSet) {
			processor.reset();
		}
	}
}