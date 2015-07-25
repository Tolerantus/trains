package com.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.AllJourneysInfo;
import com.entities.Dao;
import com.entities.Journey;
@Service("journeyBriefer")
public class JourneyBriefer {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(JourneyBriefer.class);


//	public JourneyBriefer(Dao dao) {
//		super();
//		this.dao = dao;
//	}


	@Transactional
	public  AllJourneysInfo getInfo(AllJourneysInfo dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
			List<Journey> journeys = dao.getAllJourneys();
			List<String> journeyNames = new ArrayList<String>();
			if (!journeys.isEmpty()) {
				for (Journey j : journeys) {
					journeyNames.add(j.getJourney_id() + " "
							+ dao.getRoute(j.getRoute_id()).getRoute_name());
				}
				dto.setJourneys(journeyNames);
			}
			LOG.debug(dto);
//			dao.commit();
			return dto;
//		} finally {
//			dao.close();
//		}
	}
}
