package opencranium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.management.OperationsException;

import opencranium.util.log.Logger;

/**
 * A manager to manage the types of the elements.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class IdManager {

	/**
	 * A data base indexed by the types' names and the class types with all the
	 * types.
	 */
	private HashMap<String, Id> idDBname;

	/**
	 * A data base indexed by the types' id with all the types.
	 */
	private HashSet<Id> idDBid;

	/**
	 * The last id assigned to a type.
	 */
	private int lastIdAssigned;

	/**
	 * The only instance of this class.
	 */
	private static IdManager idManager = new IdManager();

	/**
	 * Default constructor
	 */
	private IdManager() {
		this.idDBname = new HashMap<String, Id>();
		this.idDBid = new HashSet<Id>();
		this.lastIdAssigned = 0;
	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return the only instance of this class.
	 */
	public static IdManager instance() {
		return idManager;
	}

	/**
	 * Returns an id given a name and a class, if no id exits with this name a
	 * new one is created and returned.
	 * 
	 * @param name
	 *            The name of the processable element, cannot be null.
	 * @param processableClass
	 *            The processable class, cannont be null.
	 * @return An existing id or a new one created for the name and the class.
	 */
	public Id getId(String name, Class<?> processableClass) {
		if (name == null || processableClass == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("The name and the processable class cannot be null or without text.");
		}
		Id id = null;
		name = name.trim();
		String nameSearch = processableClass.getName() + ':' + name;
		if (!this.idDBname.containsKey(nameSearch)) {
			id = this.createId(name, processableClass);
			this.idDBid.add(id);
			this.idDBname.put(nameSearch, id);
		} else {
			id = this.idDBname.get(nameSearch);
		}
		return id;
	}

	/**
	 * A method to create an id and include it in the databases.
	 * 
	 * @param name
	 *            The name of the processable element.
	 * @param processableClass
	 *            The processable class.
	 * @return The new id.
	 */
	private Id createId(String name, Class<?> processableClass) {
		this.lastIdAssigned++;
		return new Id(name, this.lastIdAssigned, processableClass);
	}

	/**
	 * Returns a type given its id. If no id does not exist an
	 * IllegalArgumentExceptio is thrown.
	 * 
	 * @param id
	 *            The id of the type.
	 * @return The type or null if no type exists with that id.
	 * @deprecated Very slow method, should not be called never.
	 */
	@Deprecated
	public Id getId(int id) {
		Id identifier = null;
		Iterator<Id> iterator = this.idDBid.iterator();
		while (identifier == null && iterator.hasNext()) {
			Id iteratorId = iterator.next();
			if (iteratorId.getId() == id) {
				identifier = iteratorId;
			}
		}
		if (identifier == null) {
			throw new IllegalArgumentException("Identifier does not exist: " + id);
		}
		return identifier;
	}

	/**
	 * Save the ids into a file. A record in the log is stored if the ids are
	 * not correctly saved.
	 * 
	 * @param file
	 *            The file where the ids are saved.
	 * @return true if the types were saved, false otherwise.
	 */
	public boolean saveIds(File file) {
		boolean saved = false;
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			fos = new FileOutputStream(file);
			os = new ObjectOutputStream(fos);
			os.writeObject(this.idDBname);
			saved = true;
		} catch (IOException e) {
			Logger.warning("IdManager.saveIds(File)", "Ids not saved in file due an IOException");
			Logger.exception(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Logger.warning("IdManager.saveIds(File)", "File not closed when saving ids");
					Logger.exception(e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Logger.warning("IdManager.saveIds(File)", "File not closed when saving ids");
					Logger.exception(e);
				}
			}
		}
		return saved;
	}

	/**
	 * Load the ids from a file into the data base. A record in the log is
	 * stored if the ids are not correctly loaded.
	 * 
	 * @param file
	 *            The file that contains the ids.
	 * 
	 * @return true if the types were loaded correctly, false otherwise.
	 * @throws OperationsException
	 */
	@SuppressWarnings("unchecked")
	public boolean loadIds(File file) throws OperationsException {
		if (this.idDBid.size() > 0) {
			Logger.warning("TypeManager.loadTypes()", "The data base contains types and do not allow load new ones");
			throw new OperationsException("The data base contains types and do not allow load new ones");
		}
		boolean loaded = false;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			fis = new FileInputStream(file);
			is = new ObjectInputStream(fis);
			this.idDBname = (HashMap<String, Id>) is.readObject();
			loaded = true;
		} catch (IOException e) {
			Logger.warning("IdManager.loadIds(File)", "Ids not loaded in file due an IOException");
			Logger.exception(e);
		} catch (ClassCastException e) {
			Logger.warning("IdManager.loadIds(File)", "Ids not loaded in file due a ClassCastException");
			Logger.exception(e);
		} catch (ClassNotFoundException e) {
			Logger.warning("IdManager.loadIds(File)", "Ids not loaded in file due a ClassNotFoundException");
			Logger.exception(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Logger.warning("IdManager.loadIds(File)", "File not closed when saving ids");
					Logger.exception(e);
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					Logger.warning("IdManager.loadIds(File)", "File not closed when saving ids");
					Logger.exception(e);
				}
			}
		}
		this.idDBid.clear();
		for (Id id : this.idDBname.values()) {
			this.idDBid.add(id);
			if (id.getId() > this.lastIdAssigned) {
				this.lastIdAssigned = id.getId();
			}
		}
		return loaded;
	}

	/**
	 * WARNING this method is only used for testing purposes and exception is
	 * always thrown.
	 */
	public void clean() {
		this.idDBname = new HashMap<String, Id>();
		this.idDBid = new HashSet<Id>();
		this.lastIdAssigned = 0;
		throw new RuntimeException("This method should be never called but for testing");
	}

}