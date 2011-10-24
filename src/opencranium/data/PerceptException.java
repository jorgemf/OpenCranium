package opencranium.data;

import opencranium.OpenCraniumException;

/**
 * An exception class for being used in the Percepts.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class PerceptException extends OpenCraniumException {

	/**
	 * To serialize the class.
	 */
	private static final long serialVersionUID = -1798315210262260327L;

	/**
	 * The percept where the exception happens.
	 */
	private Percept percept;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param percept
	 *            Percept source of the exception.
	 */
	public PerceptException(String message, Percept percept) {
		super(message, percept);
		this.percept = percept;
	}

	/**
	 * @return the percept
	 */
	public Percept getPercept() {
		return this.percept;
	}

}