package opencranium.cranium;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import opencranium.util.Id;
import opencranium.util.Time;
import opencranium.util.log.Logger;

/**
 * An implementation of a blackboard communication system as a workspace where
 * the processor can be registered. The workspace also contains a thread pool of
 * ProcessorThreads which are used to execute the WorkspaceProcessors.
 * 
 * The workspace is thread safe and processors can be added/removed at the same
 * time, even if a processable element is being submitted.
 * 
 * @see ProcessorThread
 * @see WorkspaceProcessor
 * @see ProcessorThreadPool
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Workspace {

	/**
	 * Pool of ProcessorThreads
	 */
	private ProcessorThreadPool processorPool;

	/**
	 * Current execution tick
	 */
	private Time currentTick;

	/**
	 * List of listener of the events submitted.
	 */
	private HashMap<Id, Collection<WorkspaceProcessor>> listeners;

	/**
	 * A lock for concurrent operations.
	 */
	private Lock lock;

	/**
	 * Default constructor.
	 * 
	 * @param processorThreadPool
	 *            Pool of threads to execute the processors. Cannot be not null.
	 * 
	 */
	public Workspace(ProcessorThreadPool processorThreadPool) {
		if (processorThreadPool == null) {
			throw new CraniumException("Pool of threads cannot be null", this);
		}
		this.processorPool = processorThreadPool;
		this.lock = new ReentrantLock();
		this.listeners = new HashMap<Id, Collection<WorkspaceProcessor>>();
	}

	/**
	 * Submits a processable element into the workspace. The processable element
	 * is notified to all the WorspaceProcessors interested.
	 * 
	 * @param processable
	 *            A processable element as a result of an operation.
	 */
	public void submitProcessable(Processable processable) {
		WorkspaceProcessor[] processors = null;
		try {
			this.lock.lock();
			Collection<WorkspaceProcessor> collection = this.listeners.get(processable.getId());
			if (collection != null && collection.size() > 0) {
				processors = new WorkspaceProcessor[collection.size()];
				processors = collection.toArray(processors);
			}
		} finally {
			this.lock.unlock();
		}
		if (processors != null) {
			for (WorkspaceProcessor processor : processors) {
				try {
					processor.addProcessable(processable);
				} catch (CraniumException exception) {
					Logger.exception(exception);
				}
			}
		}
	}

	/**
	 * Register a WorkspaceProcessor as a listener of its inputs types in this
	 * workspace.
	 * 
	 * @param processor
	 *            The processor to be registered.
	 */
	public boolean registerProcessor(WorkspaceProcessor processor) {
		boolean added = false;
		if (this.processorPool.addProcessor(processor)) {
			try {
				this.lock.lock();
				for (Id id : processor.getInputTypes()) {
					Collection<WorkspaceProcessor> collection = null;
					if (this.listeners.containsKey(id)) {
						collection = this.listeners.get(id);
					} else {
						collection = new Vector<WorkspaceProcessor>();
						this.listeners.put(id, collection);
					}
					collection.add(processor);
				}
				added = true;
			} finally {
				this.lock.unlock();
			}
		}
		return added;
	}

	/**
	 * Unregister a WorkspaceProcessor as a listener of its inputs types in this
	 * workspace.
	 * 
	 * @param processor
	 *            The processor to be unregistered.
	 */
	public boolean unregisterProcessor(WorkspaceProcessor processor) {
		boolean removed = false;
		if (this.processorPool.removeProcessor(processor)) {
			try {
				this.lock.lock();
				for (Id id : processor.getInputTypes()) {
					Collection<WorkspaceProcessor> collection = this.listeners.get(id);
					collection.remove(processor);
					if (collection.isEmpty()) {
						this.listeners.remove(id);
					}
				}
				removed = true;
			} finally {
				this.lock.unlock();
			}
		}
		return removed;
	}

	/**
	 * @return the currentTick
	 */
	public Time getCurrentTick() {
		return this.currentTick;
	}

	/**
	 * Sets the currentTick
	 * 
	 * @param currentTick
	 *            the currentTick to set
	 */
	public void setCurrentTick(Time currentTick) {
		this.currentTick = currentTick;
	}

	/**
	 * @return the processorPool
	 */
	public ProcessorThreadPool getProcessorPool() {
		return this.processorPool;
	}

}