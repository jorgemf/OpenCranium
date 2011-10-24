package test.opencranium.cranium;

import opencranium.cranium.Processor;
import opencranium.cranium.ProcessorThreadPool;

public class ProcessorThreadPoolT extends ProcessorThreadPool {

	public Processor getNextProcessor() {
		return super.getNextProcessor();
	}

	public void executed(Processor p) {
		super.executed(p);
	}
}
