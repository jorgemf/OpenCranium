package test.opencranium.util.collection;

import java.util.NoSuchElementException;

import junit.framework.TestCase;
import opencranium.util.collection.LockedSortedList;
import opencranium.util.collection.SortedElement;
import opencranium.util.collection.SortedList;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class SortedListTest extends TestCase {

	class Element implements SortedElement {

		private int val;

		public Element(int val) {
			this.val = val;
		}

		@Override
		public int compareTo(SortedElement other) {
			return this.val - ((Element) other).val;
		}

		public String toString() {
			return Integer.toString(val);
		}

		@Override
		public int getSortingValue() {
			return this.val;
		}

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateListException() {
		try {
			new SortedList<Element>(-1);
			fail();
		} catch (IllegalArgumentException iae) {
		}
	}

	@Test
	public void testCreateList() {
		SortedList<Element> sl = new SortedList<Element>(3);
		assertEquals(sl.getMaximumSize(), 3);
		sl = new SortedList<Element>();
		assertEquals(sl.getMaximumSize(), 0);
	}

	@Test(expected = java.util.NoSuchElementException.class)
	public void testAddElement() {
		SortedList<Element> sl = new SortedList<Element>(3);
		Element e3 = new Element(1);
		Element e2 = new Element(2);
		Element e1 = new Element(3);
		Element e4 = new Element(0);
		Element e0 = new Element(4);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		assertFalse(sl.addElement(e4));

		assertTrue(sl.addElement(e0));

		Element e11 = new Element(3);
		assertTrue(sl.addElement(e11));

		assertEquals(sl.getSize(), 3);

		assertEquals(sl.getFirstElement(), e0);
		assertEquals(sl.getFirstElement(), e1);
		assertEquals(sl.getFirstElement(), e11);

		assertEquals(sl.getSize(), 0);
		try {
			sl.getFirstElement();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testGetFirstElement() {
		SortedList<Element> sl = new SortedList<Element>(3);
		Element e3 = new Element(2);
		Element e2 = new Element(3);
		Element e1 = new Element(4);
		Element e4 = new Element(1);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		assertEquals(sl.getSize(), 3);

		assertEquals(sl.getFirstElement(), e1);
		assertEquals(sl.getFirstElement(), e2);
		assertEquals(sl.getFirstElement(), e3);

		assertEquals(sl.getSize(), 0);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		assertEquals(sl.getFirstElement(), e1);

		assertTrue(sl.addElement(e4));

		assertEquals(sl.getFirstElement(), e2);
		assertEquals(sl.getFirstElement(), e3);
		assertEquals(sl.getFirstElement(), e4);

	}

	@Test
	public void testRemoveElement() {
		SortedList<Element> sl = new SortedList<Element>(3);
		Element e3 = new Element(1);
		Element e2 = new Element(2);
		Element e1 = new Element(3);
		Element e4 = new Element(0);
		Element e0 = new Element(4);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		assertTrue(sl.removeElement(e2));
		assertTrue(sl.removeElement(e1));

		assertTrue(sl.addElement(e4));
		assertTrue(sl.addElement(e0));

		assertEquals(sl.getFirstElement(), e0);
		assertEquals(sl.getFirstElement(), e3);
		assertEquals(sl.getFirstElement(), e4);
	}

	@Test
	public void testRemoveLastElement() {
		SortedList<Element> sl = new SortedList<Element>(3);
		Element e3 = new Element(2);
		Element e2 = new Element(3);
		Element e1 = new Element(4);
		Element e4 = new Element(1);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		sl.removeLastElement();

		assertTrue(sl.addElement(e4));

		assertEquals(sl.getFirstElement(), e1);
		assertEquals(sl.getFirstElement(), e2);
		assertEquals(sl.getFirstElement(), e4);
	}

	@Test
	public void testIterator() {
		SortedList<Element> sl = new SortedList<Element>(3);
		assertNotNull(sl.iterator());
		LockedSortedList<Element> lsl = new LockedSortedList<Element>();
		try {
			lsl.iterator();
			fail();
		} catch (UnsupportedOperationException e) {
		}
		assertNull(lsl.iterator(null));
		assertNotNull(lsl.iterator(this));
		assertTrue(lsl.unlock(this));
	}

	@Test
	public void testClear() {
		SortedList<Element> sl = new SortedList<Element>(3);
		Element e3 = new Element(2);
		Element e2 = new Element(3);
		Element e1 = new Element(4);

		assertTrue(sl.addElement(e3));
		assertTrue(sl.addElement(e2));
		assertTrue(sl.addElement(e1));

		sl.clear();

		assertTrue(sl.isEmpty());
		assertEquals(0, sl.getSize());
	}

}