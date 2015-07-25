package com.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.RouteStationList;
import com.dto.StationForInsertInRoute;
import com.entities.Dao;
import com.entities.Station;
@Service("routeStationsInjector")
public class RouteStationInjector {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(RouteStationInjector.class);

//	public RouteStationInjector(Dao dao) {
//		super();
//		this.dao = dao;
//	}

	@Transactional
	public   RouteStationList inject(StationForInsertInRoute dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
			String newStation = dto.getNewStation();
			String selectedStation = dto.getSelectedStation();
			int step = Integer.parseInt(dto.getStep());
			RouteStationList route = dto.getRoute();
			boolean isNewStationNew = true;
			if (newStation != "") {
				for (Station s : dao.getAllStations()) {
					if (s.getStation_name().equals(newStation)) {
						isNewStationNew = false;
					}
				}
			}
			Station stationForInsert = null;
			if (newStation == "") {
				stationForInsert = dao.getStationByName(selectedStation);
			} else {
				if (isNewStationNew) {
					stationForInsert = dao.createStation(newStation);
				} else {
					stationForInsert = dao.getStationByName(newStation);
				}
			}
			List<String> newRoute = route.getRoute();
			if (!newRoute.contains(stationForInsert.getStation_name())) {
				newRoute.add(step + 1, stationForInsert.getStation_name());
			}
			route.setRoute(newRoute);
			LOG.debug(route);
//			dao.commit();
			return route;
//		} finally {
//			dao.close();
//		}
	}
}
