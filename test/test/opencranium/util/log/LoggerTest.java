package test.opencranium.util.log;

import java.io.OutputStream;

import junit.framework.TestCase;
import opencranium.util.log.Logger;
import opencranium.util.log.Logger.Level;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class LoggerTest extends TestCase {

	protected void setUp() {
		Logger.instance().setOnlyDefaultOutput();
	}

	@Test
	public void testGetMainLogger() {
		assertNotNull(Logger.instance());
	}

	@Test
	public void testSetMainLogger() {
		Logger log = new Logger();
		assertNotSame(Logger.instance(), log);
		Logger.setMainLogger(log);
		assertSame(Logger.instance(), log);
		Logger.setMainLogger(null);
		assertNotNull(Logger.instance());
	}

	@Test
	public void testCheckOutputs() {
		Logger log = Logger.instance();

		OutputStream[] outputs = log.getOutputs();
		assertEquals(outputs.length, 1);
		assertSame(outputs[0], System.out);

		log.addOutput(System.err);
		outputs = log.getOutputs();
		assertEquals(outputs.length, 2);

		log.deleteOutput(System.err);
		outputs = log.getOutputs();
		assertEquals(outputs.length, 1);
		assertSame(outputs[0], System.out);

		log.addOutput(System.err);
		log.setOnlyDefaultOutput();
		outputs = log.getOutputs();
		assertEquals(outputs.length, 1);
		assertSame(outputs[0], System.out);
	}

	@Test
	public void testCheckLevel() {
		Logger log = Logger.instance();

		// default level
		assertEquals(log.getLevel(), Level.INFORMATION);

		log.setLevel(Level.ERROR);
		assertEquals(log.getLevel(), Level.ERROR);

		log.setLevel(Level.DEBUG);
		assertEquals(log.getLevel(), Level.DEBUG);

	}

	@Test
	public void testMessage() {
		Logger log = Logger.instance();
		log.log(Level.INFORMATION, "test case", "test message");
	}

}
