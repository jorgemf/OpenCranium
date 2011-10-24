package opencranium.cranium;

import opencranium.OpenCraniumException;

/**
 * An exception class for being used in the Workspaces and WorkspaceProcessors.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class CraniumException extends OpenCraniumException {

	/**
	 * To serialize the class.
	 */
	private static final long serialVersionUID = -2159321067782072328L;

	/**
	 * The Workspace where the exception happened.
	 */
	private Workspace workspace;

	/**
	 * The Processor where the exception happened.
	 */
	private Processor processor;

	/**
	 * The WorkspaceProcessor where the exception happened.
	 */
	private ProcessorThreadPool threadPool;

	/**
	 * The ProcessorThread where the exception happened.
	 */
	private ProcessorThread thread;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param thread
	 *            Processor thread of the exception
	 */
	public CraniumException(String message, ProcessorThread thread) {
		super(message, thread);
		this.thread = thread;
		this.workspace = null;
		this.processor = null;
		this.threadPool = thread.getProcessorThreads();
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param workspace
	 *            Workspace source of the exception.
	 */
	public CraniumException(String message, Workspace workspace) {
		super(message, workspace);
		this.thread = null;
		this.workspace = workspace;
		this.processor = null;
		this.threadPool = null;
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param processor
	 *            WorkspaceProcessor source of the exception.
	 */
	public CraniumException(String message, WorkspaceProcessor processor) {
		super(message, processor);
		this.thread = null;
		this.workspace = null;
		this.processor = processor;
		this.threadPool = null;
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param processor
	 *            Processor source of the exception.
	 * @param threadPool
	 *            ProcessorThreadPool source of the exception.
	 */
	public CraniumException(String message, Processor processor, ProcessorThreadPool threadPool) {
		super(message, threadPool);
		this.thread = null;
		this.workspace = null;
		this.processor = processor;
		this.threadPool = threadPool;
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param threadPool
	 *            ProcessorThreadPool source of the exception.
	 */
	public CraniumException(String message, ProcessorThreadPool threadPool) {
		super(message, threadPool);
		this.thread = null;
		this.workspace = null;
		this.processor = null;
		this.threadPool = threadPool;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return this.workspace;
	}

	/**
	 * @return the processor
	 */
	public Processor getProcessor() {
		return this.processor;
	}

	/**
	 * @return the threadPool
	 */
	public ProcessorThreadPool getThreadPool() {
		return this.threadPool;
	}

	/**
	 * @return the thread
	 */
	public ProcessorThread getThread() {
		return thread;
	}

}