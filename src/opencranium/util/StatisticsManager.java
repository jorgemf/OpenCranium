package opencranium.util;

/**
 * 
 * Statistics Manager, used to enable and disable the statistics.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 * 
 */
public class StatisticsManager {

	/**
	 * Boolean to know if the statistics are enabled o disabled.
	 */
	private static boolean recordStatistics = false;

	/**
	 * Enable the statistics
	 */
	public static void enableStatistics() {
		recordStatistics = true;
	}

	/**
	 * Disable the statistics
	 */
	public static void disableStatistics() {
		recordStatistics = false;
	}

	/**
	 * Returns if the statistics should be recorded.
	 * 
	 * @return whether the statistics should be recorded.
	 */
	public static boolean isRecording() {
		return recordStatistics;
	}

}