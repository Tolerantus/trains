package services.test;


import java.util.ArrayList;
import java.util.List;





import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.DirectionData;
import com.dto.RouteStationList;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Station;
import com.service.RouteDirectionsChecker;

public class RouteDirectionsCheckerTest {
	RouteDirectionsChecker checker;
	@Mock
	Dao dao = Mockito.mock(Dao.class);
	@Before
	public void init(){
		checker = new RouteDirectionsChecker(dao);
	}
	@Test
	public void testCheck() {
		String st_dep = "station1";
		Station st1 = new Station();st1.setStation_id(1);st1.setStation_name(st_dep);
		String st_arr = "station2";
		Station st2 = new Station();st2.setStation_id(2);st2.setStation_name(st_arr);
		List<String> route = new ArrayList<String>();route.add(st_dep);route.add(st_arr);
		RouteStationList input = new RouteStationList(route);
		Direction d = new Direction();d.setSt_dep(st1.getStation_id());d.setSt_arr(st2.getStation_id());
		List<Direction> oldDirections = new ArrayList<Direction>();oldDirections.add(d);
		
		Mockito.when(dao.getStationByName(st_dep)).thenReturn(st1);
		Mockito.when(dao.getStationByName(st_arr)).thenReturn(st2);
		Mockito.when(dao.getStation(st1.getStation_id())).thenReturn(st1);
		Mockito.when(dao.getStation(st2.getStation_id())).thenReturn(st2);
		
		DirectionData output = checker.check(input);
		String direction = st_dep+"/"+st_arr;
		List<String> directions = new ArrayList<String>();directions.add(direction);
		DirectionData expectedOutput = new DirectionData(directions);
		Assert.assertTrue(output.getDirections().equals(expectedOutput.getDirections()));
		
		Mockito.when(dao.getAllDirections()).thenReturn(oldDirections);
		output = checker.check(input);
		
		Assert.assertTrue(output.getDirections().size()==0);
	}
	@Test(expected=NullPointerException.class)
	public void test2(){
		checker.check(null);
	}
	@Test(expected=NullPointerException.class)
	public void test3(){
		checker.check(new RouteStationList(null));
	}

}
