package cscie160.hw3;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cscie160.hw3.Elevator;
import cscie160.hw3.ElevatorFullException;
import cscie160.hw3.Floor;

/**
 * Class to test the Elevator object.
 * 
 * @author Larry Tambascio
 */
public class ElevatorTest
{
	private Elevator elevator;
	
	@BeforeMethod
	public void setUp()
	{
		elevator = new Elevator();
	}
	
	/**
	 * Test the moving up from the first floor
	 * @throws ElevatorFullException Thrown if there's no room for the passenger
	 */
	@Test
	public void testFirstMove() throws ElevatorFullException
	{
		elevator.boardPassenger(2);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "test first move");
	}
	
	/**
	 * Test moving down at the bottom floor
	 */
	@Test
	public void testMoveDownAtBottom()
	{
		elevator.setCurrentDirection(false);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "test move down at bottom");
		assertEquals(elevator.isDirectionUp(), true, "direction didn't change " +
				"at the bottom");
	}
	
	/**
	 * Basic moving down test
	 * @throws ElevatorFullException Thrown if the elevator is at capacity
	 */
	@Test
	public void testMoveDown() throws ElevatorFullException
	{
		elevator.setCurrentFloor(3);
		elevator.boardPassenger(2);
		elevator.setCurrentDirection(false);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Test move down");
	}
	
	/**
	 * Test moving up at the top
	 * @throws ElevatorFullException Thrown if the elevator is at capacity.
	 */
	@Test
	public void testMoveUpAtTop() throws ElevatorFullException
	{
		elevator.boardPassenger(7);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 7, "test move up at top");
		assertEquals(elevator.isDirectionUp(), false, "direction didn't change " +
				"at the top");
	}
	
	/**
	 * Test boarding a passenger for the fifth floor.
	 * @throws ElevatorFullException Thrown if elevator is at capacity
	 */
	@Test
	public void testBoardPassenger() throws ElevatorFullException
	{
		elevator.boardPassenger(5);
		assertEquals(elevator.getPassengerCnt(), 1, "wrong number of " +
				"passengers");
		int[] dest = elevator.getDestination();
		assertEquals(dest[4], 1, "wrong number of passengers for floor");
	}
	
	/**
	 * Test boarding a passenger on an elevator that is already full.
	 * @throws ElevatorFullException Thrown when an elevator is at capacity
	 */
	@Test(expectedExceptions={ElevatorFullException.class})
	public void testBoardPassengerFull () throws ElevatorFullException
	{
		elevator.setPassengerCnt(Elevator.CAPACITY);
		elevator.boardPassenger(5);
	}
	
	/**
	 * Test the operations of the stop method.
	 */
	@Test
	public void testStop()
	{
		elevator.setDestination(3, 3);
		elevator.setPassengerCnt(3);
		elevator.setCurrentFloor(3);
		elevator.stop();
		assertEquals(elevator.getPassengerCnt(), 0, "left some passengers");
		assertEquals(elevator.getDestination(3), 0, "still some passengers on 3");
	}

	/**
	 * This test will validate the simple case where passengers on a floor have
	 * requested the elevator, but there are no passengers initially on the 
	 * elevator looking to go somewhere.
	 */
	@Test
	public void testStopOnCallingFloor()
	{
		Floor floor = new Floor(5);
		elevator.setFloor(3, floor);
		elevator.registerRequest(3);
		elevator.move();
		
		assertEquals(elevator.getCurrentFloor(), 3, "Didn't stop on third floor");
		assertEquals(elevator.getPassengerCnt(), 5, "Incorrect number of " +
				"passengers on board");
		assertEquals(elevator.getDestination(1), 5, "Incorrect passengers " +
				"headed to the first floor");
	}
	
	/**
	 * This method will test that when we make a request for a floor that the
	 * elevator stops on that floor and that the request is subsequently 
	 * cleared.
	 */
	@Test
	public void testRequests()
	{
		elevator.registerRequest(3);
		
		assertEquals(elevator.getCallingFloor(3), true, "Should be registered");
		
		elevator.move();
		
		assertEquals(elevator.getCurrentFloor(), 3, "Arrived on 3");
		assertEquals(elevator.getCallingFloor(3), false, "Should be cleared");
	}
	
	/**
	 * This test will the main scenario where there are many floors with various
	 * people waiting for an elevator.  A bunch of people will get on at the 
	 * first floor going to different floors.  All the appropriate stops will be
	 * made along the way.  The elevator will get full at some point, so two 
	 * tripes will be needed.
	 * 
	 * @throws ElevatorFullException Thrown when a passenger tries to board a 
	 * 									full elevator.
	 */
	@Test
	public void testMultipleFloorStops() throws ElevatorFullException
	{
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
		for (int i = 0; i < 6; i++)
			elevator.boardPassenger(3);
		elevator.boardPassenger(4);
		elevator.boardPassenger(7);
		
		// Validate initial setup
		assertEquals(elevator.getPassengerCnt(), 8, "Number of initial passengers");
		assertEquals(elevator.getDestination(3), 6, "Passengers going to 3");
		assertEquals(elevator.getDestination(4), 1, "Passengers going to 4");
		assertEquals(elevator.getDestination(7), 1, "Passengers going to 7");
		assertEquals(elevator.getFloor(2).getPassengerCnt(), 4, "second floor " +
				"not setup right");
		assertEquals(elevator.getFloor(4).getPassengerCnt(), 6, "fourth floor " +
				"not setup right");
		assertEquals(elevator.getFloor(6).getPassengerCnt(), 5, "sixth floor " +
				"not setup right");
		
		// Now do the first move and test results
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Should have stopped on " +
				"floor 2");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"maxed out after first move");
		floor = elevator.getFloor(2);
		assertEquals(floor.getPassengerCnt(), 2, "There should " +
				"be 2 passengers left on the second floor");
		
		// Do the next move
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 3, "Should have stopped on " +
				"floor 3");
		assertEquals(elevator.getPassengerCnt(), 4, "Only 4 should be left");
		floor = elevator.getFloor(3);
		assertEquals(floor.getPassengerCnt(), 0, "There shouldn't be any " +
				"passengers left on floor 3");
		
		// Next move; on 4th floor 1 gets off and six get on.
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "Should stop at 4");
		assertEquals(elevator.getPassengerCnt(), 9, "Expecting 9 passengers");
		floor = elevator.getFloor(4);
		assertEquals(floor.getPassengerCnt(), 0, "Should be no passengers " +
				"waiting on floor 4");
		
		// Next move; on 6th floor 1 get on and 4 remain
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "Should stop at floor 6");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should " +
				"be at max capacity again");
		floor = elevator.getFloor(6);
		assertEquals(floor.getPassengerCnt(), 4, "There should still be 4 " +
				"passengers left on the floor");
		
		// Next move; on 7th floor 1 gets off
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), Elevator.FLOORS, "Should be " +
				"at the top now");
		assertEquals(elevator.getPassengerCnt(), 9, "Should be 9 left after" +
				"we get to the top");
		
		// Start moving down, same situation on the 6th floor, pick up one more
		// from the sixth
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "Should stop at floor 6");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should " +
				"be at max capacity again");
		floor = elevator.getFloor(6);
		assertEquals(floor.getPassengerCnt(), 3, "There should still be 3 " +
				"passengers left on the floor");
		
		// Next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Should have stopped on " +
				"floor 2");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"maxed out after first move");
		floor = elevator.getFloor(2);
		assertEquals(floor.getPassengerCnt(), 2, "There should " +
				"be 2 passengers left on the second floor");
		
		// move to bottom floor
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "Should be at the bottom now");
		assertEquals(elevator.getPassengerCnt(), 0, "Should be empty now");
		assertEquals(elevator.isDirectionUp(), true, "Should be heading back up now");
		
		// start moving back up
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Stopping at 2 again");
		assertEquals(elevator.getPassengerCnt(), 2, "Picked up 2 passenger");
		floor = elevator.getFloor(2);
		assertEquals(floor.getPassengerCnt(), 0, "Should be nobody left");
		
		// next move up
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "Stopping on 6");
		assertEquals(elevator.getPassengerCnt(), 5, "Should have picked up 3 more");
		floor = elevator.getFloor(6);
		assertEquals(floor.getPassengerCnt(), 0, "Should be nobody left");
		
		// last move up
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), Elevator.FLOORS, "Should be " +
				"at the top now");
		assertEquals(elevator.getPassengerCnt(), 5, "Should still only have 5");
		
		// move to bottom floor again
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "Should be at the bottom " +
				"now again");
		assertEquals(elevator.getPassengerCnt(), 0, "Should be empty now again");
		assertEquals(elevator.isDirectionUp(), true, "Should be ready to head " +
				"back up now");
	}

}
