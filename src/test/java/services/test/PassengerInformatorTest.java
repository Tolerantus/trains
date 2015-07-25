package services.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.JourneyAndPassengers;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Passenger;
import com.entities.Ticket;
import com.service.PassengersInformator;

public class PassengerInformatorTest {
	PassengersInformator informator;
	@Mock
	Dao dao = Mockito.mock(Dao.class);
	@Before
	public void init(){
		informator = new PassengersInformator(dao);
	}
	@Test
	public void testGetInfo() {
		String journeyInfo = "";
		
		Journey j = new Journey();j.setJourney_id(1);journeyInfo+=String.valueOf(j.getJourney_id());
		Date birthday = new Date();SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Passenger p = new Passenger();p.setPassenger_id(2);p.setPassenger_name("name");p.setPassenger_surname("surname");p.setPassenger_birthday(birthday);
		
		Ticket t = new Ticket(); t.setPassenger_id(p.getPassenger_id());t.setJourney_id(j.getJourney_id());
		List<Ticket> tickets = new ArrayList<Ticket>();tickets.add(t);
		
		JourneyAndPassengers input = new JourneyAndPassengers(journeyInfo, null);
		
		Mockito.when(dao.getAllTickets()).thenReturn(tickets);
		Mockito.when(dao.getPassenger(p.getPassenger_id())).thenReturn(p);
		Mockito.when(dao.getJourney(j.getJourney_id())).thenReturn(j);
		
		JourneyAndPassengers output = informator.getInfo(input);
		String expectedResult = p.getPassenger_name()+ " " +  p.getPassenger_surname() + " " + sdf.format(birthday);
		Assert.assertTrue(expectedResult.equals(output.getPassInfo().get(0)));
		
	}
	@Test(expected=NullPointerException.class)
	public void test2(){
		informator.getInfo(null);
	}
	@Test(expected = NullPointerException.class)
	public void test3(){
		informator.getInfo(new JourneyAndPassengers(null, null));
	}

}
