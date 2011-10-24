package opencranium.util.collection;

/**
 * An interface to compare elements used in the lists.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public interface SortedElement extends Comparable<SortedElement> {

	/**
	 * Returns a negative number if this element should be ordered first than
	 * the other, a positive number if the other should go first and 0 if both
	 * elements are equal.
	 * 
	 * @param other
	 *            the other element.
	 * @return negative number if this elements should go first, positive if it
	 *         is the other or 0 if both elements are equals
	 */
	@Override
	public int compareTo(SortedElement other);

	/**
	 * Returns an int that represents the sorting order of the element. Elements
	 * with lower values are should be sorted first.
	 * 
	 * @return The int sorting value.
	 */
	public int getSortingValue();

}