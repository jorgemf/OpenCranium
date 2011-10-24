package test.opencranium.cranium;

import java.util.TreeSet;

import opencranium.command.Action.Priority;
import opencranium.cranium.Activation;
import opencranium.cranium.Processable;
import opencranium.cranium.WorkspaceProcessor;
import opencranium.data.Percept.Nature;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyWorkspaceProcessor2 extends WorkspaceProcessor {

	public static final Id ID = IdManager.instance().getId("EmptyWorkspaceProcessor2", WorkspaceProcessor.class);

	public int count;

	public EmptyWorkspaceProcessor2() {
		super(ID);
		TreeSet<Id> inputSet = new TreeSet<Id>();
		inputSet.add(EmptyPercept.ID);
		this.setInputTypes(inputSet);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyAction.ID);
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);
		count = 0;
	}

	@Override
	public void cleanMemory() {
	}

	@Override
	public Processable[] execute(Processable processable, long milliseconds) {
		if (processable.getId() != EmptyPercept.ID) {
			throw new RuntimeException();
		}
		Processable[] p = new Processable[1];
		switch (count) {
		case 0:
			p = null;
			break;
		case 6:
		case 7:
		case 1:
			p[0] = processable;
			break;
		case 2:
			p[0] = new EmptyAction(Priority.HIGH);
			break;
		case 3:
			p[0] = new EmptyProcessable(1, 1);
			break;
		default:
		}
		count++;
		return p;
	}

	@Override
	public Processable[] getSortTermMemory() {
		if (count < 6) {
			return null;
		} else if (count < 7) {
			return new Processable[1];
		} else if (count < 8) {
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
	protected void manageResult(Processable result) {
	}

}
