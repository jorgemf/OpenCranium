package opencranium.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * A class to store the time processing statistics of an element with id.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class StatisticsId implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -1263375962089074516L;

	/**
	 * A data base with the elements that processed this Id.
	 */
	private HashMap<Id, ElementProcessingTime> processedBy;

	/**
	 * Default constructor.
	 */
	public StatisticsId() {
		this.processedBy = new HashMap<Id, ElementProcessingTime>();
	}

	/**
	 * @return the data base with the elements that processed this element.
	 */
	public Collection<ElementProcessingTime> getProcessedBy() {
		return this.processedBy.values();
	}

	/**
	 * Increases the processing time of the element with the given id.
	 * 
	 * @param id
	 *            The id of the element that is processing.
	 * @param nanoTime
	 *            Time that spent in processing, in nano seconds.
	 */
	public void addProcessingTime(Id id, long nanoTime) {
		getStadistics(id).addProcessingTime(nanoTime);
	}

	/**
	 * Increases one unit the times the element with the given id discarded this
	 * element.
	 * 
	 * @param id
	 *            The id of the element.
	 */
	public void addDiscarded(Id id) {
		getStadistics(id).discarted();
	}

	/**
	 * Increases one unit the times the element with the given id created this
	 * element.
	 * 
	 * @param id
	 *            The id of the element.
	 */
	public void addCreated(Id id) {
		getStadistics(id).created();
	}

	/**
	 * Increases one unit the times the element with the given id paused this
	 * element.
	 * 
	 * @param id
	 *            The id of the element.
	 */
	public void addPause(Id id) {
		getStadistics(id).paused();
	}

	/**
	 * Returns the element with the statistics information. Creates a new one if
	 * it does not exist. The method throws and IllegalArgumentException if the
	 * id is not in the data base in the IdManager.
	 * 
	 * @param id
	 *            The id of the element.
	 * @return The element with the statistics information.
	 */
	public ElementProcessingTime getStadistics(Id id) {
		ElementProcessingTime ept = null;
		if (this.processedBy.containsKey(id)) {
			ept = this.processedBy.get(id);
		} else {
			ept = new ElementProcessingTime(id);
			this.processedBy.put(id, ept);
		}
		return ept;
	}
}