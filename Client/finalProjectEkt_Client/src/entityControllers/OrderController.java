package entityControllers;

import java.util.ArrayList;
import java.util.HashMap;

import logic.Product;
import logic.superProduct;
/**
 * The OrderController class is responsible for handling all actions related to orders.
 * It contains fields and methods to manage the current product category, current user cart,
 * products by ID, order number, user orders, array of added products to gridpane, order total price,
 * cart price, order total quantity, order date received, order delivery time, order type, pickup place,
 * billing date, and delivery address.
 * @author Dima, Rotem, Maxim
 *
 */
public class OrderController {
	  // order related fields
	  
	  private static ArrayList<String> CurrentProductCategory = new ArrayList<>();
	  //Map that holds the current cart contents of the user
	  private static HashMap<String,Integer> currentUserCart = new HashMap<>();
	  
	  ////// Dima 30/12 20:00
	  private static HashMap<String,superProduct> getProductByID = new HashMap<>();
	  //Max 7/1-----------------------------------------------------------------------//
	  private static Integer orderNumber = 0; //for now, change later
	  
	  private static HashMap<Integer, ArrayList<String>> userOrders = new HashMap<>();
	  //------------------------------------------------------------------------------//
	  private static ArrayList<Product> arrayOfAddedProductsToGridpane = new ArrayList<>();

	  //public static long orderCounter = 5;   
	  
	  private static Double orderTotalPrice = new Double(0.0);
	  private static HashMap<Product, Double> cartPrice = new HashMap<>();
	  
	  //Maxim Added (11.1)
	  private static Integer orderTotalQuantity;
	  private static String orderDateReceived;
	  private static String orderDeliveryTime;

	  //Maxim (12.1)
	  private static String orderType = "";
	  private static String pickupPlace = "";
	  
	  // Dima 13/1/2023
	  private static String billingDate=null;
	  
	  private static String deliveryAddress = "";
	  /**
	   * 
	   * @return Current Product Category
	   */
	  public static ArrayList<String> getCurrentProductCategory() {
			return CurrentProductCategory;
		}

