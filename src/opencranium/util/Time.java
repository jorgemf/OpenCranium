package opencranium.util;

/**
 * A class with time information, a tick and the elapsed time in milliseconds
 * since the counter started.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Time {

	/**
	 * The tick
	 */
	private int tick;

	/**
	 * The milliseconds elapsed since the time started.
	 */
	private long milliseconds;

	/**
	 * Default constructor
	 * 
	 * @param tick
	 *            The tick.
	 * @param milliseconds
	 *            The milliseconds since the beginning.
	 */
	public Time(int tick, long milliseconds) {
		this.tick = tick;
		this.milliseconds = milliseconds;
	}

	/**
	 * Default constructor. tick 0 and milliseconds the current
	 * System.milliseconds.
	 */
	public Time() {
		this.tick = 0;
		this.milliseconds = System.currentTimeMillis();
	}

	/**
	 * @return the tick
	 */
	public int getTick() {
		return this.tick;
	}

	/**
	 * @return the milliseconds
	 */
	public long getMilliseconds() {
		return this.milliseconds;
	}

	/**
	 * Updates this time with the information of other Time object.
	 * 
	 * @param newTime
	 *            The other Time object.
	 */
	public void update(Time newTime) {
		this.milliseconds = newTime.milliseconds;
		this.tick = newTime.tick;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Integer.toString(this.tick) + ' ' + Long.toString(this.milliseconds);
	}

}