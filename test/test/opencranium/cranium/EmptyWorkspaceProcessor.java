package test.opencranium.cranium;

import java.util.Set;
import java.util.TreeSet;

import opencranium.command.Action;
import opencranium.cranium.Processable;
import opencranium.cranium.ProcessorThreadPool;
import opencranium.cranium.Workspace;
import opencranium.cranium.WorkspaceProcessor;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyWorkspaceProcessor extends WorkspaceProcessor {

	public static final Id ID = IdManager.instance().getId("EmptyWorkspaceProcessor", WorkspaceProcessor.class);

	public int memory;

	public Processable lastResult;

	public Processable lastProcessable;

	public EmptyWorkspaceProcessor() {
		this(ID);
	}

	public EmptyWorkspaceProcessor(Id id) {
		super(id);
		Id idi1 = IdManager.instance().getId("Input id 1", WorkspaceProcessor.class);
		Id idi2 = IdManager.instance().getId("Input id 2", WorkspaceProcessor.class);
		Id idi3 = IdManager.instance().getId("Input id 3", WorkspaceProcessor.class);
		TreeSet<Id> inputSet = new TreeSet<Id>();
		inputSet.add(idi1);
		inputSet.add(idi2);
		inputSet.add(idi3);
		inputSet.add(EmptyPercept.ID);
		inputSet.add(EmptyAction.ID);
		this.setInputTypes(inputSet);
		Id ido1 = IdManager.instance().getId("Output id 1", WorkspaceProcessor.class);
		Id ido2 = IdManager.instance().getId("Output id 2", WorkspaceProcessor.class);
		Id ido3 = IdManager.instance().getId("Output id 3", WorkspaceProcessor.class);
		Id ido4 = IdManager.instance().getId("Output id 4", WorkspaceProcessor.class);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(ido1);
		outputSet.add(ido2);
		outputSet.add(ido3);
		outputSet.add(ido4);
		outputSet.add(EmptyAction.ID);
		this.setOutputTypes(outputSet);
		this.setWorkspace(new Workspace(new ProcessorThreadPool()));
	}

	public void emptyOutputTypes() {
		this.setOutputTypes(new TreeSet<Id>());
	}

	public void setInputId(Set<Id> inputset) {
		this.setInputTypes(inputset);
	}

	@Override
	public void cleanMemory() {
		this.memory = 0;
	}

	@Override
	public Processable[] execute(Processable processable, long milliseconds) {
		this.memory = 1;
		this.lastProcessable = processable;
		Processable[] p = new Processable[1];
		p[0] = new EmptyAction(Action.Priority.MED);
		return p;
	}

	@Override
	public Processable[] getSortTermMemory() {
		return null;
	}

	@Override
	protected void manageResult(Processable result) {
		this.lastResult = result;
	}

	public String toString() {
		return getId().toString();
	}

}
