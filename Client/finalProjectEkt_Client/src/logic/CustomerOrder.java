package logic;

import java.io.Serializable;

public class CustomerOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	
	private Integer customerId;
	private Integer orderId;
	private Integer machineId;
	private String billingDate;
	
	public CustomerOrder(Integer custoemrId, Integer orderId, Integer machineId, String billingDate){
		this.setCustomerId(custoemrId);
		this.setOrderId(orderId);
		this.setMachineId(machineId);
		this.setBillingDate(billingDate);
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public String toString() {
		return "("+getCustomerId()+","+getOrderId()+","+getMachineId()+",'"+getBillingDate()+"')";
	}
	public String getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

}