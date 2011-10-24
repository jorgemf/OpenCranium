package opencranium.cera;

import opencranium.Core;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * Layer with the processors that generate more complex percepts based on single
 * percepts and decompose complex actions into simple ones.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class PhysicalLayer extends WorkspaceLayer {

	/**
	 * ID of the layer. Static constant.
	 */
	public static final Id ID = IdManager.instance().getId("PhysicalLayer", PhysicalLayer.class);

	/**
	 * Default constructor.
	 * 
	 * @param core
	 *            Core of the CERA architecture.
	 * @param processorThreadPool
	 *            Pool of threads which execute the skills.
	 */
	public PhysicalLayer(Core core, ProcessorThreadPool processorThreadPool) {
		super(ID, Type.PHYSICAL_LAYER, processorThreadPool, core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cera.Layer#manageResult(opencranium.cranium.Processable)
	 */
	@Override
	public void layerManageResult(Processable processable) {
		if (processable.isAction()) {
			if (processable.isSimpleAction()) {
				this.core.getPhysicalLayer().submitProcessable(processable);
				this.core.getSensoryMotorLayer().submitProcessable(processable);
			} else if (processable.isComplexAction()) {
				throw new CeraException("Physical layer cannot generate Complex Actions.", this);
			} else {
				throw new CeraException("Processable is an Action but the type is unknown.", this);
			}
		} else if (processable.isPercept()) {
			if (processable.isSinglePercept()) {
				throw new CeraException("Physical layer cannot generate Single Percepts.", this);
			} else if (processable.isComplexPercept()) {
				this.core.getPhysicalLayer().submitProcessable(processable);
				this.core.getMissionLayer().submitProcessable(processable);
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