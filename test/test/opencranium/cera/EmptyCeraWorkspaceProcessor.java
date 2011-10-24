package test.opencranium.cera;

import java.util.TreeSet;

import opencranium.cera.CeraWorkspaceProcessor;
import opencranium.cera.Layer;
import opencranium.command.Action;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.data.Percept;
import opencranium.data.Percept.Nature;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyCeraWorkspaceProcessor extends CeraWorkspaceProcessor {

	public static final Id ID = IdManager.instance().getId("EmptyCeraWorkspaceProcessor",
			EmptyCeraWorkspaceProcessor.class);

	public Processable lastProcessable;

	public Processable lastPercept;
	public Processable lastAction;

	private int count;

	public EmptyCeraWorkspaceProcessor() {
		super(ID);
		TreeSet<Id> inputSet = new TreeSet<Id>();
		inputSet.add(EmptyPercept.ID);
		this.setInputTypes(inputSet);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);

		this.setLayer(new EmptyLayer());
	}

	public EmptyCeraWorkspaceProcessor(Id id) {
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
		lastProcessable = new EmptyPercept();
		lastPercept = percept;
		this.count++;
		return new Processable[] { lastProcessable };
	}

	@Override
	protected Processable[] execute(Action action, long milliseconds) {
		lastProcessable = new EmptyPercept();
		lastAction = action;
		return new Processable[] { lastProcessable };
	}

	@Override
	public void cleanMemory() {
	}

	@Override
	public Processable[] getSortTermMemory() {
		if (count == 1) {
			return null;
		} else if (count == 2) {
			Processable[] p = new Processable[1];
			EmptyPercept ep = new EmptyPercept();
			ep.setNature(Nature.INVENTED);
			p[0] = ep;
			return p;
		} else {
			Processable[] p = new Processable[3];
			EmptyPercept ep = new EmptyPercept();
			ep.setNature(Nature.INVENTED);
			ep.setActivation(new Activation(Activation.MIN + 1));
			p[0] = ep;
			ep = new EmptyPercept();
			ep.setNature(Nature.INVENTED);
			ep.setActivation(new Activation(Activation.MIN + 1));
			p[1] = ep;
			ep = new EmptyPercept();
			ep.setNature(Nature.REAL);
			ep.setActivation(new Activation(Activation.MIN + 5));
			p[2] = ep;
			return p;
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.lastAction = null;
		this.lastPercept = null;
	}

}
