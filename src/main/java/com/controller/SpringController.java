package com.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dto.AllJourneysInfo;
import com.dto.AllRoutesInfo;
import com.dto.ChoosedJourney;
import com.dto.DirectionData;
import com.dto.HoursMinutesCost;
import com.dto.InitDBRequest;
import com.dto.JourneyAndPassengers;
import com.dto.JourneysInfo;
import com.dto.ListOfTickets;
import com.dto.NewJourneyInfo;
import com.dto.NewRouteStartAndFinish;
import com.dto.NewRouteSummary;
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
import com.dto.TicketInfo;
import com.dto.UserExist;
import com.dto.UserInfo;
import com.dto.UserLoginContainer;
import com.service.Dispatcher;


@Controller
public class SpringController {
	private static final Logger LOG = Logger.getLogger("springController");
	@Autowired
	private Dispatcher dispatcher;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String hello(HttpServletRequest request) {
	    System.out.println(request.getServletPath());
	    return "Auth";
	}
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String showNewUserRegisterForm(HttpServletRequest request, Model model) {
		return "NewUser";
	}
	@RequestMapping(value = "/menu", method = RequestMethod.POST)
    public String validateUser(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String login = request.getParameter("username");
		String password = request.getParameter("password");
		UserInfo userInfo = new UserInfo(login, password);
		UserExist user = null;
		try {
			user = (UserExist)dispatcher.service(userInfo);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (user.isExist()){
			LOG.info("User " + login + " logged in");
			session.setAttribute("user", login);
			session.setAttribute("admin", user.isAdmin());
			return "Menu";
		}else{
			model.addAttribute("error", "invalid combination login/password");
			return "Auth";
		}
	}
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String returnToMenu(HttpServletRequest request, Model model) {
		return "Menu";
	}
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password1");
		NewUserInfo userInfo = new NewUserInfo(username, password);
		UserExist user = null;
		try {
			user = (UserExist)dispatcher.service(userInfo);
		} catch (Exception e) {
			LOG.error(e);
		}
		if (!user.isExist()){
			LOG.info("User "+username+" has been registered");
			session.setAttribute("user", username);
			session.setAttribute("admin", user.isAdmin());
			return "Menu";
		}else{
			model.addAttribute("error", "This username is alerady used");
			return "NewUser";
		}
	}
	@RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public String getSchedule(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("allStations");
		StationContainer container = new StationContainer(null);
		try {
			container = (StationContainer)dispatcher.service(container);
		} catch (Exception e) {
			LOG.error(e);
		}
		if (!container.getStations().isEmpty())
			session.setAttribute("allStations", container.getStations());
		model.addAttribute("simpleShedule", true);
		return "stationChoose";
	}
	@RequestMapping(value = "/appropriateJourneys", method = RequestMethod.POST)
    public String getAppropriateJourneys(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String singleStation = request.getParameter("station");
		String st_dep = request.getParameter("st_dep");
		String st_arr = request.getParameter("st_arr");
		String date = request.getParameter("date");
		session.removeAttribute("journeysData");
		session.removeAttribute("helpInfo");
		StationsForSheduling sts = new StationsForSheduling(singleStation, st_dep, st_arr,date);
		JourneysInfo info = null;
		try {
			info = (JourneysInfo)dispatcher.service(sts);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (info.getJourneyStringData() == null){
			model.addAttribute("error", true);
			if (singleStation != null)
			model.addAttribute("simpleShedule", true);
			return "StationChoose";
		}else{
			model.addAttribute("st_dep", st_dep);
			model.addAttribute("st_arr", st_arr);
			model.addAttribute("station", singleStation);
			session.setAttribute("journeysData", info.getJourneyStringData());
			session.setAttribute("helpInfo", info.getJourneyHelpInfo());
			if (info.getJourneyHelpInfo() == null){
				return "stationShedule";
			}else{
				return "apprJours";
			}
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/appropriateJourneys/buyTicket", method = RequestMethod.POST)
    public String buyTicket(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String journeyId = request.getParameter("journey");
		session.removeAttribute("journeyData");
		session.removeAttribute("emptySeats");
		List<String> journeys = (List<String>) session.getAttribute("helpInfo");
		ChoosedJourney journey = new ChoosedJourney(journeyId, journeys,0);
		try {
			journey = (ChoosedJourney)dispatcher.service(journey);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		session.setAttribute("journeyData", journey.getJourneyId());
		session.setAttribute("emptySeats", journey.getEmptySeats());
		return "PassengerRegistration";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/appropriateJourneys/buyTicket/passenger", method = RequestMethod.POST)
    public String registerPassenger(HttpServletRequest request, Model model, HttpSession session) {
		String passengerDepAndDestStations = (String) session.getAttribute("journeyData");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		String currentUser = (String) session.getAttribute("user");
		List<String> allJourneysData = (List<String>)session.getAttribute("journeysData");
		PassengerInfo passengerInfo = new PassengerInfo
				(currentUser, passengerDepAndDestStations, name, surname, year, month, day, allJourneysData);
		TicketInfo ticketInfo;
		try {
			ticketInfo = (TicketInfo) dispatcher.service(passengerInfo);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (ticketInfo.isExist()){
			model.addAttribute("error", true);
			return "passengerRegistration";
		}else{
			model.addAttribute("ticketInfo", ticketInfo.getTicketInfo());
			return "NewTicket";
		}
	}
	@RequestMapping(value = "/stationsChoosing", method = RequestMethod.POST)
    public String findJourney(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("allStations");
		StationContainer container = new StationContainer(null);
		try {
			container = (StationContainer)dispatcher.service(container);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (!container.getStations().isEmpty())
		session.setAttribute("allStations", container.getStations());
		return "stationChoose";
	}
	@RequestMapping(value = "/myTickets", method = RequestMethod.POST)
    public String checkTickets(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String user = (String)session.getAttribute("user");
		UserLoginContainer userLogin = new UserLoginContainer(user);
		ListOfTickets info;
		try {
			info = (ListOfTickets) dispatcher.service(userLogin);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		model.addAttribute("tickets", info.getTicketsInfo());
		return "tickets";
	}
	@RequestMapping(value="/out", method = RequestMethod.POST)
	public String out(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		session.removeAttribute("admin");
		session.removeAttribute("logged");
		return "Auth";
	}
	@RequestMapping(value="/newRoute", method = RequestMethod.POST)
	public String createRoute(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("allStations");
		StationContainer container = new StationContainer(null);
		try {
			container = (StationContainer)dispatcher.service(container);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (!container.getStations().isEmpty())
		session.setAttribute("allStations", container.getStations());
		return "RouteCreator";
	}
	@RequestMapping(value="/newRoute/newStartAndFinish", method = RequestMethod.POST)
	public String createStartFinish(HttpServletRequest request,	Model model) {
		HttpSession session = request.getSession();
		String typed_dep = request.getParameter("dep");
		String typed_arr = request.getParameter("arr");
		String select_dep = request.getParameter("old_st_dep");
		String select_arr = request.getParameter("old_st_arr");
		session.removeAttribute("newRoute");
		NewRouteStartAndFinish stations = new NewRouteStartAndFinish(typed_dep, typed_arr, select_dep, select_arr);
		RouteStationList route = null;
		try {
			route = (RouteStationList) dispatcher.service(stations);
		} catch (Exception e) {
			
		}
		if (route.getRoute() == null){
			return "RouteCreator";
		}else{
			session.setAttribute("newRoute", route.getRoute());
			return "StationAdding";
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newRoute/newStartAndFinish/newStation", method = RequestMethod.POST)
	public String injectNewStationInRoute(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String newStation = request.getParameter("newStation");
		String selectedStation = request.getParameter("stations");
		String step = request.getParameter("step");
		RouteStationList route = new RouteStationList((List<String>) session.getAttribute("newRoute"));
		StationForInsertInRoute station = new StationForInsertInRoute(newStation, selectedStation, step, route);
		try {
			route = (RouteStationList) dispatcher.service(station);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		session.setAttribute("newRoute", route.getRoute());
		return "StationAdding";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newRoute/newStartAndFinish/timeAndCost", method = RequestMethod.POST)
	public String inputTimeAndCost(HttpServletRequest request) {
		HttpSession session = request.getSession();
		RouteStationList route = new RouteStationList((List<String>) session.getAttribute("newRoute"));
		DirectionData directionData;
		try {
			directionData = (DirectionData) dispatcher.service(route);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		session.setAttribute("requiredDirectionData", directionData.getDirections());
		return "inputDirections";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newRoute/newStartAndFinish/timeAndCost/newRoute", method = RequestMethod.POST)
	public String buildNewRoute(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("info");
		List<String> newRoute = (List<String>) session.getAttribute("newRoute");
		List<String> newDirections = (List<String>) session.getAttribute("requiredDirectionData");
		List<HoursMinutesCost> data = new ArrayList<HoursMinutesCost>();
		String routename = request.getParameter("route");
		for (String direction : newDirections){
			String hours = request.getParameter(direction+"-time-hours");
			String minutes = request.getParameter(direction+"-time-minutes");
			String cost = request.getParameter(direction+"-cost");
			data.add(new HoursMinutesCost(hours, minutes, cost));
		}
		NewRouteSummary summary;
		try {
			summary = (NewRouteSummary) dispatcher.service
					(new RequiredDataForNewRoute(newRoute, newDirections, data, routename));
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (summary.getSummary() == null){
			model.addAttribute("error", true);
			return "inputDirections";
		}else{
			session.setAttribute("info", summary.getSummary());
			return "newRouteInfo";
		}
	}
	@RequestMapping(value="/creatingTrain", method = RequestMethod.POST)
	public String showNewTrainForm(HttpServletRequest request, Model model) {
		return "TrainCreator";
	}
	
	@RequestMapping(value="/newTrain", method = RequestMethod.POST)
	public String createTrain(HttpServletRequest request, Model model) {
		String trainCapacity = request.getParameter("seats");
		NewTrainInfo train = new NewTrainInfo(trainCapacity);
		try {
			dispatcher.service(train);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		return "Menu";
	}
	@RequestMapping(value="/routesInfo", method = RequestMethod.POST)
	public String getRoutesInfo(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("routes");
		AllRoutesInfo routes = new AllRoutesInfo(null);
		try {
			routes = (AllRoutesInfo) dispatcher.service(routes);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		session.setAttribute("routes", routes.getRoutes());
		return "ChoosingRoute";
	}
	@RequestMapping(value="/routesInfo/newJourney", method = RequestMethod.POST)
	public String planNewJourney(HttpServletRequest request, Model model) {
		String routeInfo = request.getParameter("route");
		String date = request.getParameter("date");
		String time = request.getParameter("time");
		NewJourneyInfo journey = new NewJourneyInfo(routeInfo, date, time, null, null, null, false);
		try {
			journey = (NewJourneyInfo) dispatcher.service(journey);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (journey.getDate()==""||journey.getTime()==""){
			model.addAttribute("err", true);
			return "ChoosingRoute";
		}else
		if (journey.isTrainsLack()){
			model.addAttribute("trainsLack", true);
			return "ChoosingRoute";
		}else
		if (journey.getTrain() == null){
			model.addAttribute("err", true);
			return "ChoosingRoute";
		}else{
			model.addAttribute("id", journey.getJourneyId());
			model.addAttribute("route", journey.getRouteName());
			model.addAttribute("date", journey.getDate());
			model.addAttribute("train", journey.getTrain());
			return "CreatingJourneySuccess";
		}
	}
	@RequestMapping(value="/newStationForm", method = RequestMethod.POST)
	public String showNewStationForm(HttpServletRequest request, Model model) {
		return "StationCreator";
	}
	@RequestMapping(value="/newStation", method = RequestMethod.POST)
	public String createStation(HttpServletRequest request, Model model) {
		String station = request.getParameter("station");
		NewStationInfo info = new NewStationInfo(station,false);
		try {
			info = (NewStationInfo) dispatcher.service(info);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (info.isExist()){
			model.addAttribute("err", true);
			return "StationCreator";
		}else{
			return "Menu";
		}
	}
	@RequestMapping(value="/journeyList", method = RequestMethod.POST)
	public String getJourneyList(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("journeys");
		AllJourneysInfo info = new AllJourneysInfo(null);
		try {
			info = (AllJourneysInfo) dispatcher.service(info);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		session.setAttribute("journeys", info.getJourneys());
		return "JourneyChoosing";
	}
	@RequestMapping(value="/journeyList/passengers", method = RequestMethod.POST)
	public String showPassengers(HttpServletRequest request, Model model) {
		String journeyInfo = request.getParameter("journey");
		JourneyAndPassengers journey = new JourneyAndPassengers(journeyInfo, null);
		try {
			journey = (JourneyAndPassengers) dispatcher.service(journey);
		} catch (Exception e) {
			LOG.error(e);
			return "ErrorPage";
		}
		if (journey.getPassInfo()==null){
			model.addAttribute("err", true);
			return "JourneyChoosing";
		}else{
			model.addAttribute("journey", journey.getJourneyInfo());
			model.addAttribute("passengers", journey.getPassInfo());
			return "Passengers";
		}
	}
	@RequestMapping(value="/resetDB", method = RequestMethod.POST)
	public String resetDB(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") != null)
			try {
				dispatcher.service(new ResetRequest());
			} catch (Exception e) {
				LOG.error(e);
				return "ErrorPage";
			}
		return "Menu";
	}
	@RequestMapping(value="/initDB", method = RequestMethod.POST)
	public String initDB(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user")!=null)
			try {
				dispatcher.service(new InitDBRequest());
			} catch (Exception e) {
				LOG.error(e);
				return "ErrorPage";
			}
		return "Menu";
	}
}
