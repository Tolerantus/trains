package com.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository("dao")
public class Dao {
private Random random = new Random(47);
//private final String selectTrains = "select t from Train t"; 
//private final String selectStations = "select s from Station s";
//private final String selectUsers = "select u from User u";
//private final String selectDirections = "select d from Direction d";
//private final String selectShedules = "select s from Shedule s";
//private final String selectRoutes = "select r from Route r";
//private final String selectJourneys = "select j from Journey j";
//private final String selectPassengers = "select p from Passenger p";
//private final String selectTickets = "select t from Ticket t";
//private final String selectUser_Tickets = "select ut from User_Ticket ut";
//private final String selectSeats = "select s from Seats s";
@Resource(name="sessionFactory")
private SessionFactory sessionFactory;

public  void createTrain( int seats) {
	Session session = sessionFactory.getCurrentSession();
	
	Train train = new Train();
	train.setTrain_seats(seats);
	session.save(train);
//	em.persist(train);
}

@SuppressWarnings("unchecked")
public  List<Train> getAllTrains() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("From Train");
	return query.list();
//	return em.createQuery(selectTrains).getResultList();
}


public  void initTrains() {
	clearTrains();
	for (int i=0; i<10; i++) {
		createTrain(100);
	}
}

public  Train getTrain( int train_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Train)session.get("Train", train_id);
//	return em.find(Train.class, train_id);
}
public  void deleteTrain( int train_id) {
	Session session = sessionFactory.getCurrentSession();
	Train t = (Train)session.get("Train", train_id);
	session.delete(t);
//	Train t = (Train)em.createQuery(selectTrains + " where t.train_id=" + train_id).getSingleResult();
//	em.remove(t);
//	return t;
}
public  void clearTrains() {
	Session session = sessionFactory.getCurrentSession();
	List<Train> trains = getAllTrains();
	for (Train t : trains) {
		session.delete(t);
	}
//	List<Train> trains = (List<Train>)em.createQuery(selectTrains).getResultList();
//	for (Train t: trains) {
//		em.remove(t);
//	}
}

public Station createStation(String name) {
	Session session = sessionFactory.getCurrentSession();
	Station station = new Station();
	station.setStation_name(name);
	session.save(station);
	return station;
//	em.persist(station);
//	return station;
}

public  void initStations() {
	clearStations();
	String[] cities = {"Moscow", "Saint-Petersburg", "Ryazan", "Vladivostok", "Petrozavodsk", "Volgograd", "Novgorod", "Samara"};
	for (String city:cities) {
		createStation(city);
	}
}

public  Station getStation( int station_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Station)session.get("Station", station_id);
//	return em.find(Station.class, station_id);
}

@SuppressWarnings("unchecked")
public  Station getStationByName(String name) {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Station as s where s.station_name=:name");
//	Query query = em.createQuery(selectStations + " where s.station_name=:name");
	query.setParameter("name", name);
	List<Station> stations = query.list();
	if (stations.size()==0) {
		return null;
	} else
	if (stations.size()>1) {
		throw new RuntimeException("we have duplicate stations");
	} else {
	return stations.get(0);
	}
	
}
@SuppressWarnings("unchecked")
public  List<Station> getAllStations() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Station");
	return query.list();
//	List<Station> stations = (List<Station>)em.createQuery(selectStations).getResultList();
//	return stations;
}



@SuppressWarnings("unchecked")
public  Set<Station> getAllStationsOnRoute( int route_id) {
	Session session = sessionFactory.getCurrentSession();
	Set<Station> stations = new HashSet<Station>();
	Query query = session.createQuery("from Shedule as s where s.route_id="+route_id);
//	for (Shedule s : (List<Shedule>)em.createQuery(selectShedules + " where s.route_id=" + route_id).getResultList()) {
	for (Shedule s : (List<Shedule>)query.list()) {	
		Direction d = getDirection(s.getDirection_id());
		stations.add(getStation(d.getSt_arr()));
		stations.add(getStation(d.getSt_dep()));
	}
	
	return stations;
}
public  void deleteStation( int station_id) {
	Session session = sessionFactory.getCurrentSession();
	Station s = (Station) session.get("Station", station_id);
	session.delete(s);
//	em.remove(getStation(station_id));
}

