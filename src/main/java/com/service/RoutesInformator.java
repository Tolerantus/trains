package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.AllRoutesInfo;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
@Service("routesInformator")
public class RoutesInformator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(RoutesInformator.class);

//	public RoutesInformator(Dao dao) {
//		super();
//		this.dao = dao;
//	}

	@Transactional
	public   AllRoutesInfo getInfo(AllRoutesInfo dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
			Map<Integer, String> routes = new HashMap<Integer, String>();
			if (!dao.getAllRoutes().isEmpty()) {
				for (Route r : dao.getAllRoutes()) {
					List<Shedule> steps = dao.getShedulesOfRoute(r.getRoute_id());
					StringBuilder routeStationsInfo = new StringBuilder();
					for (int i = 0; i < steps.size(); i++) {
						Direction d = dao.getDirection(steps.get(i)
								.getDirection_id());
						Station dep = dao.getStation(d.getSt_dep());
						routeStationsInfo.append(dep.getStation_name());
						routeStationsInfo.append("--->");
					}
					Direction d = dao.getDirection(steps.get(steps.size() - 1)
							.getDirection_id());
					Station arr = dao.getStation(d.getSt_arr());
					routeStationsInfo.append(arr.getStation_name());

					routes.put(r.getRoute_id(), routeStationsInfo.toString());
				}
				dto.setRoutes(routes);
			}
			LOG.debug(dto);
//			dao.commit();
			return dto;
//		} finally {
//			dao.close();
//		}
	}
}
