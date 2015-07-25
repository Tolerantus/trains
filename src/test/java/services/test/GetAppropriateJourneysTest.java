package services.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.JourneysInfo;
import com.dto.StationsForSheduling;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Journey;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
import com.service.GetAppropriateJourneys;

public class GetAppropriateJourneysTest {
	
	GetAppropriateJourneys getter;
	@Mock
	private Dao dao = Mockito.mock(Dao.class);
	
	@Before
	public void createChecker(){
		getter = new GetAppropriateJourneys(dao);
	}
	
	@Test(expected=NullPointerException.class)
	public void test() throws ParseException {
		getter.getJourneys(null);
	}
	
	@Test
	public void testSimpleSheduling() throws ParseException {
		Station station = new Station();
		station.setStation_name("Station");
		station.setStation_id(1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm  dd MMM", Locale.US);
		
		Route r = new Route();r.setRoute_id(2);
		List<Route> routes= new ArrayList<Route>();routes.add(r);
		Set<Station> stOnRoute = new HashSet<Station>();stOnRoute.add(station);
		
		Journey journey = new Journey();journey.setRoute_id(r.getRoute_id());journey.setTime_dep(new Date(new Date().getTime()+100*60*1000));
		List<Journey> journeys = new ArrayList<Journey>();journeys.add(journey);journey.setJourney_id(3);
		
		Shedule step = new Shedule();step.setDirection_id(5);step.setStep(0);
		List<Shedule> steps = new ArrayList<Shedule>();steps.add(step);
		
		Direction d = new Direction();d.setSt_dep(station.getStation_id());d.setSt_arr(4);
		d.setTime((new Date()).getTime()+1);
		StringBuilder journeyData = new StringBuilder(String.valueOf(journey.getJourney_id()));
		journeyData.append(";");
		journeyData.append(sdf1.format(journey.getTime_dep()));
		journeyData.append(";");
		journeyData.append(station.getStation_name());
		journeyData.append(";");
		journeyData.append(station.getStation_name());
		
		Mockito.when(dao.getAllRoutes()).thenReturn(routes);
		Mockito.when(dao.getAllJourneys()).thenReturn(journeys);
		Mockito.when(dao.getRoute(Mockito.anyInt())).thenReturn(r);
		Mockito.when(dao.getAllStationsOnRoute(Mockito.anyInt())).thenReturn(stOnRoute);
		Mockito.when(dao.getDirection(Mockito.anyInt())).thenReturn(d);
		Mockito.when(dao.getShedulesOfRoute(Mockito.anyInt())).thenReturn(steps);
		Mockito.when(dao.getStation(Mockito.anyInt())).thenReturn(station);
		
		String result = getter.simpleSheduling(station).getJourneyStringData().get(0);
		String expectedresult = journeyData.toString();
		Assert.assertTrue(result.equals(expectedresult));
	}
	
	@Test
	public void testGetAppropriateJourneys() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm dd MMM", Locale.US);

		Station st_dep = new Station();st_dep.setStation_id(0);
		Station st_arr = new Station();st_arr.setStation_id(1);
		String date = sdf.format(new Date());
				
		Route r = new Route();r.setRoute_id(2);
		List<Route> routes= new ArrayList<Route>();routes.add(r);
		Set<Station> stOnRoute = new HashSet<Station>();stOnRoute.add(st_dep);stOnRoute.add(st_arr);
		
		Journey journey = new Journey();journey.setRoute_id(r.getRoute_id());
		journey.setTime_dep(new Date(new Date().getTime()+100*60*1000));
		List<Journey> journeys = new ArrayList<Journey>();journeys.add(journey);journey.setJourney_id(3);
		
		Shedule step = new Shedule();step.setDirection_id(5);step.setStep(0);
		List<Shedule> steps = new ArrayList<Shedule>();steps.add(step);
		
		Direction d = new Direction();d.setSt_dep(st_dep.getStation_id());d.setSt_arr(st_arr.getStation_id());
		d.setTime((new Date()).getTime());d.setCost(1000);
		StringBuilder journeyData = new StringBuilder(String.valueOf(journey.getJourney_id()));
		journeyData.append(";");
		journeyData.append(sdf1.format(journey.getTime_dep()));
		journeyData.append(";");
		journeyData.append(sdf1.format(new Date(journey.getTime_dep().getTime()+d.getTime())));
		journeyData.append(";");
		journeyData.append(String.valueOf(1000d));
		
		
		StringBuilder info = new StringBuilder(String.valueOf(journey.getJourney_id()));
		info.append(";");
		info.append(String.valueOf(step.getStep()));
		info.append(";");
		info.append(String.valueOf(step.getStep()));
		info.append(";");
		info.append(st_dep.getStation_id());
		info.append(";");
		info.append(st_arr.getStation_id());
		
		
		Mockito.when(dao.getAllRoutes()).thenReturn(routes);
		Mockito.when(dao.getAllJourneys()).thenReturn(journeys);
		Mockito.when(dao.getRoute(Mockito.anyInt())).thenReturn(r);
		Mockito.when(dao.getAllStationsOnRoute(Mockito.anyInt())).thenReturn(stOnRoute);
		Mockito.when(dao.getDirection(Mockito.anyInt())).thenReturn(d);
		Mockito.when(dao.getShedulesOfRoute(Mockito.anyInt())).thenReturn(steps);
		
		JourneysInfo result = getter.getAppropriateJourneys(st_dep, st_arr, date);
		String expectedResult1 = journeyData.toString();
		String expectedResult2 = info.toString();
		Assert.assertTrue(result.getJourneyStringData().get(0).equals(expectedResult1));
		Assert.assertTrue(result.getJourneyHelpInfo().get(0).equals(expectedResult2));
	}
	@Test(expected=NullPointerException.class)
	public void test4() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Station st_dep = new Station();st_dep.setStation_id(0);
		Station st_arr = new Station();st_arr.setStation_id(1);
		String date = sdf.format(new Date());
				
