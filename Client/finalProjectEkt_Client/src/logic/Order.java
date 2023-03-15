package logic;

import java.io.Serializable;
import java.time.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each order.
 * 
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {
		Pickup(1), Delivery(2), Local(3);

		int typeId;

		Type(int typeId) {
			this.typeId = typeId;
		}

		private static final Map<Integer, Type> TYPES_BY_TYPE_ID;

		static {
			TYPES_BY_TYPE_ID = Stream.of(Type.values()).collect(Collectors.toMap(type -> type.typeId, type -> type));
		}

		public static Type fromTypeId(int typeId) {
			Type type = TYPES_BY_TYPE_ID.get(typeId);
			if (type == null) {
				throw new IllegalArgumentException("Invalid typeId: " + typeId);
			}
			return type;
		}

		public int getTypeId() {
			return typeId;
		}
	}

	public enum Status {
		InProgress(1, "In Progress"), Complete(2, "Complete"), Cancelled(3, "Cancelled"),
		RequestedCancellation(4, "Requested Cancellation"), Delivered(5,"Delivered"),Received(6,"Received");

		int statusId;
		String statusString;

		Status(int statusId, String statusString) {
			this.statusId = statusId;
			this.statusString = statusString;
		}
		
		private static final Map<Integer, Status> STATUS_BY_ID;
	    static {
	    	STATUS_BY_ID = Stream.of(Status.values()).collect(Collectors.toMap(status -> status.statusId, status -> status));

	    }

	    public static Status fromStatusId(int statusId) {
	        Status status = STATUS_BY_ID.get(statusId);
	        if (status == null) {
	            throw new IllegalArgumentException("Invalid statusId: " + statusId);
	        }
	        return status;
	    }
		@Override
		public String toString() {
			return statusString;
		}

		public int getStatusId() {
			return statusId;
		}
	}

	/**
	 * Order logic part. private fields that will contain order entity: ID, total
	 * price, total amount of this order, the machine this order is for, the date
	 * when the order was received, the estimated delivery time for the order.
	 */

	private Integer orderID;
	private Integer totalPrice;
	private Integer totalAmount;
	private Machine machine;
	private LocalDate dateReceived;
	private LocalDateTime deliveryTime;
	private Status status;
	private Type type;

	public Order(Integer orderID, Integer totalPrice, Integer totalAmount, Machine machine, LocalDate dateReceived,
			LocalDateTime deliveryTime, Status status, Type type) {
		this.orderID = orderID;
		this.totalPrice = totalPrice;
		this.totalAmount = totalAmount;
		this.machine = machine;
		this.dateReceived = dateReceived;
		this.deliveryTime = deliveryTime;
		this.setStatus(status);
		this.setType(type);
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Machine getMachine() {
		return machine;
	}

	public LocalDateTime getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(LocalDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getOrderID() {
		return orderID;
	}

	public LocalDate getDateReceived() {
		return dateReceived;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * toString method, returns order details
	 */
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", totalPrice=" + totalPrice + ", totalAmount=" + totalAmount
				+ ", machine name=" + machine + "dateReceived=" + dateReceived + "deliveryTime=" + deliveryTime + "]";
	}

}