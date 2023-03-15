package logic;

/**
 * 
 * The superProduct class is a child class of the Product class and contains
 * additional properties and methods.
 * 
 * @author [Author's Name]
 */
public class superProduct extends Product {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] file;

	public superProduct(String productID, String productName, String costPerUnit, String category, String subCategory,
			String fileName, byte[] file) {
		super(productID, productName, costPerUnit, category, subCategory);
		this.file = file;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

}
