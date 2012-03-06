package cscie160.hw3;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * This class represents one floor in a building.
 * 
 * @author	Larry Tambascio
 * @version	1.2
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
	 * Collection for passengers just hanging out on the floor
	 */
	private ArrayList<Passenger> onFloor;
	
	/**
	 * Collection for passengers queued to go up
	 */
	private ArrayList<Passenger> goingUp;
	
	/**
	 * Collections for passengers queued to go down
	 */
	private ArrayList<Passenger> goingDown;
	
	/**
	 * No arg constructor that will initialize the floor and assume it's the 
	 * first floor.
	 */
	public Floor ()
	{
		floorNum = 1;
		
		initCollections();
	}
	
	/**
	 * Constructor that initializes what the current floor number is.
	 * 
	 * @param	floorNum	Floor number this floor represents
	 */
	public Floor(int floorNum)
	{
		this.floorNum = floorNum;
		
		initCollections();
	}
	
	/**
	 * Initialize the passenger collections.
	 */
	private void initCollections()
	{
		onFloor = new ArrayList<Passenger>();
		goingUp = new ArrayList<Passenger>();
		goingDown = new ArrayList<Passenger>();
	}
	
	/**
	 * This method will pull passengers off of the elevator, and board waiting
	 * passengers.
	 * 
	 * @param	elevator	The elevator object to operate upon
	 */
	public void unloadPassengers (Elevator elevator)
	{
		ArrayList<Passenger> passengersGettingOff,
								boarded;
		int elevCnt;
		
		// unload passengers
		passengersGettingOff = elevator.getFloorPassengers(floorNum);
		for (Passenger pass: passengersGettingOff)
		{
			pass.arrive(this);
			onFloor.add(pass);
		}
		elevCnt = elevator.getPassengerCnt();
		elevator.setPassengerCnt(elevCnt - passengersGettingOff.size());
		passengersGettingOff.clear();
		
		// board passengers
		boarded = new ArrayList<Passenger>();
		boolean currentlyUp = elevator.isDirectionUp();
		try {
			if (currentlyUp)
				for (Passenger pass:goingUp)
				{
					elevator.boardPassenger(pass);
					boarded.add(pass);
				}
			else
				for (Passenger pass:goingDown)
				{
					elevator.boardPassenger(pass);
					boarded.add(pass);
				}
		}
		catch (ElevatorFullException efe)
		{
			elevator.registerRequest(floorNum, elevator.isDirectionUp());	// re-register for a stop
		}
		finally
		{
			// Remove the passengers that boarded the elevator from the
			// correct collection
			if (currentlyUp)
				goingUp.removeAll(boarded);
			else
				goingDown.removeAll(boarded);
		}
		
		log.info(this);
	}
	
	/**
	 * Floor number for this floor.
	 * @return	Current floor number
	 */
	public int getFloorNum()
	{
		return floorNum;
	}

	/**
	 * Changes the current floor number
	 * @param floorNum	New floor number for this floor
	 */
	protected void setFloorNum(int floorNum)
	{
		this.floorNum = floorNum;
	}
	
	/**
	 * @return	the passengers hanging out on the floor
	 */
	public ArrayList<Passenger> getOnFloor()
	{
		return onFloor;
	}

	/**
	 * @return the goingUp passengers
	 */
	public ArrayList<Passenger> getGoingUp()
	{
		return goingUp;
	}

	/**
	 * @return the goingDown passengers
	 */
	public ArrayList<Passenger> getGoingDown()
	{
		return goingDown;
	}

	/**
	 * Returns the state of this floor object
	 * @return	Internal state of the floor
	 */
	@Override
	public String toString()
	{
		return "Floor #" + floorNum + "\n\tOn Floor=" + onFloor.size() +
				"\n\tGoing Up=" + goingUp.size() + "\n\tGoing Down=" + 
				goingDown.size();
	}

}
