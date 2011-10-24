package opencranium;

/**
 * Generic exception for the platform OpenCranium.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class OpenCraniumException extends RuntimeException {

	/**
	 * The object source of this exception.
	 */
	private Object source;

	/**
	 * To serialize the class.
	 */
	private static final long serialVersionUID = -7957760052720564696L;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param source
	 *            Object source of the exception.
	 */
	public OpenCraniumException(String message, Object source) {
		super(message);
		this.source = source;
	}

	/**
	 * Returns the object source of this exception.
	 * 
	 * @return The object source of this exception.
	 */
	public Object getObjectSource() {
		return this.source;
	}

}
