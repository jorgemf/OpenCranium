package opencranium.cera;

import java.util.Set;

import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.Time;

/**
 * An abstract class for the sensor skills of the agent. This class includes
 * some restrictions to the WorkspaceProcessors. It is defined as an agent
 * skill.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class SensorSkill extends CeraWorkspaceProcessor implements AgentSkill {

	/**
	 * Last sense of this skill.
	 */
	private Percept[] lastSense;

	/**
	 * Default constructor.
	 * 
	 * @param id
	 *            Id of the motor skill.
	 */
	public SensorSkill(Id id) {
		super(id);
	}

	/**
	 * Senses the environment or the agent internal state.
	 * 
	 * @param tick
	 *            Current tick.
	 * @return An array with the sensed percepts.
	 */
	protected abstract Percept[] sense(Time tick);

	/**
	 * Checks if the perception of the skill has changed and there is new
	 * information.
	 * 
	 * @param tick
	 *            Current tick.
	 * @return True when there is new information, false otherwise.
	 */
	public abstract boolean isSenseUpdated(Time tick);

	/**
	 * Returns the last sense of this agent. Could be a null object.
	 * 
	 * @return the last sense of this agent.
	 */
	public Percept[] getLastSense() {
		return this.lastSense;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.WorkspaceProcessor#execute(long)
	 */
	@Override
	public Processable[] execute(long milliseconds) {
		Processable[] result = this.lastSense;
		if (isSenseUpdated(this.getCurrentTick())) {
			this.lastSense = sense(this.getCurrentTick());
			result = this.lastSense;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.WorkspaceProcessor#setInputTypes(java.util.Set)
	 */
	@Override
	protected final void setInputTypes(Set<Id> inputTypes) {
		if (inputTypes != null) {
			throw new CeraException("Sensor Skills do not support input types.", this);
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
		throw new CeraException("Sensor Skills do not process elements.", this);
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
		throw new CeraException("Sensor Skills do not support input types.", this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.WorkspaceProcessor#getSortTermMemory()
	 */
	@Override
	public final Processable[] getSortTermMemory() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.WorkspaceProcessor#addProcessable(opencranium.cranium
	 * .Processable)
	 */
	@Override
	public final boolean addProcessable(Processable data) {
		throw new CeraException("Sensor Skills do not support input types.", this);
	}

}