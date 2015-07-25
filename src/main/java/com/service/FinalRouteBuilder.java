package com.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.NewRouteSummary;
import com.dto.RequiredDataForNewRoute;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
@Service("finalRouteBuilder")
public class FinalRouteBuilder {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(FinalRouteBuilder.class);
	
//	public FinalRouteBuilder(Dao dao) {
//		super();
//		this.dao = dao;
//	}
	@Transactional
	public  NewRouteSummary build(RequiredDataForNewRoute dto){
//		try {
//			dao.begin();
			LOG.debug(dto);
			List<String> newRoute = dto.getNewRoute();
			List<String> newDirections = dto.getNewDirections();
			boolean error = false;
			for (Route r : dao.getAllRoutes()) {
				if (r.getRoute_name().equals(dto.getRouteName())) {
					error = true;
				}
			}
			if (!error) {
				int sumMinutes = 0;
				int sumHours = 0;
				double sumCost = 0;
				for (int i = 0; i < newDirections.size(); i++) {
					String[] stations = newDirections.get(i).split("/");
					Station s1 = dao.getStationByName(stations[0]);
					Station s2 = dao.getStationByName(stations[1]);
					int hours = Integer.parseInt(dto.getData().get(i)
							.getHours());
					int minutes = Integer.parseInt(dto.getData().get(i)
							.getMinutes());
					double cost = Double.parseDouble(dto.getData().get(i)
							.getCost());
					if (!isDirectionExist(s1.getStation_id(),s2.getStation_id())) {
						 dao.createDirection(s1.getStation_id(),
								s2.getStation_id(),
								(hours * 60 + minutes) * 60 * 1000, cost);
						
					}

				}
				Route r = dao.createRoute(dto.getRouteName());
				for (int i = 0; i < newRoute.size() - 1; i++) {
					Station s1 = dao.getStationByName(newRoute.get(i));
					Station s2 = dao.getStationByName(newRoute.get(i + 1));
					Direction d = dao.getDirectionByStartFinish(
							s1.getStation_id(), s2.getStation_id());
					sumMinutes += d.getTime() / (1000 * 60);
					sumCost += d.getCost();
					if (!isSheduleExist(d.getDirection_id(), r.getRoute_id(), i)) {
						dao.createShedule(d.getDirection_id(), r.getRoute_id(),	i);
					}
				}
				sumHours = sumMinutes / 60;
				sumMinutes = sumMinutes % 60;
				List<String> routeInfo = new ArrayList<String>();
				routeInfo.add(r.getRoute_name());
				routeInfo.add(String.valueOf(sumHours));
				routeInfo.add(String.valueOf(sumMinutes));
				routeInfo.add(String.valueOf(sumCost));
				for (String station : newRoute) {
					routeInfo.add(station);
				}
				LOG.debug(routeInfo);
				LOG.info("Route created");
//				dao.commit();
				return new NewRouteSummary(routeInfo);
			} else {
				return new NewRouteSummary(null);
			}
//		} finally {
//			dao.close();
//		}
	}
	private  boolean isDirectionExist(int st_dep, int st_arr){
		boolean isExist = false;
		for (Direction d : dao.getAllDirections()){
			Station s1 = dao.getStation(d.getSt_dep());
			Station s2 = dao.getStation(d.getSt_arr());
			if (s1.getStation_id()==st_dep&&s2.getStation_id()==st_arr){
				isExist = true;
			}
		}
		return isExist;
	}
	private  boolean isSheduleExist(int dir_id, int route_id, int step){
		boolean isExist = false;
		for (Shedule s : dao.getAllShedules()){
			if (s.getDirection_id()==dir_id&&s.getRoute_id()==route_id&&s.getStep()==step){
				isExist=true;
			}
		}
		return isExist;
	}
}
