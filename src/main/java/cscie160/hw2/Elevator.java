package cscie160.hw2;

import org.apache.log4j.Logger;

/**
 * This is the Elevator class for CSCIE160 homework 2.
 * 
 * @author Larry Tambascio
 * @version 1.1
 */
public class Elevator
{
	/**
	 * Log4j logging object
	 */
	private final static Logger log = Logger.getLogger("Elevator");
	
	public final static int CAPACITY = 10;
	
	public final static int FLOORS = 7;
	
	public final static boolean UP = true;
	
	public final static boolean DOWN = false;
	
	/**
	 * This is the 0 based index array.  The display floor will be + 1 to this.
	 */
	private int currentFloor;
	
	/**
	 * Total passengers on the elevator.
	 */
	private int passengerCnt;
	
	private boolean currentDirection;
	
	/**
	 * Number of passengers destined for a particular floor.  Zero implies
	 * nobody is getting off at that floor.
	 */
	private int[] destination;
	
	/**
	 * Array of booleans indicating whether a floor has called for an elevator.
	 */
	private boolean[] callingFloors;
	
	/**
	 * An array of Floor objects that represent all the floors in the building. 
	 */
	private Floor[] floors;
	
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
		
		this.floors = new Floor[FLOORS];
		this.callingFloors = new boolean[FLOORS];
		for (int i=0;i < FLOORS; i++)
		{
			this.floors[i] = new Floor(i + 1, 0);
			this.callingFloors[i] = false;
		}
	}
	
	/**
	 * Main method to demonstrate how the elevator behaves.
	 * 
	 * @param args	Array of string command line parameters
	 */
	public static void main (String args[])
	{
		Elevator elevator = new Elevator();
		Floor floor;
		
		// start out by loading people into floors and registering requests for
		// those floors.
		floor  = elevator.getFloor(2);
		floor.setPassengerCnt(4);
		elevator.registerRequest(2);
		floor = elevator.getFloor(4);
		floor.setPassengerCnt(6);
		elevator.registerRequest(4);
		floor = elevator.getFloor(6);
		floor.setPassengerCnt(5);
		elevator.registerRequest(6);
		
		// now load a bunch of passengers for different floors
		// With 6 bound for the third floor, we'll definitely fill the elevator
		// on the third floor.
		
		try
		{
			for (int i = 0; i < 6; i++)
				elevator.boardPassenger(3);
			elevator.boardPassenger(4);
			elevator.boardPassenger(7);
		}
		catch (ElevatorFullException efe)
		{
			log.error("Somehow the elevator is full now.");
			efe.printStackTrace();
		}
		
		log.info("Elevator initialization complete");
		log.info(elevator);
		log.info("Start moves");
		
		for (int i = 0; i < 12; i++)
		{
			log.info("Executing move #" + (i + 1));
			elevator.move();
		}
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
			while (currentFloor < FLOORS - 1)
			{
				currentFloor++;
				if (destination[currentFloor] > 0 || callingFloors[currentFloor])
				{
					stop();
					break;
				}
			}
			if (currentFloor == FLOORS - 1)
			{
				log.info("Changing direction to down");
				currentDirection = DOWN;
			}
			
		}
		else	// going down
		{
			while (currentFloor > 0)
			{
				currentFloor--;
				if (destination[currentFloor] > 0 || callingFloors[currentFloor])
				{
					stop();
					break;
				}
			}
			if (currentFloor == 0)
			{
				log.info("Changing direction to up");
				currentDirection = UP;
			}
		}
	}
	
	/**
	 * Method to discharge passengers and update state.
	 */
	public void stop()
	{
		// if we're stopping because of a request, then clear that request
		if (callingFloors[currentFloor])
			callingFloors[currentFloor] = false;
		
		floors[currentFloor].unloadPassengers(this);
		log.info(this);
	}
	
	/**
	 * Adds one passenger to the elevator, with a specific floor as the
	 * destination
	 * 
	 * @param	floor	Destination floor for the passenger boarding the 
	 * 					elevator
	 * @throws	ElevatorFullException	Thrown when the elevator is at its
	 * 									maximum capacity.
	 */
	public void boardPassenger(int floor)
		throws ElevatorFullException
	{
		log.info("boarding a passenger destined for " + floor);
		if (this.passengerCnt < CAPACITY)
		{
			this.destination[floor - 1]++;
			this.passengerCnt++;
		}
		else
		{
			log.error("This elevator has reached capacity at floor " + 
					getCurrentFloor());
			throw new ElevatorFullException("This elevator is at capacity on " +
					"floor" + getCurrentFloor());
		}
	}
	
	/**
	 * Method to register a floor calling an elevator, indicating there are 
	 * passengers that need an elevator.
	 * 
	 * @param	floor	The floor with passengers requesting an elevator.
	 */
	public void registerRequest(int floor)
	{
		callingFloors[floor - 1] = true;
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
	public int getCurrentFloor()
	{
		return currentFloor + 1;
	}

	/**
	 * Sets the current floor number, and adjusts it to represent the array
	 * index.
	 * @param currentFloor
	 */
	protected void setCurrentFloor(int currentFloor)
	{
		this.currentFloor = currentFloor - 1;
	}

	/**
	 * Returns the total number of passengers on the elevator
	 * @return	Passenger Count
	 */
	public int getPassengerCnt()
	{
		return passengerCnt;
	}

	/**
	 * Changes the number of passengers on the elevator outside of the 
	 * <code>boardPassenger</code> or <code>stop</code> methods.
	 * 
	 * @param	passengerCnt	Passenger count to change
	 */
	protected void setPassengerCnt(int passengerCnt)
	{
		this.passengerCnt = passengerCnt;
	}

	/**
	 * Returns <code>true</code> if the elevator is currently going up and 
	 * <code>false</code> if it is going down.
	 * 
	 * @return	The current direction of the elevator
	 */
	public boolean isDirectionUp()
	{
		return currentDirection;
	}

	/**
	 * Allows testing methods to set the direction the elevator is currently 
	 * traveling in.
	 * @param currentDirection	<code>true</code> indicates up and <code>false
	 * 							</code> indicates down.
	 */
	protected void setCurrentDirection(boolean currentDirection)
	{
		this.currentDirection = currentDirection;
	}

	/**
	 * Returns the destination array indicating how many passengers are destined
	 * for each floor.
	 * @return	Destination array
	 */
	protected int[] getDestination()
	{
		return destination;
	}
	
	/**
	 * Returns the number of people destined for the specified floor.
	 * @param	floor	Floor number
	 * @return	Count of people heading to the specified floor.
	 */
	public int getDestination(int floor)
	{
		return destination[floor - 1];
	}

	/**
	 * Method to setup the entire array of passenger's destination
	 * @param	destination	Array of destinations for people.
	 */
	protected void setDestination(int[] destination)
	{
		this.destination = destination;
	}
	
	/**
	 * Sets the passengers destined for the specified floor to the passed in 
	 * passenger count.
	 * 
	 * @param	floor		Floor to update the count for
	 * @param	passengers	Passengers destined for that floor
	 */
	public void setDestination(int floor, int passengers)
	{
		this.destination[floor - 1] = passengers;
	}
	
	/**
	 * Return the requested floor object.
	 * @param	floor	Floor number to return
	 * @return
	 */
	public Floor getFloor(int floor)
	{
		return floors[floor - 1];
	}
	
	/**
	 * Replaces a floor element in the array at the specified floor number.
	 * 
	 * @param	floorNum	Floor number to be replaced
	 * @param	floor		Floor object to replace it with
	 */
	protected void setFloor(int floorNum, Floor floor)
	{
		floors[floorNum - 1] = floor;
	}
	
	/**
	 * This method will indicate whether the passed in floor has a stop request
	 * associated with it.
	 * @param	floor	Floor number to return the request flag for.
	 * @return	<code>true</code> if a passenger on the floor has requested an 
	 * 			elevator; <code>false</code> if not.
	 */
	public boolean getCallingFloor(int floor)
	{
		return callingFloors[floor - 1];
	}

}
