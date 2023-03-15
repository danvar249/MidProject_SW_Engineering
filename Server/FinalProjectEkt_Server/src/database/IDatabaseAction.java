package database;

/**
 * IDatabaseAction interface defines the contract for classes that handle
 * specific database actions. It has a single method, getDatabaseAction(), which
 * takes an array of objects as a parameter and returns an Object. The objects
 * passed as parameters and the returned object will vary depending on the
 * specific implementation of the interface.
 * 
 * @author Rotem
 *
 */
public interface IDatabaseAction {
	Object getDatabaseAction(Object[] params);
}
