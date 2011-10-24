package opencranium.cranium;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import opencranium.util.collection.CircularList;
import opencranium.util.log.Logger;

/**
 * A pool of threads.
 * 
 * The initial processors to be executed are into the processors lists. When a
 * thread is free it get the first processor from the processorsList. Processors
 * that has nothing to process are put into the waiting list, processors that
 * are executing are put into the executing list and processor which execution
 * ends are put into the executed list. When this list is empty all the executed
 * processors are passed into this list and removed from the executed list.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class ProcessorThreadPool {

	/**
	 * Collection of threads. The threads are created when startAll is called
	 * and destroyed when stopAll is called.
	 * 
	 * @see ProcessorThreadPool#setProcessorThreads(int)
	 * @see ProcessorThreadPool#numberOfThreads
	 */
	private ProcessorThread[] processorThreads;

	/**
	 * List of all processors that are waiting to be executed.
	 */
	private CircularList<Processor> processorToExecuteList;

	/**
	 * List of all processors that the scheduler tried to execute but they have
	 * nothing to process.
	 */
	private CircularList<Processor> processorWaitingList;

	/**
	 * Processors that have been executed.
	 */
	private CircularList<Processor> processorExecutedList;

	/**
	 * Processors that are currently executing, even if they are in pause.
	 */
	private Set<Processor> processorExecutingSet;

	/**
	 * All the processors.
	 */
	private Set<Processor> processorsSet;

	/**
	 * Initial size of the lists.
	 */
	private static int INITIAL_LIST_SIZES = 10;

	/**
	 * Default growing rate of the lists when it is needed.
	 */
	private static int LIST_GROWING_INCREMENT = 10;

	/**
	 * For multiple thread access to the lists.
	 */
	private Lock lock;

	/**
	 * Flag to know if the threads have been started.
	 */
	private boolean threadsStarted;

	/**
	 * Default constructor. The number of threads by default is the number of
	 * CPU in the machine plus one. It is recommended to set this number bigger.
	 */
	public ProcessorThreadPool() {
		this(Runtime.getRuntime().availableProcessors() + 1);
		Logger.verbose("ProcessorThreadPool", "Number of processors available to the Java Virtual Machine: "
				+ Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Default constructor.
	 * 
	 * @param numberOfThreads
	 *            Number of threads available. Must be greater than 0.
	 */
	public ProcessorThreadPool(int numberOfThreads) {
		this(numberOfThreads, INITIAL_LIST_SIZES, LIST_GROWING_INCREMENT);
	}

	/**
	 * Constructor that sets all the parameters of the class.
	 * 
	 * @param numberOfThreads
	 *            Number of threads available. Must be greater than 0.
	 * @param initialListSizes
	 *            Initial size of the lists.Must be greater than 0.
	 * @param listGrowingIncrement
	 *            The amount of size the list increases when the maximum size is
	 *            reached. Must be greater than 0.
	 */
	public ProcessorThreadPool(int numberOfThreads, int initialListSizes, int listGrowingIncrement) {
		if (initialListSizes < 1) {
			throw new CraniumException("The initial size of the lists must be 1 or greater.", this);
		}
		if (listGrowingIncrement < 1) {
			throw new CraniumException("The growing rate of the lists must be 1 or greater.", this);
		}
		this.setProcessorThreads(numberOfThreads);
		this.processorToExecuteList = new CircularList<Processor>(initialListSizes, listGrowingIncrement);
		this.processorWaitingList = new CircularList<Processor>(initialListSizes, listGrowingIncrement);
		this.processorExecutedList = new CircularList<Processor>(initialListSizes, listGrowingIncrement);
		this.processorExecutingSet = new TreeSet<Processor>();
		this.processorsSet = new TreeSet<Processor>();
		this.lock = new ReentrantLock();
	}

	/**
	 * Returns the number of threads of this pool.
	 * 
	 * @return the number of threads of this pool.
	 */
	public int getNumberOfThreads() {
		return this.processorThreads.length;
	}

	/**
	 * Starts the execution of all processors' threads without time limitation.
	 * New threads are created, one per processor and the processors are set
	 * into paused mode. resumeAll() or executeDuring(long) must be called in
	 * order to start the execution.
	 * 
	 * @see ProcessorThreadPool#resumeAll()
	 * @see ProcessorThreadPool#executeDuring(long)
	 */
	public synchronized void startAll() {
		if (this.threadsStarted) {
			throw new CraniumException("Threads have already been started.", this);
		}
		for (ProcessorThread processorThread : this.processorThreads) {
			processorThread.start();
		}
		this.threadsStarted = true;
	}

	/**
	 * Stops and kill all the processors' threads.
	 */
	public synchronized void stopAll() {
		if (!this.threadsStarted) {
			throw new CraniumException("Threads have not been started.", this);
		}
		for (ProcessorThread processorThread : this.processorThreads) {
			processorThread.kill();
		}
		this.threadsStarted = false;
	}

	/**
	 * Pause all processors threads. The processor should call itself the method
	 * processorPause() in order to pause it, in other case it will continue
	 * executing.
	 * 
	 * @see ProcessorThread#processorPause()
	 */
	public synchronized void pauseAll() {
		if (!this.threadsStarted) {
			throw new CraniumException("Threads have not been started.", this);
		}
		for (ProcessorThread processorThread : this.processorThreads) {
			if (!processorThread.isPausing() && !processorThread.isPaused()) {
				processorThread.pause();
			}
		}
	}

	/**
	 * Resume all processors.
	 */
	public synchronized void resumeAll() {
		if (!this.threadsStarted) {
			throw new CraniumException("Threads have not been started.", this);
		}
		for (ProcessorThread processorThread : this.processorThreads) {
			processorThread.resume(0);
		}
	}

	/**
	 * Sets the number of threads and create them. If there is a thread
	 * executing it will keep it execution, then it dies. The new threads are
	 * created in pause mode. startall() and resumeAll() or executeDuring(long)
	 * methods must be called in order to start the execution of the new
	 * threads.
	 * 
	 * @param number
	 *            Number of threads available. Must be greater than 0 or an
	 *            exception is thrown.
	 * @see ProcessorThreadPool#startAll()
	 * @see ProcessorThreadPool#resumeAll()
	 * @see ProcessorThreadPool#executeDuring(long)
	 */
	public synchronized void setProcessorThreads(int number) {
		if (number <= 0) {
			throw new CraniumException("The number of threads must be greater than 0.", this);
		}
		if (this.threadsStarted) {
			this.stopAll();
		}
		this.processorThreads = new ProcessorThread[number];
		for (int i = 0; i < this.processorThreads.length; i++) {
			this.processorThreads[i] = new ProcessorThread(this);
		}
	}

	/**
	 * Adds a processor the task of this pool of threads.
	 * 
	 * @param processor
	 *            The processor to add
	 * @return True if the processor was added, false otherwise.
	 */
	public boolean addProcessor(Processor processor) {
		boolean added = false;
		try {
			this.lock.lock();
			if (!this.processorsSet.contains(processor)) {
				this.processorsSet.add(processor);
				this.processorToExecuteList.add(processor);
				added = true;
			}
		} finally {
			this.lock.unlock();
		}
		return added;
	}

	/**
	 * Removes a processor the task from this pool of threads.
	 * 
	 * @param processor
	 *            The processor to remove.
	 * @return True if the processor was removed, false otherwise.
	 */
	public boolean removeProcessor(Processor processor) {
		boolean removed = false;
		try {
			this.lock.lock();
			if (this.processorsSet.contains(processor)) {
				removed = this.processorsSet.remove(processor);
				this.processorToExecuteList.remove(processor);
				this.processorWaitingList.remove(processor);
				this.processorExecutedList.remove(processor);
			}
		} finally {
			this.lock.unlock();
		}
		return removed;
	}

	/**
	 * Execute the tasks in the queue during the given time in milliseconds.
	 * This is not an asynchronous method, it returns the execution after the
	 * time specified in milliseconds.
	 * 
	 * @param milliseconds
	 *            Milliseconds of execution.
	 */
	public synchronized void executeDuring(long milliseconds) {
		if (!this.threadsStarted) {
			throw new CraniumException("Threads have not been started.", this);
		}
		if (milliseconds < 0) {
			throw new CraniumException("Time cannot be negative.", this);
		}
		long finishTime = System.currentTimeMillis() + milliseconds;
		for (ProcessorThread processorThread : this.processorThreads) {
			processorThread.resume(finishTime);
		}
		long time = 0;
		do {
			time = System.currentTimeMillis() + 1;
			if (finishTime - time > 0) {
				try {
					Thread.sleep(finishTime - time);
				} catch (InterruptedException e) {
					Logger.warning("ProcessorThreadPool.executeDuring(long milliseconds)", e.getMessage());
				}
			}

		} while (time < finishTime);
	}

	/**
	 * Returns the next processor to be executed. The processor is added into
	 * the list of executing processors. If the next processor has nothing to
	 * executed it is included in the list of waiting processors, and a new
	 * processor is selected. If there is not any processor to execute the
	 * method returns null.
	 * 
	 * @return Returns the next processor to be executed, null if there is no
	 *         other processor to execute.
	 */
	protected Processor getNextProcessor() {
		Processor next = null;
		try {
			this.lock.lock();
			Processor candidate = null;
			boolean exit = false;
			boolean executedListClear = false;
			while (next == null && !exit) {
				if (!this.processorToExecuteList.isEmpty()) {
					candidate = this.processorToExecuteList.getFirst();
					if (candidate.isSomethingToProcess()) {
						next = candidate;
					} else {
						this.processorWaitingList.add(candidate);
					}
				} else if (!this.processorWaitingList.isEmpty()) {
					candidate = this.processorWaitingList.getFirst();
					if (candidate.isSomethingToProcess()) {
						next = candidate;
					} else {
						this.processorExecutedList.add(candidate);
					}
				} else if (!this.processorExecutedList.isEmpty()) {
					this.processorToExecuteList.addList(this.processorExecutedList);
					this.processorExecutedList.clear();
					if (executedListClear) {
						exit = true;
					}
					executedListClear = true;
				} else {
					exit = true;
				}
			}
			if (next != null) {
				if (!this.processorExecutingSet.add(next)) {
					throw new CraniumException("Processor not added into executing list, must not happent never", this);
				}
			}
		} finally {
			this.lock.unlock();
		}
		return next;
	}

	/**
	 * Notifies that a processor has been executed, and ended it execution.
	 * 
	 * @param processor
	 *            The executed processor.
	 */
	protected void executed(Processor processor) {
		try {
			this.lock.lock();
			if (this.processorExecutingSet.remove(processor)) {
				if (this.processorsSet.contains(processor)) {
					// it is not a deleted processor
					this.processorExecutedList.add(processor);
				}
			} else {
				throw new CraniumException("Marked as executed one processor that was not being executing.", processor,
						this);
			}
		} finally {
			this.lock.unlock();
		}
	}

}