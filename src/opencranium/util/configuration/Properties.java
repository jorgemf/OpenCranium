package opencranium.util.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;

import opencranium.util.log.Logger;

/**
 * A singleton class for using properties. It can read and store properties in
 * files. The class is not thread safe. In order to avoid overwrite properties
 * with the same name but used in different classes use the notation
 * pacakage.class.propertyname for the properties' names.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Properties {

	/**
	 * Main Properties class of the program.
	 */
	private static Properties instance;

	/**
	 * The java properties class with all the properties.
	 */
	private java.util.Properties properties;

	/**
	 * Default constructor. Only one instance of this class is allowed.
	 */
	private Properties() {
		this.properties = new java.util.Properties();
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @return The properties object.
	 */
	public static Properties instance() {
		if (instance == null) {
			instance = new Properties();
		}
		return instance;
	}

	/**
	 * Load a properties file. If the property exists it is overwritten. A log
	 * record is stored if the properties are not loaded correctly.
	 * 
	 * @param file
	 *            The file name
	 * @return True if the properties were loaded correctly, false otherwise.
	 */
	public boolean load(File file) {
		boolean loaded = false;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			this.properties.load(in);
			loaded = true;
		} catch (Exception exception) {
			Logger.warning("Properties.load(File)", "properties not loaded from file: " + file);
			Logger.exception(exception);
		} finally {
			try {
				in.close();
			} catch (Exception exception) {
			}
		}
		return loaded;
	}

	/**
	 * Save all the properties into a file. A log record is stored if the
	 * properties are not saved correctly.
	 * 
	 * @param file
	 *            The file name
	 * @return True if the properties were stored correctly, false otherwise.
	 */
	public boolean save(File file) {
		boolean saved = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			this.properties.store(out, null);
			saved = true;
		} catch (Exception exception) {
			Logger.warning("Properties.save(File)", "properties not saved into a file: " + file);
			Logger.exception(exception);
		} finally {
			try {
				out.close();
			} catch (Exception exception) {
			}
		}
		return saved;
	}

	/**
	 * Set a property. If the property exists it is over written. If the
	 * property name or the value are null the property is not set.
	 * 
	 * @param property
	 * @param value
	 */
	public void set(String property, String value) {
		if (property != null && value != null) {
			this.properties.put(property, value);
		} else {
			Logger.verbose("Properties.set(String,String)",
					"error setting a property, the name or the value are null: " + property + '=' + value);
		}
	}

	/**
	 * Set a integer property.
	 * 
	 * @see #set(String, String)
	 * @param property
	 * @param value
	 */
	public void set(String property, int value) {
		this.set(property, Integer.toString(value));
	}

	/**
	 * Set a long property.
	 * 
	 * @see #set(String, String)
	 * @param property
	 * @param value
	 */
	public void set(String property, long value) {
		this.set(property, Long.toString(value));
	}

	/**
	 * Set a float property.
	 * 
	 * @see #set(String, String)
	 * @param property
	 * @param value
	 */
	public void set(String property, float value) {
		this.set(property, Float.toString(value));
	}

	/**
	 * Set a double property.
	 * 
	 * @see #set(String, String)
	 * @param property
	 * @param value
	 */
	public void set(String property, double value) {
		this.set(property, Double.toString(value));
	}

	/**
	 * Set a boolean property.
	 * 
	 * @see #set(String, String)
	 * @param property
	 * @param value
	 */
	public void set(String property, boolean value) {
		this.set(property, Boolean.toString(value));
	}

	/**
	 * Returns the string value for a given property or null if the property is
	 * null or it does not exists.
	 * 
	 * @param property
	 * @return The String value of the property, null if the property is null or
	 *         it does not exists.
	 */
	public String value(String property) {
		return this.properties.getProperty(property);
	}

	/**
	 * Checks whether a property exists or not.
	 * 
	 * @param property
	 * @return True if exists, false otherwise
	 */
	public boolean exists(String property) {
		return this.properties.containsKey(property);
	}

	/**
	 * Returns an int value given a property. If the property is not an int
	 * value it returns 0 and a message in the logger is recorded. Use this
	 * method only when you are completely sure the property is an int value.
	 * 
	 * @param property
	 *            The property name.
	 * @return An int value.
	 */
	public int intValue(String property) {
		String sValue = this.value(property);
		int value = 0;
		try {
			value = Integer.parseInt(sValue);
		} catch (NumberFormatException exception) {
			Logger.warning("Properties.intValue()", "Problem parsing the property.");
			Logger.exception(exception);
		}
		return value;
	}

	/**
	 * Returns a long value given a property. If the property is not a long
	 * value it returns 0 and a message in the logger is recorded. Use this
	 * method only when you are completely sure the property is a long value.
	 * 
	 * @param property
	 *            The property name.
	 * @return A long value.
	 */
	public long longValue(String property) {
		String sValue = this.value(property);
		long value = 0;
		try {
			value = Long.parseLong(sValue);
		} catch (NumberFormatException exception) {
			Logger.warning("Properties.longValue()", "Problem parsing the property.");
			Logger.exception(exception);
		}
		return value;
	}

	/**
	 * Returns a float value given a property. If the property is not a float
	 * value it returns 0 and a message in the logger is recorded. Use this
	 * method only when you are completely sure the property is a float value.
	 * 
	 * @param property
	 *            The property name.
	 * @return A float value.
	 */
	public float floatValue(String property) {
		String sValue = this.value(property);
		float value = 0;
		try {
			value = Float.parseFloat(sValue);
		} catch (NumberFormatException exception) {
			Logger.warning("Properties.floatValue()", "Problem parsing the property.");
			Logger.exception(exception);
		}
		return value;
	}

	/**
	 * Returns a double value given a property. If the property is not a double
	 * value it returns 0 and a message in the logger is recorded. Use this
	 * method only when you are completely sure the property is a double value.
	 * 
	 * @param property
	 *            The property name.
	 * @return A double value.
	 */
	public double doubleValue(String property) {
		String sValue = this.value(property);
		double value = 0;
		try {
			value = Double.parseDouble(sValue);
		} catch (NumberFormatException exception) {
			Logger.warning("Properties.doubleValue()", "Problem parsing the property.");
			Logger.exception(exception);
		}
		return value;
	}

	/**
	 * Returns a boolean value given a property. If the property is not a
	 * boolean equals to the string true it returns FALSE. Use this method only
	 * when you are completely sure the property is a boolean value.
	 * 
	 * @param property
	 *            The property name.
	 * @return A boolean value.
	 */
	public boolean booleanValue(String property) {
		return Boolean.parseBoolean(this.value(property));
	}

	/**
	 * Removes a property from the properties set if it exists.
	 * 
	 * @param property
	 *            The property name
	 */
	public void remove(String property) {
		this.properties.remove(property);
	}

	/**
	 * Returns a set with the properties' names.
	 * 
	 * @return a set with the properties' names.
	 */
	public Set<String> getPropertiesNames() {
		return this.properties.stringPropertyNames();
	}

}