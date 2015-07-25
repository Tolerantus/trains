package com.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.JourneysInfo;
import com.dto.StationsForSheduling;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Journey;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Station;
@Service("getAppropriateJourneys")
public class GetAppropriateJourneys {
	@Autowired
	private Dao dao;
	private static final Logger LOG = Logger.getLogger(GetAppropriateJourneys.class);
//	public GetAppropriateJourneys(Dao dao) {
//		super();
//		this.dao = dao;
//	}
	@Transactional
public  JourneysInfo getJourneys(StationsForSheduling dto) throws ParseException{
//	try {
//		dao.begin();
		LOG.debug(dto);
		String station = dto.getSingleStation();
		String st_dep = dto.getSt_dep();
		String st_arr = dto.getSt_arr();
		String date = dto.getDate();
		Station stationForSimpleShedule = null;
		if (station != null) {
			stationForSimpleShedule = dao.getStationByName(station);
			JourneysInfo info = simpleSheduling(stationForSimpleShedule);
			LOG.debug(info);
//			dao.commit();
			return info;
		} else {
			JourneysInfo info = getAppropriateJourneys(dao.getStationByName(st_dep), dao.getStationByName(st_arr), date);
			LOG.debug(info);
//			dao.commit();
			return info;
		}
//	} finally {
//		dao.close();
//	}
}
public JourneysInfo simpleSheduling(Station station){
	List<Route> appropriateRoutes = new ArrayList<Route>();
	JourneysInfo journeysInfo = new JourneysInfo(null, null);
	List<Journey> appropriateJourneys = new ArrayList<Journey>();
	SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm  dd MMM", Locale.US);
	for (Route r : dao.getAllRoutes()){
		Set<Station> stationsOnRoute = dao.getAllStationsOnRoute(r.getRoute_id());
		if (stationsOnRoute.contains(station)){
			appropriateRoutes.add(r);
		}
	}
	if (!appropriateRoutes.isEmpty()){
		
		for (Journey j : dao.getAllJourneys()){
			if (appropriateRoutes.contains(dao.getRoute(j.getRoute_id()))){
				
				List<Shedule> steps = dao.getShedulesOfRoute(j.getRoute_id());
				
				for (Shedule step : steps){
					Direction direction = dao.getDirection(step.getDirection_id());
					if (direction.getSt_dep()==station.getStation_id()){
						appropriateJourneys.add(j);
					}
				}
			}
		}
	}else{
		return journeysInfo;
	}
	if (!appropriateJourneys.isEmpty()){
		
		List<String> journeyStringData = new ArrayList<String>();
		for (Journey j : appropriateJourneys){
			String id = String.valueOf(j.getJourney_id());
			Date departure = j.getTime_dep();
			Station routeBeginning = null;
			Station routeEnding = null;
			List<Shedule> steps = dao.getShedulesOfRoute(j.getRoute_id());
			int depStep = 0;
			int lastStep = 0;
			for (Shedule step : steps){
				Direction d = dao.getDirection(step.getDirection_id());
				if (step.getStep()==0){
					routeBeginning = dao.getStation(d.getSt_dep());
				}
				lastStep = Math.max(lastStep, step.getStep());
				if (lastStep==step.getStep()){
					routeEnding = dao.getStation(d.getSt_arr());
				}
				if (d.getSt_dep()==station.getStation_id()){
					depStep=step.getStep();
				}
			}
			for (Shedule step : steps){
				Direction d = dao.getDirection(step.getDirection_id());
				if (step.getStep()<depStep){
					departure = new Date(departure.getTime()+d.getTime());
				}
			}
			Date currentTime = new Date();
			if (currentTime.before(departure)){
				StringBuilder journeyData = new StringBuilder(id);
				journeyData.append(";");
				journeyData.append(sdf1.format(departure));
				journeyData.append(";");
				journeyData.append(routeBeginning.getStation_name());
				journeyData.append(";");
				journeyData.append(routeEnding.getStation_name());
				journeyStringData.add(journeyData.toString());
			}
		}
		
		if (journeyStringData.size()!=0)
			journeysInfo.setJourneyStringData(journeyStringData);
			
		return journeysInfo;
	}else{
		return journeysInfo;
	}
}

public  JourneysInfo getAppropriateJourneys(Station st_dep, Station st_arr, String date) throws ParseException{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm dd MMM", Locale.US);
	JourneysInfo journeysInfo = new JourneysInfo(null, null);
	List<Route> appropriateRoutes = new ArrayList<Route>();
	List<Journey> appropriateJourneys = new ArrayList<Journey>();
	List<String> helpInfo = new ArrayList<String>();
	for (Route r : dao.getAllRoutes()){
		Set<Station> stationsOnRoute = dao.getAllStationsOnRoute(r.getRoute_id());
		if (stationsOnRoute.contains(st_dep)&&stationsOnRoute.contains(st_arr)){
			appropriateRoutes.add(r);
		}
	}
	if (!appropriateRoutes.isEmpty()){
		for (Journey j : dao.getAllJourneys()){
			if (appropriateRoutes.contains(dao.getRoute(j.getRoute_id()))){
				List<Shedule> steps = new ArrayList<Shedule>();
				steps.addAll(dao.getShedulesOfRoute(j.getRoute_id()));
				double departurePoint = 0;
				double arrivalPoint = 0;
				for (Shedule step : steps){
					Direction direction = dao.getDirection(step.getDirection_id());
					if (direction.getSt_dep()==st_dep.getStation_id()){
						departurePoint=step.getStep();
					}
					if (direction.getSt_arr()==st_dep.getStation_id()){
						departurePoint=step.getStep()+1;
					}
					
					if (direction.getSt_dep()==st_arr.getStation_id()){
						arrivalPoint = step.getStep();
					}
					if (direction.getSt_arr()==st_arr.getStation_id()){
						arrivalPoint = step.getStep()+1;
					}
				}
				if (departurePoint<arrivalPoint){
					Date currentTime = new Date();
					if ((j.getTime_dep().getTime()-currentTime.getTime())>10*60*1000)
					appropriateJourneys.add(j);
				}
			}
		}
	
		if (!appropriateJourneys.isEmpty()){
			
			List<String> journeyStringData = new ArrayList<String>();
			for (Journey j : appropriateJourneys){
				String id = String.valueOf(j.getJourney_id());
				Date departure = j.getTime_dep();
				Date arrival = j.getTime_dep();
				
				List<Shedule> steps = dao.getShedulesOfRoute(j.getRoute_id());
				int depStep = 0;
				int arrStep = 0;
				double cost = 0;
				for (Shedule step : steps){
					Direction d = dao.getDirection(step.getDirection_id());
					if (d.getSt_dep()==st_dep.getStation_id()){
						depStep = step.getStep();
					}
					if (d.getSt_arr()==st_arr.getStation_id()){
						arrStep = step.getStep();
					}
				}
				for (Shedule step : steps){
					Direction d = dao.getDirection(step.getDirection_id());
					if (step.getStep()<depStep){
						departure = new Date(departure.getTime()+d.getTime());
						arrival = new Date(arrival.getTime()+d.getTime());
					}
					if (step.getStep()>=depStep&&step.getStep()<=arrStep){
						arrival = new Date(arrival.getTime()+d.getTime());
						cost+=d.getCost();
					}
				}
				if (date!=""){
					Date targetedDateBegin = sdf.parse(date);
					
					Date targetedDateEnd = new Date(targetedDateBegin.getTime()+24*60*60*1000);
					
					if (j.getTime_dep().getTime()>=targetedDateBegin.getTime()&&
							j.getTime_dep().getTime()<=targetedDateEnd.getTime()){
						StringBuilder journeyData = new StringBuilder(id);
						journeyData.append(";");
						journeyData.append(sdf1.format(departure));
						journeyData.append(";");
						journeyData.append(sdf1.format(arrival));
						journeyData.append(";");
						journeyData.append(cost);
						journeyStringData.add(journeyData.toString());
						
						StringBuilder info = new StringBuilder(id);
						info.append(";");
						info.append(depStep);
						info.append(";");
						info.append(arrStep);
						info.append(";");
						info.append(st_dep.getStation_id());
						info.append(";");
						info.append(st_arr.getStation_id());
						helpInfo.add(info.toString());
					}
				}else{
					StringBuilder journeyData = new StringBuilder(id);
					journeyData.append(";");
					journeyData.append(sdf1.format(departure));
					journeyData.append(";");
					journeyData.append(sdf1.format(arrival));
					journeyData.append(";");
					journeyData.append(cost);
					journeyStringData.add(journeyData.toString());
					
					StringBuilder info = new StringBuilder(id);
					info.append(";");
					info.append(depStep);
					info.append(";");
					info.append(arrStep);
					info.append(";");
					info.append(st_dep.getStation_id());
					info.append(";");
					info.append(st_arr.getStation_id());
					helpInfo.add(info.toString());
				}
			}
			if (journeyStringData.size()!=0)
			journeysInfo.setJourneyStringData(journeyStringData);
			
			if (helpInfo.size()!=0)
			journeysInfo.setJourneyHelpInfo(helpInfo);
			return journeysInfo;
		}else{
			return journeysInfo;
			
		}
	}else{
		return journeysInfo;
	}
		
}
}
