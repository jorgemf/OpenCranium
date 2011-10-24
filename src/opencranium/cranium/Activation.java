package opencranium.cranium;

/**
 * An activation value for the percepts and the actions. More activated elements
 * are more likely to be processed.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Activation {

	/**
	 * Maximum value of the activation.
	 */
	public static final int MAX = 1000;

	/**
	 * Minimum value of the activation.
	 */
	public static final int MIN = 0;

	/**
	 * Activation value.
	 */
	private int value;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 *            The value, if it is out of the bounds the value is set to the
	 *            closest one.
	 */
	public Activation(int value) {
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