package test.opencranium;

import junit.framework.TestCase;
import opencranium.Core;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class CoreTest extends TestCase {

	@Test
	public void testCoreCreation() {
		Core.instance();
	}

}
