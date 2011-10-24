package test.opencranium.data;

import junit.framework.TestCase;
import opencranium.cranium.Activation;
import opencranium.data.Percept.AppraisalType;
import opencranium.data.Percept.MemoryType;
import opencranium.data.Percept.Nature;
import opencranium.data.Percept.Source;
import opencranium.data.Percept.Type;

import org.junit.Test;

import test.opencranium.command.EmptyAction;

/**
 * @author Jorge Muñoz
 */
public class AbstractPerceptTest extends TestCase {

	@Test
	public void testTypes() {
		EmptyPercept.TYPE = Type.SINGLE_PERCEPT;
		EmptyPercept p1 = new EmptyPercept();

		assertFalse(p1.isAction());
		assertFalse(p1.isSimpleAction());
		assertFalse(p1.isComplexAction());
		assertTrue(p1.isPercept());
		assertTrue(p1.isSinglePercept());
		assertFalse(p1.isComplexPercept());
		assertFalse(p1.isMissionPercept());

		EmptyPercept.TYPE = Type.COMPLEX_PERCEPT;
		p1 = new EmptyPercept();

		assertFalse(p1.isAction());
		assertFalse(p1.isSimpleAction());
		assertFalse(p1.isComplexAction());
		assertTrue(p1.isPercept());
		assertFalse(p1.isSinglePercept());
		assertTrue(p1.isComplexPercept());
		assertFalse(p1.isMissionPercept());

		EmptyPercept.TYPE = Type.MISSION_PERCEPT;
		p1 = new EmptyPercept();

		assertFalse(p1.isAction());
		assertFalse(p1.isSimpleAction());
		assertFalse(p1.isComplexAction());
		assertTrue(p1.isPercept());
		assertFalse(p1.isSinglePercept());
		assertFalse(p1.isComplexPercept());
		assertTrue(p1.isMissionPercept());
	}

	@Test
	public void testActivations() {
		EmptyPercept p1 = new EmptyPercept();

		assertEquals(p1.getActivation().getValue(), Activation.MIN);

		EmptyAction a2 = new EmptyAction(null);
		a2.setActivation(new Activation(Activation.MIN + 10));

		assertTrue(p1.compareTo(a2) < 0);
	}

	@Test
	public void testParameters() {
		EmptyPercept p1 = new EmptyPercept();

		assertSame(p1.getNature(), Nature.UNKNOWN);
		try {
			p1.setNature(null);
			fail();
		} catch (IllegalAccessError e) {
		}
		p1.setNature(Nature.REAL);
		assertSame(p1.getNature(), Nature.REAL);
		try {
			p1.setNature(Nature.REAL);
			fail();
		} catch (IllegalAccessError e) {
		}

		assertSame(p1.getSource(), Source.UNKNOWN);
		try {
			p1.setSource(null);
			fail();
		} catch (IllegalAccessError e) {
		}
		p1.setSource(Source.PHYSICAL_PROCESSOR);
		assertSame(p1.getSource(), Source.PHYSICAL_PROCESSOR);
		try {
			p1.setSource(Source.PHYSICAL_PROCESSOR);
			fail();
		} catch (IllegalAccessError e) {
		}

		assertSame(p1.getMemoryType(), MemoryType.UNKNOWN);
		try {
			p1.setMemoryType(null);
			fail();
		} catch (IllegalAccessError e) {
		}
		p1.setMemoryType(MemoryType.EPISODIC);
		assertSame(p1.getMemoryType(), MemoryType.EPISODIC);
		try {
			p1.setMemoryType(MemoryType.SHORT_TERM);
			fail();
		} catch (IllegalAccessError e) {
		}

		assertSame(p1.getAppraisalType(), AppraisalType.UNKNOWN);
		try {
			p1.setAppraisalType(null);
			fail();
		} catch (IllegalAccessError e) {
		}
		p1.setAppraisalType(AppraisalType.MATCH);
		assertSame(p1.getAppraisalType(), AppraisalType.MATCH);
		try {
			p1.setAppraisalType(AppraisalType.UNKNOWN);
			fail();
		} catch (IllegalAccessError e) {
		}
	}

	@Test
	public void testConstructor() {
		EmptyPercept p1 = new EmptyPercept();
		assertNotNull(p1.getGeneratedBy());
		assertTrue(p1.getGeneratedBy().isEmpty());
		assertNotNull(p1.getContext());
		assertTrue(p1.getContext().isEmpty());
		assertNotNull(p1.getDataRepresentation());
		assertTrue(p1.getDataRepresentation().isEmpty());
	}

}