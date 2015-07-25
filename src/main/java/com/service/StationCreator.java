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

//	public StationCreator(Dao dao) {
//		super();
//		this.dao = dao;
//	}

	@Transactional
	public   NewStationInfo create(NewStationInfo dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
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
			LOG.debug(dto);
//			dao.commit();
			return dto;
//		} finally {
//			dao.close();
//		}
	}
}