public  void clearStations() {
//	List<Station> stations = em.createQuery(selectStations).getResultList();
	List<Station> stations = getAllStations();
	for (Station s: stations) {
		deleteStation(s.getStation_id());
	}
}

public  Direction createDirection( int st_dep,  int st_arr,  long time,  double cost) {
	Session session = sessionFactory.getCurrentSession();
	
	Direction direction = new Direction();
	direction.setSt_dep(st_dep);
	direction.setSt_arr(st_arr);
	direction.setTime(time);
	direction.setCost(cost);
	session.save(direction);
	
	Direction direction_reverse = new Direction();
	direction_reverse.setSt_dep(st_arr);
	direction_reverse.setSt_arr(st_dep);
	direction_reverse.setTime(time);
	direction_reverse.setCost(cost);
	session.save(direction_reverse);
	
	return direction;
}

@SuppressWarnings("unchecked")
public  Direction getDirectionByStartFinish( int start,  int finish){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Direction as d where d.st_dep=" + start + " and d.st_arr=" + finish);
	List<Direction> directions = query.list();
//	List<Direction> directions = em.createQuery(
//			selectDirections + " where d.st_dep=" + start + " and d.st_arr=" + finish).getResultList();
	if (directions.size() == 0) {
		return null;
	} else
	if (directions.size() > 1) {
		throw new RuntimeException("more then one direction were found");
	} else {
		return directions.get(0);
	}
}
public  void initDirections() {
	clearDirections();
	List<Station> stations = getAllStations();
	if (stations.size() > 1) {
		for (int i = 0; i < stations.size(); i++) {
			int st_dep = stations.get(i).getStation_id();
			for (int j = i + 1; j < stations.size(); j++) {
				int st_arr = stations.get(j).getStation_id();
				int time = 1000 * 60 * 30 + random.nextInt(1000 * 60 * 60 * 2);
				double cost = 400d + random.nextInt(200);
				createDirection(st_dep, st_arr, time, cost);
			}
		}
	}
}

public  Direction getDirection( int dir_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Direction) session.get("Direction", dir_id);
//	return em.find(Direction.class, dir_id);
}
@SuppressWarnings("unchecked")
public  List<Direction> getAllDirections() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Direction");
	return query.list();
//	return em.createQuery(selectDirections).getResultList();
}
@SuppressWarnings("unchecked")
public  Direction deleteDirection( int st_dep,  int st_arr){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Direction as d where d.st_sep=" + st_dep + " and st_arr=" + st_arr);
//	List<Direction> directions = em.createQuery(selectDirections + " where d.st_sep=" + st_dep + " and st_arr=" + st_arr).getResultList();
	List<Direction> directions = query.list();
	if (directions.size() == 0) {
		return null;
	} else
	if (directions.size() > 1) {
		throw new RuntimeException("more than one direction were found");
	} else {
		session.delete(directions.get(0));
	}
	return directions.get(0);
}
public  void deleteDirection( int direction_id){
		Session session = sessionFactory.getCurrentSession();
		Direction d = (Direction) session.get("Direction", direction_id);
//		Direction d = em.find(Direction.class, direction_id);
		int st_dep = d.getSt_dep();
		int st_arr = d.getSt_arr();
		deleteDirection(st_dep, st_arr);
		deleteDirection(st_arr, st_dep);
}
public  void clearDirections() {
//	Query query = em.createQuery(selectDirections);
//	List<Direction> directions = query.getResultList();
	List<Direction> directions = getAllDirections();
	for (Direction d: directions) {
//		em.remove(d);
		deleteDirection(d.getDirection_id());
	}
}

public  Route createRoute( String name) {
	Session session = sessionFactory.getCurrentSession();
	Route route = new Route();
	route.setRoute_name(name);
	session.save(route);
//	em.persist(route);
	return route;
}

public  Route getRoute( int route_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Route) session.get("Route", route_id);
//	return em.find(Route.class, route_id);
}

