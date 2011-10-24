package test.opencranium.command;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import opencranium.command.AbstractAction;
import opencranium.command.Action;
import opencranium.util.Id;
import opencranium.util.IdManager;

/**
 * @author Jorge Muñoz
 */
public class EmptyAction extends AbstractAction {

	public final static Id ID = IdManager.instance().getId("EmptyAction", EmptyAction.class);

	public static Type TYPE = Action.Type.SIMPLE;

	public EmptyAction(Priority priority) {
		super(ID, TYPE, priority);
	}

	@Override
	public Set<Entry<String, Float>> getDataRepresentation() {
		return new HashSet<Entry<String, Float>>();
	}

}