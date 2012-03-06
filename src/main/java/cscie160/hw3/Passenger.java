package cscie160.hw3;

/**
 * This class will represent a passenger on the elevator.
 * 
 * @author Larry Tambascio
 * @version 1.0
 */
public class Passenger
{
	private int currentFloor;
	
	private int destinationFloor;

	/**
	 * Constructor for a passenger that has a current floor and a destination
	 * in mind.
	 * 
	 * @param currentFloor		Floor the passenger is currently on
	 * @param destinationFloor	Floor they want to go to
	 */
	public Passenger(int currentFloor, int destinationFloor)
	{
		this.currentFloor = currentFloor;
		this.destinationFloor = destinationFloor;
	}
	
	/**
	 * Update the current floor the passenger occupies
	 * 
	 * @param	floor	Floor this passenger has just boarded.
	 */
	public void arrive(Floor floor)
	{
		currentFloor = floor.getFloorNum();
	}

	/**
	 * Returns the passenger's current floor
	 * 
	 * @return the currentFloor
	 */
	public int getCurrentFloor()
	{
		return currentFloor;
	}

	/**
	 * Sets the passenger's current floor
	 * 
	 * @param currentFloor the currentFloor to set
	 */
	public void setCurrentFloor(int currentFloor)
	{
		this.currentFloor = currentFloor;
	}

	/**
	 * Returns the destination floor
	 * 
	 * @return the destinationFloor
	 */
	public int getDestinationFloor()
	{
		return destinationFloor;
	}

	/**
	 * Sets the destination floor
	 * 
	 * @param destinationFloor the destinationFloor to set
	 */
	public void setDestinationFloor(int destinationFloor)
	{
		this.destinationFloor = destinationFloor;
	}
	
	/**
	 * Returns the current state of the passenger instance.
	 * @return	Current state of the passenger
	 */
	@Override
	public String toString()
	{
		return "Passenger currently on: " + currentFloor + "; heading to: " +
				destinationFloor;
	}

}
