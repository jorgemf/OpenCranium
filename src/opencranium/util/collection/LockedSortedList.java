package opencranium.util.collection;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A sorted list with a lock to be thread safe.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 * 
 * @param <E>
 *            Type of the elements in the list.
 */
public class LockedSortedList<E extends SortedElement> extends SortedList<E> {

	/**
	 * Lock of the list.
	 */
	private Lock lock;

	/**
	 * An object used to lock this list when the iterator is retreived.
	 */
	private Object objectLock;

	/**
	 * Default constructor with unlimited size.
	 */
	public LockedSortedList() {
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
	public LockedSortedList(int maximumSize) {
		super(maximumSize);
		this.lock = new ReentrantLock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.util.collection.SortedList#addElement(opencranium.util.collection
	 * .SortedElement)
	 */
	@Override
	public boolean addElement(E element) {
		boolean added = false;
		try {
			this.lock.lock();
			added = super.addElement(element);
		} finally {
			this.lock.unlock();
		}
		return added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.util.collection.SortedList#removeElement(opencranium.util
	 * .collection.SortedElement)
	 */
	@Override
	public boolean removeElement(E element) {
		boolean removed = false;
		try {
			this.lock.lock();
			removed = super.removeElement(element);
		} finally {
			this.lock.unlock();
		}
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedList#getElement(int)
	 */
	@Override
	public E getElement(int position) {
		E element = null;
		try {
			this.lock.lock();
			element = super.getElement(position);
		} finally {
			this.lock.unlock();
		}
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedList#removeLastElement()
	 */
	@Override
	public void removeLastElement() {
		try {
			this.lock.lock();
			super.removeLastElement();
		} finally {
			this.lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedList#getFirstElement()
	 */
	@Override
	public E getFirstElement() {
		E element = null;
		try {
			this.lock.lock();
			element = super.getFirstElement();
		} finally {
			this.lock.unlock();
		}
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedList#getIterator()
	 */
	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("Iterator not allowed without blocking the list.");
	}

	/**
	 * Retrieves the iterator of this list and locks the list, to unlock it the
	 * program must call this.unlock(Object) with the same object the list was
	 * locked.
	 * 
	 * @param object
	 *            Object used to lock the list. It is not allowed to be null.
	 * @return Iterator of the list, null if the object is null.
	 * @see LockedSortedList#unlock(Object)
	 */
	public Iterator<E> iterator(Object object) {
		Iterator<E> iterator = null;
		if (object != null) {
			this.lock.lock();
			this.objectLock = object;
			iterator = super.iterator();
		}
		return iterator;
	}

	/**
	 * Unlock the list when it was previously locked by the method
	 * iterator(Object).
	 * 
	 * @param object
	 *            Object to unlock the list, must be the same used to lock it.
	 * @return True if the list was unlocked, false otherwise.
	 * @see LockedSortedList#iterator(Object)
	 */
	public boolean unlock(Object object) {
		boolean unlocked = false;
		if (object != null && this.objectLock == object) {
			this.objectLock = null;
			this.lock.unlock();
			unlocked = true;
		}
		return unlocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedList#clear()
	 */
	@Override
	public void clear() {
		try {
			this.lock.lock();
			super.clear();
		} finally {
			this.lock.unlock();
		}
	}

}