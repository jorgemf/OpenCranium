package test.opencranium.util;

import junit.framework.TestCase;
import opencranium.util.Vector3;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class Vector3Test extends TestCase {

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSum() {
		Vector3 v1 = new Vector3(1f, 0f, 1f);
		Vector3 v2 = new Vector3(1f, 5f, 3f);
		Vector3 result = new Vector3(2f, 5f, 4f);
		v1.sum(v2);
		assertTrue(v1.equals(result));
		assertTrue(result.equals(v1));
		assertTrue(result.equals(result));
		assertFalse(result.equals(v2));
		assertFalse(v2.equals(result));
		try {
			v2.sum(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testSubtract() {
		Vector3 v1 = new Vector3(1f, 0f, 1f);
		Vector3 v2 = new Vector3(1f, 5f, 3f);
		Vector3 result = new Vector3(0f, -5f, -2f);
		v1.subtract(v2);
		assertTrue(v1.equals(result));
		assertTrue(result.equals(v1));
		assertTrue(result.equals(result));
		assertFalse(result.equals(v2));
		assertFalse(v2.equals(result));
		v1.subtract(v1);
		assertTrue(v1.equals(new Vector3()));
		try {
			v2.subtract(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testDotProduct() {
		Vector3 v1 = new Vector3(1f, 0f, 1f);
		Vector3 v2 = new Vector3(1f, 5f, 3f);
		float result = 4f;
		assertEquals(v1.dotProduct(v2), result);
		try {
			v2.dotProduct(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCrossProduct() {
		Vector3 v1 = new Vector3(1f, 0f, 1f);
		Vector3 v2 = new Vector3(1f, 5f, 3f);
		Vector3 result = new Vector3(-5f, -2f, 5f);
		v1.crossProduct(v2);
		assertTrue(v1.equals(result));
		assertTrue(result.equals(v1));
		assertTrue(result.equals(result));
		assertFalse(result.equals(v2));
		assertFalse(v2.equals(result));
		try {
			v2.crossProduct(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testNormalize() {
		Vector3 v1 = new Vector3(1f, 0f, 1f);
		Vector3 v2 = new Vector3(1f, 5f, 3f);
		v1.normalize();
		v2.normalize();
		assertTrue(Math.abs(v1.dotProduct(v1) - 1) < 0.0000001);
		assertTrue(Math.abs(v2.dotProduct(v2) - 1) < 0.0000001);
	}
}