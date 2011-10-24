package test.opencranium.command;

import junit.framework.TestCase;
import opencranium.command.Action.Priority;
import opencranium.command.Action.Type;
import opencranium.cranium.Activation;

import org.junit.Test;

/**
 * @author Jorge Muñoz
 */
public class AbstractActionTest extends TestCase {

	@Test
	public void testPriorities() {
		EmptyAction a1 = new EmptyAction(null);

		assertNotNull(a1.getPriority());
		assertEquals(a1.getPriority(), Priority.MED);

		EmptyAction a21 = new EmptyAction(Priority.HIGHEST);
		EmptyAction a22 = new EmptyAction(Priority.HIGH);
		EmptyAction a23 = new EmptyAction(Priority.MED);
		EmptyAction a24 = new EmptyAction(Priority.LOW);
		EmptyAction a25 = new EmptyAction(Priority.LOWEST);

		EmptyAction[] priorities = { a21, a22, a23, a24, a25 };

		for (int i = 0; i < priorities.length; i++) {
			for (int j = 0; j < priorities.length; j++) {
				if (i < j) {
					assertTrue(priorities[i].getPriority().isMoreUrgentThan(priorities[j].getPriority()));
					assertFalse(priorities[i].getPriority().isLessUrgentThan(priorities[j].getPriority()));
					assertTrue(priorities[i].getPriority().compareTo(priorities[j].getPriority()) < 0);
				} else if (i == j) {
					assertFalse(priorities[i].getPriority().isMoreUrgentThan(priorities[j].getPriority()));
					assertFalse(priorities[i].getPriority().isLessUrgentThan(priorities[j].getPriority()));
					assertTrue(priorities[i].getPriority().compareTo(priorities[j].getPriority()) == 0);
				} else if (i > j) {
					assertFalse(priorities[i].getPriority().isMoreUrgentThan(priorities[j].getPriority()));
					assertTrue(priorities[i].getPriority().isLessUrgentThan(priorities[j].getPriority()));
					assertTrue(priorities[i].getPriority().compareTo(priorities[j].getPriority()) > 0);
				}
			}
		}
	}

	@Test
	public void testTypes() {
		EmptyAction.TYPE = Type.SIMPLE;
		EmptyAction a1 = new EmptyAction(null);

		assertTrue(a1.isAction());
		assertTrue(a1.isSimpleAction());
		assertFalse(a1.isComplexAction());
		assertFalse(a1.isPercept());
		assertFalse(a1.isSinglePercept());
		assertFalse(a1.isComplexPercept());
		assertFalse(a1.isMissionPercept());

		EmptyAction.TYPE = Type.COMPLEX;
		a1 = new EmptyAction(null);

		assertTrue(a1.isAction());
		assertFalse(a1.isSimpleAction());
		assertTrue(a1.isComplexAction());
		assertFalse(a1.isPercept());
		assertFalse(a1.isSinglePercept());
		assertFalse(a1.isComplexPercept());
		assertFalse(a1.isMissionPercept());
	}

	@Test
	public void testActivations() {
		EmptyAction a1 = new EmptyAction(null);

		assertEquals(a1.getActivation().getValue(), Activation.MIN);

		EmptyAction a2 = new EmptyAction(null);
		a2.setActivation(new Activation(Activation.MIN + 10));

		assertTrue(a1.compareTo(a2) < 0);

		EmptyAction a3 = new EmptyAction(Priority.HIGHEST);
		EmptyAction a4 = new EmptyAction(Priority.HIGH);
		EmptyAction a5 = new EmptyAction(Priority.MED);
		EmptyAction a6 = new EmptyAction(Priority.LOW);
		EmptyAction a7 = new EmptyAction(Priority.LOWEST);

		EmptyAction[] activations = { a3, a4, a5, a6, a7 };

		for (EmptyAction activation : activations) {
			activation.setActivation(new Activation((Activation.MAX + Activation.MIN) / 2));
		}

		for (int i = 0; i < activations.length; i++) {
			for (int j = 0; j < activations.length; j++) {
				if (i < j) {
					assertTrue(activations[i].compareTo(activations[j]) > 0);
				} else if (i == j) {
					assertTrue(activations[i].compareTo(activations[j]) == 0);
				} else if (i > j) {
					assertTrue(activations[i].compareTo(activations[j]) < 0);
				}
			}
		}
	}

	@Test
	public void testConstructor() {
		EmptyAction a1 = new EmptyAction(null);
		assertNotNull(a1.getGeneratedBy());
		assertTrue(a1.getGeneratedBy().isEmpty());
	}

}