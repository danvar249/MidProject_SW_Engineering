package database;
/**
 * Enum class representing the different database operations that can be performed.
 * The available operations are:
 * @code SELECT_PROMOTIONS_TO_ACTIVE
 * @code UPDATE_PROMOTION_STATUS
 * @code INSERT
 * @code UPDATE
 * @code DELETE
 * @code ALTER
 * @code SELECT_PROMOTION
 * @code DISPLAY
 * @code USER_LOGIN
 * @code FETCH_PRODUCTS_BY_CATEGORY
 * @code FETCH_ORDERS
 * @code UPDATE_ORDERS
 * @code ADD_PROMOTION
 * @code INSERT_PROMOTION_NAMES
 * @code SELECT
 * @code GENERIC_SELECT
 * @code FETCH_MACHINES_BY_LOCATION
 * @code FETCH_PRODUCTS_IN_MACHINE
 * @author Rotem
 *
 */
public enum DatabaseOperation {
	// update this accordingly
	SELECT_PROMOTIONS_TO_ACTIVE, UPDATE_PROMOTION_STATUS,
	INSERT, UPDATE, DELETE, ALTER, SELECT_PROMOTION, DISPLAY, USER_LOGIN, 
	FETCH_PRODUCTS_BY_CATEGORY, FETCH_ORDERS, UPDATE_ORDERS,ADD_PROMOTION, INSERT_PROMOTION_NAMES
	, SELECT, GENERIC_SELECT, FETCH_MACHINES_BY_LOCATION, FETCH_PRODUCTS_IN_MACHINE, UPDATE_PRODUCTS_IN_MACHINE, REMOVE, 
	//^^^ my actual select -> I changed the old one to "select promotion" because it wasn't OK
	
	IMPORT_SIMULATION; // added way too late
}
