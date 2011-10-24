package test.opencranium.util.collection;

import java.util.NoSuchElementException;

import junit.framework.TestCase;
import opencranium.util.collection.CircularList;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class CircularListTest extends TestCase {

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateListException() {
		try {
			new CircularList<Integer>(-1);
			fail();
		} catch (IllegalArgumentException iae) {
		}
		try {
			new CircularList<Integer>(0);
			fail();
		} catch (IllegalArgumentException iae) {
		}

	}

	@Test
	public void testCreateList() {
		CircularList<Integer> cl = new CircularList<Integer>(3);
		assertEquals(cl.getMaximumsize(), 3);
	}

	@Test
	public void testAddElement() {
		CircularList<Integer> cl = new CircularList<Integer>(4);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);
		Integer i6 = new Integer(6);
		Integer i7 = new Integer(7);
		Integer i8 = new Integer(8);
		Integer i9 = new Integer(9);

		assertTrue(cl.isEmpty());

		assertTrue(cl.add(i1));
		assertFalse(cl.isEmpty());
		assertTrue(cl.add(i2));
		assertTrue(cl.add(i3));

		assertEquals(cl.getSize(), 3);
		assertFalse(cl.isEmpty());

		assertTrue(cl.add(i4));

		assertFalse(cl.add(i2));

		assertEquals(cl.getFirst(), i1);

		assertTrue(cl.add(i5));

		assertEquals(cl.getFirst(), i2);
		assertEquals(cl.getFirst(), i3);
		assertEquals(cl.getFirst(), i4);

		assertTrue(cl.add(i6));

		assertEquals(cl.getFirst(), i5);
		assertEquals(cl.getFirst(), i6);

		assertEquals(cl.getSize(), 0);
		assertTrue(cl.isEmpty());

		assertTrue(cl.add(i7));
		assertTrue(cl.add(i8));
		assertTrue(cl.add(i9));

		assertEquals(cl.getSize(), 3);

	}

	@Test(expected = java.util.NoSuchElementException.class)
	public void testGetFirstElement() {
		CircularList<Integer> cl = new CircularList<Integer>(5);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);

		assertTrue(cl.isEmpty());

		assertTrue(cl.add(i1));
		assertFalse(cl.isEmpty());
		assertTrue(cl.add(i2));
		assertTrue(cl.add(i3));
		assertTrue(cl.add(i4));
		assertTrue(cl.add(i5));

		assertEquals(cl.getSize(), 5);

		assertEquals(cl.getFirst(), i1);
		assertEquals(cl.getFirst(), i2);
		assertEquals(cl.getFirst(), i3);
		assertEquals(cl.getFirst(), i4);
		assertFalse(cl.isEmpty());
		assertEquals(cl.getFirst(), i5);

		assertTrue(cl.isEmpty());

		assertTrue(cl.add(i1));
		assertFalse(cl.isEmpty());
		assertTrue(cl.add(i2));
		assertTrue(cl.add(i3));
		assertTrue(cl.add(i4));

		assertEquals(cl.getSize(), 4);

		assertEquals(cl.getFirst(), i1);
		assertEquals(cl.getFirst(), i2);
		assertEquals(cl.getFirst(), i3);
		assertFalse(cl.isEmpty());
		assertEquals(cl.getFirst(), i4);

		assertTrue(cl.isEmpty());

		assertTrue(cl.add(i1));
		assertFalse(cl.isEmpty());
		assertTrue(cl.add(i2));
		assertTrue(cl.add(i3));

		assertEquals(cl.getSize(), 3);

		assertEquals(cl.getFirst(), i1);
		assertEquals(cl.getFirst(), i2);
		assertFalse(cl.isEmpty());
		assertEquals(cl.getFirst(), i3);

		assertTrue(cl.isEmpty());

		try {
			cl.getFirst();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testAddList() {
		CircularList<Integer> cl1 = new CircularList<Integer>(5);
		CircularList<Integer> cl2 = new CircularList<Integer>(3);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);

		assertTrue(cl1.add(i1));
		assertTrue(cl1.add(i2));
		assertTrue(cl1.add(i3));
		assertTrue(cl1.add(i4));
		assertTrue(cl1.add(i5));

		assertTrue(cl2.add(i1));
		assertTrue(cl2.add(i2));

		assertFalse(cl1.addList(cl2));

		assertEquals(cl1.getFirst(), i1);

		assertFalse(cl1.addList(cl2));

		assertEquals(cl1.getFirst(), i2);

		assertTrue(cl1.addList(cl2));

		assertEquals(cl1.getFirst(), i3);
		assertEquals(cl1.getFirst(), i4);
		assertEquals(cl1.getFirst(), i5);
		assertEquals(cl1.getFirst(), i1);
		assertEquals(cl1.getFirst(), i2);

		assertTrue(cl1.isEmpty());
	}

	@Test
	public void testListGrowing() {
		CircularList<Integer> cl1 = new CircularList<Integer>(3, 2);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);
		Integer i6 = new Integer(6);
		Integer i7 = new Integer(7);

		assertTrue(cl1.add(i1));
		assertTrue(cl1.add(i2));
		assertEquals(cl1.getFirst(), i1);
		assertTrue(cl1.add(i3));
		assertTrue(cl1.add(i4));
		assertTrue(cl1.add(i5));
		assertTrue(cl1.add(i6));
		assertTrue(cl1.add(i7));
		assertEquals(cl1.getFirst(), i2);
		assertEquals(cl1.getFirst(), i3);
		assertEquals(cl1.getFirst(), i4);
		assertEquals(cl1.getFirst(), i5);
		assertEquals(cl1.getFirst(), i6);
		assertEquals(cl1.getFirst(), i7);

		assertTrue(cl1.isEmpty());
	}

	@Test
	public void testRemoveElements() {
		CircularList<Integer> cl1 = new CircularList<Integer>(5);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);
		Integer i6 = new Integer(6);
		Integer i7 = new Integer(7);

		assertFalse(cl1.remove(i6));

		assertTrue(cl1.add(i1));
		assertTrue(cl1.add(i2));
		assertTrue(cl1.add(i3));
		assertTrue(cl1.add(i4));
		assertTrue(cl1.add(i5));
		assertEquals(cl1.getFirst(), i1);
		assertEquals(cl1.getFirst(), i2);
		assertFalse(cl1.remove(i6));
		assertTrue(cl1.add(i6));
		assertTrue(cl1.add(i7));

		assertTrue(cl1.remove(i3));
		assertTrue(cl1.remove(i4));
		assertTrue(cl1.remove(i5));
		assertFalse(cl1.remove(i5));
		assertFalse(cl1.remove(i5));
		assertTrue(cl1.remove(i6));
		assertFalse(cl1.remove(i6));
		assertFalse(cl1.remove(i6));
		assertFalse(cl1.remove(i6));
		assertTrue(cl1.remove(i7));

		assertTrue(cl1.isEmpty());
	}

	@Test
	public void testClear() {
		CircularList<Integer> cl1 = new CircularList<Integer>(20);
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(2);
		Integer i3 = new Integer(3);
		Integer i4 = new Integer(4);
		Integer i5 = new Integer(5);
		Integer i6 = new Integer(6);
		Integer i7 = new Integer(7);

		assertTrue(cl1.add(i1));
		assertTrue(cl1.add(i2));
		assertTrue(cl1.add(i3));
		assertTrue(cl1.add(i4));
		assertTrue(cl1.add(i5));
		assertTrue(cl1.add(i6));
		assertTrue(cl1.add(i7));

		assertFalse(cl1.isEmpty());
		cl1.clear();
		assertTrue(cl1.isEmpty());

		try {
			cl1.getFirst();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

}