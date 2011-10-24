package opencranium;

/**
 * Interface for properties of cera-cranium.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public interface Property {

	/**
	 * Property name of number of threads of the thread pool.
	 */
	public static final String RUNTIME_THREADS = "cranium.threadpool.threads";

	/**
	 * Property name of whether the architecture use one pool of threads or one
	 * per layer.
	 */
	public static final String MULTI_POOL = "cranium.threadpool.multipool";

}
