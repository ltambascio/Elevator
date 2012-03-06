package cscie160.hw3;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cscie160.hw3.Elevator;
import cscie160.hw3.ElevatorFullException;
import cscie160.hw3.Floor;

/**
 * Class to test the Elevator object.
 * 
 * @author Larry Tambascio
 * @version	1.3
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
		Passenger pass = new Passenger(1,2);
		elevator.boardPassenger(pass);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "test first move");
		assertEquals(elevator.getPassengerCnt(), 0, "there should be no " +
				"passengers left");
	}
	
	/**
	 * Test moving down at the bottom floor
	 * @throws ElevatorFullException Thrown if someone tries to board a full elevator
	 */
	@Test
	public void testMoveDownAtBottom() throws ElevatorFullException
	{
		elevator.setCurrentDirection(false);
		elevator.setCurrentFloor(2);
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
		Passenger pass = new Passenger(3,2);
		elevator.setCurrentFloor(3);
		elevator.boardPassenger(pass);
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
		Passenger pass = new Passenger(1,7);
		elevator.boardPassenger(pass);
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
		ArrayList<Passenger> floorPassengers;
		
		Passenger pass = new Passenger(1,5);
		elevator.boardPassenger(pass);
		assertEquals(elevator.getPassengerCnt(), 1, "wrong number of " +
				"passengers");
		floorPassengers = elevator.getFloorPassengers(5);
//		int[] dest = elevator.getDestination();
		assertEquals(floorPassengers.size(), 1, "wrong number of passengers for floor");
	}
	
	/**
	 * Test boarding a passenger on an elevator that is already full.
	 * @throws ElevatorFullException Thrown when an elevator is at capacity
	 */
	@Test(expectedExceptions=ElevatorFullException.class)
	public void testBoardPassengerFull () throws ElevatorFullException
	{
		Passenger pass = new Passenger(1,5);
		elevator.setPassengerCnt(Elevator.CAPACITY);
		elevator.boardPassenger(pass);
	}
	
	/**
	 * Test the operations of the stop method.
	 * @throws ElevatorFullException if elevator is full
	 */
	@Test
	public void testStop() throws ElevatorFullException
	{
		Passenger pass = new Passenger(1,3);
		for (int i = 0; i < 3; i++)
			elevator.boardPassenger(pass);
//		elevator.setDestination(3, 3);
		elevator.setPassengerCnt(3);
		elevator.setCurrentFloor(3);
		elevator.stop();
		assertEquals(elevator.getPassengerCnt(), 0, "left some passengers");
		assertEquals(elevator.getFloorPassengers(3).size(), 0, "still some passengers on 3");
	}

	/**
	 * This test will validate the simple case where passengers on a floor have
	 * requested the elevator, but there are no passengers initially on the 
	 * elevator looking to go somewhere.
	 */
	@Test(groups="unit")
	public void testStopOnCallingFloor()
	{
		Floor floor = new Floor();
		
		for (int i=0; i < 5; i++)
			floor.getGoingDown().add(new Passenger(3,1));
		floor.setFloorNum(3);
		elevator.setFloor(3, floor);
		elevator.setCurrentDirection(Elevator.DOWN);
		elevator.setCurrentFloor(6);
		elevator.registerRequest(3, false);
		elevator.move();
		
		assertEquals(elevator.getCurrentFloor(), 3, "Didn't stop on third floor");
		assertEquals(elevator.getPassengerCnt(), 5, "Incorrect number of " +
				"passengers on board");
		assertEquals(elevator.getFloorPassengers(1).size(), 5, "Incorrect passengers " +
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
		elevator.registerRequest(3, true);
		
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
	 * trips will be needed.
	 * 
	 * @throws ElevatorFullException Thrown when a passenger tries to board a 
	 * 									full elevator.
	 */
	@Test(groups="integration")
	public void testMultipleFloorStops() throws ElevatorFullException
	{
		Floor floor;
		
		// start out by loading people into floors and registering requests for
		// those floors.
		floor  = elevator.getFloor(2);
		for (int i=0; i < 4; i++)
			floor.getGoingDown().add(new Passenger(2,1));
		elevator.registerRequest(2, Elevator.DOWN);
		
		floor = elevator.getFloor(4);
		for (int i=0; i < 6; i++)
			floor.getGoingDown().add(new Passenger(4,1));
		elevator.registerRequest(4, Elevator.DOWN);
		
		floor = elevator.getFloor(6);
		for (int i=0; i < 5; i++)
			floor.getGoingDown().add(new Passenger(6,1));
		elevator.registerRequest(6, Elevator.DOWN);
		
		// now load a bunch of passengers for different floors
		// With 6 bound for the third floor, we'll definitely fill the elevator
		// on the third floor.
		for (int i = 0; i < 6; i++)
			elevator.boardPassenger(new Passenger(1, 3));
		elevator.boardPassenger(new Passenger(1, 4));
		elevator.boardPassenger(new Passenger(1, 7));
		
		// Validate initial setup
		assertEquals(elevator.getPassengerCnt(), 8, "Number of initial passengers");
		assertEquals(elevator.getFloorPassengers(3).size(), 6, "Passengers going to 3");
		assertEquals(elevator.getFloorPassengers(4).size(), 1, "Passengers going to 4");
		assertEquals(elevator.getFloorPassengers(7).size(), 1, "Passengers going to 7");
		assertEquals(elevator.getFloor(2).getGoingDown().size(), 4, "second floor " +
				"not setup right");
		assertEquals(elevator.getFloor(4).getGoingDown().size(), 6, "fourth floor " +
				"not setup right");
		assertEquals(elevator.getFloor(6).getGoingDown().size(), 5, "sixth floor " +
				"not setup right");
		
		// Now do the first move and test results
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 3, "Should have stopped on " +
				"floor 3");
		assertEquals(elevator.getPassengerCnt(), 2, "Only 2 should be left");
		floor = elevator.getFloor(3);
		assertEquals(floor.getOnFloor().size(), 6, "six should be on floor");
		
		// Next move; on 4th floor 1 gets off and none get on.
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "Should stop at 4");
		assertEquals(elevator.getPassengerCnt(), 1, "Expecting 1 passengers");
		floor = elevator.getFloor(4);
		assertEquals(floor.getOnFloor().size(), 1, "Should be 1 passengers " +
				"waiting on floor 4");
		
		// Next move; on 7th floor 1 gets off
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), Elevator.FLOORS, "Should be " +
				"at the top now");
		assertEquals(elevator.getPassengerCnt(), 0, "Should be no passengers left");
		floor = elevator.getFloor(7);
		assertEquals(floor.getOnFloor().size(), 1, "Should be 1 on floor now");
		
		assertEquals(elevator.isDirectionUp(), false, "Going down now");
		
		// Start moving down, same situation on the 6th floor, pick up one more
		// from the sixth
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "Should stop at floor 6");
		assertEquals(elevator.getPassengerCnt(), 5, "Picked up 5 from 6th");
		floor = elevator.getFloor(6);
		assertEquals(floor.getGoingDown().size(), 0, "There should still be 0 " +
				"passengers left on the floor going down");
		
		// Next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "Should have stopped on " +
				"floor 4");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"full on this trip down");
		floor = elevator.getFloor(4);
		assertEquals(floor.getGoingDown().size(), 1, "There should " +
				"be 1 passenger left on the 4th floor");
		
		// Next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Should have stopped on " +
				"floor 2");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "Should be " +
				"full on this trip down");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingDown().size(), 4, "There should " +
				"be 1 passenger left on the 2nd floor");
		
		// move to bottom floor
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "Should be at the bottom now");
		assertEquals(elevator.getPassengerCnt(), 0, "Should be empty now");
		assertEquals(elevator.isDirectionUp(), true, "Should be heading back up now");
		
		// start moving back up
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 7, "At the top again");
		assertEquals(elevator.getPassengerCnt(), 0, "Should still be empty");
		assertEquals(elevator.isDirectionUp(), false, "Heading back down again");
		
		// next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "stopping at 4");
		assertEquals(elevator.getPassengerCnt(), 1, "retrieved last passenger");
		floor = elevator.getFloor(4);
		assertEquals(floor.getGoingDown().size(), 0, "one left going down");
		
		// next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "stopping at 2");
		assertEquals(elevator.getPassengerCnt(), 5, "picking up all on 2");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingDown().size(), 0, "everyone still on 2");
		
		// next move down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "Should be at the bottom " +
				"now again");
		assertEquals(elevator.getPassengerCnt(), 0, "Should be empty now again");
		assertEquals(elevator.isDirectionUp(), true, "Should be ready to head " +
				"back up now");
	}
	
	/**
	 * This test will follow the demonstration requirements for homework 3.
	 * 
	 * @throws ElevatorFullException Thrown when someone tries to board a full 
	 * 								elevator.
	 */
	@Test(groups="integration")
	public void testUpAndDownStops() throws ElevatorFullException
	{
		Floor floor;
		
		// Start with a bunch of passengers on the elevator bound for different
		// floors
		for (int i = 0; i < 3; i++)
			elevator.boardPassenger(new Passenger(1, 3));
		for (int i = 0; i < 3; i++)
			elevator.boardPassenger(new Passenger(1, 4));
		for (int i = 0; i < 3; i++)
			elevator.boardPassenger(new Passenger(1, 7));
		
		// start out by loading people into floors and registering requests for
		// those floors in different directions.
		
		// second floor
		// 4 passengers going down to 1
		// 3 passengers going up to 5
		initFloorForTest(2, 5, 3, 1, 4);
		
		// third floor
		// 2 going down to 1
		// 3 going up to 4
		initFloorForTest(3, 4, 3, 1, 2);
		
		// fourth floor
		// 2 going down to 2
		// 1 going up to 5
		initFloorForTest(4, 5, 1, 2, 2);
		
		// fifth floor
		// 1 going down to 3
		// 1 going up to 7
		initFloorForTest(5, 7, 1, 3, 1);
		
		// sixth floor
		// 3 going up to 7
		// 5 going down to 4
		initFloorForTest(6, 7, 3, 4, 5);
		
		// seventh floor
		// 0 going up
		// 5 going down to 1
		initFloorForTest(7, 0, 0, 1, 5);
		
		// Validate initial setup
		assertEquals(elevator.getPassengerCnt(), 9, "Number of initial passengers");
		assertEquals(elevator.getFloorPassengers(3).size(), 3, "Passengers going to 3");
		assertEquals(elevator.getFloorPassengers(4).size(), 3, "Passengers going to 4");
		assertEquals(elevator.getFloorPassengers(7).size(), 3, "Passengers going to 7");

		validateFloor(2, 5, 3, 1, 4);
		validateFloor(3, 4, 3, 1, 2);
		validateFloor(4, 5, 1, 2, 2);
		validateFloor(5, 7, 1, 3, 1);
		validateFloor(6, 7, 3, 4, 5);
		validateFloor(7, 0, 0, 1, 5);
		
		// Do first move up
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "stopping at 2 going up");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "all full");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingUp().size(), 2, "2 still waiting up");
		assertEquals(floor.getOnFloor().size(), 0, "nobody on 2");
		assertEquals(floor.getGoingDown().size(), 4, "4 waiting to go down");
		
		// move 2
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 3, "stopping at 3 going up");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "3 got off " +
				"and 3 got on");
		floor = elevator.getFloor(3);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 3, "3 got off");
		assertEquals(floor.getGoingDown().size(), 2, "2 waiting to go down");
		
		// move 3
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "stopping at 4 going up");
		assertEquals(elevator.getPassengerCnt(), 5, "6 got off and 1 got on");
		floor = elevator.getFloor(4);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 6, "6 got off");
		assertEquals(floor.getGoingDown().size(), 2, "2 waiting to go down");
		
		// move 4
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 5, "stopping at 5 going up");
		assertEquals(elevator.getPassengerCnt(), 4, "2 got off and 1 got on");
		floor = elevator.getFloor(5);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 2, "2 got off");
		assertEquals(floor.getGoingDown().size(), 1, "1 waiting to go down");
		
		// move 5
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "stopping at 6 going up");
		assertEquals(elevator.getPassengerCnt(), 7, "0 got off and 3 got on");
		floor = elevator.getFloor(6);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 0, "0 got off");
		assertEquals(floor.getGoingDown().size(), 5, "5 waiting to go down");
		
		// move 6 to top
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 7, "stopping at 7 going up");
		assertEquals(elevator.getPassengerCnt(), 5, "7 got off and 5 got on");
		floor = elevator.getFloor(7);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 7, "7 got off");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		assertEquals(elevator.isDirectionUp(), Elevator.DOWN, "going down now");
		
		// move 7 heading back down
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 6, "stopping at 6 going down");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "0 got off and 5 got on");
		floor = elevator.getFloor(6);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 0, "0 got off");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");

		// move 8
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 5, "stopping at 5 going down");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "0 got off and 0 got on");
		floor = elevator.getFloor(5);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 2, "2 already on floor");
		assertEquals(floor.getGoingDown().size(), 1, "1 waiting to go down");

		// move 9
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 4, "stopping at 4 going down");
		assertEquals(elevator.getPassengerCnt(), 7, "5 got off and 2 got on");
		floor = elevator.getFloor(4);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 11, "6 already on floor " +
				"and 5 got on");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");

		// move 10
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 3, "stopping at 3 going down");
		assertEquals(elevator.getPassengerCnt(), 9, "0 got off and 2 got on");
		floor = elevator.getFloor(3);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 3, "3 already on floor");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");

		// move 11
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "stopping at 2 going down");
		assertEquals(elevator.getPassengerCnt(), Elevator.CAPACITY, "2 got off " +
				"and 3 got on");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingUp().size(), 2, "2 still waiting up");
		assertEquals(floor.getOnFloor().size(), 2, "2 already on floor");
		assertEquals(floor.getGoingDown().size(), 1, "1 waiting to go down");

		// move 12
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "stopping at 1 going down");
		assertEquals(elevator.getPassengerCnt(), 0, "10 got off and 0 got on");
		floor = elevator.getFloor(1);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 10, "10 got off");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		assertEquals(elevator.isDirectionUp(), Elevator.UP, "Going back up again");

		// move 13
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "stopping at 2 going up");
		assertEquals(elevator.getPassengerCnt(), 2, "0 got off and 2 got on");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 2, "2 got off");
		assertEquals(floor.getGoingDown().size(), 1, "1 waiting to go down");
		
		// move 14
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 5, "stopping at 5 going up");
		assertEquals(elevator.getPassengerCnt(), 0, "2 got off and 0 got on");
		floor = elevator.getFloor(5);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 4, "2 already on floor and " +
				"2 got off");
		assertEquals(floor.getGoingDown().size(), 1, "1 waiting to go down");
		
		// move 15
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 7, "stopping at 5 going up");
		assertEquals(elevator.getPassengerCnt(), 0, "nobody on elevator");
		floor = elevator.getFloor(7);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 7, "7 already on floor");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		assertEquals(elevator.isDirectionUp(), Elevator.DOWN, "going down now");
		
		// move 16
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 5, "stopping at 5 going down");
		assertEquals(elevator.getPassengerCnt(), 1, "1 got on");
		floor = elevator.getFloor(5);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 4, "4 already on floor");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		// move 17
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 3, "stopping at 3 going down");
		assertEquals(elevator.getPassengerCnt(), 0, "1 got off");
		floor = elevator.getFloor(3);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 4, "4 already on floor");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		// move 18
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "stopping at 2 going down");
		assertEquals(elevator.getPassengerCnt(), 1, "1 got on");
		floor = elevator.getFloor(2);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 2, "2 already on floor");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
		
		// move 19
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 1, "stopping at 1 going down");
		assertEquals(elevator.getPassengerCnt(), 0, "1 got off and 0 got on");
		floor = elevator.getFloor(1);
		assertEquals(floor.getGoingUp().size(), 0, "0 still waiting up");
		assertEquals(floor.getOnFloor().size(), 11, "10 already there and 1 " +
				"got off");
		assertEquals(floor.getGoingDown().size(), 0, "0 waiting to go down");
	}

	/**
	 * Validate particular values for a given floor.
	 * 
	 * @param	floorNum	Floor to modify
	 * @param	upFloor		Floor people are going up to
	 * @param	upCount		Number of people going up
	 * @param	downFloor	Floor people are going down to
	 * @param	downCount	Number of people going down
	 */
	private void validateFloor(int floorNum, int upFloor, int upCount,
			int downFloor, int downCount)
	{
		Floor floor;
		
		floor = elevator.getFloor(floorNum);
		
		assertEquals(floor.getFloorNum(), floorNum, "Floor number");
		if (downCount > 0)
		{
			assertEquals(floor.getGoingDown().size(), downCount, "Down count");
			assertEquals(floor.getGoingDown().get(0).getDestinationFloor(), downFloor,
					"Down floor");
		}
		if (upCount > 0)
		{
			assertEquals(floor.getGoingUp().size(), upCount, "Up count");
			assertEquals(floor.getGoingUp().get(0).getDestinationFloor(), upFloor,
					"Up floor");
		}
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * Initialize a floor for testing
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
		
		floor = elevator.getFloor(floorNum);
		for (int i=0; i < downCount; i++)
			floor.getGoingDown().add(new Passenger(floorNum, downFloor));
		for (int i=0; i < upCount; i++)
			floor.getGoingUp().add(new Passenger(floorNum, upFloor));
		if (downCount > 0)
			elevator.registerRequest(floorNum, Elevator.DOWN);
		if (upCount > 0)
			elevator.registerRequest(floorNum, Elevator.UP);
	}

}
