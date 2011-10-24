package opencranium.cranium;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import opencranium.util.Id;

/**
 * Abstract class that implements some operations of a Processor and defines
 * another ones that a Workspace processor must implement.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class WorkspaceProcessor extends Processor {

	/**
	 * Input types allowed by this WorkspaceProcessor.
	 */
	private SortedSet<Id> inputTypes;

	/**
	 * Output types allowed by this WorkspaceProcessor.
	 */
	private SortedSet<Id> outputTypes;

	/**
	 * Current Workspace of this WorkspaceProcessor
	 */
	private Workspace workspace;

	/**
	 * Variable to set if the output is enabled or not. If it is not enabled all
	 * the outputs will be automatically discarded.
	 */
	private boolean outputEnabled;

	/**
	 * Variable to set if the input is enabled or not. If it is not enabled all
	 * the input will be automatically droped. Without intputs not outputs are
	 * generated..
	 */
	private boolean inputEnabled;

	/**
	 * Default constructor with id, inputTypes and outputTypes.
	 * 
	 * @param id
	 *            Id of the processor.
	 * @param inputTypes
	 *            Input types allowed by this WorkspaceProcessor.
	 * @param outputTypes
	 *            Output types allowed by this WorkspaceProcessor.
	 */
	public WorkspaceProcessor(Id id, Set<Id> inputTypes, Set<Id> outputTypes) {
		super(id);
		this.setInputTypes(inputTypes);
		this.setOutputTypes(outputTypes);
		this.inputEnabled = true;
		this.outputEnabled = true;
	}

	/**
	 * Default constructor with id.
	 * 
	 * @param id
	 *            Id of the processor.
	 */
	public WorkspaceProcessor(Id id) {
		this(id, null, null);
	}

	/**
	 * Sets the input types of this workspace processor.
	 * 
	 * @param inputTypes
	 *            The input types.
	 */
	protected void setInputTypes(Set<Id> inputTypes) {
		this.inputTypes = this.parseInputOutputTypes(inputTypes);
	}

	/**
	 * Sets the output types of this workspace processor.
	 * 
	 * @param outputTypes
	 *            The input types.
	 */
	protected void setOutputTypes(Set<Id> outputTypes) {
		this.outputTypes = this.parseInputOutputTypes(outputTypes);
	}

	/**
	 * Parse a set of types into a sorted set of types. More efficiently.
	 * 
	 * @param types
	 *            Set with the types.
	 * @return A sorted set with the types.
	 */
	private SortedSet<Id> parseInputOutputTypes(Set<Id> types) {
		SortedSet<Id> set = new TreeSet<Id>();
		if (types != null && types.size() > 0) {
			set.addAll(types);
		}
		return set;
	}

	/**
	 * Purges the processable queue and removes the last elements to fit the
	 * final size.
	 * 
	 * @param finalSize
	 *            final size of the processable elements.
	 */
	public void purgeProcessableList(int finalSize) {
		while (this.processableSortedList.getSize() > finalSize) {
			this.processableSortedList.removeLastElement();
		}
	}

	/**
	 * Process the next processable.
	 * 
	 * @param processable
	 *            Next processable to process.
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @return An array with the processable elements generated.
	 */
	protected abstract Processable[] execute(Processable processable, long milliseconds);

	/**
	 * Process nothing but returns a processable elements generated. This method
	 * is only called for sensory-motor processors.
	 * 
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @return An array with the processable elements generated.
	 */
	public Processable[] execute(long milliseconds) {
		throw new CraniumException("Method execute(long) not overwritted in class: "
				+ this.getClass().getCanonicalName(), this);
	}

	/**
	 * Resets this workspace processor, removes all elements in the queue.
	 */
	public void reset() {
		this.processableSortedList.clear();
		this.cleanMemory();
	}

	/**
	 * Checks if an input Id is a valid input.
	 * 
	 * @param id
	 *            The input Id.
	 * @return true if it is a valid input.
	 */
	public boolean isValidInput(Id id) {
		return id != null && this.inputTypes.contains(id);
	}

	/**
	 * Checks if an output Id is a valid output.
	 * 
	 * @param id
	 *            The output Id.
	 * @return true if it is a valid output.
	 */
	public boolean isValidOutput(Id id) {
		return id != null && this.outputTypes.contains(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.Processor#addProcessable(opencranium.cranium.Processable
	 * )
	 */
	@Override
	public boolean addProcessable(Processable data) {
		boolean added = false;
		if (this.isValidInput(data.getId())) {
			if (this.inputEnabled) {
				added = super.addProcessable(data);
			}
		} else {
			throw new CraniumException("Not valid input type: " + data.getId() + " for workspace processor: "
					+ this.getId(), this);
		}
		return added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processor#processNextElement(Processable,long)
	 */
	@Override
	protected final void processNextElement(Processable element, long milliseconds) {
		Processable[] result = execute(element, milliseconds);
		processResults(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processor#processNoElement(long)
	 */
	@Override
	protected final void processNoElement(long milliseconds) {
		Processable[] result = this.execute(milliseconds);
		processResults(result);
	}

	/**
	 * Process the results of processing an element.
	 * 
	 * @param result
	 *            The results to process.
	 */
	private void processResults(Processable[] result) {
		if (result != null && result.length > 0) {
			for (Processable processable : result) {
				if (isValidOutput(processable.getId())) {
					if (this.outputEnabled) {
						manageResult(processable);
					}
				} else {
					throw new CraniumException("Not valid output type: " + processable.getId(), this);
				}
			}
		}
	}

	/**
	 * Method called when it is needed to clean the current short-term memory.
	 * 
	 * @see WorkspaceProcessor#reset()
	 */
	public abstract void cleanMemory();

	/**
	 * @return the inputTypes
	 */
	public Set<Id> getInputTypes() {
		return this.inputTypes;
	}

	/**
	 * @return the outputTypes
	 */
	public Set<Id> getOutputTypes() {
		return this.outputTypes;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return this.workspace;
	}

	/**
	 * Sets the Workspace of this WorkspaceProcessor
	 * 
	 * @param workspace
	 *            the workspace to set
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	/**
	 * Returns the current short term memory.
	 * 
	 * @return An array of processable with the current short term memory.
	 */
	public abstract Processable[] getSortTermMemory();

	/**
	 * Manage a result generated by a processor. This method must be overrider
	 * in order to know what to do with the results.
	 * 
	 * @param result
	 *            The result.
	 */
	protected abstract void manageResult(Processable result);

	/**
	 * @return the outputEnabled
	 */
	public boolean isOutputEnabled() {
		return outputEnabled;
	}

	/**
	 * @param outputEnabled
	 *            the outputEnabled to set
	 */
	public void setOutputEnabled(boolean outputEnabled) {
		this.outputEnabled = outputEnabled;
	}

	/**
	 * @return the inputEnabled
	 */
	public boolean isInputEnabled() {
		return inputEnabled;
	}

	/**
	 * @param inputEnabled
	 *            the inputEnabled to set
	 */
	public void setInputEnabled(boolean inputEnabled) {
		this.inputEnabled = inputEnabled;
		if (!this.inputEnabled) {
			this.processableSortedList.clear();
		}
	}

}