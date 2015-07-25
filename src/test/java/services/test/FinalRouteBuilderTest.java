package services.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.HoursMinutesCost;
import com.dto.NewRouteSummary;
import com.dto.RequiredDataForNewRoute;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
import com.service.FinalRouteBuilder;

public class FinalRouteBuilderTest {
	FinalRouteBuilder builder;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
builder = new FinalRouteBuilder(dao);
}
	@Test
	public void test() {
		HoursMinutesCost data = new HoursMinutesCost("1","1","1000");
		List<HoursMinutesCost> hdc = new ArrayList<HoursMinutesCost>();hdc.add(data);
		Route new_r = new Route();new_r.setRoute_name("new");
		Station s1 = new Station();s1.setStation_name("s1");s1.setStation_id(1);
		Station s2 = new Station();s2.setStation_name("s2");s2.setStation_id(2);
		Direction d = new Direction(3,s1.getStation_id(),s2.getStation_id(),(1*60+1)*60*1000, 1000);
	
	Mockito.when(dao.getAllRoutes()).thenReturn(new ArrayList<Route>());
	Mockito.when(dao.getStationByName("s1")).thenReturn(s1);
	Mockito.when(dao.getStationByName("s2")).thenReturn(s2);
	Mockito.when(dao.createDirection(s1.getStation_id(), s2.getStation_id(), d.getTime(), d.getCost())).thenReturn(d);
	Mockito.when(dao.createRoute(new_r.getRoute_name())).thenReturn(new_r);
	Mockito.when(dao.getDirectionByStartFinish(s1.getStation_id(), s2.getStation_id())).thenReturn(d);
	Mockito.when(dao.createShedule(d.getDirection_id(), new_r.getRoute_id(),0)).thenReturn(new Shedule(3,d.getDirection_id(),new_r.getRoute_id(),0));
	Mockito.when(dao.getAllDirections()).thenReturn(new ArrayList<Direction>());
	Mockito.when(dao.getAllShedules()).thenReturn(new ArrayList<Shedule>());
	Mockito.doNothing().when(dao).close();
	
	List<String> newRoute = new ArrayList<String>();
	newRoute.add(s1.getStation_name());
	newRoute.add(s2.getStation_name());
	
	List<String> newDirections = new ArrayList<String>();
	newDirections.add(s1.getStation_name()+"/"+s2.getStation_name());
	RequiredDataForNewRoute reqData = new RequiredDataForNewRoute(newRoute, newDirections, hdc, "new");

	
	
	List<String> routeInfo = new ArrayList<String>();
	routeInfo.add(new_r.getRoute_name());
	routeInfo.add(String.valueOf(data.getHours()));
	routeInfo.add(String.valueOf(data.getMinutes()));
	routeInfo.add(String.valueOf(Double.parseDouble(data.getCost())));
	for (String station : newRoute) {
		routeInfo.add(station);
	}
	NewRouteSummary summary = builder.build(reqData);
	Assert.assertTrue(summary.getSummary().equals(routeInfo));
	
	}
	
	@Test(expected=NullPointerException.class)
	public void test2(){
		builder.build(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void test3(){
		builder.build(new RequiredDataForNewRoute(null, null, null, null));
	}
	
	@Test(expected=NullPointerException.class)
	public void test4(){
		builder.build(new RequiredDataForNewRoute(new ArrayList<String>(), null, null, null));
	}
	
	@Test(expected=NullPointerException.class)
	public void test5(){
		builder.build(new RequiredDataForNewRoute(new ArrayList<String>(), new ArrayList<String>(), null, null));
	}
	
	@Test(expected=NullPointerException.class)
	public void test6(){
		builder.build(new RequiredDataForNewRoute(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<HoursMinutesCost>(), null));
	}
	
	@Test(expected=NullPointerException.class)
	public void test7(){
		builder.build(new RequiredDataForNewRoute(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<HoursMinutesCost>(), ""));
	}
	
	@Test
	public void test8(){
		Route r =new Route();r.setRoute_name("old");
		List<Route> oldRoutes = new ArrayList<Route>();oldRoutes.add(r);
		Mockito.when(dao.getAllRoutes()).thenReturn(oldRoutes);
		NewRouteSummary info=builder.build(new RequiredDataForNewRoute(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<HoursMinutesCost>(), "old"));
		Assert.assertTrue(info.getSummary()==null);
	}

}
