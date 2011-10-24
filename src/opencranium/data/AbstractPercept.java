package opencranium.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import opencranium.Core;
import opencranium.cognitive.attention.Context;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.util.Id;
import opencranium.util.Time;
import opencranium.util.collection.SortedElement;

/**
 * Abstract class that overrides the common methods of all percepts.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class AbstractPercept implements Percept {

	/**
	 * Activation of the percept.
	 */
	private Activation activation;

	/**
	 * When this percept was created.
	 */
	private Time creationTime;

	/**
	 * The collection of Processable that generates this Percept.
	 */
	private Collection<Processable> generatedBy;

	/**
	 * Context of this Percept.
	 */
	private Collection<Context> context;

	/**
	 * Confidence of the percept.
	 */
	private Confidence confidence;

	/**
	 * The nature type of the percept.
	 */
	private Nature nature;

	/**
	 * The source type of the percept.
	 */
	private Source source;

	/**
	 * The memory type of the percept.
	 */
	private MemoryType memoryType;

	/**
	 * The appraisal type of the percept.
	 */
	private AppraisalType appraisalType;

	/**
	 * Id of the percept.
	 */
	private Id id;

	/**
	 * Type of the percept.
	 */
	private Type type;

	/**
	 * 
	 * Default constructor.
	 * 
	 * @param id
	 *            Id of the percept, cannot be null.
	 * @param type
	 *            Type of the percept, cannot be null.
	 * @param generatedBy
	 *            Collection of element that generates this percept.
	 * @param context
	 *            Current context of the percept.
	 * @param confidence
	 *            Confidence of the percept.
	 */
	public AbstractPercept(Id id, Type type, Collection<Processable> generatedBy, Collection<Context> context,
			Confidence confidence) {
		this.id = id;
		if (this.id == null) {
			throw new IllegalArgumentException("The id can not be null.");
		}
		this.type = type;
		if (this.type == null) {
			throw new IllegalArgumentException("The type can not be null.");
		}
		this.activation = new Activation(Activation.MIN);
		this.creationTime = new Time(Core.instance().getCurrentTick().getTick(), System.currentTimeMillis());
		this.generatedBy = generatedBy;
		if (this.generatedBy == null) {
			this.generatedBy = new ArrayList<Processable>();
		}
		this.confidence = confidence;
		if (this.confidence == null) {
			this.confidence = new Confidence(Confidence.MIN);
		}
		this.context = context;
		if (this.context == null) {
			this.context = new ArrayList<Context>();
		}
		this.nature = Nature.UNKNOWN;
		this.source = Source.UNKNOWN;
		this.memoryType = MemoryType.UNKNOWN;
		this.appraisalType = AppraisalType.UNKNOWN;
	}

	/**
	 * Default constructor with the context and the confidence of the percept.
	 * Used when this percept is generated in the sensor layer.
	 * 
	 * @param id
	 *            Id of the percept, cannot be null.
	 * @param type
	 *            Type of the percept, cannot be null.
	 * @param context
	 *            Current context of the percept.
	 * @param confidence
	 *            Confidence of the percept.
	 */
	public AbstractPercept(Id id, Type type, Collection<Context> context, Confidence confidence) {
		this(id, type, null, context, confidence);
	}

	/**
	 * Default constructor with the collection of elements that generated this
	 * percept. Used when this percept is generated as a consequence of other
	 * percepts or actions.
	 * 
	 * @param id
	 *            Id of the percept, cannot be null.
	 * @param type
	 *            Type of the percept, cannot be null.
	 * @param generatedBy
	 *            Collection of element that generates this percept.
	 */
	public AbstractPercept(Id id, Type type, Collection<Processable> generatedBy) {
		this(id, type, generatedBy, null, null);
	}

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            Id of the percept, cannot be null.
	 * @param type
	 *            Type of the percept, cannot be null.
	 */
	public AbstractPercept(Id id, Type type) {
		this(id, type, null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getType()
	 */
	@Override
	public final Type getType() {
		return this.type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#getId()
	 */
	@Override
	public final Id getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.util.collection.SortedElement#getSortingValue()
	 */
	@Override
	public final int getSortingValue() {
		return this.activation.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#getGeneratedBy()
	 */
	@Override
	public final Collection<Processable> getGeneratedBy() {
		return this.generatedBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#setGeneratedBy(java.util.Collection)
	 */
	@Override
	public final void setGeneratedBy(Collection<Processable> generatedBy) {
		this.generatedBy = generatedBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#getActivation()
	 */
	@Override
	public final Activation getActivation() {
		return this.activation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.Processable#setActivation(opencranium.cranium.Activation
	 * )
	 */
	@Override
	public final void setActivation(Activation activation) {
		this.activation = activation;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#getCreationTime()
	 */
	@Override
	public final Time getCreationTime() {
		return this.creationTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isPercept()
	 */
	@Override
	public final boolean isPercept() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isAction()
	 */
	@Override
	public final boolean isAction() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isSinglePercept()
	 */
	@Override
	public final boolean isSinglePercept() {
		return this.getType() == Percept.Type.SINGLE_PERCEPT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isComplexPercept()
	 */
	@Override
	public final boolean isComplexPercept() {
		return this.getType() == Percept.Type.COMPLEX_PERCEPT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isMissionPercept()
	 */
	@Override
	public final boolean isMissionPercept() {
		return this.getType() == Percept.Type.MISSION_PERCEPT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isSimpleAction()
	 */
	@Override
	public final boolean isSimpleAction() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isComplexAction()
	 */
	@Override
	public final boolean isComplexAction() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.cranium.Processable#isAnUpdatedProcessable(opencranium.cranium
	 * .Processable)
	 */
	@Override
	public final boolean isAnUpdatedProcessable(Processable processable) {
		if (processable.isPercept()) {
			return this.isAnUpdatedPercept((Percept) processable);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.util.collection.SortedElement#compareTo(opencranium.util.
	 * collection.SortedElement)
	 */
	@Override
	public final int compareTo(SortedElement other) {
		return this.getSortingValue() - other.getSortingValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.data.Percept#isAnUpdatedPercept(opencranium.data.Percept)
	 */
	@Override
	public boolean isAnUpdatedPercept(Percept percept) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#similarity(opencranium.data.Percept)
	 */
	@Override
	public float similarity(Percept other) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getConfidence()
	 */
	@Override
	public final Confidence getConfidence() {
		return this.confidence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getNature()
	 */
	@Override
	public final Nature getNature() {
		return this.nature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#setNature(opencranium.data.Percept.Nature)
	 */
	@Override
	public final void setNature(Nature nature) throws IllegalAccessError {
		if (this.nature != Nature.UNKNOWN || nature == null) {
			throw new IllegalAccessError();
		}
		this.nature = nature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getSource()
	 */
	@Override
	public final Source getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#setSource(opencranium.data.Percept.Source)
	 */
	@Override
	public final void setSource(Source source) throws IllegalAccessError {
		if (this.source != Source.UNKNOWN || source == null) {
			throw new IllegalAccessError();
		}
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getMemoryType()
	 */
	@Override
	public final MemoryType getMemoryType() {
		return this.memoryType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.data.Percept#setMemoryType(opencranium.data.Percept.MemoryType
	 * )
	 */
	@Override
	public final void setMemoryType(MemoryType memoryType) throws IllegalAccessError {
		if (this.memoryType != MemoryType.UNKNOWN || memoryType == null) {
			throw new IllegalAccessError();
		}
		this.memoryType = memoryType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getAppraisalType()
	 */
	@Override
	public final AppraisalType getAppraisalType() {
		return this.appraisalType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeopencranium.data.Percept#setAppraisalType(opencranium.data.Percept.
	 * AppraisalType)
	 */
	@Override
	public final void setAppraisalType(AppraisalType appraisalType) throws IllegalAccessError {
		if (this.appraisalType != AppraisalType.UNKNOWN || appraisalType == null) {
			throw new IllegalAccessError();
		}
		this.appraisalType = appraisalType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getCotext()
	 */
	@Override
	public final Collection<Context> getContext() {
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Percept#getDescription()
	 */
	@Override
	public PerceptDescription getDescription() {
		return new PerceptDescription(this.id.getName(), null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#getDataRepresentation()
	 */
	@Override
	public Set<Entry<String, Float>> getDataRepresentation() {
		return new TreeSet<Entry<String, Float>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId().getName()).append(" [").append(this.getId().getId()).append(']');
		sb.append(" (").append(this.getType().toString()).append(')');
		sb.append(" a: ").append(this.getActivation().getValue());
		sb.append(" c: ").append(this.getConfidence().getValue());
		sb.append(" ct: ").append(this.getCreationTime().toString());
		sb.append(" nature: ").append(this.getNature().toString());
		sb.append(" source: ").append(this.getSource().toString());
		sb.append(" memory: ").append(this.getMemoryType().toString());
		sb.append(" appraisal: ").append(this.getAppraisalType().toString());
		sb.append(" context: ( ");
		for (Context context : this.getContext()) {
			sb.append(context.toString()).append(' ');
		}
		sb.append(')');
		sb.append(" description: (").append(this.getDescription().toString()).append(')');
		for (Entry<String, Float> entry : this.getDataRepresentation()) {
			sb.append(entry.getKey()).append(':').append(entry.getValue()).append(' ');
		}
		sb.append(')');
		return sb.toString();
	}

}