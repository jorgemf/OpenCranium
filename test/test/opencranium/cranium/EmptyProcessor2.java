package test.opencranium.cranium;

import opencranium.cranium.Processable;
import opencranium.cranium.Processor;
import opencranium.util.Id;
import opencranium.util.IdManager;

public class EmptyProcessor2 extends Processor {

	public static final Id ID = IdManager.instance().getId("EmptyProcessor2", EmptyProcessor2.class);

	private int timeSleep;

	public int processedSomething;
	public int processedNothing;

	public EmptyProcessor2(int timeSleep) {
		super(ID);
		this.timeSleep = timeSleep;
		this.processedSomething = 0;
		this.processedNothing = 0;
	}

	@Override
	protected void processNextElement(Processable element, long milliseconds) {
		try {
			Thread.sleep(timeSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.processedSomething++;
	}

	@Override
	protected void processNoElement(long milliseconds) {
		try {
			Thread.sleep(timeSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.processedNothing++;
	}

}
