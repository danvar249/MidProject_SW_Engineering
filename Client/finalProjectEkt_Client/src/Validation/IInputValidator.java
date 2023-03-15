package Validation;

/**
 * This is an interface for input validation, to be used by many parts of the UI as such:
 * Caller implements this function locally, using a field to keep a message in case of failure,
 * Object returns the message (if it's boolean, it should be 'true', but can be false if a check has no return message). 
 * @author Rotem
 *
 */

public interface IInputValidator {
	Object validate(Object input);
}
