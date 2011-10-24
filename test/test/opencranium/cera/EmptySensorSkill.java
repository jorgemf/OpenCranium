package test.opencranium.cera;

import java.util.TreeSet;

import opencranium.cera.SensorSkill;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.IdManager;
import opencranium.util.Time;
import test.opencranium.data.EmptyPercept;

public class EmptySensorSkill extends SensorSkill {

	public final static Id ID = IdManager.instance().getId("EmptySensorSkill", EmptySensorSkill.class);

	public boolean senseUpdated;

	public Percept sense;

	public EmptySensorSkill() {
		super(ID);

		TreeSet<Id> outputSet = new TreeSet<Id>();
		outputSet.add(EmptyPercept.ID);
		this.setOutputTypes(outputSet);

		this.setLayer(new EmptyLayer());
	}

	@Override
	public boolean isSenseUpdated(Time tick) {
		return senseUpdated;
	}

	@Override
	protected Percept[] sense(Time tick) {
		senseUpdated = false;
		return new Percept[] { sense };
	}

	@Override
	public void cleanMemory() {
	}

}
