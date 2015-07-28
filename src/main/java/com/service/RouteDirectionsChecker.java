package com.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.DirectionData;
import com.dto.RouteStationList;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Station;
@Service("routeDirectionsChecker")
public class RouteDirectionsChecker {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(RouteDirectionsChecker.class);

	@Transactional
	public   DirectionData check(RouteStationList dto){
		LOG.debug("=====================================================================");
		LOG.debug(dto);
		LOG.debug("=====================================================================");
			List<String> newRoute = dto.getRoute();
			List<String> directionData = new ArrayList<String>();
			for (int i = 0; i < newRoute.size() - 1; i++) {
				Station s1 = dao.getStationByName(newRoute.get(i));
				Station s2 = dao.getStationByName(newRoute.get(i + 1));
				boolean isDirectionNew = true;
				for (Direction d : dao.getAllDirections()) {
					Station dep = dao.getStation(d.getSt_dep());
					Station arr = dao.getStation(d.getSt_arr());
					if (dep.getStation_id() == (s1.getStation_id())
							&& arr.getStation_id() == (s2.getStation_id())) {
						isDirectionNew = false;
					}
				}
				if (isDirectionNew) {
					StringBuilder sb = new StringBuilder(s1.getStation_name());
					sb.append("/");
					sb.append(s2.getStation_name());
					directionData.add(sb.toString());
				}
			}
			DirectionData d = new DirectionData(directionData);
			LOG.debug("=====================================================================");
			LOG.debug(dto);
			LOG.debug("=====================================================================");
			return d;

	}
}
