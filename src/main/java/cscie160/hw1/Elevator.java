package cscie160.hw1;

/**
 * This is the Elevator class for CSCIE160 homework 1.
 * 
 * @author Larry Tambascio
 * @version 1.0
 */
public class Elevator
{
	private final static int CAPACITY = 10;
	
	private final static int FLOORS = 7;
	
	private final static boolean UP = true;
	
	private final static boolean DOWN = false;
	
	private int currentFloor;
	
	private int passengerCnt;
	
	private boolean currentDirection;
	
	private boolean[] destination;
	
	/**
	 * No arg constructor to initiate the elevator's state.  The elevator is
	 * starting on the first floor, there are no passengers, and it is going 
	 * up.  There is no destination initially.
	 */
	public Elevator()
	{
		currentFloor = 1;
		
		passengerCnt = 0;
		
		currentDirection = UP;
		
		destination = new boolean[FLOORS];
	}
	
	/**
	 * Method to move the elevator in the current direction one floor.  If the 
	 * elevator is at the top floor, it can't go up any higher, and if it is 
	 * on the first floor, it can't go lower.
	 */
	public void move ()
	{
		if (currentDirection)	// true => up
		{
			if (currentFloor < FLOORS)
				currentFloor++;
		}
		else
		{
			if (currentFloor > 1)
				currentFloor--;
		}
	}

	/**
	 * @return	Current state of the elevator object
	 */
	@Override
	public String toString()
	{
		return "Elevator [currentFloor=" + currentFloor + ", currentDirection="
				+ currentDirection + ", passengerCnt=" + passengerCnt + "]";
	}

}
