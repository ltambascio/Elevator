package cscie160.hw2;

/**
 * This is the Elevator class for CSCIE160 homework 1.
 * 
 * @author Larry Tambascio
 * @version 1.0
 */
public class Elevator
{
	public final static int CAPACITY = 10;
	
	public final static int FLOORS = 7;
	
	public final static boolean UP = true;
	
	public final static boolean DOWN = false;
	
	/**
	 * This is the 0 based index array.  The display floor will be + 1 to this.
	 */
	private int currentFloor;
	
	private int passengerCnt;
	
	private boolean currentDirection;
	
	/**
	 * Number of passengers destined for a particular floor.  Zero implies
	 * nobody is getting off at that floor.
	 */
	private int[] destination;
	
	/**
	 * No arg constructor to initiate the elevator's state.  The elevator is
	 * starting on the first floor, there are no passengers, and it is going 
	 * up.  There is no destination initially.
	 */
	public Elevator()
	{
		/*
		 * Floors in buildings are 1 based, while arrays are 0 based.  Will 
		 * need to adjust.
		 */
		this.currentFloor = 0;
		
		this.passengerCnt = 0;
		
		this.currentDirection = UP;
		
		this.destination = new int[FLOORS];
	}
	
	/**
	 * Main method to demonstrate how the elevator behaves.
	 * 
	 * @param args	Array of string command line parameters
	 */
	public static void main (String args[])
	{
		Elevator elevator = new Elevator();
		
		elevator.boardPassenger(2);
		elevator.boardPassenger(2);
		elevator.boardPassenger(3);
		
		System.out.println(elevator);
		
		for (int i = 0; i < 3; i++)
			elevator.move();
	}
	
	/**
	 * Method to move the elevator in the current direction until it stops at a
	 * floor.  If the elevator is at the top floor going up or at the bottom
	 * floor going down, it will switch direction.
	 */
	public void move ()
	{
		if (currentDirection)	// true => up
		{
			while (currentFloor < FLOORS - 1 && passengerCnt > 0)
			{
				currentFloor++;
				if (destination[currentFloor] > 0)
				{
					stop();
					break;
				}
			}
			if (currentFloor == FLOORS - 1)
				currentDirection = DOWN;
			
		}
		else	// going down
		{
			while (currentFloor > 0 && passengerCnt > 0)
			{
				currentFloor--;
				if (destination[currentFloor] > 0)
				{
					stop();
					break;
				}
			}
			if (currentFloor == 0)
				currentDirection = UP;
		}
	}
	
	/**
	 * Method to discharge passengers and update state.
	 */
	public void stop()
	{
		passengerCnt -= destination[currentFloor];
		destination[currentFloor] = 0;
		System.out.println(this);
	}
	
	/**
	 * Adds one passenger to the elevator, with a specific floor as the
	 * destination
	 * 
	 * @param floor		Destination floor for the passenger boarding the 
	 * 					elevator
	 */
	public void boardPassenger(int floor)
	{
		this.destination[floor - 1]++;
		this.passengerCnt++;
	}

	/**
	 * @return	Current state of the elevator object
	 */
	@Override
	public String toString()
	{
		return "Elevator [currentFloor=" + getCurrentFloor() + ", currentDirection="
				+ (currentDirection ? "Up" : "Down") + ", passengerCnt=" + passengerCnt + "]";
	}
	
	// Getters and setters to facilitate testing

	/**
	 * Gets the current floor number.
	 * @return	Adjust current floor to show the floor number as opposed to the 
	 * 			array index.
	 */
	int getCurrentFloor()
	{
		return currentFloor + 1;
	}

	/**
	 * Sets the current floor number, and adjusts it to represent the array
	 * index.
	 * @param currentFloor
	 */
	void setCurrentFloor(int currentFloor)
	{
		this.currentFloor = currentFloor - 1;
	}

	int getPassengerCnt()
	{
		return passengerCnt;
	}

	void setPassengerCnt(int passengerCnt)
	{
		this.passengerCnt = passengerCnt;
	}

	boolean isDirectionUp()
	{
		return currentDirection;
	}

	void setCurrentDirection(boolean currentDirection)
	{
		this.currentDirection = currentDirection;
	}

	int[] getDestination()
	{
		return destination;
	}
	
	int getDestination(int floor)
	{
		return destination[floor - 1];
	}

	void setDestination(int[] destination)
	{
		this.destination = destination;
	}
	
	void setDestination(int floor, int passengers)
	{
		this.destination[floor - 1] = passengers;
	}

}
