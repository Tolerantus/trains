package com.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.PassengerInfo;
import com.dto.TicketInfo;
import com.entities.Dao;
import com.entities.Passenger;
import com.entities.Station;
import com.entities.Ticket;
import com.entities.User;
@Service("passengerRegistrator")
public class PassengerRegistrator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(PassengerRegistrator.class);

/*input data like:
 * currentUser:"root",
 * passengerDepAndDestStations:"journeyId;step_dep;step_arr;st_dep;st_arr"
 * day:"1"
 * month:"1"
 * name:"name"
 * surname:"surname"
 * year:"2015"
 * allJourneysData:"journeyId;HH:mm dd MMM;HH:mm dd MMM;cost"
 * */
	@Transactional
	public  TicketInfo register(PassengerInfo dto) throws ParseException{

		LOG.debug("=====================================================================");
		LOG.debug(dto);
		LOG.debug("=====================================================================");
			TicketInfo info = new TicketInfo(null, false);
			String month = dto.getMonth();
			String day = dto.getDay();
			String year = dto.getYear();
			String name = dto.getName();
			String surname = dto.getSurname();
			String passengerDepAndDestStations = dto
					.getPassengerDepAndDestStations();
			String user = dto.getCurrentUser();
			if (month.length() == 1) {
				month = '0' + month;
			}
			if (day.length() == 1) {
				day = '0' + day;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date birthday = new Date();
			
				birthday = sdf.parse(day + "/" + month + "/" + year);
			
			Passenger newPassenger = dao.createPassenger(name, surname,
					birthday);
			String[] passengerDepAndDestStations_tokens = passengerDepAndDestStations
					.split(";");
			int journeyId = Integer
					.parseInt(passengerDepAndDestStations_tokens[0]);
			Station st_dep = dao.getStation(Integer
					.parseInt(passengerDepAndDestStations_tokens[3]));
			Station st_arr = dao.getStation(Integer
					.parseInt(passengerDepAndDestStations_tokens[4]));
			List<Ticket> allTickets = dao.getAllTickets();
			for (Ticket t : allTickets) {
				Passenger oldPassenger = dao.getPassenger(t.getPassenger_id());
				if (t.getJourney_id() == journeyId
						&& oldPassenger.equals(newPassenger)) {
					dao.deletePassenger(newPassenger.getPassenger_id());
					info.setExist(true);
				}
			}
			if (!info.isExist()) {
				Ticket newTicket = dao.createTicket(
						newPassenger.getPassenger_id(), journeyId,
						st_dep.getStation_id(), st_arr.getStation_id());
				User currentUser = dao.getUserByName(user);
				dao.createUser_Ticket(newTicket.getTicket_id(),
						currentUser.getUser_id());
				dao.decrementEmptySeats(journeyId, st_dep.getStation_id(),
						st_arr.getStation_id());
				List<String> allJourneysData = dto.getAllJourneysData();
				String passengerDepAndDestTime = "";
				for (String s : allJourneysData) {
					if (s.startsWith(passengerDepAndDestStations_tokens[0])) {
						passengerDepAndDestTime = s;
					}
				}
				String[] passengerDepAndDestTime_tokens = passengerDepAndDestTime
						.split(";");
				String passengerDepTime = passengerDepAndDestTime_tokens[1];
				String passengerDestTime = passengerDepAndDestTime_tokens[2];
				String cost = passengerDepAndDestTime_tokens[3];

				StringBuilder ticketInfo = new StringBuilder(
						String.valueOf(newTicket.getTicket_id()));
				ticketInfo.append(";");
				ticketInfo.append(newPassenger.getPassenger_name());
				ticketInfo.append(";");
				ticketInfo.append(newPassenger.getPassenger_surname());
				ticketInfo.append(";");
				ticketInfo.append(st_dep.getStation_name());
				ticketInfo.append(";");
				ticketInfo.append(passengerDepTime);
				ticketInfo.append(";");
				ticketInfo.append(st_arr.getStation_name());
				ticketInfo.append(";");
				ticketInfo.append(passengerDestTime);
				ticketInfo.append(";");
				ticketInfo.append(cost);
				info.setTicketInfo(ticketInfo.toString());
			}
			LOG.debug("=====================================================================");
			LOG.debug(info);
			LOG.debug("=====================================================================");
			return info;
	}
}
