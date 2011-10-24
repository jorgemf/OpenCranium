package test.opencranium.cera;

import java.util.TreeSet;

import opencranium.cera.MotorSkill;
import opencranium.command.Action;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.IdManager;
import test.opencranium.command.EmptyAction;
import test.opencranium.data.EmptyPercept;

public class EmptyMotorSkill2 extends MotorSkill {

	public final static Id ID = IdManager.instance().getId("EmptyMotorSkill", EmptyMotorSkill2.class);

	public Action lastAction;

	public EmptyMotorSkill2(Id inputType) {
		super(ID, EmptyAction.ID);
		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);

		this.setLayer(null);
	}

	@Override
	public Percept[] executeAction(Action action) {
		this.lastAction = action;
		return new Percept[] {};
	}

	@Override
	public void cleanMemory() {
	}

}
