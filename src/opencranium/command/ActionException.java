package opencranium.command;

import opencranium.OpenCraniumException;

/**
 * An exception class for being used in the Actions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class ActionException extends OpenCraniumException {

	/**
	 * To serialize the class.
	 */
	private static final long serialVersionUID = 7512656747097612436L;

	/**
	 * The action where the exception happens.
	 */
	private Action action;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param action
	 *            Action source of the exception.
	 */
	public ActionException(String message, Action action) {
		super(message, action);
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return this.action;
	}

}