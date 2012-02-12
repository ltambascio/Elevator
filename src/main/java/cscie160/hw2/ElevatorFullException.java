package cscie160.hw2;

/**
 * Exception indicating the elevator is full and no more passengers may board.
 * 
 * @author	Larry Tambascio
 * @version	1.0
 */
public class ElevatorFullException extends Exception
{
	private String message;
	
	/**
	 * Constructor that takes a message for the exception.
	 * 
	 * @param message
	 */
	public ElevatorFullException(String message)
	{
		this.message = message;
	}
	
	/**
	 * Method to return the exception's message.
	 * @return	Message
	 */
	@Override
	public String getMessage()
	{
		return message;
	}
}
