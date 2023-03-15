package common;

import java.util.Arrays;
import java.util.List;

/**
 * Class: InputValidator
 * Contains a static method that validates, for any list of objects (stored as Object[]), that the elements inside are of some desired types.
 * @author Rotem
 *
 */
public class TypeChecker {
	/**
	 * This static method compares classes between an array[Object] and an arrayList of classes, in a given range of indices.
	 * We get the class for each object and compare it with our expected type (stored, for objects[i], in paramTypes.get(i))
	 * @param objects - the array of objects we want to validate
	 * @param paramTypes - the types for the segment of objects we are interested in (segment inside objects)
	 * @param startIdx - the first index of objects to compare with the 0th index of paramTypes
	 * @return true if all objects in range are of the desired types, else false
	 */
	public static boolean validate(Object[] objects, List<Class<?>> paramTypes, int startIdx) {
		if(objects.length < (startIdx + paramTypes.size()))
			throw new IllegalArgumentException(
					"Invalid range of parameters for validateInput {"
							+ Arrays.toString(objects) + ", "
							+ Arrays.toString(paramTypes.toArray())+", "+ startIdx + "}");

		for(int j=0;j<paramTypes.size();j++) {
			if(!objects[j + startIdx].getClass().equals(paramTypes.get(j))) {
				return false;
			}
		}
		return true;
	}
}