@SuppressWarnings("unchecked")
public  List<Route> getAllRoutes() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Route");
	return query.list();
//	return em.createQuery(selectRoutes).getResultList();
}
public  void initRoutes(){
	clearRoutes();
	String alphabet = "abcdefghijklmnopqrstuvwxyz";
	for (int i = 0; i < 10; i++) {
		createRoute(String.valueOf(100 + i) + alphabet.charAt(random.nextInt(alphabet.length())));
	}
}
@SuppressWarnings("unchecked")
public  Route deleteRoute( int route_id){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Route where r.route_id=" + route_id);
	List<Route> routes = query.list();
//	List<Route> routes = em.createQuery(selectRoutes + " where r.route_id=" + route_id).getResultList();
	if (routes.size() == 0) {
		return null;
	} else
	if (routes.size() > 1) {
		throw new RuntimeException("more than one route were found");
	} else {
		session.delete(routes.get(0));
	}
	return routes.get(0);
}
public void clearRoutes(){
	
//	List<Route> routes = em.createQuery(selectRoutes).getResultList();
//	for (Route r: routes) {
	for (Route r: getAllRoutes()) {
		deleteRoute(r.getRoute_id());
	}
}
public   Shedule createShedule( int direction_id,  int route_id,  int step) {
	Session session = sessionFactory.getCurrentSession();
	Shedule shedule = new Shedule();
	shedule.setDirection_id(direction_id);
	shedule.setRoute_id(route_id);
	shedule.setStep(step);
	session.save(shedule);
//	em.persist(shedule);
	return shedule;
}

@SuppressWarnings("unchecked")
public  void initShedules() {
	Session session = sessionFactory.getCurrentSession();
	clearShedules();
	List<Route> routes = getAllRoutes();
	List<Station> stations = getAllStations();
	for (Route r: routes) {
		Set<Integer> route_stations = new HashSet<Integer>();
		int steps = 4 + random.nextInt(3);
		int currentStep = 0;
		int st_dep_id = stations.get(random.nextInt(stations.size())).getStation_id();
		route_stations.add(st_dep_id);
		while (currentStep<steps) {
			Query query = session.createQuery("from Direction where d.st_dep=" + st_dep_id);
			List<Direction> possibleDirections = query.list();
//			List<Direction> possibleDirections = em.createQuery(
//					selectDirections + " where d.st_dep=" + st_dep_id)
//					.getResultList();
			Direction currentDirection = possibleDirections.get(random
					.nextInt(possibleDirections.size()));
			while (route_stations.contains(currentDirection.getSt_arr())) {
				currentDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
			}
			createShedule(currentDirection.getDirection_id(), r.getRoute_id(), currentStep);
			currentStep++;
			st_dep_id = currentDirection.getSt_arr();
			route_stations.add(st_dep_id);
		}
	}
}

@SuppressWarnings("unchecked")
public List<Shedule> getShedulesOfRoute( int route_id) {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Shedule where s.route_id=" + route_id);
	return query.list();
//	return em.createQuery(selectShedules + " where s.route_id=" + route_id)
//			.getResultList();
}
@SuppressWarnings("unchecked")
public List<Shedule> getAllShedules() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Shedule");
	return query.list();
//	return em.createQuery(selectShedules).getResultList();
}

public void clearShedules() {
	Session session = sessionFactory.getCurrentSession();
	List<Shedule> shedules = getAllShedules();
	for (Shedule s: shedules) {
		session.delete(s);
	}
}

public   Journey createJourney( int train_id,  int route_id,  Date time_dep) {
	Session session = sessionFactory.getCurrentSession();
	Journey journey = new Journey();
	journey.setTrain_id(train_id);
	journey.setRoute_id(route_id);
	journey.setTime_dep(time_dep);
	session.save(journey);
//	em.persist(journey);
	return journey;
}
public void initJourneys() {
	clearJourneys();
	Date currentTime = new Date();
	long currentMillis = currentTime.getTime();
	long m = 1000 * 60l;
	long h = m * 60;
	List<Route> routes = getAllRoutes();
	List<Train> trains = getAllTrains();
	for (int i = 0; i < routes.size(); i++) {
		Route r = routes.get(i);
		Train t = trains.get(i % trains.size());
		createJourney(t.getTrain_id(), r.getRoute_id(), new Date(currentMillis + (i + 1) * h));
	}
}
@SuppressWarnings("unchecked")
public List<Journey> getAllJourneys() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Journey");
	return query.list();
