package logic;
/**
 * TODO:
 * Copy this to client's logic (or whatever)
 * 
 */


/**
 * To be used as a bridge between an object and the specific object you passed to the server.
 * For example we pass Products inside an array of objects, and we want the toString of it to be of the format (pid, pname, etc)
 * so we implement this one in every logic class, and replace the message handler expected input with something that implements this type.
 * 
 * implement as such:
 * class Item implements IObj...{
 * 		@Override
 * 	String sqlFormatString(){
 * 		return "('b', 1, 'y@')";
 * 	}
 * } 
 * 
 * You still have a lot of work to do if we want this to work, but I can't
 * 
 * @author Rotem
 *
 */

public interface IObjectInDatabase {
	String sqlFormatString();
}
