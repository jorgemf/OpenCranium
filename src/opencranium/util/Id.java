package opencranium.util;

/**
 * A generic identifier called type to identify different elements.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Id extends StatisticsId implements Comparable<Id> {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 27694347410000001L;

	/**
	 * The id of the type.
	 */
	private int id;

	/**
	 * The name of the type.
	 */
	private String name;

	/**
	 * The class associated with this type
	 */
	private Class<?> processableClass;

	/**
	 * Default constructor, creates a class Id with the given information.
	 * 
	 * @param name
	 *            The name of the type.
	 * @param id
	 *            The long id of the type.
	 * @param processableClass
	 *            The processable class associated with this type.
	 * 
	 * @see IdManager#getId(String, Class)
	 */
	protected Id(String name, int id, Class<?> processableClass) {
		super();
		this.name = name;
		this.id = id;
		this.processableClass = processableClass;
	}

	/**
	 * @return the id of the type
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the name of the type
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the processableClass
	 */
	public Class<?> getProcessableClass() {
		return this.processableClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Long.toString(this.id) + '+' + this.processableClass.getName() + ':' + this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Id o) {
		return this.id - o.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Id) {
			return this.id == ((Id) obj).id;
		}
		return super.equals(obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.id;
	}

}