//	return em.createQuery(selectJourneys).getResultList();
}
public Journey getJourney( int jour_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Journey) session.get("Journey", jour_id);
//	return em.find(Journey.class, jour_id);
}
@SuppressWarnings("unchecked")
public   List<Journey> getAllJourneysOfTrain( int train_id) {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Journey as j where j.train_id=" + train_id);
	return query.list();
//	return em.createQuery(selectJourneys + " where j.train_id=" + train_id).getResultList();
}

public void clearJourneys() {
	Session session = sessionFactory.getCurrentSession();
	List<Journey> journeys = getAllJourneys();
	for (Journey j: journeys) {
		session.delete(j);
	}
}

public Passenger createPassenger( String name,  String surname,  Date birthday) {
	Session session = sessionFactory.getCurrentSession();
	Passenger p = new Passenger();
	p.setPassenger_name(name);
	p.setPassenger_surname(surname);
	p.setPassenger_birthday(birthday);
	session.save(p);
//	em.persist(p);
	return p;
}

public void deletePassenger(int passengerId){
	Session session = sessionFactory.getCurrentSession();
	Passenger p = (Passenger) session.get("Passenger", passengerId);
	session.delete(p);
//	em.remove(em.find(Passenger.class, passengerId));
}
public Passenger getPassenger(int pass_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Passenger) session.get("Passenger", pass_id);
//	return em.find(Passenger.class, pass_id);
}
@SuppressWarnings("unchecked")
public List<Passenger> getAllPassengers(){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Passenger");
	return query.list();
}


public void clearPassengers() {
	Session session = sessionFactory.getCurrentSession();
	List<Passenger> passengers = getAllPassengers();
	for (Passenger p : passengers) {
		session.delete(p);
	}
}

public User createUser( String login,  String password,  boolean account_type) {
	Session session = sessionFactory.getCurrentSession();
	User u = new User();
	u.setUser_login(login);
	u.setUser_password(password);
	u.setAccount_type(account_type);
	session.save(u);
//	em.persist(u);
	return u;
}
public   void initUsers() {
	clearUsers();
	createUser("root","root",true);
}

public User getUser( int user_id) {
	Session session = sessionFactory.getCurrentSession();
	return (User) session.get("User", user_id);
//	return em.find(User.class, user_id);
}
@SuppressWarnings("unchecked")
public   List<User> getAllUsers() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from User");
	return query.list();
//	return em.createQuery(selectUsers).getResultList();
	
}

@SuppressWarnings("unchecked")
public User getUserByName( String name){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from User as u where u.user_login=:name");
//	Query query  = em.createQuery(selectUsers+" where u.user_login=:name");
	query.setParameter("name", name);
	List<User> foundUsers = query.list();
//	List<User> foundUsers = query.getResultList();
	if (foundUsers.isEmpty()) {
		return null;
	} else
	if (foundUsers.size() > 1) {
		throw new RuntimeException();
	} else {
		return foundUsers.get(0);
	}
	
}

public void clearUsers() {
	Session session = sessionFactory.getCurrentSession();
	List<User> users = getAllUsers();
//	List<User> users = em.createQuery(selectUsers).getResultList();
	for (User u : users) {
		session.delete(u);
	}
}

public Ticket createTicket( int passenger_id,  int journey_id,  int st_dep,  int st_arr) {
	Session session = sessionFactory.getCurrentSession();
	Ticket t = new Ticket();
	t.setPassenger_id(passenger_id);
	t.setJourney_id(journey_id);
	t.setSt_dep(st_dep);
	t.setSt_arr(st_arr);
	session.save(t);
//	em.persist(t);
	return t;
}

