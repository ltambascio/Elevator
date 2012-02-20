package cscie160.hw2;

import org.apache.log4j.Logger;

/**
 * This class represents one floor in a building.
 * 
 * @author	Larry Tambascio
 * @version	1.0
 */
public class Floor
{
	/**
	 * Log4j logging object
	 */
	private final static Logger log = Logger.getLogger("Floor");
	
	/**
	 * Number of passengers on this floor waiting to go to the first floor.
	 */
	private int passengerCnt;
	
	/**
	 * No arg constructor that will initialize the number of passengers on a 
	 * floor to 0.
	 */
	public Floor ()
	{
		passengerCnt = 0;
	}
	
	/**
	 * Constructor that takes a count of how many passengers are waiting for an
	 * elevator
	 * 
	 * @param	passengers	Number of passengers waiting for an elevator
	 */
	public Floor(int passengers)
	{
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
		int currentFloor,
			passengersGettingOff,
			elevCnt;
		
		// unload passengers
		currentFloor = elevator.getCurrentFloor();
		passengersGettingOff = elevator.getDestination(currentFloor);
		log.info("Arriving at floor:" + currentFloor + " and discharging " +
				passengersGettingOff + " passengers");
		elevator.setDestination(currentFloor, 0);
		elevCnt = elevator.getPassengerCnt();
		elevator.setPassengerCnt(elevCnt - passengersGettingOff);
		
		log.info("Now attemtping to board " + passengerCnt + " passnegers on " +
				"the elevator");
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
			elevator.registerRequest(currentFloor);	// re-register for a stop
		}
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

}
