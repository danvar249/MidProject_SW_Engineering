package logic;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Location {
	North(1), South(2), UAE(3);
	
	private int locationId;
	
	Location(int locationId){
		this.locationId = locationId;
	}
	private static final Map<Integer, Location> LOCATIONS_BY_LOCATION_ID;

	static {
		LOCATIONS_BY_LOCATION_ID = Stream.of(Location.values()).collect(Collectors.toMap(location -> location.locationId, location -> location));
	}

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
