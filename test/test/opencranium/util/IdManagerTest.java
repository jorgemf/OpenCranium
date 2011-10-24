package test.opencranium.util;

import java.io.File;

import javax.management.OperationsException;

import junit.framework.TestCase;
import opencranium.util.Id;
import opencranium.util.IdManager;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class IdManagerTest extends TestCase {

	protected void setUp() {
		try {
			IdManager.instance().clean();
		} catch (RuntimeException exception) {
		}
	}

	@Test(expected = java.lang.RuntimeException.class)
	public void testGetInstance() {
		assertNotNull(IdManager.instance());
		try {
			IdManager.instance().clean();
		} catch (RuntimeException exception) {
		}

	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testGetId() {
		IdManager idm = IdManager.instance();

		// new ID
		Id id1 = idm.getId("a", this.getClass());
		Id id2 = idm.getId("b", this.getClass());
		Id id3 = idm.getId("c", this.getClass());

		// previous ID
		Id id11 = idm.getId("a", this.getClass());
		Id id22 = idm.getId("b", this.getClass());
		Id id33 = idm.getId("c", this.getClass());

		assertNotNull(id1);
		assertNotNull(id2);
		assertNotNull(id3);
		assertNotNull(id11);
		assertNotNull(id22);
		assertNotNull(id33);

		assertSame(id1, id11);
		assertSame(id2, id22);
		assertSame(id3, id33);

		assertNotSame(id1, id22);
		assertTrue(id1 == id11);

		try {
			idm.getId(null, this.getClass());
			fail();
		} catch (IllegalArgumentException exception) {
		}

		try {
			idm.getId("something", null);
			fail();
		} catch (IllegalArgumentException exception) {
		}

		try {
			idm.getId("", null);
			fail();
		} catch (IllegalArgumentException exception) {
		}

		try {
			idm.getId("      \t  ", null);
			fail();
		} catch (IllegalArgumentException exception) {
		}
	}

	@SuppressWarnings("deprecation")
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testGetIdLong() {
		IdManager idm = IdManager.instance();

		Id id1 = idm.getId("a", this.getClass());
		Id id2 = idm.getId("b", this.getClass());
		Id id3 = idm.getId("c", this.getClass());

		assertNotNull(id1);
		assertNotNull(id2);
		assertNotNull(id3);

		assertSame(id1, idm.getId(id1.getId()));
		assertSame(id2, idm.getId(id2.getId()));
		assertSame(id3, idm.getId(id3.getId()));

		try {
			idm.getId(1000);
			fail();
		} catch (IllegalArgumentException exception) {
		}
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testSaveLoadIds() {
		File file = new File("tmp/ids.file");
		IdManager idm = IdManager.instance();

		Id id1 = idm.getId("a", this.getClass());
		Id id2 = idm.getId("b", this.getClass());
		Id id3 = idm.getId("c", this.getClass());

		assertNotNull(id1);

		idm.saveIds(file);

		try {
			idm.loadIds(file);
			fail();
		} catch (OperationsException exception) {

		}

		try {
			IdManager.instance().clean();
		} catch (RuntimeException exception) {
		}

		try {
			idm.loadIds(file);
		} catch (OperationsException exception) {
			fail();
		}

		assertSame(id1.getId(), idm.getId(id1.getId()).getId());
		assertSame(idm.getId(id2.getId()), idm.getId("b", this.getClass()));
		assertSame(id3.getId(), idm.getId(id3.getId()).getId());
	}

}