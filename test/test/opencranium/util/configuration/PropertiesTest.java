package test.opencranium.util.configuration;

import java.io.File;
import java.util.Set;

import junit.framework.TestCase;
import opencranium.util.configuration.Properties;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class PropertiesTest extends TestCase {

	protected void setUp() {
		Properties p = Properties.instance();

		Set<String> keys = p.getPropertiesNames();
		for (String property : keys) {
			p.remove(property);
		}
	}

	@Test
	public void testGetProperties() {
		assertNotNull(Properties.instance());
	}

	@Test
	public void testSetProperty() {
		Properties p = Properties.instance();

		p.set("algo", "valor");
		p.set("double", 3.3);

		assertTrue(p.exists("double"));
		assertEquals(p.value("algo"), "valor");
		assertTrue(p.doubleValue("double") == 3.3);

		p.set("double", 4.2);
		assertTrue(p.doubleValue("double") == 4.2);

		Set<String> keys = p.getPropertiesNames();
		for (String property : keys) {
			p.remove(property);
		}

		assertFalse(p.exists("double"));

		assertFalse(p.exists("boolean"));
		p.set("boolean", true);
		assertTrue(p.booleanValue("boolean"));
		p.remove("boolean");
		assertFalse(p.exists("boolean"));
		p.set("boolean", "foo");
		assertFalse(p.booleanValue("boolean"));
		p.set("boolean", "TRUE");
		assertTrue(p.booleanValue("boolean"));
		p.remove("boolean");
		assertFalse(p.exists("boolean"));
	}

	@Test
	public void testWriteLoadProperties() {
		Properties p = Properties.instance();

		p.set("a", "a");
		p.set("c", "c");
		p.set("b", "b");

		File file = new File("tmp/test.properties");
		p.save(file);

		Set<String> keys = p.getPropertiesNames();
		for (String property : keys) {
			p.remove(property);
		}

		p.load(file);
		assertEquals(p.value("a"), "a");
		assertEquals(p.value("b"), "b");
		assertEquals(p.value("c"), "c");

	}

}
