package test.opencranium.cranium;

import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.util.Id;
import opencranium.util.IdManager;

public class EmptyProcessor extends Processor {

	public static final Id ID = IdManager.instance().getId("EmptyProcessor", EmptyProcessor.class);

	public boolean processedSomething;

	public boolean processedNothing;

	public Processable lastProcessable;

	public EmptyProcessor() {
		super(ID);
		this.processedSomething = false;
		this.processedNothing = false;
	}

	public EmptyProcessor(Id id) {
		super(id);
		this.processedSomething = false;
		this.processedNothing = false;
	}

	@Override
	public void processNextElement(Processable element, long milliseconds) {
		this.processedSomething = true;
		try {
			this.lastProcessable = element;
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processNoElement(long milliseconds) {
		this.processedNothing = true;
		this.lastProcessable = null;
	}

}
