package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each promotion.
 * 
 * @author Maxim Lebedinsky,Nastya chesnov,Raz waiss
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

	public String getStoreLocation() {
		return storeLocation;
	}

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

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

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

	// (promotionName, promotionDescription, storeLocation, productID,
	// discountPercentage, startDate, endDate)

//	public static void main(String[] args) {
//		Promotions promotion = new Promotions("1","ä","b","c", "123", new Date(0), new Date(1) );
//		
//		System.out.println(promotion.toString());
//		
//	}
}
