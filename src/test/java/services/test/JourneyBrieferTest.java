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
	briefer = new JourneyBriefer(dao);
}
	@Test
	public void test() {
		List<Journey> jours = new ArrayList<Journey>();
		Route r = new Route();r.setRoute_id(1);r.setRoute_name("name");
		Journey j = new Journey(); j.setRoute_id(r.getRoute_id());jours.add(j);
		String journeyName = j.getJourney_id()+" "+ r.getRoute_name();
		
		Mockito.when(dao.getAllJourneys()).thenReturn(jours);
		Mockito.when(dao.getRoute(r.getRoute_id())).thenReturn(r);
		AllJourneysInfo info = new AllJourneysInfo(null);
		info = briefer.getInfo(info);
		Assert.assertTrue(info.getJourneys().get(0).equals(journeyName));
	}
	
	@Test(expected=NullPointerException.class)
	public void test2(){
		List<Journey> jours = new ArrayList<Journey>();
		Route r = new Route();r.setRoute_id(1);r.setRoute_name("name");
		Journey j = new Journey(); j.setRoute_id(r.getRoute_id());jours.add(j);
		
		Mockito.when(dao.getAllJourneys()).thenReturn(jours);
		Mockito.when(dao.getRoute(r.getRoute_id())).thenReturn(r);
		briefer.getInfo(null);
	}

}
