package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.AllJourneysInfo;
import com.dto.AllRoutesInfo;
import com.dto.ChoosedJourney;
import com.dto.InitDBRequest;
import com.dto.JourneyAndPassengers;
import com.dto.NewJourneyInfo;
import com.dto.NewRouteStartAndFinish;
import com.dto.NewStationInfo;
import com.dto.NewTrainInfo;
import com.dto.NewUserInfo;
import com.dto.PassengerInfo;
import com.dto.RequiredDataForNewRoute;
import com.dto.ResetRequest;
import com.dto.RouteStationList;
import com.dto.StationContainer;
import com.dto.StationForInsertInRoute;
import com.dto.StationsForSheduling;
import com.dto.UserInfo;
import com.dto.UserLoginContainer;
@Service("dispatcher")
public class Dispatcher {
	@Autowired
	private CheckUser checkUser; 
	@Autowired
	private UserRegistrator userRegistrator;
	@Autowired
	private StationReturner stationReturner;
	@Autowired
	private GetAppropriateJourneys getAppropriateJourneys;
	@Autowired
	private SeatsChecker seatsChecker;
	@Autowired
	private PassengerRegistrator passengerRegistrator;
	@Autowired
	private TicketsChecker ticketsChecker;
	@Autowired
	private RouteCreator routeCreator;
	@Autowired
	private RouteStationInjector routeStationInjector;
	@Autowired
	private RouteDirectionsChecker routeDirectionsChecker;
	@Autowired
	private FinalRouteBuilder finalRouteBuilder;
	@Autowired
	private TrainCreator trainCreator;
	@Autowired
	private RoutesInformator routesInformator;
	@Autowired
	private JourneyPlanner journeyPlanner;
	@Autowired
	private StationCreator stationCreator;
	@Autowired
	private JourneyBriefer journeyBriefer;
	@Autowired
	private PassengersInformator passengersInformator;
	@Autowired
	private Resetter resetter;
	@Autowired
	private DBInitializer dbInitializer;
	
	public  Object service(Object dto) throws Exception{
		if (dto instanceof UserInfo){
			return checkUser.check((UserInfo)dto);
		}else
		if (dto instanceof NewUserInfo){
			return userRegistrator.register((NewUserInfo)dto);
		}else
		if (dto instanceof StationContainer){
			return stationReturner.getStations((StationContainer)dto);
		}else
		if (dto instanceof StationsForSheduling){
			return getAppropriateJourneys.getJourneys((StationsForSheduling) dto);
		}else
		if (dto instanceof ChoosedJourney){
			return seatsChecker.check((ChoosedJourney) dto);
		}else
		if (dto instanceof PassengerInfo){
			return passengerRegistrator.register((PassengerInfo) dto);
		}else
		if (dto instanceof UserLoginContainer){
			return ticketsChecker.check((UserLoginContainer) dto);
		}else
		if (dto instanceof NewRouteStartAndFinish){
			return routeCreator.append((NewRouteStartAndFinish) dto);
		}else
		if (dto instanceof StationForInsertInRoute){
			return routeStationInjector.inject((StationForInsertInRoute) dto);
		}else
		if (dto instanceof RouteStationList){
			return routeDirectionsChecker.check((RouteStationList) dto);
		}else
		if (dto instanceof RequiredDataForNewRoute){
			return finalRouteBuilder.build((RequiredDataForNewRoute) dto);
		}else
		if (dto instanceof NewTrainInfo){
			trainCreator.create((NewTrainInfo) dto);
		}else
		if (dto instanceof AllRoutesInfo){
			return routesInformator.getInfo((AllRoutesInfo) dto);
		}else
		if (dto instanceof NewJourneyInfo){
			return journeyPlanner.plan((NewJourneyInfo) dto);
		}else
		if (dto instanceof NewStationInfo){
			return stationCreator.create((NewStationInfo) dto);
		}else
		if (dto instanceof AllJourneysInfo){
			return journeyBriefer.getInfo((AllJourneysInfo) dto);
		}else
		if (dto instanceof JourneyAndPassengers){
			return passengersInformator.getInfo((JourneyAndPassengers) dto);
		}else
		if (dto instanceof ResetRequest){
			resetter.reset((ResetRequest) dto);
		}else
		if (dto instanceof InitDBRequest){
			dbInitializer.init((InitDBRequest) dto);
		}
	return null;
	}
}
