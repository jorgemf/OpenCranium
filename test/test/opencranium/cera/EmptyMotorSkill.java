package test.opencranium.cera;

import java.util.TreeSet;

import opencranium.cera.MotorSkill;
import opencranium.command.Action;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyMotorSkill extends MotorSkill {

	public final static Id ID = IdManager.instance().getId("EmptyMotorSkill", EmptyMotorSkill.class);

	public EmptyMotorSkill(Id inputType) {
		super(ID, EmptyAction.ID);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);

		this.setLayer(new EmptyLayer());
	}

	public EmptyMotorSkill() {
		this(ID);
		this.setLayer(null);
	}

	@Override
	public Percept[] executeAction(Action action) {
		return new Percept[] { new EmptyPercept() };
	}

	@Override
	public void cleanMemory() {
	}

}
