package com.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.NewRouteStartAndFinish;
import com.dto.RouteStationList;
import com.entities.Dao;
import com.entities.Station;
@Service("routeCreator")
public class RouteCreator {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(RouteCreator.class);

	@Transactional
	public  RouteStationList append(NewRouteStartAndFinish dto){

			LOG.debug(dto);
			RouteStationList routeStationList = new RouteStationList(null);
			String typed_dep = dto.getTyped_dep();
			String typed_arr = dto.getTyped_arr();
			String select_dep = dto.getSelect_dep();
			String select_arr = dto.getSelect_arr();
			List<Station> allStations = dao.getAllStations();
			if (allStations == null && typed_dep == "" && typed_arr == "") {
				return routeStationList;
			} else {

				boolean is_st_dep_exist = false;
				boolean is_st_arr_exist = false;

				if (typed_dep != "") {
					for (Station station : allStations) {
						if (station.getStation_name().equals(typed_dep)) {
							is_st_dep_exist = true;
						}
					}
				}
				if (typed_arr != "") {
					for (Station station : allStations) {
						if (station.getStation_name().equals(typed_arr)) {
							is_st_arr_exist = true;
						}
					}
				}

				Station start = null;
				Station finish = null;
				if (typed_arr == "") {
					finish = dao.getStationByName(select_arr);
				} else {
					if (!is_st_arr_exist) {
						finish = dao.createStation(typed_arr);
					} else {
						finish = dao.getStationByName(typed_arr);
					}
				}

				if (typed_dep == "") {
					start = dao.getStationByName(select_dep);
				} else {
					if (!is_st_dep_exist) {
						start = dao.createStation(typed_dep);
					} else {
						start = dao.getStationByName(typed_dep);
					}
				}

				List<String> route = new LinkedList<String>();
				route.add(start.getStation_name());
				route.add(finish.getStation_name());
				routeStationList.setRoute(route);
				LOG.debug(routeStationList);
				return routeStationList;
			}
	}
}