		public static void setCurrentProductCategory(ArrayList<String> currentProductCategory) {
			CurrentProductCategory = currentProductCategory;
		}
		/**
		 * 
		 * @return current User Cart
		 */
		public static HashMap<String,Integer> getCurrentUserCart() {
			return currentUserCart;
		}
		/**
		 * setting current User Cart
		 * @param currentUserCart
		 */
		public static void setCurrentUserCart(HashMap<String,Integer> currentUserCart) {
			OrderController.currentUserCart = currentUserCart;
		}
		/**
		 * 
		 * @return Product ID
		 */
		public static HashMap<String,superProduct> getGetProductByID() {
			return getProductByID;
		}
		/**
		 * setting product id
		 * @param getProductByID
		 */
		public static void setGetProductByID(HashMap<String,superProduct> getProductByID) {
			OrderController.getProductByID = getProductByID;
		}
		/**
		 * 
		 * @return user orders
		 */
		public static HashMap<Integer, ArrayList<String>> getUserOrders() {
			return userOrders;
		}
		/**
		 * setting user orders
		 * @param userOrders
		 */
		public static void setUserOrders(HashMap<Integer, ArrayList<String>> userOrders) {
			OrderController.userOrders = userOrders;
		}
		/**
		 * 
		 * @return order number
		 */
		public static Integer getOrderNumber() {
			return orderNumber;
		}
		/**
		 * setting order number
		 * @param orderNumber
		 */
		public static void setOrderNumber(Integer orderNumber) {
			OrderController.orderNumber = orderNumber;
		}
		/**
		 * 
		 * @return array of Added Products To Grid pane
		 */
		public static ArrayList<Product> getArrayOfAddedProductsToGridpane() {
			return arrayOfAddedProductsToGridpane;
		}
		/**
		 * setting the array of products 
		 * @param arrayOfAddedProductsToGridpane
		 */
		public static void setArrayOfAddedProductsToGridpane(ArrayList<Product> arrayOfAddedProductsToGridpane) {
			OrderController.arrayOfAddedProductsToGridpane = arrayOfAddedProductsToGridpane;
		}
		/**
		 * 
		 * @return order total price
		 */
		public static Double getOrderTotalPrice() {
			return orderTotalPrice;
		}
		/**
		 * setting orders total price
		 * @param orderTotalPrice
		 */
		public static void setOrderTotalPrice(Double orderTotalPrice) {
			OrderController.orderTotalPrice = orderTotalPrice;
		}
		/**
		 * 
		 * @return cart price
		 */
		public static HashMap<Product, Double> getCartPrice() {
			return cartPrice;
		}
		/**
		 * setting the cart price
		 * @param cartPrice
		 */
		public static void setCartPrice(HashMap<Product, Double> cartPrice) {
			OrderController.cartPrice = cartPrice;
		}
		/**
		 * 
		 * @return orderTotalQuantity
		 */
		public static Integer getOrderTotalQuantity() {
			return orderTotalQuantity;
		}
		/**
		 * setting the order total quantity
		 * @param orderTotalQuantity
		 */
		public static void setOrderTotalQuantity(Integer orderTotalQuantity) {
			OrderController.orderTotalQuantity = orderTotalQuantity;
		}
		/**
		 * 
		 * @return orderDateReceived
		 */
		public static String getOrderDateReceived() {
			return orderDateReceived;
		}
		/**
		 * setting date received for order
		 * @param orderDateReceived
		 */
		public static void setOrderDateReceived(String orderDateReceived) {
			OrderController.orderDateReceived = orderDateReceived;
		}
		/**
		 * 
		 * @return orderDeliveryTime
		 */
		public static String getOrderDeliveryTime() {
			return orderDeliveryTime;
		}
		/**
		 * setting order delivery time
		 * @param orderDeliveryTime
		 */
		public static void setOrderDeliveryTime(String orderDeliveryTime) {
			OrderController.orderDeliveryTime = orderDeliveryTime;
		}
		/**
		 * 
		 * @return orderType
		 */
		public static String getOrderType() {
			return orderType;
		}
		/**
		 * setting order type
		 * @param orderType
		 */
		public static void setOrderType(String orderType) {
			OrderController.orderType = orderType;
		}
		/**
		 * 
		 * @return order pickupPlace
		 */
		public static String getPickupPlace() {
			return pickupPlace;
		}
		/**
		 * setting pickup place for the order
		 * @param pickupPlace
		 */
		public static void setPickupPlace(String pickupPlace) {
			OrderController.pickupPlace = pickupPlace;
		}
		/**
		 * 
		 * @return billingDate
		 */
		public static String getBillingDate() {
			return billingDate;
		}
		/**
		 * setting order billing date
		 * @param billingDate
		 */
		public static void setBillingDate(String billingDate) {
			OrderController.billingDate = billingDate;
		}
		/**
		 * 
		 * @return order delivery Address
		 */
		public static String getDeliveryAddress() {
			return deliveryAddress;
		}
		/**
		 * setting order delivery address
		 * @param deliveryAddress
		 */
		public static void setDeliveryAddress(String deliveryAddress) {
			OrderController.deliveryAddress = deliveryAddress;
		}
		/**
		 * The resetVars method is responsible for resetting all fields in the OrderController class to their default values.
		 * This includes the current product category, current user cart, products by ID, user orders,
		 * array of added products to gridpane, order total price, cart price, order type, pickup place,
		 * and billing date.
		 */
		public static void resetVars() {
			// TODO Auto-generated method stub
			  setCurrentProductCategory(new ArrayList<>());
			  //Map that holds the current cart contents of the user
			  setCurrentUserCart(new HashMap<>());
			  
			  ////// Dima 30/12 20:00
			  setGetProductByID(new HashMap<>());
			  setUserOrders(new HashMap<>());
			  //------------------------------------------------------------------------------//
			  setArrayOfAddedProductsToGridpane(new ArrayList<>());
			  
			  setOrderTotalPrice(new Double(0.0));
			  setCartPrice(new HashMap<>());
			  //Maxim (12.1)
			  setOrderType("");
			  setPickupPlace("");
			  
			  // Dima 13/1/2023
			  setBillingDate(null);
		}

}
