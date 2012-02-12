package cscie160.hw2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cscie160.hw2.Elevator;
import static org.testng.Assert.assertEquals;

/**
 * Class to test the Elevator object.
 * 
 * @author Larry Tambascio
 */
public class TestElevator
{
	private Elevator elevator;
	
	@BeforeMethod
	public void setUp()
	{
		elevator = new Elevator();
	}
	
	/**
	 * Test the moving up from the first floor
	 */
	@Test
	public void testFirstMove()
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
	 */
	@Test
	public void testMoveDown()
	{
		elevator.setCurrentFloor(3);
		elevator.boardPassenger(2);
		elevator.setCurrentDirection(false);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 2, "Test move down");
	}
	
	/**
	 * Test moving up at the top
	 */
	@Test
	public void testMoveUpAtTop()
	{
		elevator.boardPassenger(7);
		elevator.move();
		assertEquals(elevator.getCurrentFloor(), 7, "test move up at top");
		assertEquals(elevator.isDirectionUp(), false, "direction didn't change " +
				"at the top");
	}
	
	/**
	 * Test boarding a passenger for the fifth floor.
	 */
	@Test
	public void testBoardPassenger()
	{
		elevator.boardPassenger(5);
		assertEquals(elevator.getPassengerCnt(), 1, "wrong number of " +
				"passengers");
		int[] dest = elevator.getDestination();
		assertEquals(dest[4], 1, "wrong number of passengers for floor");
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

}
