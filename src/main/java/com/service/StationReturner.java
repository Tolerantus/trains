package com.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.StationContainer;
import com.entities.Dao;
import com.entities.Station;
@Service("stationReturner")
public class StationReturner {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(StationReturner.class);

//public StationReturner(Dao dao) {
//		super();
//		this.dao = dao;
//	}

@Transactional
public   StationContainer getStations(StationContainer dto){
//	try {
//		dao.begin();
		LOG.debug(dto);
		List<Station> allStations = dao.getAllStations();
		dto.setStations(allStations);
		LOG.debug(dto);
//		dao.commit();
		return dto;
//	} finally {
//		dao.close();
//	}
}
}
