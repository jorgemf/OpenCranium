package opencranium.util.collection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A Sorted list of sorted elements. It can contain a limited number of elements
 * or be unlimited. If a maximum size is set when an element with more priority
 * than others in the list is added the element in the last position of the list
 * is removed. The list is not thread safe. Sorted elements with more value are
 * ordered first.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 * 
 * @param <E>
 *            Type of the elements in the list.
 */
public class SortedList<E extends SortedElement> {

	/**
	 * Maximum size of the list.
	 */
	private int maximumSize;

	/**
	 * Vector with the ordered elements of the list.
	 */
	private LinkedList<E> elements;

	/**
	 * Default constructor with unlimited size.
	 */
	public SortedList() {
		this(0);
	}

	/**
	 * Constructor with a fixed size. If the size is 0 the maximum size of the
	 * list is unlimited, if it is negative the constructor throws an
	 * IllegalArgumentException.
	 * 
	 * @param maximumSize
	 *            The maximum size of the ordered list.
	 * @throws IllegalArgumentException
	 *             If the maximum size is negative.
	 */
	public SortedList(int maximumSize) {
		if (maximumSize < 0) {
			throw new IllegalArgumentException("Invalid maximum size for the SortedList");
		} else {
			this.maximumSize = maximumSize;
			this.elements = new LinkedList<E>();
		}
	}

	/**
	 * Adds an element to the list. If the list is full and this element is
	 * ordered first than other elements the last element is removed and this
	 * one inserted. If there are more than one element with the same priority
	 * the last element inserted is in the last position.
	 * 
	 * @param element
	 *            The element to add.
	 * @return True if the element was added, false otherwise.
	 */
	public boolean addElement(E element) {
		boolean added = false;
		if (element != null) {
			if (this.maximumSize > 0 && this.elements.size() == this.maximumSize
					&& this.elements.peekLast().compareTo(element) < 0) {
				this.elements.removeLast();
			}
			if (this.elements.size() < this.maximumSize || this.maximumSize == 0) {
				ListIterator<E> iterator = this.elements.listIterator();
				while (iterator.hasNext() && !added) {
					SortedElement current = iterator.next();
					if (element.compareTo(current) > 0) {
						// > elements with more value are ordered first,
						// and older elements latter
						iterator.previous();
						iterator.add(element);
						added = true;
					}
				}
				if (!iterator.hasNext() && !added) {
					iterator.add(element);
					added = true;
				}
			}
		}
		return added;
	}

	/**
	 * Removes a concrete element from the list if it exists.
	 * 
	 * @param element
	 *            The element to remove.
	 * @return True if the element was removed, false otherwise.
	 */
	public boolean removeElement(E element) {
		return this.elements.remove(element);
	}

	/**
	 * Returns the element of the position, an exception is thrown the position
	 * is out of the list.
	 * 
	 * @param position
	 * @return The element
	 */
	public E getElement(int position) {
		return this.elements.get(position);
	}

	/**
	 * Removes the last element of the list, an exception is thrown if it is
	 * empty.
	 */
	public void removeLastElement() {
		this.elements.removeLast();
	}

	/**
	 * Retrieves and removes the first element in the sorted list.
	 * 
	 * @return The first element of the list, an exception is thrown if it is
	 *         empty.
	 */
	public E getFirstElement() {
		return this.elements.removeFirst();
	}

	/**
	 * Returns the size of the list.
	 * 
	 * @return The size of the list.
	 */
	public int getSize() {
		return this.elements.size();
	}

	/**
	 * Returns the maximum size of the list, 0 if it is unlimited.
	 * 
	 * @return The maximum size of the list, 0 if it is unlimited.
	 */
	public int getMaximumSize() {
		return this.maximumSize;
	}

	/**
	 * Returns true if the list is empty.
	 * 
	 * @return true if the list is empty.
	 */
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	/**
	 * Returns the iterator for this list.
	 * 
	 * @return the iterator for this list.
	 */
	public Iterator<E> iterator() {
		return this.elements.iterator();
	}

	/**
	 * Removes all of the elements from this list.
	 */
	public void clear() {
		this.elements.clear();
	}

}