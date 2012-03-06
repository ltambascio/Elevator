package cscie160.hw3;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * This is the Elevator class for CSCIE160 homework 2.
 * 
 * @author Larry Tambascio
 * @version 1.3
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
	
	/**
	 * Collection of passengers on the elevator.  The number of floors is 
	 * constant, so that should still be an array, but the number of passengers
	 * on each floor can vary greatly, so each floor should be an ArrayList of 
	 * Passengers.
	 */
	private ArrayList<Passenger>[] elevPassengers;
	
	private boolean currentDirection;
	
	/**
	 * Number of passengers destined for a particular floor.  Zero implies
	 * nobody is getting off at that floor.
	 */
//	private int[] destination;
	
	/**
	 * Array of booleans indicating whether a floor has called for an elevator.
	 */
	private boolean[]	upCalls,
						downCalls;
	
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
		
		this.floors = new Floor[FLOORS];
		this.upCalls = new boolean[FLOORS];
		this.downCalls = new boolean[FLOORS];
		this.elevPassengers = new ArrayList[FLOORS];
		for (int i=0;i < FLOORS; i++)
		{
			this.floors[i] = new Floor(i + 1);
			this.upCalls[i] = false;
			this.elevPassengers[i] = new ArrayList<Passenger>();
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
		
		// Start with a bunch of passengers on the elevator bound for different
		// floors
		try {
			for (int i = 0; i < 3; i++)
				elevator.boardPassenger(new Passenger(1, 3));
			for (int i = 0; i < 3; i++)
				elevator.boardPassenger(new Passenger(1, 4));
			for (int i = 0; i < 3; i++)
				elevator.boardPassenger(new Passenger(1, 7));
		}
		catch (ElevatorFullException efe)
		{
			log.error(efe);
		}
		
		// start out by loading people into floors and registering requests for
		// those floors in different directions.
		
		// second floor
		// 4 passengers going down to 1
		// 3 passengers going up to 5
		elevator.initFloorForTest(2, 5, 3, 1, 4);
		
		// third floor
		// 2 going down to 1
		// 3 going up to 4
		elevator.initFloorForTest(3, 4, 3, 1, 2);
		
		// fourth floor
		// 2 going down to 2
		// 1 going up to 5
		elevator.initFloorForTest(4, 5, 1, 2, 2);
		
		// fifth floor
		// 1 going down to 3
		// 1 going up to 7
		elevator.initFloorForTest(5, 7, 1, 3, 1);
		
		// sixth floor
		// 3 going up to 7
		// 5 going down to 4
		elevator.initFloorForTest(6, 7, 3, 4, 5);
		
		// seventh floor
		// 0 going up
		// 5 going down to 1
		elevator.initFloorForTest(7, 0, 0, 1, 5);
		
		log.info("Elevator initialization complete");
		log.info(elevator);
		
		for (int i = 0; i < 19; i++)
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
				if (currentFloor == FLOORS - 1)
					currentDirection = DOWN;
				if (elevPassengers[currentFloor].size() > 0 || upCalls[currentFloor])
				{
					stop();
					break;
				}
			}
		}
		else	// going down
		{
			while (currentFloor > 0)
			{
				currentFloor--;
				if (currentFloor == 0)
					currentDirection = UP;
				if (elevPassengers[currentFloor].size() > 0 || downCalls[currentFloor])
				{
					stop();
					break;
				}
			}
		}
	}
	
	/**
	 * Method to discharge passengers and update state.
	 */
	public void stop()
	{
		// if we're stopping because of a request, then clear that request
		if (upCalls[currentFloor] && currentDirection == UP)
			upCalls[currentFloor] = false;
		else if (downCalls[currentFloor] && currentDirection == DOWN)
			downCalls[currentFloor] = false;
		
		log.info(this);
		floors[currentFloor].unloadPassengers(this);
	}
	
	/**
	 * Adds one passenger to the elevator.
	 * 
	 * @param	passenger	Passenger boarding the elevator.  The passenger
	 * 						knows which floor it's destined for.
	 * @throws	ElevatorFullException	Thrown when the elevator is at its
	 * 									maximum capacity.
	 */
	public void boardPassenger(Passenger passenger)
		throws ElevatorFullException
	{
		log.info("boarding a passenger destined for " + passenger.getDestinationFloor());
		if (this.passengerCnt < CAPACITY)
		{
			elevPassengers[passenger.getDestinationFloor() - 1].add(passenger);
//			this.destination[floor - 1]++;
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
	 * @param	floor		The floor with passengers requesting an elevator.
	 * @param	direction	Direction of the request - <code>true</code> means
	 * 						up and <code>false</code> means down
	 */
	public void registerRequest(int floor, boolean direction)
	{
		if (direction)
			upCalls[floor - 1] = true;
		else
			downCalls[floor - 1] = true;
	}

	/**
	 * @return	Current state of the elevator object
	 */
	@Override
	public String toString()
	{
		StringBuffer sb;
		
		sb = new StringBuffer("Elevator [currentFloor=");
		sb.append(getCurrentFloor());
		sb.append(", currentDirection=");
		sb.append(currentDirection ? "Up" : "Down"); 
		sb.append(", passengerCnt=");
		sb.append(passengerCnt); 
		sb.append("]\nPassenger Detail:\n===============");
		for (int i = 0; i < FLOORS; i++)
			for (Passenger p: elevPassengers[i])
			{
				sb.append("\n\t");
				sb.append(p);
			}
		sb.append("\n===============");
		
		return sb.toString();
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
	 * Return the array list for the selected floor.
	 * @param	floor	Floor number to retrieve
	 * @return	Passenger array list for the specified floor.
	 */
	public ArrayList<Passenger> getFloorPassengers(int floor)
	{
		return elevPassengers[floor - 1];
	}

	/**
	 * Return the requested floor object.
	 * @param	floor	Floor number to return
	 * @return	Floor object requested
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
		return upCalls[floor - 1];
	}

	/**
	 * Convenience method to initialize a floor for testing
	 * 
	 * @param	floorNum	Floor to modify
	 * @param	upFloor		Floor people are going up to
	 * @param	upCount		Number of people going up
	 * @param	downFloor	Floor people are going down to
	 * @param	downCount	Number of people going down
	 */
	private void initFloorForTest(int floorNum, int upFloor, int upCount,
			int downFloor, int downCount)
	{
		Floor floor;
		
		floor = this.getFloor(floorNum);
		for (int i=0; i < downCount; i++)
			floor.getGoingDown().add(new Passenger(floorNum, downFloor));
		for (int i=0; i < upCount; i++)
			floor.getGoingUp().add(new Passenger(floorNum, upFloor));
		if (downCount > 0)
			this.registerRequest(floorNum, Elevator.DOWN);
		if (upCount > 0)
			this.registerRequest(floorNum, Elevator.UP);
		
		log.info(floor);
	}

}
