package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each promotion.
 * 
 * @author Nastya chesnov,Raz waiss
 * @version 16/12/2022
 */
public class Promotions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Promotions logic part. private fields that will contain: products details,
	 * discount amount and location of the promotion
	 */
	private String storeLocation;
	private String promotionName;
	private String promotionDescription;
	private String productID;
	private String discountPercentage;
	private Date startDate;
	private Date endDate;
	private String promotionId;
	private int locationID;
	private Boolean promotionStatus;
	/**
	 * 
	 * Constructor for creating a new promotion object
	 * @param promotionName the name of the promotion
	 * @param locationID the location where the promotion is valid
	 * @param promotionDescription the description of the promotion
	 * @param productID the id of the product that the promotion applies to
	 * @param promotionId the id of the promotion
	 * @param discountPercentage the percentage of discount for the promotion
	 * @param startDate the start date of the promotion
	 * @param endDate the end date of the promotion
	 */
	public Promotions(String promotionName, int locationID, String promotionDescription, String productID,
			String promotionId, String discountPercentage, java.sql.Date startDate, java.sql.Date endDate,
			Boolean promotionStatus) {
		this.promotionName = promotionName;
		this.promotionDescription = promotionDescription;
		this.productID = productID;
		this.discountPercentage = discountPercentage;
		this.startDate = startDate;
		this.endDate = endDate;
		this.locationID = locationID;
		this.promotionId = null;
		this.promotionStatus = promotionStatus;

	}
	/**
	 * @return the store location
	 */
	public String getStoreLocation() {
		return storeLocation;
	}
	/**
	 * @param storeLocation the store location to set
	 */
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	/**
	 * setting the details of the products
	 * 
	 * @param productsDetails
	 */

	public int getLocationID() {
		return locationID;
	}
	/**
	 * Setter for the location ID of the promotion.
	 * @param locationID the location ID of the promotion.
	 */
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	/**
	 * Getter for the ID of the promotion.
	 * @return the ID of the promotion.
	 */
	public String getPromotionId() {
		return promotionId;
	}
	/**
	 * Setter for the ID of the promotion.
	 * @param promotionId the ID of the promotion.
	 */
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	/**
	 * A default constructor for the Promotions class.
	 */
	public Promotions() {
		// TODO Auto-generated constructor stub
	}

	public Promotions(String promotionId, String promotionName2, String promotionDescription2, int locationId2,
			String productID2, String discountPercentage2, Date startDate2, Date endDate2, boolean promotionStatus2) {
		this.promotionId = promotionId;
		this.promotionName = promotionName2;
		this.promotionDescription = promotionDescription2;
		this.locationID = locationId2;
		this.productID = productID2;
		this.discountPercentage = discountPercentage2;
		this.startDate = startDate2;
		this.endDate = endDate2;

		this.promotionStatus = promotionStatus2;
	}

	/**
	 * setting the location of the promotion
	 * 
	 * @param promotionLocation
	 */
	public void setstoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getproductID() {
		return productID;
	}

	public void setproductID(String productID) {
		this.productID = productID;
	}

	public String getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "(" + promotionId + ", " + "'" + promotionName + "', " + "'" + promotionDescription + "', "
				+ locationID + ", " + "\"" + productID + "\", " + discountPercentage + ", " + "\"" + startDate + "\", "
				+ "\"" + endDate + "\",0)";
	}

	public Boolean getPromotionStatus() {
		return promotionStatus;
	}

	public void setPromotionStatus(Boolean promotionStatus) {
		this.promotionStatus = promotionStatus;
	}
}
