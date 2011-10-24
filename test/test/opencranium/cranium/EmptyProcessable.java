package test.opencranium.cranium;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.util.Id;
import opencranium.util.IdManager;
import opencranium.util.Time;
import opencranium.util.collection.SortedElement;

/**
 * @author Jorge Muñoz
 */
public class EmptyProcessable implements Processable {

	private int value;
	public int id;
	public Id realid;

	public Activation activation;

	public EmptyProcessable(int value, int id) {
		this.realid = IdManager.instance().getId("EmptyProcessable", EmptyProcessable.class);
		this.id = id;
		this.value = value;
	}

	public EmptyProcessable(Id realid, int value, int id) {
		this.realid = realid;
		this.id = id;
		this.value = value;
	}

	@Override
	public boolean isAnUpdatedProcessable(Processable processable) {
		boolean is = false;
		if (processable instanceof EmptyProcessable) {
			EmptyProcessable o = (EmptyProcessable) processable;
			is = this.value == o.value;
		}
		return is;
	}

	@Override
	public Activation getActivation() {
		return this.activation;
	}

	@Override
	public Time getCreationTime() {
		return null;
	}

	@Override
	public Set<Entry<String, Float>> getDataRepresentation() {
		return null;
	}

	@Override
	public Collection<Processable> getGeneratedBy() {
		return null;
	}

	@Override
	public void setGeneratedBy(Collection<Processable> generatedBy) {
	}

	@Override
	public Id getId() {
		return this.realid;
	}

	@Override
	public boolean isAction() {
		return false;
	}

	@Override
	public boolean isComplexAction() {
		return false;
	}

	@Override
	public boolean isComplexPercept() {
		return false;
	}

	@Override
	public boolean isMissionPercept() {
		return false;
	}

	@Override
	public boolean isPercept() {
		return false;
	}

	@Override
	public boolean isSimpleAction() {
		return false;
	}

	@Override
	public boolean isSinglePercept() {
		return false;
	}

	@Override
	public void setActivation(Activation activation) {
		this.activation = activation;
	}

	@Override
	public int compareTo(SortedElement other) {
		return 0;
	}

	@Override
	public int getSortingValue() {
		return 0;
	}

	@Override
	public String toString() {
		return "[ID: " + this.realid + "] val: " + value + " id: " + this.id;
	}

}
