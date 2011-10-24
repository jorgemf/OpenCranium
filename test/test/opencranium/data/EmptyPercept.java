package test.opencranium.data;

import opencranium.data.AbstractPercept;
import opencranium.data.Percept;
import opencranium.util.Id;
import opencranium.util.IdManager;

public class EmptyPercept extends AbstractPercept {

	public final static Id ID = IdManager.instance().getId("EmptyPercept", EmptyPercept.class);

	public static Type TYPE = Percept.Type.SINGLE_PERCEPT;

	public EmptyPercept() {
		super(ID, TYPE);
	}

}
