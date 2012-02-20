package cscie160.hw2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;

/**
 * Test methods for the Floor class.
 * 
 * @author	Larry Tambascio
 * @version	1.0
 */
public class FloorTest
{
	private Floor floor;
	
	@BeforeMethod
	public void beforeTest()
	{
		floor = new Floor();
	}

	/**
	 * Testing no arg constructor
	 */
	@Test
	public void testFloorInitialized()
	{
		assertEquals(floor.getPassengerCnt(), 0, "Not initialized correctly");
	}
	
	/**
	 * Testing constructor that takes a passenger count
	 */
	@Test
	public void testFloorWithPassengers()
	{
		floor = new Floor(7);
		assertEquals(floor.getPassengerCnt(), 7, "Floor with passengers not " +
				"initialized correctly");
	}
	
	/**
	 * This test will validate the basic function of unload passengers.
	 * @throws ElevatorFullException Thrown if elevator is full
	 */
	@Test
	public void testUnloadPassengers() throws ElevatorFullException
	{
		Elevator elevator = new Elevator();
		for (int i = 0; i < 3; i++)	// board three passengers for the 5th floor
			elevator.boardPassenger(5);
		elevator.setCurrentFloor(5);
		floor.setPassengerCnt(4);	// Four passengers on the floor are waiting
		
		floor.unloadPassengers(elevator);
		
		assertEquals(floor.getPassengerCnt(), 0, "Passengers were left on " +
				"the floor");
		assertEquals(elevator.getPassengerCnt(), 4, "Wrong number of passenger " +
				"got on the elevator");
		assertEquals(elevator.getDestination(1), 4, "Four not going to 1st floor");
	}
	
	/**
	 * This method will test the elevator getting overfilled exception.
	 */
	@Test
	public void testOverloadElevator()
	{
		Elevator elevator = new Elevator();
		floor.setPassengerCnt(20);
		elevator.setCurrentFloor(2);
		
		floor.unloadPassengers(elevator);
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"at max capacity");
		assertEquals(floor.getPassengerCnt(), 10, "There weren't 10 passengers " +
				"left on the floor");
	}
}
