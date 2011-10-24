package opencranium.cera;

import opencranium.OpenCraniumException;

/**
 * An exception class for being used in the CERA architecture.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class CeraException extends OpenCraniumException {

	/**
	 * To serialize the class.
	 */
	private static final long serialVersionUID = 3446265334917582187L;

	/**
	 * Layer where the exception happens.
	 */
	private Layer layer;

	/**
	 * Processor where the exception happens.
	 */
	private CeraWorkspaceProcessor processor;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param layer
	 *            Layer where the exception happens.
	 */
	public CeraException(String message, Layer layer) {
		super(message, layer);
		this.layer = layer;
		this.processor = null;
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            Message for the exception.
	 * @param processor
	 *            Processor where the exception happens.
	 */
	public CeraException(String message, CeraWorkspaceProcessor processor) {
		super(message, processor);
		this.layer = processor.getLayer();
		this.processor = processor;
	}

	/**
	 * @return the layer
	 */
	public Layer getLayer() {
		return layer;
	}

	/**
	 * @return the processor
	 */
	public CeraWorkspaceProcessor getProcessor() {
		return processor;
	}

}