package opencranium.cranium;

/**
 * A priority value for sorting the process and execute first the process with
 * more priority.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Priority {

	/**
	 * Maximum value of the priority.
	 */
	public static final int MAX = 1000;

	/**
	 * Minimum value of the priority.
	 */
	public static final int MIN = 0;

	/**
	 * Normal value of the priority.
	 */
	public static final Priority NORMAL = new Priority((MAX + MIN) / 2);

	/**
	 * Priority value.
	 */
	private int value;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 *            The value, if it is out of the bounds the value is set to the
	 *            closest one.
	 */
	public Priority(int value) {
		setValue(value);
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Sets the value. If it is out of the bounds the value is set to the
	 * closest one.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
		if (this.value > MAX) {
			this.value = MAX;
		} else if (this.value < MIN) {
			this.value = MIN;
		}
	}

}