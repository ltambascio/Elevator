package cscie160.hw3;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;

/**
 * Passenger class test
 * 
 * @author Larry Tambascio
 * @version	1.0
 */
public class PassengerTest
{
	private Passenger passenger;
	
	@BeforeMethod
	public void setUp()
	{
		passenger = new Passenger(1,Elevator.FLOORS);
	}

	@Test
	public void getCurrentFloor()
	{
		assertEquals(passenger.getCurrentFloor(), 1, "default current floor");
	}

	@Test
	public void getDestinationFloor()
	{
		assertEquals(passenger.getDestinationFloor(), 7, "get destination floor");
	}

	@Test
	public void setCurrentFloor()
	{
		passenger.setCurrentFloor(2);
		assertEquals(passenger.getCurrentFloor(), 2, "set current floor");
	}

	@Test
	public void setDestinationFloor()
	{
		passenger.setDestinationFloor(5);
		assertEquals(passenger.getDestinationFloor(), 5, "set destination floor");
	}
}
