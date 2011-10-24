package opencranium.cognitive;

import opencranium.OpenCraniumException;

/**
 * An exception class for being used in Cognitive Functions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class CognitiveFunctionException extends OpenCraniumException {

	/**
	 * Generated serail id.
	 */
	private static final long serialVersionUID = 7135790568582827057L;

	/**
	 * The cognitive function where the exception happened.
	 */
	private CognitiveFunction congnitiveFunction;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param congnitiveFunction
	 *            Cognitive function of the exception
	 */
	public CognitiveFunctionException(String message, CognitiveFunction congnitiveFunction) {
		super(message, congnitiveFunction);
		this.congnitiveFunction = congnitiveFunction;
	}

	/**
	 * @return the congnitiveFunction
	 */
	public CognitiveFunction getCongnitiveFunction() {
		return this.congnitiveFunction;
	}

}