package services.test;


import java.util.ArrayList;
import java.util.List;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.ChoosedJourney;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Seats;
import com.service.SeatsChecker;

public class SeatsCheckerTest {
SeatsChecker checker;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
	checker = new SeatsChecker(dao);
}
	@Test
	public void testCheck() {
		String journeyId = "1";
		Journey j = new Journey();j.setJourney_id(1);
		List<String> journeys = new ArrayList<String>();journeys.add(j.getJourney_id()+";"+"0;0");
		ChoosedJourney input = new ChoosedJourney(journeyId, journeys, null);
		Seats seats = new Seats();seats.setJourney_id(j.getJourney_id());seats.setRoute_step(0);seats.setEmpty_seats(100);
		List<Seats> seatsOnJourney = new ArrayList<Seats>(); seatsOnJourney.add(seats);
		
		Mockito.when(dao.getJourney(Integer.parseInt(journeyId))).thenReturn(j);
		Mockito.when(dao.getSeatsOnJourney(j.getJourney_id())).thenReturn(seatsOnJourney);
		
		ChoosedJourney output = checker.check(input);
		Assert.assertTrue(output.getEmptySeats()==seats.getEmpty_seats());
	}
	@Test(expected=NullPointerException.class)
	public void test2(){
		checker.check(null);
	}
	@Test(expected=NumberFormatException.class)
	public void test3(){
		checker.check(new ChoosedJourney(null,null,null));
	}
	@Test(expected=NullPointerException.class)
	public void test4(){
		checker.check(new ChoosedJourney("1",null,null));
	}
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void test5(){
		checker.check(new ChoosedJourney("1",new ArrayList<String>(),null));
	}
}
