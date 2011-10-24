package opencranium.util.collection;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A circular list of elements with a lock to be thread safe.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 * 
 * @param <E>
 *            Type of the elements in the list.
 */
public class LockedCircularList<E> extends CircularList<E> {

	/**
	 * Lock of the list.
	 */
	public Lock lock;

	/**
	 * Default constructor. The list has a maximum size.
	 * 
	 * @param maximumSize
	 *            The maximum size of the list.
	 * @throws IllegalArgumentException
	 *             Thrown if the maximum size is 0 or negative.
	 */
	public LockedCircularList(int maximumSize) {
		super(maximumSize);
		this.lock = new ReentrantLock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.util.collection.CircularList#addElement(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		boolean added = false;
		try {
			this.lock.lock();
			added = super.add(e);
		} finally {
			this.lock.unlock();
		}
		return added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.CircularList#getFirst()
	 */
	@Override
	public E getFirst() {
		E e = null;
		try {
			this.lock.lock();
			e = super.getFirst();
		} finally {
			this.lock.unlock();
		}
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.util.collection.CircularList#addList(opencranium.util.collection
	 * .CircularList)
	 */
	@Override
	public boolean addList(CircularList<E> list) {
		boolean added = false;
		try {
			this.lock.lock();
			added = super.addList(list);
		} finally {
			this.lock.unlock();
		}
		return added;
	}

}