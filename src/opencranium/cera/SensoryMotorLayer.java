package opencranium.cera;

import java.util.ArrayList;
import java.util.List;

import opencranium.Core;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * Layer with the processors in charge of sense the world (generate the most
 * basic single percepts) and actuate over it (execute the simple actions).
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class SensoryMotorLayer extends WorkspaceLayer {

	/**
	 * ID of the layer. Static constant.
	 */
	public static final Id ID = IdManager.instance().getId("SensoryMotorLayer", SensoryMotorLayer.class);

	/**
	 * List of skills of the agent, both sensor and motor skills.
	 */
	private List<AgentSkill> agentSkills;

	/**
	 * Default constructor.
	 * 
	 * @param core
	 *            Core of the CERA architecture.
	 * @param processorThreadPool
	 *            Pool of threads which execute the skills.
	 */
	public SensoryMotorLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(ID, Type.SENSORY_MOTOR_LAYER, processorThreadPool, core);
		this.agentSkills = new ArrayList<AgentSkill>();
	}

	/**
	 * Register an agent skill.
	 * 
	 * @param skill
	 *            Skill of the agent
	 * @return True if the skill was register, false otherwise.
	 */
	public boolean registerAgentSkill(AgentSkill skill) {
		boolean added = false;
		if (!this.agentSkills.contains(skill) && this.agentSkills.add(skill)) {
			added = super.registerProcessor((CeraWorkspaceProcessor) skill);
		}
		return added;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeopencranium.cera.WorkspaceLayer#registerProcessor(opencranium.cera.
	 * CeraWorkspaceProcessor)
	 */
	@Override
	public boolean registerProcessor(CeraWorkspaceProcessor processor) {
		throw new CeraException(
				"In sensory-motor layer the only processors must be agent skills, use registerAgentSkill method instead.",
				this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#manageResult(opencranium.cranium.Processable)
	 */
	@Override
	protected void layerManageResult(Processable processable) {
		if (processable.isAction()) {
			throw new CeraException("Processable should not be an action.", this);
		} else if (processable.isPercept()) {
			if (processable.isSinglePercept()) {
				this.core.getPhysicalLayer().submitProcessable(processable);
			} else if (processable.isComplexPercept()) {
				this.core.getPhysicalLayer().submitProcessable(processable);
			} else if (processable.isMissionPercept()) {
				this.core.getPhysicalLayer().submitProcessable(processable);
				this.core.getMissionLayer().submitProcessable(processable);
			} else {
				throw new CeraException("Processable is a Percept but the type is unknown.", this);
			}
		} else {
			throw new CeraException("Type of the result not exptected: " + processable.getClass().getName(), this);
		}
	}

}