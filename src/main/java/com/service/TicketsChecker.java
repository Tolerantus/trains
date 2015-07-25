package com.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.ListOfTickets;
import com.dto.UserLoginContainer;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Journey;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
import com.entities.Ticket;
import com.entities.User;
@Service("ticketsChecker")
public class TicketsChecker {
	@Autowired
	private Dao dao;	
	private static final Logger LOG = Logger.getLogger(TicketsChecker.class);
	
//	public TicketsChecker(Dao dao) {
//		super();
//		this.dao = dao;
//	}
	@Transactional
	public ListOfTickets check(UserLoginContainer dto){
		
//		try {
//			dao.begin();
			LOG.debug(dto);
			String username = dto.getLogin();
			User u = dao.getUserByName(username);
			List<Ticket> tickets = dao.getTicketOfUser(u.getUser_id());
			List<String> ticketsData = getTicketData(tickets);
			ListOfTickets list = new ListOfTickets(ticketsData);
			LOG.debug(list);
//			dao.commit();
			return list;
//		} finally {
//			dao.close();
//		}
		
	}
	private  List<String> getTicketData(List<Ticket> tickets){
		if (tickets==null){
			return null;
		}else if(tickets.isEmpty()){
			return null;
		}else{
			List<String> ticketData = new ArrayList<String>();
			for (Ticket ticket:tickets){
				String passenger_name = dao.getPassenger(ticket.getPassenger_id()).getPassenger_name();
				String passenger_surname = dao.getPassenger(ticket.getPassenger_id()).getPassenger_surname();

				Station st_dep = dao.getStation(ticket.getSt_dep());
				Station st_arr = dao.getStation(ticket.getSt_arr());
				Journey journey = dao.getJourney(ticket.getJourney_id()); 
				Date trainDeparture = journey.getTime_dep();
				Date passengerDep = trainDeparture;
				Date passengerArr = trainDeparture;
				Route route = dao.getRoute(journey.getRoute_id());
				List<Shedule> steps = dao.getShedulesOfRoute(route.getRoute_id());
				int jour_begin=0;
				int jour_end=0;
				double cost=0;
				for (Shedule s: steps){
					Direction d = dao.getDirection(s.getDirection_id());
					
					Station s_dep= dao.getStation(d.getSt_dep());
					Station s_arr= dao.getStation(d.getSt_arr());
					if (s_dep.getStation_id()==st_dep.getStation_id()){
						jour_begin=s.getStep();
					}
					if (s_arr.getStation_id()==st_arr.getStation_id()){
						jour_end=s.getStep();
					}
				}
				for(Shedule s : steps){
					Direction d = dao.getDirection(s.getDirection_id());
					if (s.getStep()<jour_begin){
						passengerDep=new Date(passengerDep.getTime()+d.getTime());
						passengerArr=new Date(passengerArr.getTime()+d.getTime());
					}
					if (s.getStep()>=jour_begin&&s.getStep()<=jour_end){
						passengerArr=new Date(passengerArr.getTime()+d.getTime());
						cost+=d.getCost();
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
				StringBuilder sb = new StringBuilder();
				sb.append(ticket.getTicket_id());
				sb.append(",");
				sb.append(passenger_name+" "+passenger_surname);
				sb.append(",");
				sb.append(st_dep.getStation_name());
				sb.append(",");
				sb.append(sdf.format(passengerDep));
				sb.append(",");
				sb.append(st_arr.getStation_name());
				sb.append(",");
				sb.append(sdf.format(passengerArr));
				sb.append(",");
				sb.append(cost);
				ticketData.add(sb.toString());
				
			}
			
			return ticketData;
		}
		
	}
}
