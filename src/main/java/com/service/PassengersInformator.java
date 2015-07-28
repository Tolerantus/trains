package com.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.JourneyAndPassengers;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Passenger;
import com.entities.Ticket;
@Service("passengerInformator")
public class PassengersInformator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(PassengersInformator.class);

	@Transactional
	public  JourneyAndPassengers getInfo(JourneyAndPassengers dto){

		LOG.debug("=====================================================================");
		LOG.debug(dto);
		LOG.debug("=====================================================================");
			String journeyInfo = dto.getJourneyInfo();
			String[] tokens = journeyInfo.split(" ");
			Journey j = dao.getJourney(Integer.parseInt(tokens[0]));
			List<Passenger> passengers = new ArrayList<Passenger>();
			for (Ticket t : dao.getAllTickets()) {
				if (t.getJourney_id() == j.getJourney_id()) {
					passengers.add(dao.getPassenger(t.getPassenger_id()));
				}
			}
			List<String> passInfo = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			if (!passengers.isEmpty()) {
				for (Passenger p : passengers) {
					StringBuilder sb = new StringBuilder();
					sb.append(p.getPassenger_name());
					sb.append(" ");
					sb.append(p.getPassenger_surname());
					sb.append(" ");
					sb.append(sdf.format(p.getPassenger_birthday()));
					passInfo.add(sb.toString());
				}

				dto.setPassInfo(passInfo);
				LOG.debug("=====================================================================");
				LOG.debug(dto);
				LOG.debug("=====================================================================");
				return dto;
			} else {
				LOG.debug("=====================================================================");
				LOG.debug(dto);
				LOG.debug("=====================================================================");
				return dto;
			}
	}
}
