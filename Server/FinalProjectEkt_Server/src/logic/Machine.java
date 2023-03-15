package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each machine.
 * 
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Machine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2396441473921458334L;
	/**
	 * Machine logic part. private fields that will contain: list of products and
	 * their stock, name, threshold level, status and location of the machine
	 */
	private int machineId;
	private String machineName;
	private ArrayList<ProductInMachine> products = new ArrayList<>();
	private Location location;
	private int threshold;

	/**
	 * Machine constructor.
	 * 
	 * @param threshold TODO
	 */
	public Machine(int machineId, String machineName, Location location, int threshold) {
		this.machineId = machineId;
		this.machineName = machineName;
		this.location = location;
		this.setThreshold(threshold);
	}

	/**
	 * getting list of products that the machine contain
	 * 
	 * @return products
	 */
	public ArrayList<ProductInMachine> getProducts() {
		return products;
	}

	/**
	 * setting the products list
	 * 
	 * @param products
	 */
	public void setProducts(ArrayList<ProductInMachine> products) {
		this.products = products;
	}

	/**
	 * getting the stock of a product
	 * 
	 * @return stock
	 */
	public int getStock(Product product) {
		// find product in machine products
		ProductInMachine productToGet = getProductInMachine(product);
		if (productToGet != null) {
			return productToGet.getStock();
		}
		// if not found return stock 0.
		return 0;
	}

	/**
	 * setting the stock of a product
	 * 
	 * @param stock
	 */
	public void setStock(Product product, int stock) {
		// find product in machine products and set the stock.
		ProductInMachine productToUpdate = getProductInMachine(product);
		if (productToUpdate != null) {
			productToUpdate.setStock(stock);
		}
	}

	/**
	 * getting machine location
	 * 
	 * @return location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * setting machine location
	 * 
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the machineId
	 */
	public int getMachineId() {
		return machineId;
	}

	/**
	 * toString method, returns machine details
	 */
	@Override
	public String toString() {
		return machineName;
	}

	public ProductInMachine getProductInMachine(Product productToGet) {
		for (ProductInMachine p : products) {
			if (p.getProduct().equals(productToGet)) {
				return p;
			}
		}
		return null;
	}

	public void addProduct(Product productToAdd, int initialStock, int minStock, int maxStock, boolean restockFlag) {
		// product already in machine
		if (getProductInMachine(productToAdd) != null)
			return;
		products.add(new ProductInMachine(productToAdd, this, initialStock, minStock, maxStock, restockFlag));
	}

	public void removeProduct(Product productToRemove) {
		ProductInMachine productInMachineToRemove = getProductInMachine(productToRemove);
		if (productInMachineToRemove != null) {
			products.remove(productInMachineToRemove);
		}
	}

	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * 
	 * @return machine threshold
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * @param set machine threshold
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
