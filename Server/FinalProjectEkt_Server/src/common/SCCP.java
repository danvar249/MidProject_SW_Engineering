package common;

import java.io.Serializable;

// BOTH server and client will have an object of this type, which will be used to pass messages!
/**
 * The SCCP class is a part of the Server-Client Communication Protocol (SCCP)
 * used to pass messages between a server and a client. It is used in
 * conjunction with an enum, {@link ServerClientRequestTypes}, to specify the
 * type of message being sent and to determine the necessary action to be taken
 * by the recipient. The class implements {@link java.io.Serializable} interface
 * and it's fields are requestType and messageSent. The SCCP object can be sent
 * from the client to the server and vice versa using the OCS framework, and the
 * recipient will perform the necessary action and respond (if needed)
 * 
 * @author Rotem
 */

public class SCCP implements Serializable {
	/**
	 * default f
	 */
	private static final long serialVersionUID = 1L;
	private ServerClientRequestTypes requestType;
	private Object messageSent;

	public SCCP() {

	}

	/**
	 * The SCCP class constructor, which creates an instance of SCCP with the given
	 * requestType and messageSent.
	 * 
	 * @param requestType an enumeration of type {@link ServerClientRequestTypes}
	 *                    which represents the type of message being sent.
	 * @param messageSent an Object which represents the message being sent.
	 */
	public SCCP(ServerClientRequestTypes requestType, Object messageSent) {
		this.requestType = requestType;
		this.messageSent = messageSent;
	}

	/**
	 * Gets the request type of the SCCP object.
	 * 
	 * @return an enumeration of type {@link ServerClientRequestTypes} which
	 *         represents the type of message being sent.
	 */
	public ServerClientRequestTypes getRequestType() {
		return requestType;
	}

	/**
	 * Sets the request type of the SCCP object.
	 * 
	 * @param requestType an enumeration of type {@link ServerClientRequestTypes}
	 *                    which represents the type of message being sent.
	 */
	public void setRequestType(ServerClientRequestTypes requestType) {
		this.requestType = requestType;
	}

	/**
	 * Gets the message sent in the SCCP object.
	 * 
	 * @return an Object which represents the message being sent.
	 */
	public Object getMessageSent() {
		return messageSent;
	}

	/**
	 * Sets the message sent in the SCCP object.
	 * 
	 * @param messageSent an Object which represents the message being sent.
	 */
	public void setMessageSent(Object messageSent) {
		this.messageSent = messageSent;
	}

	/**
	 * Returns a string representation of the SCCP object, which includes the
	 * request type and message sent.
	 * 
	 * @return a string representation of the SCCP object.
	 */
	@Override
	public String toString() {
		return requestType.name() + ", " + messageSent.toString();
	}

}
