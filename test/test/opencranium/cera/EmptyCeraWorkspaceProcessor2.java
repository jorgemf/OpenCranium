package test.opencranium.cera;

import java.util.TreeSet;

import opencranium.cera.CeraWorkspaceProcessor;
import opencranium.cera.Layer;
import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyCeraWorkspaceProcessor2 extends CeraWorkspaceProcessor {

	public static final Id ID = IdManager.instance().getId("EmptyCeraWorkspaceProcessor",
			EmptyCeraWorkspaceProcessor2.class);

	public Processable lastPercept;
	public Processable lastAction;

	public EmptyCeraWorkspaceProcessor2() {
		this(ID);
	}

	public EmptyCeraWorkspaceProcessor2(Id id) {
		super(id);
		TreeSet<Id> inputSet = new TreeSet<Id>();
		inputSet.add(EmptyPercept.ID);
		inputSet.add(EmptyAction.ID);
		this.setInputTypes(inputSet);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);
		this.setLayer(null);
	}

	public void nullLayer() {
		this.setLayer(null);
	}

	public void setLayer(Layer layer) {
		super.setLayer(layer);
	}

	@Override
	protected Processable[] execute(Percept percept, long milliseconds) {
		lastPercept = percept;
		return new Processable[] {};
	}

	@Override
	protected Processable[] execute(Action action, long milliseconds) {
		lastAction = action;
		return new Processable[] {};
	}

	@Override
	public void cleanMemory() {
	}

	@Override
	public Processable[] getSortTermMemory() {
		return null;
	}

	@Override
	public void reset() {
		super.reset();
		this.lastAction = null;
		this.lastPercept = null;
	}

}