public Ticket getTicket( int ticket_id) {
	Session session = sessionFactory.getCurrentSession();
	return (Ticket) session.get("Ticket", ticket_id);
//	return em.find(Ticket.class, ticket_id);
}

@SuppressWarnings("unchecked")
public List<Ticket> getAllTickets() {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Ticket");
	return query.list();
//	return em.createQuery(selectTickets).getResultList();
}

@SuppressWarnings("unchecked")
public List<Ticket> getTicketOfUser( int user_id) {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from User_Ticket where ut.user_id=" + user_id);
	List<User_Ticket> userTickets = query.list();
//	List<User_Ticket> userTickets = em.createQuery(
//			selectUser_Tickets + " where ut.user_id=" + user_id).getResultList(); 
	List<Ticket> tickets = new ArrayList<Ticket>();
	for (User_Ticket ut: userTickets) {
		tickets.add(getTicket(ut.getTicket_id()));
	}
	return tickets;
}

@SuppressWarnings("unchecked")
public void initTickets() throws ParseException {
	Session session = sessionFactory.getCurrentSession();
	clearPassengers();
	String[] names = {"Aaron", "Charles", "Beatrice", "Douglas", "Emma", "Fred", "Graham", "Luccile"};
	String[] surnames = {"Abramson", "Hoggarth", "Larkins", "Bootman", "Miller", "Cramer", "Parkinson", "Erickson"};
	String[] birthdays = {"12/03/1961", "07/06/2003", "30/05/1968", "22/12/1986", "31/01/1996", "15/03/2000", "02/10/1955", "19/11/2012"};
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	List<Journey> journeys = getAllJourneys();
	Query query = session.createQuery("from User where u.account_type=" + true);
	User u = (User) query.list().get(0);
//	User u = (User) em.createQuery(selectUsers+" where u.account_type=" + true).getSingleResult();

	for (int i = 0; i < names.length; i++) {
		Passenger p = createPassenger(names[i], surnames[i], sdf.parse(birthdays[i]));
		Journey j = journeys.get(i % journeys.size());
		int route_id = j.getRoute_id();
		query = session.createQuery("from Shedule as s where s.route_id=" + route_id + " and s.step=" + 0);
		Shedule route_beginning = (Shedule) query.list().get(0);
//		Shedule route_beginning = (Shedule) em.createQuery(
//				selectShedules + " where s.route_id=" + route_id + " and s.step=" + 0).getSingleResult();
		query = session.createQuery("from Shedule as s where s.route_id=" + route_id);
		List<Shedule> steps = query.list();
//		List<Shedule> steps  = em.createQuery(selectShedules + " where s.route_id=" + route_id)
//				.getResultList();
		int lastStep = 0;
		for (Shedule s : steps) {
			lastStep = Math.max(lastStep, s.getStep());
		}
		query = session.createQuery("from Shedule as s where s.route_id=" + route_id + " and s.step=" + lastStep);
		Shedule route_ending = (Shedule) query.list().get(0);
//		Shedule route_ending = (Shedule) em.createQuery(
//				selectShedules+" where s.route_id=" + route_id + " and s.step=" + lastStep)
//				.getSingleResult();
		Direction route_beginning_d = getDirection(route_beginning.getDirection_id());
		Direction route_ending_d = getDirection(route_ending.getDirection_id());
		boolean trainHasEmptySeats = decrementEmptySeats(
				j.getJourney_id(), route_beginning_d.getSt_dep(), route_ending_d.getSt_arr());
		if (trainHasEmptySeats) {
		Ticket t = createTicket(p.getPassenger_id(), j.getJourney_id(),
				route_beginning_d.getSt_dep(), route_ending_d.getSt_arr());
		createUser_Ticket(t.getTicket_id(), u.getUser_id());
		}
	}
}
public Ticket deleteTicket( int ticket_id) {
	Session session = sessionFactory.getCurrentSession();
	Ticket t = getTicket(ticket_id);
	session.delete(t);
	return t;
}

