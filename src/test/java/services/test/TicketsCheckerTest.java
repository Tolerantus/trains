package services.test;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.ListOfTickets;
import com.dto.UserLoginContainer;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Journey;
import com.entities.Passenger;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
import com.entities.Ticket;
import com.entities.User;
import com.service.TicketsChecker;

public class TicketsCheckerTest {
TicketsChecker checker;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
	checker = new TicketsChecker(dao);
}
	@Test
	public void test() throws Exception {
		User user= new User();user.setUser_id(1);user.setUser_login("login");user.setUser_password("password");
		UserLoginContainer input = new UserLoginContainer(user.getUser_login());
		Station st_dep = new Station();st_dep.setStation_id(3);st_dep.setStation_name("st_dep");
		Station st_arr = new Station();st_arr.setStation_id(4);st_arr.setStation_name("st_arr");
		Route r = new Route();r.setRoute_id(6);
		Direction d = new Direction();d.setDirection_id(8);d.setSt_dep(st_dep.getStation_id());d.setSt_arr(st_arr.getStation_id());
		d.setTime(1000*60*60);d.setCost(1000);
		Shedule s = new Shedule();s.setShedule_id(7);s.setRoute_id(r.getRoute_id());s.setDirection_id(d.getDirection_id());
		s.setStep(0);
		List<Shedule> steps = new ArrayList<Shedule>();steps.add(s);
		Date birthday = new Date();
		Journey j = new Journey();j.setJourney_id(5);j.setRoute_id(r.getRoute_id());j.setTime_dep(birthday);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
		Passenger p = new Passenger();p.setPassenger_id(6);p.setPassenger_name("name");p.setPassenger_surname("surname");
		p.setPassenger_birthday(birthday);
		Ticket t = new Ticket();t.setTicket_id(2);t.setJourney_id(j.getJourney_id());t.setSt_dep(st_dep.getStation_id());
		t.setSt_arr(st_arr.getStation_id());t.setPassenger_id(p.getPassenger_id());
		List<Ticket> ticketsOfUser = new ArrayList<Ticket>();ticketsOfUser.add(t);
		
		Mockito.when(dao.getUserByName(user.getUser_login())).thenReturn(user);
		Mockito.when(dao.getTicketOfUser(user.getUser_id())).thenReturn(ticketsOfUser);
		Mockito.when(dao.getPassenger(p.getPassenger_id())).thenReturn(p);
		Mockito.when(dao.getStation(st_dep.getStation_id())).thenReturn(st_dep);
		Mockito.when(dao.getStation(st_arr.getStation_id())).thenReturn(st_arr);
		Mockito.when(dao.getRoute(r.getRoute_id())).thenReturn(r);
		Mockito.when(dao.getShedulesOfRoute(r.getRoute_id())).thenReturn(steps);
		Mockito.when(dao.getDirection(d.getDirection_id())).thenReturn(d);
		Mockito.when(dao.getJourney(j.getJourney_id())).thenReturn(j);
		
		ListOfTickets output = checker.check(input);
		String expectedResult = t.getTicket_id()+","+p.getPassenger_name()+" "+p.getPassenger_surname()+","+
		st_dep.getStation_name()+","+sdf.format(j.getTime_dep())+","+st_arr.getStation_name()+","+
		sdf.format(new Date(birthday.getTime()+d.getTime()))+","+d.getCost();
		Assert.assertTrue(output.getTicketsInfo().get(0).equals(expectedResult));
		
	}
	@Test(expected=NullPointerException.class)
	public void test2() throws Exception{
		checker.check(null);
	}
	@Test(expected=NullPointerException.class)
	public void test3() throws Exception{
		User user= new User();user.setUser_id(1);user.setUser_login("login");user.setUser_password("password");
		UserLoginContainer input = new UserLoginContainer(user.getUser_login());
		Station st_dep = new Station();st_dep.setStation_id(3);st_dep.setStation_name("st_dep");
		Station st_arr = new Station();st_arr.setStation_id(4);st_arr.setStation_name("st_arr");
		Route r = new Route();r.setRoute_id(6);
		Direction d = new Direction();d.setDirection_id(8);d.setSt_dep(st_dep.getStation_id());d.setSt_arr(st_arr.getStation_id());
		d.setTime(1000*60*60);d.setCost(1000);
		Shedule s = new Shedule();s.setShedule_id(7);s.setRoute_id(r.getRoute_id());s.setDirection_id(d.getDirection_id());
		s.setStep(0);
		List<Shedule> steps = new ArrayList<Shedule>();steps.add(s);
		Date birthday = new Date();
		Journey j = new Journey();j.setJourney_id(5);j.setRoute_id(r.getRoute_id());j.setTime_dep(birthday);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
		Passenger p = new Passenger();p.setPassenger_id(6);p.setPassenger_name("name");p.setPassenger_surname("surname");
		p.setPassenger_birthday(birthday);
		Ticket t = new Ticket();t.setTicket_id(2);t.setJourney_id(j.getJourney_id());t.setSt_dep(st_dep.getStation_id());
		t.setSt_arr(st_arr.getStation_id());t.setPassenger_id(p.getPassenger_id());
		List<Ticket> ticketsOfUser = new ArrayList<Ticket>();ticketsOfUser.add(t);
		
		Mockito.when(dao.getUserByName(user.getUser_login())).thenReturn(user);
		Mockito.when(dao.getTicketOfUser(user.getUser_id())).thenReturn(new ArrayList<Ticket>());
		Mockito.when(dao.getPassenger(p.getPassenger_id())).thenReturn(p);
		Mockito.when(dao.getStation(st_dep.getStation_id())).thenReturn(st_dep);
		Mockito.when(dao.getStation(st_arr.getStation_id())).thenReturn(st_arr);
		Mockito.when(dao.getRoute(r.getRoute_id())).thenReturn(r);
		Mockito.when(dao.getShedulesOfRoute(r.getRoute_id())).thenReturn(steps);
		Mockito.when(dao.getDirection(d.getDirection_id())).thenReturn(d);
		Mockito.when(dao.getJourney(j.getJourney_id())).thenReturn(j);
		
		ListOfTickets output = checker.check(input);
		
		Assert.assertTrue(output.getTicketsInfo().size()==0);
	}

}
