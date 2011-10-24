package opencranium.command;

import java.util.ArrayList;
import java.util.Collection;

import opencranium.Core;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.Time;
import opencranium.util.collection.SortedElement;
import opencranium.util.configuration.Properties;
import opencranium.util.log.Logger;

/**
 * Abstract class that overrides the common methods of all actions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public abstract class AbstractAction implements Action {

	/**
	 * The last time this action object was executed. Null if never has been
	 * executed.
	 */
	private Time executionTime;

	/**
	 * When this action was created.
	 */
	private Time creationTime;

	/**
	 * Priority of this action.
	 */
	private Priority priority;

	/**
	 * Activation of this action.
	 */
	private Activation activation;

	/**
	 * The collection of Processable that generates this Action.
	 */
	private Collection<Processable> generatedBy;

	/**
	 * Id of the action.
	 */
	private Id id;

	/**
	 * Type of the action.
	 */
	private Type type;

	/**
	 * Modification for highest priority
	 */
	private static double MODIFICATION_HIGHEST = 1.3;

	/**
	 * Modification for high priority
	 */
	private static double MODIFICATION_HIGH = 1.15;

	/**
	 * Modification for medium priority
	 */
	private static double MODIFICATION_MED = 1;

	/**
	 * Modification for low priority
	 */
	private static double MODIFICATION_LOW = 0.85;

	/**
	 * Modification for lowest priority
	 */
	private static double MODIFICATION_LOWEST = 0.7;

	/**
	 * Load the modifications for the priorities from a properties object.
	 * 
	 * @param properties
	 *            Properties used.
	 * @return true if all the properties were loaded, false otherwise.
	 */
	public static boolean loadModifications(Properties properties) {
		boolean loaded = true;
		try {
			String value = null;
			value = properties.value(AbstractAction.class.getName() + ".MODIFICATION_HIGHEST");
			if (value != null && value.length() > 0) {
				MODIFICATION_HIGHEST = Double.parseDouble(value);
			} else {
				loaded = false;
			}
			value = properties.value(AbstractAction.class.getName() + ".MODIFICATION_HIGH");
			if (value != null && value.length() > 0) {
				MODIFICATION_HIGH = Double.parseDouble(value);
			} else {
				loaded = false;
			}
			value = properties.value(AbstractAction.class.getName() + ".MODIFICATION_MED");
			if (value != null && value.length() > 0) {
				MODIFICATION_MED = Double.parseDouble(value);
			} else {
				loaded = false;
			}
			value = properties.value(AbstractAction.class.getName() + ".MODIFICATION_LOW");
			if (value != null && value.length() > 0) {
				MODIFICATION_LOW = Double.parseDouble(value);
			} else {
				loaded = false;
			}
			value = properties.value(AbstractAction.class.getName() + ".MODIFICATION_LOWEST");
			if (value != null && value.length() > 0) {
				MODIFICATION_LOWEST = Double.parseDouble(value);
			} else {
				loaded = false;
			}
		} catch (Exception e) {
			Logger.verbose("Property " + "" + " not loaded due to: " + e.getMessage());
			loaded = false;
		}
		return loaded;
	}

	/**
	 * Default constructor with a given priority and the collection of
	 * Processable that generates this Action.
	 * 
	 * @param id
	 *            Id of the action, cannot be null.
	 * @param type
	 *            Type of the action, cannot be null.
	 * @param generatedBy
	 *            The collection of Processable that generates this Action.
	 * @param priority
	 *            Priority of the action.
	 */
	public AbstractAction(Id id, Type type, Collection<Processable> generatedBy, Priority priority) {
		this.id = id;
		if (this.id == null) {
			throw new IllegalArgumentException("The id can not be null.");
		}
		this.type = type;
		if (this.type == null) {
			throw new IllegalArgumentException("The type can not be null.");
		}
		this.executionTime = null;
		this.priority = priority;
		if (this.priority == null) {
			this.priority = Priority.MED;
		}
		this.activation = new Activation(Activation.MIN);
		this.generatedBy = generatedBy;
		if (this.generatedBy == null) {
			this.generatedBy = new ArrayList<Processable>();
		}
		this.creationTime = new Time(Core.instance().getCurrentTick().getTick(), System.currentTimeMillis());
	}

	/**
	 * Default constructor with a given priority
	 * 
	 * @param id
	 *            Id of the action, cannot be null.
	 * @param type
	 *            Type of the action, cannot be null.
	 * @param priority
	 *            Priority of the action.
	 */
	public AbstractAction(Id id, Type type, Priority priority) {
		this(id, type, null, priority);
	}

	/**
	 * Default constructor with the collection of Processable that generates
	 * this Action.
	 * 
	 * @param id
	 *            Id of the action, cannot be null.
	 * @param type
	 *            Type of the action, cannot be null.
	 * @param generatedBy
	 *            The collection of Processable that generates this Action.
	 */
	public AbstractAction(Id id, Type type, Collection<Processable> generatedBy) {
		this(id, type, generatedBy, Action.Priority.MED);
	}

	/**
	 * Default constructor.
	 * 
	 * @param id
	 *            Id of the action, cannot be null.
	 * @param type
	 *            Type of the action, cannot be null.
	 */
	public AbstractAction(Id id, Type type) {
		this(id, type, null, Action.Priority.MED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.data.Action#getActionType()
	 */
	@Override
	public final Type getActionType() {
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
	 * @see opencranium.command.Action#getExecutionTime()
	 */
	@Override
	public final Time getExecutionTime() {
		return this.executionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.command.Action#getExpectations()
	 */
	@Override
	public Collection<Percept> getExpectations() {
		return new ArrayList<Percept>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.command.Action#getPriority()
	 */
	@Override
	public final Priority getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.command.Action#isAnUpdatedAction(opencranium.command.Action)
	 */
	@Override
	public boolean isAnUpdatedAction(Action action) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * opencranium.command.Action#priorityCompareTo(opencranium.command.Action)
	 */
	@Override
	public final int priorityCompareTo(Action action) {
		return this.priority.compareTo(action.getPriority());
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
	 * @see opencranium.cranium.Processable#getCreationTime()
	 */
	@Override
	public final Time getCreationTime() {
		return this.creationTime;
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
	 * @see opencranium.cranium.Processable#isAction()
	 */
	@Override
	public final boolean isAction() {
		return true;
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
		if (processable.isAction()) {
			return this.isAnUpdatedAction((Action) processable);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.command.Action#setExecutionTime(opencranium.util.Time)
	 */
	@Override
	public final void setExecutionTime(Time time) {
		this.executionTime = time;
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
	 * @see opencranium.util.collection.SortedElement#getSortingValue()
	 */
	@Override
	public final int getSortingValue() {
		double modification = 1;
		switch (this.priority) {
		case HIGHEST:
			modification = MODIFICATION_HIGHEST;
			break;
		case HIGH:
			modification = MODIFICATION_HIGH;
			break;
		case MED:
			modification = MODIFICATION_MED;
			break;
		case LOW:
			modification = MODIFICATION_LOW;
			break;
		case LOWEST:
			modification = MODIFICATION_LOWEST;
			break;
		default:
			throw new RuntimeException();
		}
		Activation a = new Activation((int) (this.activation.getValue() * modification));
		return a.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isComplexAction()
	 */
	@Override
	public final boolean isComplexAction() {
		return this.getActionType() == Action.Type.COMPLEX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isComplexPercept()
	 */
	@Override
	public final boolean isComplexPercept() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isMissionPercept()
	 */
	@Override
	public final boolean isMissionPercept() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isPercept()
	 */
	@Override
	public final boolean isPercept() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isSimpleAction()
	 */
	@Override
	public final boolean isSimpleAction() {
		return this.getActionType() == Action.Type.SIMPLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opencranium.cranium.Processable#isSinglePercept()
	 */
	@Override
	public final boolean isSinglePercept() {
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId().getName()).append(" [").append(this.getId().getId()).append(']');
		sb.append(" (").append(this.getActionType().toString()).append(')');
		sb.append(" a: ").append(this.getActivation().getValue());
		sb.append(" p: ").append(this.getPriority().name());
		sb.append(" ct: ").append(this.getCreationTime().toString());
		if (this.getExecutionTime() != null) {
			sb.append(" et: ").append(this.getExecutionTime().toString());
		} else {
			sb.append(" et: never ");
		}
		sb.append(" expectations: ( ");
		for (Percept percept : this.getExpectations()) {
			sb.append(percept.getId().getName()).append(' ');
		}
		sb.append(')');
		return sb.toString();
	}
}