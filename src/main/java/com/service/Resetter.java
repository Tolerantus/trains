package com.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.ResetRequest;
import com.entities.Dao;
@Service("resetter")
public class Resetter {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(Resetter.class);


//	public Resetter(Dao dao) {
//		super();
//		this.dao = dao;
//	}


	@Transactional
	public  void reset(ResetRequest dto){
//		try {
//			dao.begin();
			dao.clearStations();
			dao.clearTrains();
			dao.clearRoutes();
			dao.clearPassengers();
			dao.initUsers();
			LOG.info("DB reset");
//			dao.commit();
//		} finally {
//			dao.close();
//		}
	}
}
