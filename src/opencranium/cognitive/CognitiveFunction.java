package opencranium.cognitive;

import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.util.Id;

/**
 * Abstract class for cognitive functions. This class has two methods that have
 * to be overwritten <em>implicitProcess</em> and <em>explicitProcess</em>.
 * Implicit process is the implicit processing of elements when these are
 * created in the processors, and explicit process is the processing of elements
 * when these ones arrives into the core layer, that is, the conscious content
 * of the agent.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class CognitiveFunction extends Processor {

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            Id of the cognitive function
	 */
	public CognitiveFunction(Id id) {
		super(id);
	}

	/**
	 * Process elements when these ones are created in the processors. This
	 * method should spend very short time to process the element.
	 * 
	 * @param element
	 */
	public abstract void implicitProcess(Processable element);

	/**
	 * Process elements when these ones arrives to the core layer, that is,
	 * these elements are the conscious content of the agent. This method works
	 * as <em>processNextElement</em> method work.
	 * 
	 * @param element
	 *            The element to process
	 * @param milliseconds
	 *            The time when the processor should stop processing.
	 * @see Processor#processNextElement(Processable, long)
	 */
	public abstract void explicitProcess(Processable element, long milliseconds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.Processor#processNextElement(opencranium.cranium.
	 * Processable, long)
	 */
	@Override
	protected final void processNextElement(Processable element, long milliseconds) {
		this.explicitProcess(element, milliseconds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processor#processNoElement(long)
	 */
	@Override
	protected final void processNoElement(long milliseconds) {
		throw new CognitiveFunctionException("Cognitive functions do not suppor process no element method", this);
	}

	/**
	 * Removes the temporal information of the cognitive function. Only sort
	 * term memory elements.
	 */
	public void reset() {
		// empty
	}

}