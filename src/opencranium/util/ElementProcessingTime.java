package opencranium.util;

/**
 * This class stores statistics information about the processing time of
 * elements.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class ElementProcessingTime {

	/**
	 * The id of the processing element.
	 */
	private Id id;

	/**
	 * Accumulate nanoseconds processing time.
	 */
	private long processingNanoTime;

	/**
	 * Number of times processed.
	 */
	private int processedTimes;

	/**
	 * Number of times the element was paused.
	 */
	private int pausedTimes;

	/**
	 * Number of game ticks the element was processed.
	 */
	private int gameTicks;

	/**
	 * Last game tick the element was processed.
	 */
	private Time lastGameTick;

	/**
	 * Number of times this element created the processed element.
	 */
	private int createdTimes;

	/**
	 * Number of times this element discarded the processed element.
	 */
	private int discardedTimes;

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            The id of the element.
	 */
	public ElementProcessingTime(Id id) {
		this.id = id;
		this.processingNanoTime = 0;
		this.processedTimes = 0;
		this.pausedTimes = 0;
		this.gameTicks = 0;
		this.lastGameTick = new Time(0, 0);
		this.createdTimes = 0;
		this.discardedTimes = 0;
	}

	/**
	 * @return the id.
	 */
	public Id getId() {
		return this.id;
	}

	/**
	 * @return the processingNanoTime
	 */
	public long getProcessingNanoTime() {
		return this.processingNanoTime;
	}

	/**
	 * @return the processedTimes
	 */
	public int getProcessedTimes() {
		return this.processedTimes;
	}

	/**
	 * @return the pausedTimes
	 */
	public int getPausedTimes() {
		return this.pausedTimes;
	}

	/**
	 * @return the gameTicks
	 */
	public int getGameTicks() {
		return this.gameTicks;
	}

	/**
	 * @return the lastGameTick
	 */
	public Time getLastGameTick() {
		return this.lastGameTick;
	}

	/**
	 * @return The average processing time in nanoseconds of the element. The
	 *         total time executing divided by the times the element was
	 *         processed.
	 */
	public long getAverageProcessingTime() {
		if (this.processedTimes == 0) {
			return 0;
		}
		return this.processingNanoTime / this.processedTimes;

	}

	/**
	 * @return The average processing time in nanoseconds of the element
	 *         including the times the element was paused. The total time
	 *         executing divided by the times the element was processed plus the
	 *         times the element was paused while it was processing.
	 */
	public long getAverageProcessingTimeWithPauses() {
		if ((this.processedTimes + this.pausedTimes) == 0) {
			return 0;
		}
		return this.processingNanoTime / (this.processedTimes + this.pausedTimes);
	}

	/**
	 * Adds processing time and the times the element is processed to this
	 * element and count the times the game tick changes.
	 * 
	 * @param nanoTime
	 *            Nanoseconds of processing.
	 * @param currentTick
	 *            The current tick of the execution cycle.
	 */
	public void addProcessingTime(long nanoTime, Time currentTick) {
		this.processingNanoTime += nanoTime;
		this.processedTimes++;
		if (currentTick != null && this.lastGameTick.getTick() != currentTick.getTick()) {
			this.lastGameTick.update(currentTick);
			this.gameTicks++;
		}
	}

	/**
	 * Adds processing time and increases the times the element was processed.
	 * 
	 * @param nanoTime
	 *            The nanoseconds to add.
	 */
	public void addProcessingTime(long nanoTime) {
		this.processingNanoTime += nanoTime;
		this.processedTimes++;
	}

	/**
	 * Increases the number of times the element processed is paused.
	 */
	public void paused() {
		this.pausedTimes++;
	}

	/**
	 * @return The average processing time in nanoseconds of the element by game
	 *         tick.
	 */
	public long getProcessedTimesPerGameTick() {
		if (this.gameTicks == 0) {
			return 0;
		}
		return this.processingNanoTime / this.gameTicks;
	}

	/**
	 * @return the number of times the processing element was created.
	 */
	public int getCreatedTimes() {
		return this.createdTimes;
	}

	/**
	 * @return the number of times the processing element was discarded.
	 */
	public int getDiscardedTimes() {
		return this.discardedTimes;
	}

	/**
	 * Increases in one unit the times the element creates the processing unit.
	 */
	public void created() {
		this.createdTimes++;
	}

	/**
	 * Increases in one unit the times the element discarded the processing
	 * unit.
	 */
	public void discarted() {
		this.discardedTimes++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name:  ").append(this.id.toString()).append('\n');
		sb.append("Created times:   ").append(this.createdTimes).append('\n');
		sb.append("Discarted times: ").append(this.discardedTimes).append('\n');
		sb.append("Paused times:    ").append(this.pausedTimes).append('\n');
		sb.append("Processed times: ").append(this.processedTimes).append('\n');
		sb.append("Processing time: ").append(this.processingNanoTime).append('\n');
		sb.append("Game ticks:      ").append(this.gameTicks).append('\n');
		sb.append("Average processing time: ").append(this.getAverageProcessingTime()).append('\n');
		sb.append("Average processing time (with pauses): ").append(this.getAverageProcessingTimeWithPauses())
				.append('\n');
		return sb.toString();
	}

}