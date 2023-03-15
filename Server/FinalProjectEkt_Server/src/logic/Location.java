package logic;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Location enum represents the different locations that a user can have. It
 * includes the fields locationId and the different possible location values:
 * North, South, UAE. The enum also includes a static method fromLocationId()
 * that takes an integer locationId as input and returns the corresponding
 * Location value.
 * 
 * @author Maxim, Rotem
 *
 */
public enum Location {
	North(1), South(2), UAE(3);

	private int locationId;

	Location(int locationId) {
		this.locationId = locationId;
	}

	private static final Map<Integer, Location> LOCATIONS_BY_LOCATION_ID;

	static {
		LOCATIONS_BY_LOCATION_ID = Stream.of(Location.values())
				.collect(Collectors.toMap(location -> location.locationId, location -> location));
	}

	/**
	 * Returns the {@link Location} object for the given locationId.
	 * 
	 * @param locationId the id of the location
	 * @return the corresponding {@link Location} object
	 * @throws IllegalArgumentException if the locationId is invalid
	 */
	public static Location fromLocationId(int locationId) {
		Location location = LOCATIONS_BY_LOCATION_ID.get(locationId);
		if (location == null) {
			throw new IllegalArgumentException("Invalid locationId: " + locationId);
		}
		return location;
	}

	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}
}
