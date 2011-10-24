package opencranium.cognitive.attention;

import opencranium.cranium.Activation;

/**
 * 
 * Interface for context information.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public interface Context {

	/**
	 * Returns the distance between two contexts. If they are not compatible the
	 * method should return a negative distance equals to -1.
	 * 
	 * @param context
	 *            the other context.
	 * @return The distance (always positive) between the context. A negative
	 *         number equals to -1 if the context are not compatible.
	 */
	public float distance(Context context);

	/**
	 * The current activation of this context.
	 * 
	 * @return the current activation of this context.
	 */
	public Activation getActivation();

}