		Route r = new Route();r.setRoute_id(2);
		List<Route> routes= new ArrayList<Route>();routes.add(r);
		Set<Station> stOnRoute = new HashSet<Station>();stOnRoute.add(st_dep);stOnRoute.add(st_arr);
		
		Journey journey = new Journey();journey.setRoute_id(r.getRoute_id());
		journey.setTime_dep(new Date(new Date().getTime()+100*60*1000));
		List<Journey> journeys = new ArrayList<Journey>();journeys.add(journey);journey.setJourney_id(3);
		
		Shedule step = new Shedule();step.setDirection_id(5);step.setStep(0);
		List<Shedule> steps = new ArrayList<Shedule>();steps.add(step);
		
		Direction d = new Direction();d.setSt_dep(st_dep.getStation_id());d.setSt_arr(st_arr.getStation_id());
		d.setTime((new Date()).getTime());d.setCost(1000);
		
		
		Mockito.when(dao.getAllRoutes()).thenReturn(routes);
		Mockito.when(dao.getAllJourneys()).thenReturn(journeys);
		Mockito.when(dao.getRoute(Mockito.anyInt())).thenReturn(r);
		Mockito.when(dao.getAllStationsOnRoute(Mockito.anyInt())).thenReturn(stOnRoute);
		Mockito.when(dao.getDirection(Mockito.anyInt())).thenReturn(d);
		Mockito.when(dao.getShedulesOfRoute(Mockito.anyInt())).thenReturn(steps);
		
		JourneysInfo result = getter.getAppropriateJourneys(st_dep, null, date);
		Assert.assertTrue(result.getJourneyStringData().isEmpty());
		Assert.assertTrue(result.getJourneyHelpInfo().isEmpty());
		
		result = getter.getAppropriateJourneys(null, st_arr, date);
		Assert.assertTrue(result.getJourneyStringData().isEmpty());
		Assert.assertTrue(result.getJourneyHelpInfo().isEmpty());
	}
	@Test
	public void test5() throws ParseException{
		JourneysInfo result = getter.getJourneys(new StationsForSheduling(null, null, null, null));
		Assert.assertTrue(result.getJourneyStringData()==null);
		Assert.assertTrue(result.getJourneyHelpInfo()==null);
	}
}
