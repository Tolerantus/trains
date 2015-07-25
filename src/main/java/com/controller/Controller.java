package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.dto.AllJourneysInfo;
import com.dto.AllRoutesInfo;
import com.dto.ChoosedJourney;
import com.dto.HoursMinutesCost;
import com.dto.DirectionData;
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

//@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(Controller.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Action action = Action.valueOf(request.getParameter("action"));
		RequestDispatcher dispatcher = null;
		try {
			HttpSession session = request.getSession();
			switch(action){
				case USER_VALIDATION:{
					String login = request.getParameter("username");
					String password = request.getParameter("password");
					UserInfo userInfo = new UserInfo(login, password);
					UserExist user = (UserExist)(new Dispatcher()).service(userInfo);
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/plain");

					if (user.isExist()){
						response.getWriter().write("exist");
					}else{
						response.getWriter().write("not exist");
					}
				};break;
				case AUTHENTICATION:{
					String login = request.getParameter("username");
					String password = request.getParameter("password");
					UserInfo userInfo = new UserInfo(login, password);
					UserExist user = (UserExist)(new Dispatcher()).service(userInfo);
					if (user.isExist()){
						dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
						LOG.info("User "+login+" logged in");
						session.setAttribute("user", login);
						session.setAttribute("admin", user.isAdmin());
						dispatcher.forward(request, response);
					}else{
						dispatcher = getServletContext().getRequestDispatcher("/Auth.jsp");
						request.setAttribute("error", "invalid combination login/password");;
						dispatcher.forward(request, response);
						
					}
						
				};break;
				case NEW_USER:{
					String username = request.getParameter("username");
					String password = request.getParameter("password1");
					NewUserInfo userInfo = new NewUserInfo(username, password);
					UserExist user = (UserExist)( new Dispatcher()).service(userInfo);
					if (!user.isExist()){
						LOG.info("User "+username+" has been registered");
						dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
						session.setAttribute("user", username);
						session.setAttribute("admin", user.isAdmin());
						
					}else{
						request.setAttribute("error", true);
						dispatcher = getServletContext().getRequestDispatcher("/NewUser.jsp");
					}
					dispatcher.forward(request, response);
				};break;
				case GET_SHEDULE:{
					session.removeAttribute("allStations");
					StationContainer container = new StationContainer(null);
					container = (StationContainer)( new Dispatcher()).service(container);
					if (!container.getStations().isEmpty())
					session.setAttribute("allStations", container.getStations());
					request.setAttribute("simpleShedule", true);
					dispatcher = getServletContext().getRequestDispatcher("/stationChoose.jsp");
					dispatcher.forward(request, response);
				};break;
				case GET_APPROPRIATE_JOURNEYS:{
					session.removeAttribute("journeysData");
					session.removeAttribute("helpInfo");
					String singleStation = request.getParameter("station");
					String st_dep = request.getParameter("st_dep");
					String st_arr = request.getParameter("st_arr");
					String date = request.getParameter("date");
					StationsForSheduling sts = new StationsForSheduling(singleStation, st_dep, st_arr,date);
					JourneysInfo info = (JourneysInfo)( new Dispatcher()).service(sts);
					if (info.getJourneyStringData()==null){
						request.setAttribute("error", true);
						if (singleStation!=null)
						request.setAttribute("simpleShedule", true);
						dispatcher = getServletContext().getRequestDispatcher("/stationChoose.jsp");
					}else{
						request.setAttribute("st_dep", st_dep);
						request.setAttribute("st_arr", st_arr);
						request.setAttribute("station", singleStation);
						session.setAttribute("journeysData", info.getJourneyStringData());
						session.setAttribute("helpInfo", info.getJourneyHelpInfo());
						if (info.getJourneyHelpInfo()==null){
							dispatcher = getServletContext().getRequestDispatcher("/stationShedule.jsp");
						}else{
							dispatcher = getServletContext().getRequestDispatcher("/apprJours.jsp");
						}
					}
					dispatcher.forward(request, response);
				};break;
				case BUY_TICKET:{
					session.removeAttribute("journeyData");
					session.removeAttribute("emptySeats");
					String journeyId = request.getParameter("journey");
					List<String> journeys = (List<String>) session.getAttribute("helpInfo");
					ChoosedJourney journey = new ChoosedJourney(journeyId, journeys,0);
					journey = (ChoosedJourney)( new Dispatcher()).service(journey);
					session.setAttribute("journeyData", journey.getJourneyId());
					session.setAttribute("emptySeats", journey.getEmptySeats());
					dispatcher  = getServletContext().getRequestDispatcher("/passengerRegistration.jsp");
					dispatcher.forward(request, response);
				};break;
				case REGISTER_PASSENGER:{
					String passengerDepAndDestStations = (String) session.getAttribute("journeyData");
					String name = request.getParameter("name");
					String surname = request.getParameter("surname");
					String year = request.getParameter("year");
					String month = request.getParameter("month");
					String day = request.getParameter("day");
					String currentUser = (String) session.getAttribute("user");
					List<String> allJourneysData = (List<String>)session.getAttribute("journeysData");
					PassengerInfo passengerInfo = new PassengerInfo
							(currentUser, passengerDepAndDestStations, name, surname, year, month, day,allJourneysData);
					TicketInfo ticketInfo = (TicketInfo) ( new Dispatcher()).service(passengerInfo);
					if (ticketInfo.isExist()){
						dispatcher = getServletContext().getRequestDispatcher("/passengerRegistration.jsp");
						request.setAttribute("error", true);
					}else{
						request.setAttribute("ticketInfo", ticketInfo.getTicketInfo());
						dispatcher = getServletContext().getRequestDispatcher("/NewTicket.jsp");
					}
					dispatcher.forward(request, response);
				};break;
				case FIND_JOURNEY:{
					session.removeAttribute("allStations");
					StationContainer container = new StationContainer(null);
					container = (StationContainer)( new Dispatcher()).service(container);
					if (!container.getStations().isEmpty())
					session.setAttribute("allStations", container.getStations());
					dispatcher = getServletContext().getRequestDispatcher("/stationChoose.jsp");
					dispatcher.forward(request, response);
				};break;
				case CHECK_TICKETS:{
					String user = (String)session.getAttribute("user");
					UserLoginContainer userLogin = new UserLoginContainer(user);
					ListOfTickets info = (ListOfTickets) ( new Dispatcher()).service(userLogin);
					dispatcher = getServletContext().getRequestDispatcher("/tickets.jsp");
					request.setAttribute("tickets", info.getTicketsInfo());
					dispatcher.forward(request, response);
				};break;
				case OUT:{
					dispatcher = getServletContext().getRequestDispatcher("/Auth.jsp");
					session.removeAttribute("user");
					session.removeAttribute("admin");
					session.removeAttribute("logged");
					dispatcher.forward(request, response);
				};break;
				case CREATE_ROUTE:{
					session.removeAttribute("allStations");
					StationContainer container = new StationContainer(null);
					container = (StationContainer)( new Dispatcher()).service(container);
					if (!container.getStations().isEmpty())
					session.setAttribute("allStations", container.getStations());
					dispatcher = getServletContext().getRequestDispatcher("/RouteCreator.jsp");
					dispatcher.forward(request, response);
				};break;
				case CREATE_START_FINISH:{
					session.removeAttribute("newRoute");
					String typed_dep = request.getParameter("dep");
					String typed_arr = request.getParameter("arr");
					String select_dep = request.getParameter("old_st_dep");
					String select_arr = request.getParameter("old_st_arr");
					NewRouteStartAndFinish stations = new NewRouteStartAndFinish(typed_dep, typed_arr, select_dep, select_arr);
					RouteStationList route = (RouteStationList) ( new Dispatcher()).service(stations);
					if (route.getRoute()==null){
						dispatcher = getServletContext().getRequestDispatcher("/RouteCreator.jsp");
					}else{
						dispatcher = getServletContext().getRequestDispatcher("/StationAdding.jsp");
						session.setAttribute("newRoute", route.getRoute());
					}
					dispatcher.forward(request, response);
				}; break;
				case INSERT_STATION:{
					String newStation = request.getParameter("newStation");
					String selectedStation = request.getParameter("stations");
					String step = request.getParameter("step");
					RouteStationList route = new RouteStationList((List<String>) session.getAttribute("newRoute"));
					StationForInsertInRoute station = new StationForInsertInRoute(newStation, selectedStation, step, route);
					route = (RouteStationList) ( new Dispatcher()).service(station);
					session.setAttribute("newRoute", route.getRoute());
					dispatcher = getServletContext().getRequestDispatcher("/StationAdding.jsp");
					dispatcher.forward(request, response);
				};break;
				case GET_TIME_AND_COST:{
					RouteStationList route = new RouteStationList((List<String>) session.getAttribute("newRoute"));
					DirectionData directionData = (DirectionData) ( new Dispatcher()).service(route);
					session.setAttribute("requiredDirectionData", directionData.getDirections());
					dispatcher = getServletContext().getRequestDispatcher("/inputDirections.jsp");
					dispatcher.forward(request, response);
				};break;
				case BUILD_NEW_ROUTE:{
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
					NewRouteSummary summary = (NewRouteSummary) ( new Dispatcher()).service
							(new RequiredDataForNewRoute(newRoute, newDirections, data, routename));
					if (summary.getSummary()==null){
						request.setAttribute("error", true);
						dispatcher = getServletContext().getRequestDispatcher("/inputDirections.jsp");
					}else{
						session.setAttribute("info", summary.getSummary());
						dispatcher = getServletContext().getRequestDispatcher("/newRouteInfo.jsp");
					}
					dispatcher.forward(request, response);
				};break;
				case CREATE_TRAIN:{
					String trainCapacity = request.getParameter("seats");
					NewTrainInfo train = new NewTrainInfo(trainCapacity);
					( new Dispatcher()).service(train);
					dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
					dispatcher.forward(request, response);
				};break;
				case GET_ROUTES_INFO:{
					session.removeAttribute("routes");
					AllRoutesInfo routes = new AllRoutesInfo(null);
					routes = (AllRoutesInfo) ( new Dispatcher()).service(routes);
					session.setAttribute("routes", routes.getRoutes());
					dispatcher = getServletContext().getRequestDispatcher("/ChoosingRoute.jsp");
					dispatcher.forward(request, response);
				};break;
				case PLAN_JOURNEY:{
					String routeInfo = request.getParameter("route");
					String date = request.getParameter("date");
					String time = request.getParameter("time");
					NewJourneyInfo journey = new NewJourneyInfo(routeInfo, date, time, null, null, null, false);
					journey = (NewJourneyInfo) ( new Dispatcher()).service(journey);
					if (journey.getDate()==""||journey.getTime()==""){
						request.setAttribute("err", true);
						dispatcher = getServletContext().getRequestDispatcher("/ChoosingRoute.jsp");
					}else
					if (journey.isTrainsLack()){
						dispatcher = getServletContext().getRequestDispatcher("/ChoosingRoute.jsp");
						request.setAttribute("trainsLack", true);
					}else
					if (journey.getTrain()==null){
						request.setAttribute("err", true);
						dispatcher = getServletContext().getRequestDispatcher("/ChoosingRoute.jsp");
					}else{
						request.setAttribute("id", journey.getJourneyId());
						request.setAttribute("route", journey.getRouteName());
						request.setAttribute("date", journey.getDate());
						request.setAttribute("train", journey.getTrain());
						dispatcher = getServletContext().getRequestDispatcher("/CreatingJourneySuccess.jsp");
					}
					
					dispatcher.forward(request, response);
				};break;
				case CREATE_STATION:{
					String station = request.getParameter("station");
					NewStationInfo info = new NewStationInfo(station,false);
					info = (NewStationInfo) ( new Dispatcher()).service(info);
					if (info.isExist()){
						request.setAttribute("err", true);
						dispatcher = getServletContext().getRequestDispatcher("/StationCreator.jsp");
					}else{
						dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
					}
					dispatcher.forward(request, response);
				};break;
				case GET_ALL_JOURNEYS_BRIEF:{
					session.removeAttribute("journeys");
					AllJourneysInfo info = new AllJourneysInfo(null);
					info = (AllJourneysInfo) ( new Dispatcher()).service(info);
					session.setAttribute("journeys", info.getJourneys());
					dispatcher = getServletContext().getRequestDispatcher("/JourneyChoosing.jsp");
					dispatcher.forward(request, response);
				};break;
				case GET_PASSENGERS:{
					String journeyInfo = request.getParameter("journey");
					JourneyAndPassengers journey = new JourneyAndPassengers(journeyInfo, null);
					journey = (JourneyAndPassengers) ( new Dispatcher()).service(journey);
					if (journey.getPassInfo()==null){
						request.setAttribute("err", true);
						dispatcher = getServletContext().getRequestDispatcher("/JourneyChoosing.jsp");
					}else{
						request.setAttribute("journey", journey.getJourneyInfo());
						request.setAttribute("passengers", journey.getPassInfo());
						dispatcher = getServletContext().getRequestDispatcher("/Passengers.jsp");
					}
					dispatcher.forward(request, response);
				};break;
				case RESET_DB:{
					if (session.getAttribute("user")!=null)
					( new Dispatcher()).service(new ResetRequest());
					dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
					dispatcher.forward(request, response);
				};break;
				case INIT_DB:{
					if (session.getAttribute("user")!=null)
					( new Dispatcher()).service(new InitDBRequest());
					dispatcher = getServletContext().getRequestDispatcher("/Menu.jsp");
					dispatcher.forward(request, response);
				};break;
				default:{
					dispatcher = getServletContext().getRequestDispatcher("/ErrorPage.jsp");
					dispatcher.forward(request, response);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e);
			dispatcher = getServletContext().getRequestDispatcher("/ErrorPage.jsp");
			dispatcher.forward(request, response);
		}
	}

}