public void clearTickets() {
	Session session = sessionFactory.getCurrentSession();
	List<Ticket> tickets = getAllTickets();
//	List<Ticket> tickets = em.createQuery(selectTickets).getResultList();
	for (Ticket t:tickets) {
		session.delete(t);
//		em.remove(t);
	}
}

public User_Ticket createUser_Ticket( int ticket_id,  int user_id) {
	Session session = sessionFactory.getCurrentSession();
	User_Ticket ut = new User_Ticket();
	ut.setTicket_id(ticket_id);
	ut.setUser_id(user_id);
	session.save(ut);
//	em.persist(ut);
	return ut;
}


public   User_Ticket deleteUser_Ticket( int ticket_id) {
	Session session = sessionFactory.getCurrentSession();
	User_Ticket ut = (User_Ticket) session.get("User_Ticket", ticket_id);
//	User_Ticket ut = em.find(User_Ticket.class, ticket_id);
//	em.remove(ut);
	session.delete(ut);
	return ut;
}

public Seats createSeats( int journey_id,  int step,  int empty_seats) {
	Session session = sessionFactory.getCurrentSession();
	Seats s = new Seats();
	s.setJourney_id(journey_id);
	s.setRoute_step(step);
	s.setEmpty_seats(empty_seats);
	session.save(s);
//	em.persist(s);
	return s;
}


@SuppressWarnings("unchecked")
public List<Seats> getSeatsOnJourney(int journey_id) {
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Seats as s where s.journey_id=" + journey_id);
	return query.list();
//	return em.createQuery(selectSeats+" where s.journey_id=" + journey_id).getResultList();
}
public Seats deleteSeats(int seats_id) {
	Session session = sessionFactory.getCurrentSession();
	Seats s = (Seats) session.get("Seats", seats_id);
//	Seats s = em.find(Seats.class, seats_id);
//	em.remove(s);
	session.delete(s);
	return s;
}

public void initSeats() {
	List<Journey> journeys = getAllJourneys();
	for (Journey j : journeys) {
		int train_id = j.getTrain_id();
		int route_id = j.getRoute_id();
		Train train = getTrain(train_id);
		List<Shedule> steps = getShedulesOfRoute(route_id);
		for (Shedule s : steps) {
			createSeats(j.getJourney_id(), s.getStep(), train.getTrain_seats());
		}
		
	}
}

public boolean decrementEmptySeats( int journey_id,  int st_dep,  int st_arr) {
	Journey j = getJourney(journey_id);
	List<Seats> seats = getSeatsOnJourney(journey_id);
	List<Shedule> steps = getShedulesOfRoute(j.getRoute_id());
	
	int start_step=0;
	int stop_step=0;
	for (Shedule s : steps) {
		Direction d = getDirection(s.getDirection_id());
		if (d.getSt_dep()==st_dep) {
			start_step=s.getStep();
		}
		if (d.getSt_arr()==st_arr) {
			stop_step=s.getStep();
		}
	}
	for (Seats s : seats) {
		if (s.getRoute_step()>=start_step && s.getRoute_step()<=stop_step && s.getEmpty_seats()==0) {
			return false;
		}
	}
	
	for (Seats s : seats) {
		Session session = sessionFactory.getCurrentSession();
		int empty_seats = s.getEmpty_seats();
		if (s.getRoute_step()>=start_step && s.getRoute_step()<=stop_step) {
		s.setEmpty_seats(empty_seats - 1);
//		em.merge(s);
		session.merge("Seats", s);
		}
	}
	return true;
	
}
@SuppressWarnings("unchecked")
public List<Seats> getAllSeats(){
	Session session = sessionFactory.getCurrentSession();
	Query query = session.createQuery("from Seats");
	return query.list();
}
public void clearSeats() {
	Session session = sessionFactory.getCurrentSession();
	List<Seats> seats = getAllSeats();
	for (Seats s : seats) {
		session.delete(s);
	}
	
}

public void initDatabase() throws ParseException{
	initStations();
	initTrains();
	initRoutes();
	initDirections();
	initShedules();
	initJourneys();
	initSeats();
	initUsers();
	initTickets();
}


}
