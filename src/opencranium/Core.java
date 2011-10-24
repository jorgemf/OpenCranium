package opencranium;

import static opencranium.Property.MULTI_POOL;
import static opencranium.Property.RUNTIME_THREADS;

import java.io.File;

import opencranium.cera.CoreLayer;
import opencranium.cera.MissionLayer;
import opencranium.cera.PhysicalLayer;
import opencranium.cera.SensoryMotorLayer;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.util.Time;
import opencranium.util.configuration.Properties;
import opencranium.util.log.Logger;

/**
 * Core class of the CERA-CRANIUM architecture. This class initialize the CERA
 * layers and the CRANIUM workspace.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Core implements Runnable {

	/**
	 * File where the properties are loaded
	 */
	public static final String PROPERTIES = "ceracranium.properties";

	/**
	 * Unique instance of the class.
	 */
	private static Core core = new Core();

	/**
	 * Current time of execution.
	 */
	private Time currentTick;

	/**
	 * Sensory motor layer.
	 */
	private SensoryMotorLayer sensoryMotorLayer;

	/**
	 * Physical layer.
	 */
	private PhysicalLayer physicalLayer;

	/**
	 * Mission layer.
	 */
	private MissionLayer missionLayer;

	/**
	 * Core layer.
	 */
	private CoreLayer coreLayer;

	/**
	 * Thread pool of processors of all layer.
	 */
	private ProcessorThreadPool threadPool;

	/**
	 * Thread pool of processors of the sensory motor layer.
	 */
	private ProcessorThreadPool threadPoolSensoryMotor;

	/**
	 * Thread pool of processors of the physical layer.
	 */
	private ProcessorThreadPool threadPoolPhysical;

	/**
	 * Thread pool of processors of the mission layer.
	 */
	private ProcessorThreadPool threadPoolMission;

	/**
	 * Thread pool of processors of the core layer.
	 */
	private ProcessorThreadPool threadPoolCore;

	/**
	 * Thread of the architecture, only used if it is started as thread.
	 */
	private Thread thread;

	/**
	 * Variable to know if the thread is running.
	 */
	private boolean running;

	/**
	 * Variable to know if the architecture was started.
	 */
	private boolean started;

	/**
	 * Variable to know if the architecture use multipool or not.
	 */
	private boolean multiPool;

	/**
	 * Total execution time of the thread pool when the class is created as not
	 * multipool.
	 */
	private int executionTime;

	/**
	 * Time the thread is pause between two consecutive executions. Negative
	 * values means it is never paused.
	 */
	private int pauseTime;

	/**
	 * Execution time of the sensory-motor layer's thread pool when the class is
	 * created as multipool.
	 */
	private int sensoryLayerTime;

	/**
	 * Execution time of the physical layer's thread pool when the class is
	 * created as multipool.
	 */
	private int physicalLayerTime;

	/**
	 * Execution time of the mission layer's thread pool when the class is
	 * created as multipool.
	 */
	private int missionLayerTime;

	/**
	 * Execution time of the core layer's thread pool when the class is created
	 * as multipool.
	 */
	private int coreLayerTime;

	/**
	 * Default constructor
	 */
	private Core() {
		this.currentTick = new Time(0, System.currentTimeMillis());
		Properties properties = Properties.instance();
		int threads = 0;
		this.multiPool = false;
		if (properties.load(new File(PROPERTIES))) {
			if (properties.exists(RUNTIME_THREADS)) {
				threads = properties.intValue(RUNTIME_THREADS);
			}
			if (properties.exists(MULTI_POOL)) {
				this.multiPool = properties.booleanValue(MULTI_POOL);
			}
		}
		if (multiPool) {
			if (threads > 0) {
				this.threadPoolSensoryMotor = new ProcessorThreadPool(threads);
				this.threadPoolPhysical = new ProcessorThreadPool(threads);
				this.threadPoolMission = new ProcessorThreadPool(threads);
				this.threadPoolCore = new ProcessorThreadPool(threads);
			} else {
				this.threadPoolSensoryMotor = new ProcessorThreadPool();
				this.threadPoolPhysical = new ProcessorThreadPool();
				this.threadPoolMission = new ProcessorThreadPool();
				this.threadPoolCore = new ProcessorThreadPool();
			}
			this.sensoryMotorLayer = new SensoryMotorLayer(this, this.threadPoolSensoryMotor);
			this.physicalLayer = new PhysicalLayer(this, this.threadPoolPhysical);
			this.missionLayer = new MissionLayer(this, this.threadPoolMission);
			this.coreLayer = new CoreLayer(this, this.threadPoolCore);
		} else {
			if (threads > 0) {
				this.threadPool = new ProcessorThreadPool(threads);
			} else {
				this.threadPool = new ProcessorThreadPool();
			}
			this.sensoryMotorLayer = new SensoryMotorLayer(this, this.threadPool);
			this.physicalLayer = new PhysicalLayer(this, this.threadPool);
			this.missionLayer = new MissionLayer(this, this.threadPool);
			this.coreLayer = new CoreLayer(this, this.threadPool);
		}
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the unique instance of this class.
	 */
	public static Core instance() {
		return core;
	}

	/**
	 * Returns the current tick of execution.
	 * 
	 * @return the current tick of execution.
	 */
	public Time getCurrentTick() {
		return this.currentTick;
	}

	/**
	 * Sets the current tick of execution.
	 * 
	 * @param tick
	 *            current tick of execution.
	 */
	public void setCurrentTick(Time tick) {
		this.currentTick.update(tick);
	}

	/**
	 * @return the sensory-motor layer
	 */
	public SensoryMotorLayer getSensoryMotorLayer() {
		return this.sensoryMotorLayer;
	}

	/**
	 * @return the physical layer
	 */
	public PhysicalLayer getPhysicalLayer() {
		return this.physicalLayer;
	}

	/**
	 * @return the mission layer
	 */
	public MissionLayer getMissionLayer() {
		return this.missionLayer;
	}

	/**
	 * @return the core layer
	 */
	public CoreLayer getCoreLayer() {
		return this.coreLayer;
	}

	/**
	 * Resets all layers.
	 */
	public void reset() {
		this.sensoryMotorLayer.reset();
		this.physicalLayer.reset();
		this.missionLayer.reset();
		this.coreLayer.reset();
	}

	/**
	 * Starts all the processors in the layers. The processors must be added to
	 * the layer before this method call.
	 */
	public void startArchitecture() {
		if (this.started) {
			throw new OpenCraniumException("Core started previously.", this);
		}
		if (this.multiPool) {
			this.threadPoolSensoryMotor.startAll();
			this.threadPoolPhysical.startAll();
			this.threadPoolMission.startAll();
			this.threadPoolCore.startAll();
		} else {
			this.threadPool.startAll();
		}
	}

	/**
	 * Stops all the processors in the layers. Must be called the start method
	 * before.
	 */
	public void stopArchitecture() {
		if (!this.started) {
			throw new OpenCraniumException("Core was not previously started.", this);
		}
		if (this.multiPool) {
			this.threadPoolSensoryMotor.stopAll();
			this.threadPoolPhysical.stopAll();
			this.threadPoolMission.stopAll();
			this.threadPoolCore.stopAll();
		} else {
			this.threadPool.stopAll();
		}
	}

	/**
	 * Executing the layers during some time. The first layer executed is the
	 * sensory motor layer, them the physical layer, the mission layer and the
	 * last one the core layer.
	 * 
	 * @param sensoryLayerTime
	 *            Time in milliseconds the sensory-motor layer is executed.
	 * @param physicalLayerTime
	 *            Time in milliseconds the physical layer is executed.
	 * @param missionLayerTime
	 *            Time in milliseconds the mission layer is executed.
	 * @param coreLayerTime
	 *            Time in milliseconds the core layer is executed.
	 * @throws OpenCraniumException
	 *             When the system was not started and if the multipool mode is
	 *             not enabled in the properties file.
	 */
	public void executeDuring(int sensoryLayerTime, int physicalLayerTime, int missionLayerTime, int coreLayerTime) {
		if (!this.started) {
			throw new OpenCraniumException("Core was not previously started.", this);
		}
		if (!this.multiPool) {
			throw new OpenCraniumException("This method only can be called when core is created in multy pool mode.",
					this);
		}
		this.threadPoolSensoryMotor.executeDuring(sensoryLayerTime);
		this.threadPoolPhysical.executeDuring(physicalLayerTime);
		this.threadPoolMission.executeDuring(missionLayerTime);
		this.threadPoolCore.executeDuring(coreLayerTime);
	}

	/**
	 * Executing the layers during some time.
	 * 
	 * @param totalTime
	 *            Time in milliseconds the layeres are executed.
	 * @throws OpenCraniumException
	 *             When the system was not started and if the multipool mode is
	 *             enabled in the properties file.
	 */
	public void executeDuring(int totalTime) {
		if (!this.started) {
			throw new OpenCraniumException("Core was not previously started.", this);
		}
		if (this.multiPool) {
			throw new OpenCraniumException(
					"This method only can be called when core is not created in multy pool mode.", this);
		}
		this.threadPool.executeDuring(totalTime);
	}

	/**
	 * Let the layers execute until the method stopArchitecture is called.
	 * 
	 * @throws OpenCraniumException
	 *             If the system was not started.
	 */
	public void execute() {
		if (!this.started) {
			throw new OpenCraniumException("Core was not previously started.", this);
		}
		if (this.multiPool) {
			this.threadPoolSensoryMotor.resumeAll();
			this.threadPoolPhysical.resumeAll();
			this.threadPoolMission.resumeAll();
			this.threadPoolCore.resumeAll();
		} else {
			this.threadPool.resumeAll();
		}
	}

	/**
	 * Starts the architecture as a autonomous thread.
	 * 
	 * @param executionTime
	 *            Time the threadpool is executed per cycle.
	 * @param pauseTime
	 *            Time the threadpool waits between cycles. Zero or negative
	 *            values means the system never waits.
	 * 
	 * @throws OpenCraniumException
	 *             When the system was not started, if the multipool mode is
	 *             enabled in the properties file or if the architecture was
	 *             launched previously as a thread.
	 */
	public void startAsThread(int executionTime, int pauseTime) {
		if (this.started) {
			throw new OpenCraniumException("Core started previously.", this);
		}
		if (this.running) {
			throw new OpenCraniumException("Core can be only started as thread once.", this);
		}
		if (this.multiPool) {
			throw new OpenCraniumException(
					"This method only can be called when core is not created in multy pool mode.", this);
		}
		if (this.thread == null) {
			this.thread = new Thread(this);
		}
		this.executionTime = executionTime;
		this.pauseTime = pauseTime;
		this.thread.start();
	}

	/**
	 * Starts the architecture as a autonomous thread. The first layer executed
	 * is the sensory motor layer, them the physical layer, the mission layer
	 * and the last one the core layer.
	 * 
	 * @param sensoryLayerTime
	 *            Time in milliseconds the sensory-motor layer is executed.
	 * @param physicalLayerTime
	 *            Time in milliseconds the physical layer is executed.
	 * @param missionLayerTime
	 *            Time in milliseconds the mission layer is executed.
	 * @param coreLayerTime
	 *            Time in milliseconds the core layer is executed.
	 * @param pauseTime
	 *            Time the threadpool waits between cycles. Zero or negative
	 *            values means the system never waits.
	 * 
	 * @throws OpenCraniumException
	 *             When the system was not started, if the multipool mode is not
	 *             enabled in the properties file or if the architecture was
	 *             launched previously as a thread.
	 */
	public void startAsThread(int sensoryLayerTime, int physicalLayerTime, int missionLayerTime, int coreLayerTime,
			int pauseTime) {
		if (this.started) {
			throw new OpenCraniumException("Core started previously.", this);
		}
		if (this.running) {
			throw new OpenCraniumException("Core can be only started as thread once.", this);
		}
		if (!this.multiPool) {
			throw new OpenCraniumException(
					"This method only can be called when core is not created in multy pool mode.", this);
		}
		if (this.thread == null) {
			this.thread = new Thread(this);
		}
		this.sensoryLayerTime = sensoryLayerTime;
		this.physicalLayerTime = physicalLayerTime;
		this.missionLayerTime = missionLayerTime;
		this.coreLayerTime = coreLayerTime;
		this.pauseTime = pauseTime;
		this.thread.start();
	}

	/**
	 * Stops the architecture as a autonomous thread.
	 * 
	 * @throws OpenCraniumException
	 *             If the architecture was not running as thread.
	 */
	public void stopAsThread() {
		if (!this.running) {
			throw new OpenCraniumException("Core was not launched as thread previously.", this);
		}
		this.running = false;
	}

	@Override
	public void run() {
		if (this.running) {
			throw new OpenCraniumException("Core can be only started as thread once.", this);
		}
		this.running = true;
		startArchitecture();
		if (this.multiPool) {
			if (this.pauseTime > 0) {
				while (this.running) {
					this.threadPoolSensoryMotor.executeDuring(this.sensoryLayerTime);
					this.threadPoolPhysical.executeDuring(this.physicalLayerTime);
					this.threadPoolMission.executeDuring(this.missionLayerTime);
					this.threadPoolCore.executeDuring(this.coreLayerTime);
					try {
						Thread.sleep(this.pauseTime);
					} catch (InterruptedException e) {
						Logger.exception(e);
					}
				}
			} else {
				while (this.running) {
					this.threadPoolSensoryMotor.executeDuring(this.sensoryLayerTime);
					this.threadPoolPhysical.executeDuring(this.physicalLayerTime);
					this.threadPoolMission.executeDuring(this.missionLayerTime);
					this.threadPoolCore.executeDuring(this.coreLayerTime);
				}
			}
		} else {
			if (this.pauseTime > 0) {
				while (this.running) {
					this.threadPool.executeDuring(this.executionTime);
					try {
						Thread.sleep(this.pauseTime);
					} catch (InterruptedException e) {
						Logger.exception(e);
					}
				}
			} else {
				while (this.running) {
					this.threadPool.executeDuring(this.executionTime);
				}
			}
		}
		stopArchitecture();
	}

}
