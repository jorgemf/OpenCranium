package opencranium.util.collection;

import java.util.NoSuchElementException;

/**
 * A circular list of elements. It contains a limited number of elements. The
 * list is not thread safe.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 * 
 * @param <E>
 *            Type of the elements in the list.
 */
public class CircularList<E> {

	/**
	 * The list of elements.
	 */
	private E[] elements;

	/**
	 * The number of elements in the list.
	 */
	private int size;

	/**
	 * The amount of size the list grows when the maximum size is reached.
	 */
	private int incrementalSize;

	/**
	 * The position of the first element in the list.
	 */
	private int initialPos;

	/**
	 * Default constructor. The list has a maximum size.
	 * 
	 * @param maximumSize
	 *            The maximum size of the list.
	 * @throws IllegalArgumentException
	 *             Thrown if the maximum size is 0 or negative.
	 */
	public CircularList(int maximumSize) {
		this(maximumSize, 0);
	}

	/**
	 * Default constructor. The list has a maximum size.
	 * 
	 * @param maximumSize
	 *            The maximum size of the list.
	 * @throws IllegalArgumentException
	 *             Thrown if the maximum size is 0 or negative.
	 */
	@SuppressWarnings("unchecked")
	public CircularList(int maximumSize, int incrementalSize) {
		if (maximumSize <= 0) {
			throw new IllegalArgumentException("The size of the circular list must be greater than 0.");
		}
		if (incrementalSize < 0) {
			throw new IllegalArgumentException(
					"The incremental size of the circular list must be equals or greater than 0.");
		}
		this.elements = (E[]) new Object[maximumSize];
		this.initialPos = 0;
		this.size = 0;
		this.incrementalSize = incrementalSize;
	}

	/**
	 * Adds an element to the list if the list is not full.
	 * 
	 * @param e
	 *            Element to add
	 * @return true if the element was added, false otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean add(E e) {
		boolean added = false;
		if (this.elements.length == this.size && this.incrementalSize > 0) {
			E[] elements = (E[]) new Object[this.elements.length + this.incrementalSize];
			for (int i = 0; i < this.size; i++) {
				elements[i] = this.elements[(this.initialPos + i) % this.elements.length];
			}
			this.initialPos = 0;
			this.elements = elements;
		}
		if (this.elements.length > this.size) {
			this.elements[(this.size + this.initialPos) % this.elements.length] = e;
			this.size++;
			added = true;
		}
		return added;
	}

	/**
	 * Removes an element from the list.
	 * 
	 * @param e
	 *            Element to remove
	 * @return true if the element was removed, false otherwise.
	 */
	public boolean remove(E e) {
		boolean removed = false;
		if (this.size > 0) {
			for (int i = 0; i < this.size; i++) {
				removed = removed || e == this.elements[(this.initialPos + i) % this.elements.length];
				if (removed) {
					this.elements[(this.initialPos + i) % this.elements.length] = this.elements[(this.initialPos + i + 1)
							% this.elements.length];
				}
			}
			if (removed) {
				this.size--;
			}
		}
		return removed;
	}

	/**
	 * Retrieves and removes the first element in the list.
	 * 
	 * @return The first element, null if the list is empty.
	 */
	public E getFirst() {
		if (this.size == 0) {
			throw new NoSuchElementException();
		}
		E e = this.elements[this.initialPos];
		this.size--;
		this.initialPos = (this.initialPos + 1) % this.elements.length;
		return e;
	}

	/**
	 * Adds a list of element to this list. If there is no enough room in this
	 * list none element of the new list is added and the method returns false.
	 * 
	 * @param list
	 *            The list to add to this one.
	 * @return True if all the elements of the other list were added, false
	 *         otherwise.
	 */
	public boolean addList(CircularList<E> list) {
		boolean added = false;
		if (this.elements.length - this.size >= list.size) {
			for (int i = 0; i < list.size; i++) {
				this.elements[(this.initialPos + this.size + i) % this.elements.length] = list.elements[(list.initialPos + i)
						% list.elements.length];
			}
			this.size += list.size;
			added = true;
		}
		return added;
	}

	/**
	 * Clear this list and removes all elements.
	 */
	public void clear() {
		this.initialPos = 0;
		this.size = 0;
	}

	/**
	 * Returns wheter the list is empty or not.
	 * 
	 * @return True if the list is empty.
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns the maximum size of the list.
	 * 
	 * @return The maximum size of the list.
	 */
	public int getMaximumsize() {
		return this.elements.length;
	}

	/**
	 * Returns the number of elements in the list.
	 * 
	 * @return the number of elements in the list.
	 */
	public int getSize() {
		return this.size;
	}

}