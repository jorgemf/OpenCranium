package opencranium.cranium;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import opencranium.util.Id;
import opencranium.util.Time;
import opencranium.util.collection.SortedElement;

/**
 * This interface represents the common methods of all elements that are
 * processable by the cranium workspace.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public interface Processable extends SortedElement {

	/**
	 * Returns the processable elements that generated this elements, an empty
	 * list if it was not generated in a WorkspaceProcessr.
	 * 
	 * @return the processable elements that generated this elements.
	 */
	public Collection<Processable> getGeneratedBy();

	/**
	 * Returns the Id of this processable element.
	 * 
	 * @return The Id.
	 */
	public Id getId();

	/**
	 * Returns the activation value of the processable element.
	 * 
	 * @return the activation value.
	 */
	public Activation getActivation();

	/**
	 * Sets the activation value of this processable element.
	 * 
	 * @param activation
	 *            the activation value of this processable element.
	 */
	public void setActivation(Activation activation);

	/**
	 * Returns the time when this processable element was created.
	 * 
	 * @return the time when this element was created.
	 */
	public Time getCreationTime();

	/**
	 * Returns whether this element is a percept or not..
	 * 
	 * @return True if it is a percept.
	 */
	public boolean isPercept();

	/**
	 * Returns whether this element is an action or not..
	 * 
	 * @return True if it is an action.
	 */
	public boolean isAction();

	/**
	 * Returns whether this element is a single percept or not..
	 * 
	 * @return True if it is a single percept.
	 */
	public boolean isSinglePercept();

	/**
	 * Returns whether this element is a complex percept or not..
	 * 
	 * @return True if it is a complex percept.
	 */
	public boolean isComplexPercept();

	/**
	 * Returns whether this element is a mission percept or not..
	 * 
	 * @return True if it is a mission percept.
	 */
	public boolean isMissionPercept();

	/**
	 * Returns whether this element is a simple action or not.
	 * 
	 * @return True if it is a simple action.
	 */
	public boolean isSimpleAction();

	/**
	 * Returns whether this element is a complex action or not.
	 * 
	 * @return True if it is a complex action.
	 */
	public boolean isComplexAction();

	/**
	 * Checks if the other processable is an updated version of this processable
	 * element. Both processable have the same purpose or are incompatibles but
	 * the other is more recent.
	 * 
	 * @param processable
	 *            The other processable element.
	 * @return true if the other processable element is an updated version of
	 *         this one.
	 */
	public boolean isAnUpdatedProcessable(Processable processable);

	/**
	 * Returns a set of a pairs String-Float that represents the processable
	 * element. The pair can contain only the String when it is a symbolic
	 * information o a float if it is subsymbolic information.
	 * 
	 * @return a set of a pairs String-Float that represents the processable
	 *         element.
	 */
	public Set<Entry<String, Float>> getDataRepresentation();

	/**
	 * Sets the processable elements that generate this processable element.
	 * 
	 * @param generatedBy
	 *            Collection of processable elements that generate this
	 *            processable element.
	 */
	public void setGeneratedBy(Collection<Processable> generatedBy);

}