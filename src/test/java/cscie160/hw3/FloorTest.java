package cscie160.hw3;

import java.util.ArrayList;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cscie160.hw3.Elevator;
import cscie160.hw3.ElevatorFullException;
import cscie160.hw3.Floor;

import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;

/**
 * Test methods for the Floor class.
 * 
 * @author	Larry Tambascio
 * @version	1.2
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
		assertEquals(floor.getFloorNum(), 1, "Not initialized correctly");
	}
	
	/**
	 * Testing constructor that takes a passenger count
	 */
	@Test
	public void testFloorWithPassengers()
	{
		floor = new Floor(7);
		assertEquals(floor.getFloorNum(), 7, "Floor number initialized " +
				"correctly");
	}
	
	/**
	 * This test will validate the basic function of unload passengers.
	 * @throws ElevatorFullException Thrown if elevator is full
	 */
	@Test
	public void testUnloadPassengers() throws ElevatorFullException
	{
		ArrayList<Passenger> up;
		
		Elevator elevator = new Elevator();
		for (int i = 0; i < 3; i++)	// board three passengers for the 5th floor
			elevator.boardPassenger(new Passenger(1, 5));
		elevator.setCurrentFloor(5);
		floor.setFloorNum(5);
		//floor.setPassengerCnt(4);
		// Four passengers on the floor are waiting
		up = floor.getGoingUp();
		for (int i = 0; i < 4; i++)
			up.add(new Passenger (4,6));
		
		floor.unloadPassengers(elevator);
		
		assertEquals(floor.getOnFloor().size(), 3, "3 passengers should be on floor");
		assertEquals(floor.getGoingUp().size(), 0, "no passengers left going up");
		assertEquals(elevator.getPassengerCnt(), 4, "Wrong number of passenger " +
				"got on the elevator");
		assertEquals(elevator.getFloorPassengers(6).size(), 4, "Four should be " +
				"headed to the 6th floor");
	}
	
	/**
	 * This method will test the elevator getting over filled exception.
	 */
	@Test(groups="unit")
	public void testOverloadElevator()
	{
		ArrayList<Passenger> up;
		
		Elevator elevator = new Elevator();
		up = floor.getGoingUp();
		for (int i=0; i<20; i++)
			up.add(new Passenger(2, 6));
		elevator.setCurrentFloor(2);
		
		floor.unloadPassengers(elevator);
		
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"at max capacity");
		assertEquals(floor.getGoingUp().size(), 10, "There should be 10 " +
				"passengers left on the floor");
	}
}
