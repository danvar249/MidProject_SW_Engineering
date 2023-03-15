package common;

/**
 * This enum represents the average number of products bought from each
 * category, per hour, based on their category. These are then used to estimated
 * time left to refill a certain product in every machine
 * 
 * @author danielvardimon
 *
 */
public enum InventoryCalculations {
	HEALTHY(1.7), SOFT_DRINKS(1.2), FRUITS(3), VEGETABLES(3.4), SNACKS(1.2), SANDWICHES(0.9), CHEWING_GUM(2.5),
	DAIRY(0.5);

	private final double value;

	InventoryCalculations(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

}
