package com.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.ChoosedJourney;
import com.entities.Dao;
import com.entities.Journey;
import com.entities.Seats;
@Service("seatsChecker")
public class SeatsChecker {
@Autowired
private Dao dao;
private static final Logger LOG = Logger.getLogger(SeatsChecker.class);

//public SeatsChecker(Dao dao) {
//	super();
//	this.dao = dao;
//}


public   ChoosedJourney check(ChoosedJourney dto){
//	try {
//		dao.begin();
		LOG.debug(dto);
		String journeyId = dto.getJourneyId();
		Journey j = dao.getJourney(Integer.parseInt(journeyId));
		List<String> journeys = dto.getJourneys();
		String journeyData = "";
		for (String js : journeys) {
			if (js.startsWith(journeyId)) {
				journeyData = js;
				break;
			}
		}
		String[] tokens = journeyData.split(";");
		int depStep = Integer.parseInt(tokens[1]);
		int arrStep = Integer.parseInt(tokens[2]);
		List<Seats> seats = dao.getSeatsOnJourney(j.getJourney_id());
		if (!seats.isEmpty()) {
//			int emptySeats = seats.get(0).getEmpty_seats();
			int emptySeats = 200;
			for (Seats s : seats) {
				if (s.getRoute_step() >= depStep
						&& s.getRoute_step() <= arrStep) {
					emptySeats = Math.min(emptySeats, s.getEmpty_seats());
				}
			}

			dto.setJourneyId(journeyData);
			dto.setEmptySeats(emptySeats);

		}
		LOG.debug(dto);
//		dao.commit();
		return dto;
//	} finally {
//		dao.close();
//	}
	
	
}
}
