package services.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.AllJourneysInfo;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Route;
import com.service.JourneyBriefer;

public class JourneyBrieferTest {
JourneyBriefer briefer;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
	briefer = new JourneyBriefer();
	briefer.setDao(dao);
}
	@Test
	public void test() {
		List<Journey> jours = new ArrayList<Journey>();
		Route r = new Route();r.setRouteId(1); r.setRouteName("name");
		Journey j = new Journey(); j.setRoute(r); jours.add(j);
		String journeyName = j.getJourneyId()+" "+ r.getRouteName();
		
		Mockito.when(dao.getAllJourneys()).thenReturn(jours);
		AllJourneysInfo info = new AllJourneysInfo(null);
		info = briefer.getInfo(info);
		Assert.assertTrue(info.getJourneys().get(0).equals(journeyName));
	}
	
	@Test(expected=NullPointerException.class)
	public void test2(){
		List<Journey> jours = new ArrayList<Journey>();
		Route r = new Route(); r.setRouteId(1); r.setRouteName("name");
		Journey j = new Journey(); j.setRoute(r); jours.add(j);
		
		Mockito.when(dao.getAllJourneys()).thenReturn(jours);
		briefer.getInfo(null);
	}

}
