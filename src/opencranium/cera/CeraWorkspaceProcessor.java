package opencranium.cera;

import java.util.Collection;
import java.util.Vector;

import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.cranium.WorkspaceProcessor;
import opencranium.data.Percept;
import opencranium.data.Percept.MemoryType;
import opencranium.data.Percept.Nature;
import opencranium.data.Percept.Source;
import opencranium.util.Id;

/**
 * An specific workspace processor for CERA architecture. It overrides the
 * method manageResults which decides what to do with the results of the
 * workspace processors, in this case send the processable result to the layer
 * it belongs to.
 * 
 * @see opencranium.cranium.WorkspaceProcessor#manageResult(opencranium.cranium.Processable)
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class CeraWorkspaceProcessor extends WorkspaceProcessor {

	/**
	 * The layer of this workspace.
	 */
	private Layer layer;

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            Id of the workspace.
	 */
	public CeraWorkspaceProcessor(Id id) {
		super(id);
	}

	/**
	 * Sets the layer of this workspace.
	 * 
	 * @param layer
	 *            The layer of this workspace.
	 */
	protected void setLayer(Layer layer) {
		this.layer = layer;
	}

	/**
	 * Retrieves the layer of this workspace.
	 * 
	 * @return the layer of this workspace.
	 */
	protected Layer getLayer() {
		return this.layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.WorkspaceProcessor#manageResult(opencranium.cranium
	 * .Processable)
	 */
	@Override
	protected void manageResult(Processable result) {
		if (result != null) {
			Processable[] shortTermMemory = this.getSortTermMemory();
			Collection<Processable> generatedBy = result.getGeneratedBy();
			if ((generatedBy == null || generatedBy.isEmpty()) && shortTermMemory != null) {
				if (generatedBy == null) {
					generatedBy = new Vector<Processable>(shortTermMemory.length);
				}
				for (Processable p : shortTermMemory) {
					if (p != null) {
						generatedBy.add(p);
					}
				}
				result.setGeneratedBy(generatedBy);
			}
			if (result instanceof Percept) {
				Percept p = (Percept) result;
				if (p.getMemoryType() == MemoryType.UNKNOWN) {
					p.setMemoryType(MemoryType.SHORT_TERM);
				}
				if (p.getNature() == Nature.UNKNOWN && shortTermMemory != null && shortTermMemory.length > 0) {
					float maximumValue = 0;
					int pos = 0;
					Nature[] nature = Nature.values();
					float[] values = new float[nature.length];
					int i = 0;
					for (Processable proaux : shortTermMemory) {
						if (proaux instanceof Percept) {
							Percept peraux = (Percept) proaux;
							Nature perNature = peraux.getNature();
							i = 0;
							while (i < nature.length && nature[i] != perNature) {
								i++;
							}
							values[i] += peraux.getActivation().getValue() + 1;
							if (values[i] > maximumValue) {
								maximumValue = values[i];
								pos = i;
							}
						}
					}
					/*
					 * maybe is not a good decision but we set the most
					 * frequently nature when it is unknown
					 */
					p.setNature(nature[pos]);
				}
				if (p.getSource() == Source.UNKNOWN) {
					switch (this.layer.getLayerType()) {
					case SENSORY_MOTOR_LAYER:
						p.setSource(Source.EXTEROCEPTIVE_SENSOR);
						break;
					case PHYSICAL_LAYER:
						p.setSource(Source.PHYSICAL_PROCESSOR);
						break;
					case MISSION_LAYER:
						p.setSource(Source.MISSION_PROCESSOR);
						break;
					case CORE_LAYER:
						break;
					default:
					}
				}
			}
		}
		this.layer.manageResult(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeopencranium.cranium.WorkspaceProcessor#execute(opencranium.cranium.
	 * Processable, long)
	 */
	@Override
	protected final Processable[] execute(Processable processable, long milliseconds) {
		Processable[] result = null;
		if (processable.isAction()) {
			result = this.execute((Action) processable, milliseconds);
		} else if (processable.isPercept()) {
			result = this.execute((Percept) processable, milliseconds);
		} else {
			throw new CeraException("The element to process has to be an either action or a percept, and it is a: "
					+ processable.getClass(), this);
		}
		return result;
	}

	/**
	 * Process the next percept.
	 * 
	 * @param percept
	 *            Next percept to process.
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @return An array with the processable elements generated.
	 */
	protected abstract Processable[] execute(Percept percept, long milliseconds);

	/**
	 * Process the next action.
	 * 
	 * @param action
	 *            Next action to process.
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @return An array with the processable elements generated.
	 */
	protected abstract Processable[] execute(Action action, long milliseconds);

}
