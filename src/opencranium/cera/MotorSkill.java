package opencranium.cera;

import java.util.Set;
import java.util.TreeSet;

import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.util.Id;

/**
 * An abstract class for the motor skills of the agent. This class includes some
 * restrictions to the WorkspaceProcessors. It is defined as an agent skill.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class MotorSkill extends CeraWorkspaceProcessor implements AgentSkill {

	/**
	 * The last action executed by this motor skill.
	 */
	private Action lastActionExecuted;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 *            Id of the motor skill.
	 * @param inputType
	 *            Input type allowed by the motor skill, it should be a simple
	 *            action but it is not checked until a processable with the same
	 *            id arrives.
	 */
	public MotorSkill(Id id, Id inputType) {
		super(id);
		Set<Id> inputTypes = new TreeSet<Id>();
		inputTypes.add(inputType);
		super.setInputTypes(inputTypes);
	}

	/**
	 * Executes a simple action.
	 * 
	 * @param action
	 *            The action to execute.
	 * @return The percepts generated due to execution of the action.
	 */
	public abstract Percept[] executeAction(Action action);

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.WorkspaceProcessor#setInputTypes(java.util.Set)
	 */
	@Override
	protected final void setInputTypes(Set<Id> inputTypes) {
		if (inputTypes != null && inputTypes.size() != 1) {
			throw new CeraException("Motor Skills do not support more than one input type.", this);
		} else {
			super.setInputTypes(inputTypes);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.WorkspaceProcessor#execute(opencranium.command.Action
	 * , long)
	 */
	@Override
	public final Processable[] execute(Action action, long milliseconds) {
		if (!action.isSimpleAction()) {
			throw new CeraException("Motor Skills are not able to execute other actions than simple actions.", this);
		}
		this.lastActionExecuted = action;
		return this.executeAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.WorkspaceProcessor#execute(opencranium.data.Percept,
	 * long)
	 */
	@Override
	public final Processable[] execute(Percept percept, long milliseconds) {
		throw new CeraException("Motor Skills are not able to execute percepts.", this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.WorkspaceProcessor#getSortTermMemory()
	 */
	@Override
	public final Processable[] getSortTermMemory() {
		Processable[] memory = null;
		if (this.lastActionExecuted != null) {
			memory = new Processable[1];
			memory[0] = this.lastActionExecuted;
		}
		return memory;
	}

}