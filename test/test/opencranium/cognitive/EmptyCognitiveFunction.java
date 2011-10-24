package test.opencranium.cognitive;

import opencranium.cognitive.CognitiveFunction;
import opencranium.cranium.Processable;
import opencranium.util.Id;
import opencranium.util.IdManager;

public class EmptyCognitiveFunction extends CognitiveFunction {

	public static final Id ID = IdManager.instance().getId("EmptyCognitiveFunction", EmptyCognitiveFunction.class);

	public static long sleep = 10;

	public Processable lastExplicit;

	public Processable lastImplicit;

	public EmptyCognitiveFunction() {
		super(ID);
	}

	public EmptyCognitiveFunction(Id id) {
		super(id);
	}

	@Override
	public void explicitProcess(Processable element, long milliseconds) {
		lastExplicit = element;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void implicitProcess(Processable element) {
		lastImplicit = element;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void reset() {
		super.reset();
		this.lastExplicit = null;
		this.lastImplicit = null;
	}

}
