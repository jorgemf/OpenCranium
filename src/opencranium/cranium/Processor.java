package opencranium.cranium;

import java.util.Iterator;

import opencranium.util.ElementProcessingTime;
import opencranium.util.Id;
import opencranium.util.Statistics;
import opencranium.util.StatisticsManager;
import opencranium.util.Time;
import opencranium.util.collection.LockedSortedList;

/**
 * An abstract class to create the specialized processors.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class Processor implements Statistics, Comparable<Processor> {

	/**
	 * Current thread that is executing this processor.
	 */
	private ProcessorThread currentThread;

	/**
	 * Priority of the procesor. By default is normal.
	 */
	private Priority priority;

	/**
	 * Id of the processor.
	 */
	private Id id;

	/**
	 * Current time.
	 */
	private Time currentTick;

	/**
	 * Sorted list of processable elements to process.
	 */
	protected LockedSortedList<Processable> processableSortedList;

	/**
	 * An element to control the execution statistics of this processor
	 */
	private ElementProcessingTime statitstics;

	/**
	 * Default constructor. Sets to normal the priority.
	 * 
	 * @param id
	 *            Identifier of the processor. Cannnot be null.
	 */
	public Processor(Id id) {
		this.id = id;
		if (this.id == null) {
			throw new IllegalArgumentException("The id can not be null.");
		}
		this.statitstics = new ElementProcessingTime(id);
		this.processableSortedList = new LockedSortedList<Processable>();
		this.priority = Priority.NORMAL;
	}

	/**
	 * Processes the next element in the queue of this Processor. The
	 * milliseconds parameter is the time when the processor should finished,
	 * used for real time applications. The method pause can be called if the
	 * processor spends more time and the execution is paused until the next
	 * cycle in the real time application.
	 * 
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @see Processor#processorPause()
	 * @see WorkspaceProcessor#processNextElement(Processable, long)
	 * @see WorkspaceProcessor#processNoElement(long)
	 */
	public final void process(long milliseconds) {
		long t1 = System.nanoTime();
		Processable processable = null;
		if (this.processableSortedList.isEmpty()) {
			this.processNoElement(milliseconds);
		} else {
			processable = this.processableSortedList.getFirstElement();
			this.processNextElement(processable, milliseconds);
		}
		long t2 = System.nanoTime();
		long nanoTime = t2 - t1;
		// Statistics
		if (StatisticsManager.isRecording()) {
			this.getElementProcessingTime().addProcessingTime(nanoTime, this.currentTick);
			if (processable != null) {
				processable.getId().addProcessingTime(this.id, nanoTime);
				this.id.addProcessingTime(processable.getId(), nanoTime);
			}
		}
	}

	/**
	 * Processes the next element in the queue of this Processor. The
	 * milliseconds parameter is the time when the processor should finished,
	 * used for real time applications. The method pause can be called if the
	 * processor spends more time and the execution is paused until the next
	 * cycle in the real time application.
	 * 
	 * @param element
	 *            Element to process. Retrieved from the processable list.
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @see Processor#process(long)
	 */
	protected abstract void processNextElement(Processable element, long milliseconds);

	/**
	 * Called when there is something to process but nothing in the queue of
	 * elements to process.
	 * 
	 * @param milliseconds
	 *            The time when the current cycle ends, when the processor
	 *            should has finished.
	 * @see Processor#process(long)
	 * @see WorkspaceProcessor#processNextElement(Processable, long)
	 */
	protected abstract void processNoElement(long milliseconds);

	/**
	 * Adds a processable element to the ordered queue of elements to process.
	 * 
	 * @param data
	 *            The data to process.
	 * @return True if the data is inserted in the queue of element to process
	 *         correctly.
	 */
	public boolean addProcessable(Processable data) {
		Iterator<Processable> iterator = this.processableSortedList.iterator(this);
		while (iterator.hasNext()) {
			if (data.isAnUpdatedProcessable(iterator.next())) {
				iterator.remove();
				/*
				 * Optimization. We supppose can not be more than one
				 * processable that is an updated version of the new processable
				 */
				break;
			}
		}
		this.processableSortedList.unlock(this);
		return this.processableSortedList.addElement(data);
	}

	/**
	 * Checks if the processor should be pause and pauses itself when needed.
	 * Returns the time the processor should check for the next pause.
	 * 
	 * @return Time of execution until next pause.
	 */
	public final long processorPause() {
		return this.currentThread.processorPause();
	}

	/**
	 * Checks if it something to process in the list of processable elements.
	 * 
	 * @return true if it is something to process.
	 */
	public boolean isSomethingToProcess() {
		return !this.processableSortedList.isEmpty();
	}

	/**
	 * @return the currentThread
	 */
	public ProcessorThread getCurrentThread() {
		return this.currentThread;
	}

	/**
	 * @param currentThread
	 *            the currentThread to set
	 */
	public void setCurrentThread(ProcessorThread currentThread) {
		this.currentThread = currentThread;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority() {
		return this.priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * @return the currentTick
	 */
	public Time getCurrentTick() {
		return this.currentTick;
	}

	/**
	 * @param currentTick
	 *            the currentTick to set
	 */
	public void setCurrentTick(Time currentTick) {
		this.currentTick = currentTick;
	}

	/**
	 * @return the id
	 */
	public final Id getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.Statistics#getElementProcessingTime()
	 */
	@Override
	public ElementProcessingTime getElementProcessingTime() {
		return this.statitstics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Processor o) {
		return this.id.compareTo(o.id);
	}
}