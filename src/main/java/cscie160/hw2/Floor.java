package cscie160.hw2;

import org.apache.log4j.Logger;

/**
 * This class represents one floor in a building.
 * 
 * @author	Larry Tambascio
 * @version	1.1
 */
public class Floor
{
	/**
	 * Log4j logging object
	 */
	private final static Logger log = Logger.getLogger("Floor");
	
	/**
	 * The number of the floor in the building
	 */
	private int floorNum;
	
	/**
	 * Number of passengers on this floor waiting to go to the first floor.
	 */
	private int passengerCnt;
	
	/**
	 * No arg constructor that will initialize the number of passengers on a 
	 * floor to 0, and assume it's the first floor.
	 */
	public Floor ()
	{
		passengerCnt = 0;
		floorNum = 1;
	}
	
	/**
	 * Constructor that takes a count of how many passengers are waiting for an
	 * elevator, again assume it's the first floor.
	 * 
	 * @param	passengers	Number of passengers waiting for an elevator
	 */
	public Floor(int passengers)
	{
		passengerCnt = passengers;
		floorNum = 1;
	}
	
	/**
	 * Constructor that takes a count of how many passengers are waiting for an
	 * elevator, again assume it's the first floor.
	 *
	 * @param	floor		The floor number for this floor.
	 * @param	passengers	Number of passengers waiting for an elevator
	 */
	public Floor(int floor, int passengers)
	{
		floorNum = floor;
		passengerCnt = passengers;
	}
	
	/**
	 * This method will pull passengers off of the elevator, and board waiting
	 * passengers.
	 * 
	 * @param	elevator	The elevator object to operate upon
	 */
	public void unloadPassengers (Elevator elevator)
	{
		int passengersGettingOff,
			elevCnt;
		
		// unload passengers
		passengersGettingOff = elevator.getDestination(floorNum);
		elevator.setDestination(floorNum, 0);
		elevCnt = elevator.getPassengerCnt();
		elevator.setPassengerCnt(elevCnt - passengersGettingOff);
		
		// board passengers
		try {
			
			while (passengerCnt > 0)
			{
				elevator.boardPassenger(1);
				passengerCnt--;
			}
		}
		catch (ElevatorFullException efe)
		{
			elevator.registerRequest(floorNum);	// re-register for a stop
		}
		
		log.info(this);
	}
	
	/**
	 * Floor number for this floor.
	 * @return	Current floor number
	 */
	int getFloorNum()
	{
		return floorNum;
	}

	/**
	 * Changes the current floor number
	 * @param floorNum	New floor number for this floor
	 */
	void setFloorNum(int floorNum)
	{
		this.floorNum = floorNum;
	}

	/**
	 * Current passenger count waiting for an elevator.
	 * @return	Passenger count
	 */
	public int getPassengerCnt()
	{
		return passengerCnt;
	}
	
	/**
	 * Sets the passenger count waiting on this floor for the elevator.
	 * @param 	passengerCnt	Number of passengers waiting
	 */
	void setPassengerCnt(int passengerCnt)
	{
		this.passengerCnt = passengerCnt;
	}
	
	/**
	 * Returns the state of this floor object
	 * @return	Internal state of the floor
	 */
	@Override
	public String toString()
	{
		return "Floor #" + floorNum + " - passenger count:" + passengerCnt;
	}

}
