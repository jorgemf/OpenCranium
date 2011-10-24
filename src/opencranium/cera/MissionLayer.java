package opencranium.cera;

import opencranium.Core;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * Layer with the processors that generate mission percepts and complex actions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class MissionLayer extends WorkspaceLayer {

	/**
	 * ID of the layer. Static constant.
	 */
	public static final Id ID = IdManager.instance().getId("MissionLayer", MissionLayer.class);

	/**
	 * Default constructor.
	 * 
	 * @param core
	 *            Core of the CERA architecture.
	 * @param processorThreadPool
	 *            Pool of threads which execute the skills.
	 */
	public MissionLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(ID, Type.MISSION_LAYER, processorThreadPool, core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#manageResult(opencranium.cranium.Processable)
	 */
	@Override
	protected void layerManageResult(Processable processable) {
		if (processable.isAction()) {
			if (processable.isSimpleAction()) {
				throw new CeraException("Mission layer cannot generate Simple Actions.", this);
			} else if (processable.isComplexAction()) {
				this.core.getMissionLayer().submitProcessable(processable);
				this.core.getCoreLayer().submitProcessable(processable);
				this.core.getPhysicalLayer().submitProcessable(processable);
			} else {
				throw new CeraException("Processable is an Action but the type is unknown.", this);
			}
		} else if (processable.isPercept()) {
			if (processable.isSinglePercept()) {
				throw new CeraException("Mission layer cannot generate Single Percepts.", this);
			} else if (processable.isComplexPercept()) {
				throw new CeraException("Mission layer cannot generate Complex Percepts.", this);
			} else if (processable.isMissionPercept()) {
				this.core.getMissionLayer().submitProcessable(processable);
				this.core.getCoreLayer().submitProcessable(processable);
			} else {
				throw new CeraException("Processable is a Percept but the type is unknown.", this);
			}
		} else {
			throw new CeraException("Type of the result not exptected: " + processable.getClass().getName(), this);
		}

	}

}