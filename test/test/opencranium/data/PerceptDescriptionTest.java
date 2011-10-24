package test.opencranium.data;

import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;
import opencranium.data.PerceptDescription;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class PerceptDescriptionTest extends TestCase {

	@Test
	public void testDescriptions() {
		TreeSet<String> symbolicSet_1 = new TreeSet<String>();
		symbolicSet_1.add("one");
		symbolicSet_1.add("two");
		symbolicSet_1.add("three");
		TreeSet<String> symbolicSet_2 = new TreeSet<String>();
		symbolicSet_2.add("one");
		symbolicSet_2.add("two");
		symbolicSet_2.add("four");
		TreeSet<String> symbolicSet_3 = new TreeSet<String>();
		symbolicSet_3.add("five");

		PerceptDescription pd1 = new PerceptDescription("pd1", symbolicSet_1, null);
		PerceptDescription pd2 = new PerceptDescription("pd2", symbolicSet_2, null);
		PerceptDescription pd3 = new PerceptDescription("pd3", symbolicSet_3, null);

		double similarity = pd1.similarity(pd2);
		assertEquals(similarity, 2.0 / (Math.sqrt(3) * Math.sqrt(3)), 0.0000001);
		assertEquals(pd1.similarity(pd2), pd2.similarity(pd1));
		similarity = pd2.similarity(pd3);
		assertTrue(Math.abs(similarity) < 0.0000001);
		assertEquals(pd2.similarity(pd3), pd3.similarity(pd2));
		assertEquals(pd1.similarity(pd1), 1.0, 0);

		TreeMap<String, Float> subsymbolicSet_1 = new TreeMap<String, Float>();
		subsymbolicSet_1.put("one", 0.5f);
		subsymbolicSet_1.put("two", 0.5f);
		subsymbolicSet_1.put("four", 0.5f);

		TreeMap<String, Float> subsymbolicSet_2 = new TreeMap<String, Float>();
		subsymbolicSet_2.put("one", 0.5f);
		subsymbolicSet_2.put("two", 1f);
		subsymbolicSet_2.put("three", 0.5f);

		TreeMap<String, Float> subsymbolicSet_3 = new TreeMap<String, Float>();
		subsymbolicSet_3.put("five", 0.5f);

		TreeMap<String, Float> subsymbolicSet_4 = new TreeMap<String, Float>();
		subsymbolicSet_4.put("one", -0.5f);
		subsymbolicSet_4.put("two", -1f);
		subsymbolicSet_4.put("three", -0.5f);

		PerceptDescription pd4 = new PerceptDescription("pd4", null, subsymbolicSet_1);
		PerceptDescription pd5 = new PerceptDescription("pd5", null, subsymbolicSet_2);
		PerceptDescription pd6 = new PerceptDescription("pd6", null, subsymbolicSet_3);
		PerceptDescription pd9 = new PerceptDescription("pd9", null, subsymbolicSet_4);

		similarity = pd4.similarity(pd5);
		double expectedSim = (0.5 * 0.5 + 0.5 * 1) / (Math.sqrt(0.5 * 0.5 * 2 + 1) * Math.sqrt(0.5 * 0.5 * 3));
		assertEquals(similarity, expectedSim, 0.0000001);
		assertEquals(pd4.similarity(pd5), pd5.similarity(pd4));
		similarity = pd5.similarity(pd6);
		assertTrue(Math.abs(similarity) < 0.0000001);
		assertEquals(pd5.similarity(pd6), pd3.similarity(pd5));
		assertEquals(pd4.similarity(pd4), 1, 0.0000001);
		assertEquals(pd5.similarity(pd9), -1, 0.0000001);

		PerceptDescription pd7 = new PerceptDescription("pd7", symbolicSet_2, subsymbolicSet_2);
		PerceptDescription pd8 = new PerceptDescription("pd8", symbolicSet_3, subsymbolicSet_3);
		similarity = pd7.similarity(pd8);
		assertTrue(Math.abs(similarity) < 0.0000001);
		similarity = pd8.similarity(pd7);
		assertTrue(Math.abs(similarity) < 0.0000001);

	}

}