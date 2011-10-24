package opencranium.cranium;

/**
 * A thread to process things. It has 4 states: initialized, running, paused,
 * killing. If this thread get nothing to process from the pool it keeps doing
 * polling to the pool until is something to process or the thread is paused or
 * killed.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class ProcessorThread implements Runnable {

	/**
	 * True if the thread is initializing.
	 */
	private boolean initialized;

	/**
	 * True if the thread is being killed.
	 */
	private boolean killing;

	/**
	 * True if the thread is being paused.
	 */
	private boolean pausing;

	/**
	 * True if the thread is paused.
	 */
	private boolean paused;

	/**
	 * Pool of threads owner of this thread.
	 */
	private ProcessorThreadPool processorThreads;

	/**
	 * Current process to execute.
	 */
	private Processor currentProcess;

	/**
	 * Time in milliseconds when the real time execution cycle ends. If it is 0
	 * there is no cycle ends. Used to pause the thread when needed.
	 */
	private long cycleEnds;

	/**
	 * Thread of this processor thread. Is set when the thread is started.
	 */
	private Thread thread;

	/**
	 * Default constructor.
	 * 
	 * @param processorThreads
	 *            Pool of threads owner of this thread. Cannot be null.
	 */
	protected ProcessorThread(ProcessorThreadPool processorThreads) {
		this.processorThreads = processorThreads;
		this.currentProcess = null;
		this.initialized = false;
		this.killing = false;
		this.pausing = false;
		this.paused = false;
		this.thread = null;
		this.cycleEnds = 0;
		this.initialized = false;
		this.init();
		this.initialized = true;
	}

	/**
	 * Default constructor.
	 */
	public ProcessorThread() {
		this(null);
	}

	/**
	 * Initiate the thread. Called in the constructor.
	 */
	protected void init() {
	}

	/**
	 * @return if the thread is started.
	 */
	public boolean isStarted() {
		return this.thread != null;
	}

	/**
	 * @return if the thread was initialized
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

	/**
	 * @return if the thread is being paused
	 */
	public boolean isPausing() {
		return this.pausing;
	}

	/**
	 * @return if the thread is paused
	 */
	public boolean isPaused() {
		return this.paused;
	}

	/**
	 * @return if the thread is running
	 */
	public boolean isRunning() {
		return this.currentProcess != null;
	}

	/**
	 * @return if the thread is being killed
	 */
	public boolean isKilling() {
		return this.killing;
	}

	/**
	 * The pool of thread owner of this thread.
	 * 
	 * @return the processorThreads
	 */
	public ProcessorThreadPool getProcessorThreads() {
		return this.processorThreads;
	}

	/**
	 * Returns the process is executing, null if it is noone.
	 * 
	 * @return the currentProcess.
	 */
	public Processor getCurrentProcess() {
		return this.currentProcess;
	}

	/**
	 * Method to start the thread after it has been initialized.
	 */
	public void start() {
		if (!this.initialized) {
			throw new CraniumException("This thread has not being initialized.", this);
		} else if (this.thread != null) {
			throw new CraniumException("This thread was previously started.", this);
		} else {
			this.thread = new Thread(this);
			this.pause();
			this.thread.start();
		}
	}

	/**
	 * Mark the processor as paused and do not execute it until resume method is
	 * called.
	 */
	public void pause() {
		if (this.thread == null) {
			throw new CraniumException("The processor was not started.", this);
		} else if (this.pausing) {
			throw new CraniumException("The processor was being paused.", this);
		} else if (this.paused) {
			throw new CraniumException("The processor was paused.", this);
		} else {
			this.pausing = true;
		}
	}

	/**
	 * Kill the thread. It waits until the processors ends its execution. If
	 * this method is overwrited it must be called at the top of the method:
	 * super.kil().
	 */
	public void kill() {
		if (this.thread == null) {
			throw new CraniumException("The processor was not started.", this);
		} else if (this.killing) {
			throw new CraniumException("The processor is being killed.", this);
		} else {
			this.killing = true;
		}
	}

	/**
	 * Set the current process to execute.
	 * 
	 * @param currentProcess
	 *            the currentProcess to set
	 */
	public void setCurrentProcess(Processor currentProcess) {
		if (this.thread == null) {
			throw new CraniumException("The processor was not started.", this);
		} else if (this.currentProcess != null) {
			throw new CraniumException("The processor is running.", this);
		} else if (this.killing) {
			throw new CraniumException("The processor is being killed.", this);
		} else {
			this.currentProcess = currentProcess;
		}
	}

	/**
	 * Resumes or starts the execution of the processor. If the processor was
	 * killed it is initialized again and executed as normally.
	 * 
	 * @param milliseconds
	 *            Time in milliseconds when the processor should stop executing.
	 *            0 if the time is unlimited.
	 * @see System#currentTimeMillis()
	 */
	public void resume(long milliseconds) {
		if (this.thread == null) {
			throw new CraniumException("The processor was not started.", this);
		} else {
			synchronized (this) {
				this.cycleEnds = milliseconds;
				this.paused = false;
				this.pausing = false;
				this.notify();
			}
		}
	}

	/**
	 * Checks if the thread should stop and stop it when needed.
	 * 
	 * @return the time in milliseconds when the processor should finish or call
	 *         pause again. 0 or negative if the time is unlimited.
	 */
	protected long processorPause() {
		synchronized (this) {
			while ((this.cycleEnds > 0 && System.currentTimeMillis() >= this.cycleEnds) || this.pausing || this.paused) {
				this.paused = true;
				this.pausing = false;
				try {
					this.wait();
				} catch (InterruptedException exception) {
					// nothing to do
				}
			}
		}
		return this.cycleEnds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (this.thread == null) {
			throw new CraniumException("The processor was not started.", this);
		}
		while (this.thread != null) {
			if (this.killing && this.currentProcess == null) {
				// being killed
				this.thread = null;
			} else if (this.pausing) {
				// being paused
				this.processorPause();
			} else if (this.currentProcess == null) {
				this.currentProcess = this.processorThreads.getNextProcessor();
				if (this.currentProcess != null) {
					this.currentProcess.process(this.cycleEnds);
					this.currentProcess = null;
				} else if (!this.pausing) {
					this.pause();
				}
			} else {
				this.currentProcess.process(this.cycleEnds);
			}
		}
		this.killing = false;
		this.pausing = false;
	}
}