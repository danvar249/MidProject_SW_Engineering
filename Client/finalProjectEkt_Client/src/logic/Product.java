package logic;

import java.io.Serializable;

/**
 * Project Name: finalProjectEkt_Client
 * Logic class that contains the details needed to save up for each report.
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	*Product logic part.
	*private fields that will contain product's: id, name and cost per unit
	*/
	private String productID; //String cuz maybe => id = A12
	private String productName;
	private String costPerUnit;//String cuz maybe => costPerUnit = 10NIS
	private String category;
	private String subCategory;
	/**
	 * Product constructor.
	 * @param productID
	 * @param productName
	 * @param costPerUnit
	 */
	public Product(String productID, String productName, String costPerUnit, String category, String subCategory) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.costPerUnit = costPerUnit;
		this.category = category;
		this.subCategory = subCategory;
	}
	/**
	 * getting the id of a product
	 * @return productID
	 */
	public String getProductID() {
		return productID;
	}
	/**
	 * setting the id of a product
	 * @param productID
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}
	/**
	 * getting product name
	 * @return productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * setting product name
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * getting product cost per unit
	 * @return costPerUnit
	 */
	public String getCostPerUnit() {
		return costPerUnit;
	}
	/**
	 * setting product cost per unit
	 * @param costPerUnit
	 */
	public void setCostPerUnit(String costPerUnit) {
		this.costPerUnit = costPerUnit;
	}
	/**
	 * toString method, returns report details
	 */
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", costPerUnit=" + costPerUnit
				+ "]";
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	
	
}
