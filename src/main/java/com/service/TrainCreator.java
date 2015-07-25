package com.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.NewTrainInfo;
import com.entities.Dao;
@Service("trainCreator")
public class TrainCreator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(TrainCreator.class);

//	public TrainCreator(Dao dao) {
//		super();
//		this.dao = dao;
//	}

	@Transactional
	public   void create(NewTrainInfo dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
			dao.createTrain(Integer.parseInt(dto.getTrainCapacity()));
//			dao.commit();
//		} finally {
//			dao.close();
//		}
	}
}
