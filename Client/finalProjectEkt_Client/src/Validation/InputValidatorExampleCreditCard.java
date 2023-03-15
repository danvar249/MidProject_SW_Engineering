package Validation;


/**
 * Example implementation for the input validation interface
 * @author Rotem
 *
 */

public class InputValidatorExampleCreditCard implements IInputValidator{
	/**
	 * Example validate function for a credit card (only checks for 16 digits, no check for valid ones)
	 * @param input
	 */
	@Override
	public Object validate(Object input) {
		// assuming we want a string credit card
		if(input instanceof String) {
			String sInput = (String)input;
			if(!sInput.matches("^[0-9]*$")) {
				return "Credit card must only contain numbers";
			}
			return (input.toString().length() == 16) ? true : "Credit card must have 16 digits";
		}
		if(input == null) {
			return "Error - input is null (throw?)";
		}
		return "Error - input is not a String (this should probably throw an exception)";
	}
	
	/**
	 * Main example for this implementation
	 * @param args
	 */
	
	public static void main(String[] args) {
		String[] testStrings = new String[] {"Ass (as in donkey, obviously)", "1", "123", "1111111111111111", "%", "", "x", null};
		InputValidatorExampleCreditCard iv = new InputValidatorExampleCreditCard();
		for(String s : testStrings) {
			Object o = iv.validate(s);
			if((o instanceof Boolean) && (Boolean)o) {
				System.out.println("String " + s + " is valid!");
			}
			else {
				System.out.println("String " + s + " is not valid, message: " + o);
			}
		}
	}
	
	
}
