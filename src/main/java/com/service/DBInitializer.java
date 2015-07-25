package com.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.InitDBRequest;
import com.entities.Dao;
@Service("DBInitializer")
public class DBInitializer {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(DBInitializer.class);
	

//	public DBInitializer(Dao dao) {
//		super();
//		this.dao = dao;
//	}


	@Transactional
	public void init(InitDBRequest dto) throws ParseException{
//		try {
//			dao.begin();
			dao.initDatabase();
			LOG.info("Database initialized");
//			dao.commit();
//		} finally {
//			dao.close();
//		}
		
	}
}
