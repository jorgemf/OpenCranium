package opencranium.command;

import java.util.Collection;

import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.util.Time;

/**
 * An interface with the common methods of the actions. An action is an element
 * that should be executed or decomposed into simpler actions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public interface Action extends Processable {

	/**
	 * Type of the action, it can be simple or complex.
	 * 
	 * @author Jorge Muñoz
	 * @author Raúl Arrabales
	 */
	public enum Type {
		/**
		 * Simple actions, can not be decomposed.
		 */
		SIMPLE,

		/**
		 * Complex actions, must be decomposed into other actions.
		 */
		COMPLEX
	}

	/**
	 * The priority of the action. Actions with more priority are executed
	 * before the others and can substitute actions with less priority.
	 * 
	 * @author Jorge Muñoz
	 * @author Raúl Arrabales
	 */
	public enum Priority {
		HIGHEST(1), HIGH(2), MED(3), LOW(4), LOWEST(5);

		/**
		 * Value of the priority.
		 */
		private int value;

		/**
		 * Default constructor.
		 * 
		 * @param val
		 *            Value of the priority
		 */
		Priority(int value) {
			this.value = value;
		}

		/**
		 * Returns true if this priority is more urgent than the other.
		 * 
		 * @param p
		 *            Other priority.
		 * @return true if this priority is more urgent than the other.
		 */
		public boolean isMoreUrgentThan(Priority p) {
			return this.value < p.value;
		}

		/**
		 * Returns true if this priority is less urgent than the other.
		 * 
		 * @param p
		 *            Other priority.
		 * @return true if this priority is less urgent than the other.
		 */
		public boolean isLessUrgentThan(Priority p) {
			return this.value > p.value;
		}

		/**
		 * Compares this priority with another, returns a negative value if this
		 * priority is more urgent, a positive value if this priority is less
		 * urgent and 0 if both priorities are equals.
		 * 
		 * @param p
		 *            Other priority.
		 * @return a negative value if this priority is more urgent, a positive
		 *         value if this priority is less urgent and 0 if both
		 *         priorities are equals.
		 */
		public int priorityCompareTo(Priority p) {
			return this.value - p.value;
		}
	}

	/**
	 * Returns the action type.
	 * 
	 * @return the action type.
	 */
	public Type getActionType();

	/**
	 * Returns the priority of the action.
	 * 
	 * @return the priority of the action.
	 */
	public Priority getPriority();

	/**
	 * Compares this action with another, returns a negative value if this
	 * action has more priority, a positive value if this actions has less
	 * priority and 0 if both actions have the same priority.
	 * 
	 * @param action
	 *            Other action.
	 * @return a negative value if this action has more priority, a positive
	 *         value if this actions has less priority and 0 if both actions
	 *         have the same priority.
	 */
	public int priorityCompareTo(Action action);

	/**
	 * Returns when this action was executed. An action can be executed once
	 * only.
	 * 
	 * @return The Time when this action was executed.
	 */
	public Time getExecutionTime();

	/**
	 * Sets the execution time of this action. Throws an ActionException if this
	 * actions was already executed.
	 * 
	 * @param time
	 *            The time when the action was executed.
	 * @throws ActionException
	 *             if this actions was already executed.
	 */
	public void setExecutionTime(Time time);

	/**
	 * Returns true when this action is the same as other action. Same means
	 * that both actions have the same purpose or are incompatible.
	 * 
	 * @param action
	 *            The other action.
	 * @return true when this action is the same as other action.
	 */
	public boolean isAnUpdatedAction(Action action);

	/**
	 * Returns the expectations of this action. The percepts that should be
	 * sense in order to check the actions was executed with the expected
	 * consequences.
	 * 
	 * @return the expectations of this action.
	 */
	public Collection<Percept> getExpectations();

}