package com.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.NewStationInfo;
import com.entities.Dao;
import com.entities.Station;
@Service("stationCreator")
public class StationCreator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(StationCreator.class);

	@Transactional
	public   NewStationInfo create(NewStationInfo dto){

		LOG.debug("=====================================================================");
		LOG.debug(dto);
		LOG.debug("=====================================================================");
			String stationName = dto.getStation();
			boolean isExist = false;
			for (Station s : dao.getAllStations()) {
				if (s.getStation_name().equals(stationName)) {
					isExist = true;
				}
			}
			if (!isExist) {
				dao.createStation(stationName);
				LOG.info("Station created");
			} else {
				dto.setExist(true);
			}
			LOG.debug("=====================================================================");
			LOG.debug(dto);
			LOG.debug("=====================================================================");
			return dto;
	}
}
