package opencranium.cera;

import java.util.List;
import java.util.Vector;

import opencranium.Core;
import opencranium.cognitive.CognitiveFunction;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.data.Percept;
import opencranium.data.Percept.AppraisalType;
import opencranium.data.Percept.Source;
import opencranium.util.ElementProcessingTime;
import opencranium.util.Id;
import opencranium.util.Statistics;
import opencranium.util.StatisticsManager;
import opencranium.util.Time;

/**
 * An abstract class with the main functions of all the layers in the CERA
 * architecture.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class Layer implements Statistics {

	/**
	 * Type of the layer, it can be the sensory-motor layer, physical layer,
	 * mission layer or core layer.
	 * 
	 * @author Jorge Muñoz
	 * @author Raúl Arrabales
	 */
	public enum Type {

		/**
		 * The sensory-motor layer.
		 */
		SENSORY_MOTOR_LAYER,

		/**
		 * The physical layer.
		 */
		PHYSICAL_LAYER,

		/**
		 * The mission layer.
		 */
		MISSION_LAYER,

		/**
		 * The core layer.
		 */
		CORE_LAYER;

		/**
		 * Returns the Layer type given the source of a percept.
		 * 
		 * @param source
		 *            The source of the percept.
		 * @return The layer where the percept should be generated.
		 */
		public static Type getType(Source source) {
			Type layerType = null;
			switch (source) {
			case COGNITIVE_FUNCTION:
				layerType = CORE_LAYER;
				break;
			case MISSION_PROCESSOR:
				layerType = MISSION_LAYER;
				break;
			case PHYSICAL_PROCESSOR:
				layerType = PHYSICAL_LAYER;
				break;
			case EXTEROCEPTIVE_SENSOR:
			case PROPRIOCEPTIVE_SENSOR:
				layerType = SENSORY_MOTOR_LAYER;
				break;
			default:
			}
			return layerType;
		}
	};

	private Type layerType;

	/**
	 * Current tick of execution.
	 */
	private Time currentTick;

	/**
	 * List of cognitive functions. Each function is called when a new
	 * Processable element is created in the processors.
	 */
	private List<CognitiveFunction> cognitiveFunctions;

	/**
	 * Id of the layer.
	 */
	private Id id;

	/**
	 * Element for count the processing time. Use for statistical information.
	 */
	private ElementProcessingTime elementProcessingTime;

	/**
	 * Core of the CERA architecture
	 */
	protected Core core;

	/**
	 * Last maximum activation of the layer.
	 */
	private Activation lastTickMaximumActivation;

	/**
	 * Last minimum activation of the layer.
	 */
	private Activation lastTickMiniumActivation;

	/**
	 * Current maximum activation of the layer.
	 */
	private Activation curentTickMaximumActivation;

	/**
	 * Current minimum activation of the layer.
	 */
	private Activation curentTickMiniumActivation;

	/**
	 * Activation threshold to accept processable elements.
	 */
	private Activation activationThreshold;

	/**
	 * Average activation during last tick, only processed elements.
	 */
	private Activation lastTickAverageActivation;

	/**
	 * Average activation during last tick, including the ones discarded by the
	 * threshold.
	 */
	private Activation lastTickTotalAverageActivation;

	/**
	 * Number of processed elements during last tick, only processed elements.
	 */
	private int lastNumberOfProcessableElementsSubmitted;

	/**
	 * Number of processed elements during last tick, including the ones
	 * discarded by the threshold.
	 */
	private int lastNumberOfTotalProcessableElementsSubmitted;

	/**
	 * Number of processed elements during this tick, only processed elements.
	 */
	private int numberOfProcessableElementsSubmitted;

	/**
	 * Number of processed elements during this tick, including the ones
	 * discarded by the threshold.
	 */
	private int numberOfTotalProcessableElementsSubmitted;

	/**
	 * Sum of activations during this tick, including the ones discarded by the
	 * threshold.
	 */
	private long sumTickActivation;

	/**
	 * Sum of activations during this tick, including the ones discarded by the
	 * threshold.
	 */
	private long sumTickTotalActivation;

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            Id of the layer
	 * @param layerType
	 *            Type of the layer
	 */
	protected Layer(Id id, Type layerType, Core core) {
		this.id = id;
		this.layerType = layerType;
		this.currentTick = new Time();
		this.cognitiveFunctions = new Vector<CognitiveFunction>();
		this.elementProcessingTime = new ElementProcessingTime(this.id);
		this.core = core;

		this.lastTickMaximumActivation = new Activation(Activation.MIN);
		this.lastTickMiniumActivation = new Activation(Activation.MAX);
		this.curentTickMaximumActivation = new Activation(Activation.MIN);
		this.curentTickMiniumActivation = new Activation(Activation.MAX);
		this.activationThreshold = new Activation(Activation.MIN);
		this.lastTickAverageActivation = new Activation(Activation.MIN);
		this.lastTickTotalAverageActivation = new Activation(Activation.MIN);
	}

	/**
	 * Submits a processable element to the layer. Actions and novelty or
	 * missmatched percepts always goes into the layer. Other percepts with less
	 * activation than the threshold the element are automatically discarded. If
	 * the percept was generated in the layer or in a cognitive function it is
	 * also submitted.
	 * 
	 * @param processable
	 *            The processable element.
	 */
	public void submitProcessable(Processable processable) {
		long t1 = System.nanoTime();
		int activationValue = processable.getActivation().getValue();
		if (activationValue > this.curentTickMaximumActivation.getValue()) {
			this.curentTickMaximumActivation.setValue(activationValue);
		}
		if (activationValue < this.curentTickMiniumActivation.getValue()) {
			this.curentTickMiniumActivation.setValue(activationValue);
		}
		this.sumTickTotalActivation += activationValue;
		this.numberOfTotalProcessableElementsSubmitted++;
		AppraisalType appraisal = null;
		Source source = null;
		boolean sameLayer = false;
		if (processable.isPercept()) {
			Percept percept = (Percept) processable;
			appraisal = percept.getAppraisalType();
			source = percept.getSource();
			sameLayer = percept.getSource() != Source.UNKNOWN && Type.getType(percept.getSource()) == this.layerType;
		}
		if (activationValue >= this.activationThreshold.getValue() || processable.isAction()
				|| (source == Source.COGNITIVE_FUNCTION || sameLayer)
				|| (appraisal != null && (appraisal == AppraisalType.NOVELTY || appraisal == AppraisalType.MISSMATCH))) {
			this.sumTickActivation += activationValue;
			this.numberOfProcessableElementsSubmitted++;
			layerSubmitProcessable(processable);
			if (StatisticsManager.isRecording()) {
				long t2 = System.nanoTime();
				long nanoTime = t2 - t1;
				processable.getId().addProcessingTime(this.id, nanoTime);
				this.id.addProcessingTime(processable.getId(), nanoTime);
			}
		} else {
			if (StatisticsManager.isRecording()) {
				processable.getId().addDiscarded(this.id);
			}
		}
	}

	/**
	 * Updates the internal tick of the layer.
	 * 
	 * @param tick
	 *            Current tick.
	 */
	public void systemTick(Time tick) {
		if (tick.getTick() != this.currentTick.getTick()) {
			this.lastTickMaximumActivation.setValue(this.curentTickMaximumActivation.getValue());
			this.lastTickMiniumActivation.setValue(this.curentTickMiniumActivation.getValue());
			this.curentTickMaximumActivation.setValue(Activation.MIN);
			this.curentTickMiniumActivation.setValue(Activation.MAX);

			this.lastNumberOfProcessableElementsSubmitted = this.numberOfProcessableElementsSubmitted;
			this.lastNumberOfTotalProcessableElementsSubmitted = this.numberOfTotalProcessableElementsSubmitted;

			if (this.numberOfProcessableElementsSubmitted > 0) {
				this.lastTickAverageActivation
						.setValue((int) (this.sumTickActivation / this.numberOfProcessableElementsSubmitted));
			} else {
				this.lastTickAverageActivation.setValue(Activation.MIN);
			}
			if (this.numberOfTotalProcessableElementsSubmitted > 0) {
				this.lastTickTotalAverageActivation
						.setValue((int) (this.sumTickTotalActivation / this.numberOfTotalProcessableElementsSubmitted));
			} else {
				this.lastTickTotalAverageActivation.setValue(Activation.MIN);
			}

			this.sumTickActivation = 0;
			this.sumTickTotalActivation = 0;
			this.numberOfProcessableElementsSubmitted = 0;
			this.numberOfTotalProcessableElementsSubmitted = 0;
		}
		this.currentTick.update(tick);
	}

	/**
	 * Submits a processable element to the layer.
	 * 
	 * @param processable
	 *            The processable element.
	 */
	protected abstract void layerSubmitProcessable(Processable processable);

	/**
	 * Manages the results of a processor, send it first to the cognitive
	 * function for an implicit processing and them to the current layer which
	 * decides where send the results.
	 * 
	 * @param processable
	 */
	protected void manageResult(Processable processable) {
		long t1 = 0;
		long nanoTime = 0;
		for (CognitiveFunction function : this.cognitiveFunctions) {
			t1 = System.nanoTime();
			function.implicitProcess(processable);
			if (StatisticsManager.isRecording()) {
				nanoTime = System.nanoTime() - t1;
				processable.getId().addProcessingTime(function.getId(), nanoTime);
				function.getId().addProcessingTime(processable.getId(), nanoTime);
			}
		}
		t1 = System.nanoTime();
		this.layerManageResult(processable);
		if (StatisticsManager.isRecording()) {
			nanoTime = System.nanoTime() - t1;
			processable.getId().addProcessingTime(this.id, nanoTime);
			this.id.addProcessingTime(processable.getId(), nanoTime);
		}
	}

	/**
	 * Manages the results of a processor, send it to the current layer and the
	 * proper higher or lower layer depending on the type of processable elment
	 * generated. Actions go always to current and lower layers and percepts to
	 * current and upper layers.
	 * 
	 * @param processable
	 */
	protected abstract void layerManageResult(Processable processable);

	/**
	 * Resets the layer. Remove current information and send the reset command
	 * to processors and cognitive functions in order to remove their current
	 * information. Long term information is not removed.
	 */
	public void reset() {
		this.lastTickMaximumActivation = new Activation(Activation.MIN);
		this.lastTickMiniumActivation = new Activation(Activation.MAX);
		this.curentTickMaximumActivation = new Activation(Activation.MIN);
		this.curentTickMiniumActivation = new Activation(Activation.MAX);
		this.activationThreshold = new Activation(Activation.MIN);

		this.lastTickAverageActivation = new Activation(Activation.MIN);
		this.lastTickTotalAverageActivation = new Activation(Activation.MIN);
		this.lastNumberOfProcessableElementsSubmitted = 0;
		this.lastNumberOfTotalProcessableElementsSubmitted = 0;

		this.sumTickActivation = 0;
		this.sumTickTotalActivation = 0;
		this.numberOfProcessableElementsSubmitted = 0;
		this.numberOfTotalProcessableElementsSubmitted = 0;
	}

	/**
	 * Adds a cognitive function to the layer.
	 * 
	 * @param function
	 *            The cognitive function
	 * @return True if the functions was added, false otherwise.
	 */
	protected boolean addCognitiveFunction(CognitiveFunction function) {
		boolean added = false;
		if (!this.cognitiveFunctions.contains(function)) {
			added = this.cognitiveFunctions.add(function);
		}
		return added;
	}

	/**
	 * Removes a cognitive function from the layer.
	 * 
	 * @param function
	 *            The cognitive function
	 * @return True if the functions was removed, false otherwise.
	 */
	protected boolean removeCognitiveFunction(CognitiveFunction function) {
		boolean removed = false;
		if (this.cognitiveFunctions.contains(function)) {
			removed = this.cognitiveFunctions.remove(function);
		}
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.Statistics#getElementProcessingTime()
	 */
	@Override
	public ElementProcessingTime getElementProcessingTime() {
		return this.elementProcessingTime;
	}

	/**
	 * Returns whether the layer is the sensory motor layer or not.
	 * 
	 * @return true if the layer is the sensory motor layer.
	 */
	public final boolean isSensoryMotorLayer() {
		return this.layerType == Type.SENSORY_MOTOR_LAYER;
	}

	/**
	 * Returns whether the layer is the physical layer or not.
	 * 
	 * @return true if the layer is the physical layer.
	 */
	public final boolean isPhysicalLayer() {
		return this.layerType == Type.PHYSICAL_LAYER;
	}

	/**
	 * Returns whether the layer is the mission layer or not.
	 * 
	 * @return true if the layer is the mission layer.
	 */
	public final boolean isMissionLayer() {
		return this.layerType == Type.MISSION_LAYER;
	}

	/**
	 * Returns whether the layer is the core layer or not.
	 * 
	 * @return true if the layer is the core layer.
	 */
	public final boolean isCoreLayer() {
		return this.layerType == Type.CORE_LAYER;
	}

	/**
	 * Returns the layer type.
	 * 
	 * @return The layer type.
	 */
	public Type getLayerType() {
		return this.layerType;
	}

	/**
	 * Returns the activation threshold, the processable elements with less
	 * value than this activation will be discarded.
	 * 
	 * @return the activationThreshold
	 */
	public Activation getActivationThreshold() {
		return this.activationThreshold;
	}

	/**
	 * Sets the activation threshold, the processable elements with less value
	 * than this activation will be discarded.
	 * 
	 * @param activationThreshold
	 *            the activationThreshold to set
	 */
	public void setActivationThreshold(Activation activationThreshold) {
		this.activationThreshold.setValue(activationThreshold.getValue());
	}

	/**
	 * Returns the maximum activation of the processable elements during the
	 * last tick, it includes the discarded elements.
	 * 
	 * @return the lastTickMaximumActivation
	 */
	public Activation getLastTickMaximumActivation() {
		return this.lastTickMaximumActivation;
	}

	/**
	 * Returns the minimum activation of the processable elements during the
	 * last tick, it includes the discarded elements.
	 * 
	 * @return the lastTickMiniumActivation
	 */
	public Activation getLastTickMiniumActivation() {
		return this.lastTickMiniumActivation;
	}

	/**
	 * Returns the average activation of the processable elements during the
	 * last tick, it does not include the discarded elements.
	 * 
	 * @return the lastTickAverageActivation
	 */
	public Activation getLastTickAverageActivation() {
		return this.lastTickAverageActivation;
	}

	/**
	 * Returns the average activation of the processable elements during the
	 * last tick, it includes the discarded elements.
	 * 
	 * @return the lastTickTotalAverageActivation
	 */
	public Activation getLastTickTotalAverageActivation() {
		return this.lastTickTotalAverageActivation;
	}

	/**
	 * Returns the number of processable elements submitted to this layer,
	 * without taking into account the discarded elements.
	 * 
	 * @return the lastNumberOfProcessableElementsSubmitted
	 */
	public int getLastNumberOfProcessableElementsSubmitted() {
		return this.lastNumberOfProcessableElementsSubmitted;
	}

	/**
	 * Returns the number of processable elements submitted to this layer,
	 * including the discarded elements.
	 * 
	 * @return the lastNumberOfTotalProcessableElementsSubmitted
	 */
	public int getLastNumberOfTotalProcessableElementsSubmitted() {
		return this.lastNumberOfTotalProcessableElementsSubmitted;
	}

	/**
	 * @return the id
	 */
	public Id getId() {
		return this.id;
	}

	/**
	 * Register a processor into the layer.
	 * 
	 * @param processor
	 *            The Processor to register.
	 * @return true if the processor was added, false otherwise.
	 */
	public abstract boolean registerProcessor(Processor processor);

}