package services.test;


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
		informator = new PassengersInformator();
		informator.setDao(dao);
	}
	@Test
	public void testGetInfo() {
		String journeyInfo = "";
		
		Journey j = new Journey(); j.setJourneyId(1); journeyInfo += String.valueOf(j.getJourneyId());
		Date birthday = new Date(); SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Passenger p = new Passenger(); p.setPassengerId(2); p.setPassengerName("name");
		p.setPassengerSurname("surname"); p.setPassengerBirthday(birthday);
		
		Ticket t = new Ticket(); t.setPassenger(p); t.setJourney(j);
		List<Ticket> tickets = new ArrayList<Ticket>(); tickets.add(t);
		
		JourneyAndPassengers input = new JourneyAndPassengers(journeyInfo, null);
		
		Mockito.when(dao.getTicketsOfJourney(j)).thenReturn(tickets);
		Mockito.when(dao.getJourney(j.getJourneyId())).thenReturn(j);
		
		JourneyAndPassengers output = informator.getInfo(input);
		String expectedResult = p.getPassengerName()+ " " +  p.getPassengerSurname() + " " + sdf.format(birthday);
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
