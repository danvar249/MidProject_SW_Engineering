package logic;

import java.io.Serializable;

/**
 * The class represents a customer order made by a specific customer, with order
 * ID, machine ID and billing date. Implements Serializable interface.
 * 
 * @author Maxim, Dima
 *
 */
public class CustomerOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;

	private Integer customerId;
	private Integer orderId;
	private Integer machineId;
	private String billingDate;

	/**
	 * Constructor for the CustomerOrder class.
	 * 
	 * @param custoemrId  The ID of the customer placing the order.
	 * @param orderId     The ID of the order being placed.
	 * @param machineId   The ID of the machine involved in the order.
	 * @param billingDate The date of the billing for the order.
	 */
	public CustomerOrder(Integer custoemrId, Integer orderId, Integer machineId, String billingDate) {
		this.setCustomerId(custoemrId);
		this.setOrderId(orderId);
		this.setMachineId(machineId);
		this.setBillingDate(billingDate);
	}

	/**
	 * Getter method for the customer's ID.
	 * 
	 * @return Integer representing the customer's ID.
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * Setter method for the customer's ID.
	 * 
	 * @param customerId Integer representing the customer's ID.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * Getter method for the order's ID.
	 * 
	 * @return Integer representing the order's ID.
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * Setter method for the order's ID.
	 * 
	 * @param orderId Integer representing the order's ID.
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * Getter method for the machine's ID.
	 * 
	 * @return Integer representing the machine's ID.
	 */
	public Integer getMachineId() {
		return machineId;
	}

	/**
	 * Setter method for the machine's ID.
	 * 
	 * @param machineId Integer representing the machine's ID.
	 */
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	/**
	 * Returns a string representation of the object in the format (customerId,
	 * orderId, machineId, billingDate)
	 * 
	 * @return A string representation of the object.
	 */
	public String toString() {
		return "(" + getCustomerId() + "," + getOrderId() + "," + getMachineId() + ",'" + getBillingDate() + "')";
	}

	/**
	 * Getter method for the billing date.
	 * 
	 * @return String representing the billing date.
	 */
	public String getBillingDate() {
		return billingDate;
	}

	/**
	 * Setter method for the billing date.
	 * 
	 * @param billingDate String representing the billing date.
	 */
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

}