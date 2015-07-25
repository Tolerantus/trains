package services.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;






import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.PassengerInfo;
import com.dto.TicketInfo;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Passenger;
import com.entities.Station;
import com.entities.Ticket;
import com.entities.User;
import com.service.PassengerRegistrator;

public class PassengerRegistratorTest {
PassengerRegistrator registrator;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
	registrator = new PassengerRegistrator(dao);
}

	@Test
	public void testRegister() throws Exception {
		Station s1 = new Station(); s1.setStation_id(1);s1.setStation_name("s1");Station s2 = new Station(); s2.setStation_id(2);s2.setStation_name("s2");
		Journey j = new Journey(); j.setJourney_id(3);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Date passengerDep = new Date();
		Date passengerDest = new Date(new Date().getTime()+1000*60*60*24);
		String cost = "1000";
		String journeyData = j.getJourney_id()+";"+sdf2.format(passengerDep)+";"+sdf2.format(passengerDest)+";"+cost;
		List<String> allJourneysData = new ArrayList<String>();allJourneysData.add(journeyData);
		PassengerInfo info = new PassengerInfo
				("user",j.getJourney_id()+";0;0;"+s1.getStation_id()+";"+s2.getStation_id(),"name","surname","2000","11","11",allJourneysData);
		
		
		
		Passenger newPassenger = new Passenger();newPassenger.setPassenger_name(info.getName());newPassenger.setPassenger_surname(info.getSurname());
		newPassenger.setPassenger_birthday(sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()));
				
		Ticket ticket = new Ticket();ticket.setJourney_id(j.getJourney_id());ticket.setPassenger_id(newPassenger.getPassenger_id());
		ticket.setSt_arr(s2.getStation_id());ticket.setSt_dep(s1.getStation_id());ticket.setTicket_id(3);
		
		User user = new User();user.setUser_login("user");user.setUser_id(4);
		
		
		Mockito.when(dao.createPassenger(info.getName(), info.getSurname(), sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()))).
		thenReturn(newPassenger);
		Mockito.when(dao.getStation(s1.getStation_id())).thenReturn(s1);
		Mockito.when(dao.getStation(s2.getStation_id())).thenReturn(s2);
		Mockito.when(dao.getAllTickets()).thenReturn(new ArrayList<Ticket>());
		Mockito.when(dao.createTicket(newPassenger.getPassenger_id(), j.getJourney_id(),
				s1.getStation_id(), s2.getStation_id())).thenReturn(ticket);
		Mockito.when(dao.getUserByName(info.getCurrentUser())).thenReturn(user);
		
		StringBuilder ticketInfo = new StringBuilder(
				String.valueOf(ticket.getTicket_id()));
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_name());
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_surname());
		ticketInfo.append(";");
		ticketInfo.append(s1.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDep));
		ticketInfo.append(";");
		ticketInfo.append(s2.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDest));
		ticketInfo.append(";");
		ticketInfo.append(cost);
		String expectedResult = ticketInfo.toString();
		TicketInfo TI = registrator.register(info);
		Assert.assertTrue(TI.getTicketInfo().equals(expectedResult));
		
	}
	@Test(expected=NullPointerException.class)
	public void test2() throws Exception{
		registrator.register(null);
	}
	@Test(expected=NullPointerException.class)
	public void test3() throws Exception{
		registrator.register(new PassengerInfo(null, null, null, null, null, null, null, null));
	}
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void test4() throws Exception{
		Station s1 = new Station(); s1.setStation_id(1);s1.setStation_name("s1");Station s2 = new Station(); s2.setStation_id(2);s2.setStation_name("s2");
		Journey j = new Journey(); j.setJourney_id(3);
		
		Date passengerDep = new Date();
		Date passengerDest = new Date(new Date().getTime()+1000*60*60*24);
		String cost = "1000";
		List<String> allJourneysData = new ArrayList<String>();
		
		PassengerInfo info = new PassengerInfo
				("user",j.getJourney_id()+";0;0;"+s1.getStation_id()+";"+s2.getStation_id(),"name","surname","2000","11","11",allJourneysData);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		
		Passenger newPassenger = new Passenger();newPassenger.setPassenger_name(info.getName());newPassenger.setPassenger_surname(info.getSurname());
		newPassenger.setPassenger_birthday(sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()));
				
		Ticket ticket = new Ticket();ticket.setJourney_id(j.getJourney_id());ticket.setPassenger_id(newPassenger.getPassenger_id());
		ticket.setSt_arr(s2.getStation_id());ticket.setSt_dep(s1.getStation_id());ticket.setTicket_id(3);
		
		User user = new User();user.setUser_login("user");user.setUser_id(4);
		
		
		Mockito.when(dao.createPassenger(info.getName(), info.getSurname(), sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()))).
		thenReturn(newPassenger);
		Mockito.when(dao.getStation(s1.getStation_id())).thenReturn(s1);
		Mockito.when(dao.getStation(s2.getStation_id())).thenReturn(s2);
		Mockito.when(dao.getAllTickets()).thenReturn(new ArrayList<Ticket>());
		Mockito.when(dao.createTicket(newPassenger.getPassenger_id(), j.getJourney_id(),
				s1.getStation_id(), s2.getStation_id())).thenReturn(ticket);
		Mockito.when(dao.getUserByName(info.getCurrentUser())).thenReturn(user);
		
		StringBuilder ticketInfo = new StringBuilder(
				String.valueOf(ticket.getTicket_id()));
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_name());
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_surname());
		ticketInfo.append(";");
		ticketInfo.append(s1.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDep));
		ticketInfo.append(";");
		ticketInfo.append(s2.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDest));
		ticketInfo.append(";");
		ticketInfo.append(cost);
		
		registrator.register(info);
		
	}
	
	@Test(expected=ParseException.class)
	public void test5() throws Exception {
		Station s1 = new Station(); s1.setStation_id(1);s1.setStation_name("s1");Station s2 = new Station(); s2.setStation_id(2);s2.setStation_name("s2");
		Journey j = new Journey(); j.setJourney_id(3);
		
		Date passengerDep = new Date();
		Date passengerDest = new Date(new Date().getTime()+1000*60*60*24);
		String cost = "1000";
		String journeyData = j.getJourney_id()+";"+passengerDep+";"+passengerDest+";"+cost;
		List<String> allJourneysData = new ArrayList<String>();allJourneysData.add(journeyData);
		PassengerInfo info = new PassengerInfo
				("user",j.getJourney_id()+";0;0;"+s1.getStation_id()+";"+s2.getStation_id(),"name","surname"," "," "," ",allJourneysData);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		
		Passenger newPassenger = new Passenger();newPassenger.setPassenger_name(info.getName());newPassenger.setPassenger_surname(info.getSurname());
		newPassenger.setPassenger_birthday(sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()));
				
		Ticket ticket = new Ticket();ticket.setJourney_id(j.getJourney_id());ticket.setPassenger_id(newPassenger.getPassenger_id());
		ticket.setSt_arr(s2.getStation_id());ticket.setSt_dep(s1.getStation_id());ticket.setTicket_id(3);
		
		User user = new User();user.setUser_login("user");user.setUser_id(4);
		
		
		Mockito.when(dao.createPassenger(info.getName(), info.getSurname(), sdf.parse(info.getYear()+"-"+info.getMonth()+"-"+info.getDay()))).
		thenReturn(newPassenger);
		Mockito.when(dao.getStation(s1.getStation_id())).thenReturn(s1);
		Mockito.when(dao.getStation(s2.getStation_id())).thenReturn(s2);
		Mockito.when(dao.getAllTickets()).thenReturn(new ArrayList<Ticket>());
		Mockito.when(dao.createTicket(newPassenger.getPassenger_id(), j.getJourney_id(),
				s1.getStation_id(), s2.getStation_id())).thenReturn(ticket);
		Mockito.when(dao.getUserByName(info.getCurrentUser())).thenReturn(user);
		
		StringBuilder ticketInfo = new StringBuilder(
				String.valueOf(ticket.getTicket_id()));
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_name());
		ticketInfo.append(";");
		ticketInfo.append(newPassenger.getPassenger_surname());
		ticketInfo.append(";");
		ticketInfo.append(s1.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDep));
		ticketInfo.append(";");
		ticketInfo.append(s2.getStation_name());
		ticketInfo.append(";");
		ticketInfo.append(sdf2.format(passengerDest));
		ticketInfo.append(";");
		ticketInfo.append(cost);
		
		TicketInfo TI = registrator.register(info);
		Assert.assertTrue(TI.getTicketInfo().equals(ticketInfo.toString()));
		
	}
	
	

}
