package common;

/**
 * The IServerSideFunction interface defines a protocol for handling client-side
 * actions that require server communication. Each client-side action must have
 * a corresponding server-side function that is mapped to it in a static
 * hash-map. The {@link #handleMessage(SCCP)} method takes in an SCCP message
 * and returns an SCCP message as the response.
 * 
 * @author Rotem
 */

public interface IServerSideFunction {
	SCCP handleMessage(SCCP message);
}
