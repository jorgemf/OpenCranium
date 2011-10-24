package opencranium.data;

/**
 * A confidence for the perceptual information.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Confidence {

	/**
	 * Maximum value of the confidence.
	 */
	public static final int MAX = 1000;

	/**
	 * Minimum value of the confidence.
	 */
	public static final int MIN = 0;

	/**
	 * Confidence value.
	 */
	private int value;

	/**
	 * Default constructor.
	 * 
	 * @param value
	 *            The value, if it is out of the bounds the value is set to the
	 *            closest one.
	 */
	public Confidence(int value) {
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