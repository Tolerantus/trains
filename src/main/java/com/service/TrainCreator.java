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

	@Transactional
	public   void create(NewTrainInfo dto){
			LOG.debug(dto);
			dao.createTrain(Integer.parseInt(dto.getTrainCapacity()));
	}
}
