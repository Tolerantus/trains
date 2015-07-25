package services.test;


import java.util.ArrayList;
import java.util.List;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.NewRouteStartAndFinish;
import com.dto.RouteStationList;
import com.entities.Dao;
import com.entities.Station;
import com.service.RouteCreator;

public class RouteCreatorTest {
	RouteCreator creator;
	@Mock
	Dao dao = Mockito.mock(Dao.class);
	@Before
	public void init(){
		creator = new RouteCreator(dao);
	}
	@Test
	public void test() {
		String typed_dep = "station1";
		Station st1 = new Station();st1.setStation_id(1);st1.setStation_name(typed_dep);
		String typed_arr = "station2";
		Station st2 = new Station();st2.setStation_id(2);st2.setStation_name(typed_arr);
		List<String> newStations = new ArrayList<String>();newStations.add(typed_dep);newStations.add(typed_arr);
		String select_dep = "select1";
		Station st3 = new Station();st3.setStation_id(3);st3.setStation_name(select_dep);
		String select_arr = "select2";
		Station st4 = new Station(); st4.setStation_id(4);st4.setStation_name(select_arr);
		List<Station> oldStations = new ArrayList<Station>();oldStations.add(st3);oldStations.add(st4);
		List<String> oldStationNamings = new ArrayList<String>();oldStationNamings.add(st3.getStation_name());oldStationNamings.add(st4.getStation_name());
		NewRouteStartAndFinish input = new NewRouteStartAndFinish(typed_dep, typed_arr, select_dep, select_arr);
		
		
		Mockito.when(dao.createStation(typed_dep)).thenReturn(st1);
		Mockito.when(dao.createStation(typed_arr)).thenReturn(st2);
		Mockito.when(dao.getStationByName(typed_dep)).thenReturn(st1);
		Mockito.when(dao.getStationByName(typed_arr)).thenReturn(st2);
		Mockito.when(dao.getStationByName(select_dep)).thenReturn(st3);
		Mockito.when(dao.getStationByName(select_arr)).thenReturn(st4);
		
		RouteStationList output = creator.append(input);
		Assert.assertTrue(newStations.equals(output.getRoute()));
		
		Mockito.when(dao.getAllStations()).thenReturn(oldStations);
		
		NewRouteStartAndFinish input2 = new NewRouteStartAndFinish("", "", select_dep, select_arr);
		
		RouteStationList output2 = creator.append(input2);
		Assert.assertTrue(oldStationNamings.equals(output2.getRoute()));
		
		NewRouteStartAndFinish input3 = new NewRouteStartAndFinish(typed_dep, "", select_dep, select_arr);
		RouteStationList output3 = creator.append(input3);
		List<String> stNames = new ArrayList<String>(); stNames.add(typed_dep);stNames.add(select_arr);
		
		Assert.assertTrue(stNames.equals(output3.getRoute()));
		
	}
	@Test(expected=NullPointerException.class)
	public void test2(){
		creator.append(null);
	}
	@Test(expected=NullPointerException.class)
	public void test3(){
		creator.append(new NewRouteStartAndFinish(null, null, null, null));
	}
	@Test(expected=NullPointerException.class)
	public void test4(){
		creator.append(new NewRouteStartAndFinish("", "", "", ""));
	}
}
