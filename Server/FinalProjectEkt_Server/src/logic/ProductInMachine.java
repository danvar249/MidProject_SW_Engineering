package logic;

import java.io.Serializable;

//Class that represents a product in a machine, that includes that product and it's stock in the machine.
/**
 * ProductInMachine class represents a product and its stock in a specific
 * machine. It includes information about the product, the machine that contains
 * it, the stock amount, the minimum and maximum stock amount and a restock
 * flag.
 * 
 * @serial serialVersionUID
 * @author Maxim, Rotem
 *
 */
public class ProductInMachine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// the product
	private Product product;
	// the machine that contains the product
	private Machine machine;
	// how much of it is there in the machine.
	private int stock;
	// minimum stock amount
	private int minStock;
	// maximum stock amount
	private int maxStock;
	// flag that says if restock is needed. defualt false
	private boolean restockFlag = false;

	/**
	 * Constructor for creating a new ProductInMachine object.
	 * 
	 * @param product     The Product object that represents the product.
	 * @param machine     The Machine object that represents the machine that
	 *                    contains the product.
	 * @param stock       The current stock of the product in the machine.
	 * @param minStock    The minimum stock level of the product in the machine.
	 * @param maxStock    The maximum stock level of the product in the machine.
	 * @param restockFlag A boolean flag that indicates if the product needs to be
	 *                    restocked in the machine.
	 */
	public ProductInMachine(Product product, Machine machine, int stock, int minStock, int maxStock,
			boolean restockFlag) {
		this.product = product;
		this.machine = machine;
		this.stock = stock;
		this.minStock = minStock;
		this.maxStock = maxStock;
		this.setRestockFlag(restockFlag);
	}

	/**
	 * @return the stock of the product in the machine
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * @param stock set the stock of the product in the machine
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * 
	 * @return the product object
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 
	 * @return the machine object
	 */
	public Machine getMachine() {
		return machine;
	}

	/**
	 * @return the minStock
	 */
	public int getMinStock() {
		return minStock;
	}

	/**
	 * @param minStock the minStock to set
	 */
	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}

	/**
	 * @return the maxStock
	 */
	public int getMaxStock() {
		return maxStock;
	}

	/**
	 * @param maxStock the maxStock to set
	 */
	public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
	}

	/**
	 * @return the restockFlag
	 */
	public boolean isRestockFlag() {
		return restockFlag;
	}

	/**
	 * @param restockFlag the restockFlag to set
	 */
	public void setRestockFlag(boolean restockFlag) {
		this.restockFlag = restockFlag;
	}

	/**
	 * ToString
	 */
	@Override
	public String toString() {
		return product + ", " + machine + ", stock=" + stock + ", mistock=" + minStock + ", maxstock=" + maxStock
				+ ", restock flag=" + restockFlag;
	}

}
