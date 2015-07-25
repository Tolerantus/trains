package com.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.NewJourneyInfo;
import com.entities.Dao;
import com.entities.Direction;
import com.entities.Journey;
import com.entities.Route;
import com.entities.Shedule;
import com.entities.Train;
@Service("journeyPlanner")
public class JourneyPlanner {
	@Autowired
	private Dao dao;
	private static final Logger log = Logger.getLogger(JourneyPlanner.class);

//	public JourneyPlanner(Dao dao) {
//		super();
//		this.dao = dao;
//	}

	@Transactional
	public  NewJourneyInfo plan(NewJourneyInfo dto) throws ParseException{
//		try {
//			dao.begin();
			log.debug(dto);
			String routeInfo = dto.getRouteInfo();
			String date = dto.getDate();
			String time = dto.getTime();
			if (date != "" && time != "") {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String[] hoursAndMinutes = time.split(":");
				Integer h = Integer.parseInt(hoursAndMinutes[0]);
				Integer m = Integer.parseInt(hoursAndMinutes[1]);
				Date choosedDate = sdf.parse(date);

				Date departureDate = new Date(choosedDate.getTime()
						+ (h * 60 + m) * 60 * 1000);
				Date currentDate = new Date();
				if (departureDate.getTime() - currentDate.getTime() > 10 * 60 * 1000) {
					Date destinationDate = departureDate;
					Route route = dao.getRoute(Integer.parseInt(routeInfo));
					for (Shedule step : dao.getShedulesOfRoute(route.getRoute_id())) {
						Direction d = dao.getDirection(step.getDirection_id());
						destinationDate = new Date(destinationDate.getTime()
								+ d.getTime());
					}
					Train train = null;
					for (Train t : dao.getAllTrains()) {
						List<Journey> jours = dao.getAllJourneysOfTrain(t
								.getTrain_id());
						if (jours == null || jours.isEmpty()) {
							train = t;
							break;
						} else {
							boolean isFree = true;
							for (Journey j : jours) {
								int route_id = j.getRoute_id();
								Date depTime = j.getTime_dep();
								Date arrTime = depTime;
								List<Shedule> steps = dao.getShedulesOfRoute(route_id);
								for (Shedule step : steps) {
									Direction d = dao.getDirection(step
											.getDirection_id());
									arrTime = new Date(arrTime.getTime()
											+ d.getTime());
								}

								if (departureDate.getTime() >= (depTime
										.getTime())
										&& departureDate.getTime() <= (arrTime
												.getTime())
										|| destinationDate.getTime() >= (depTime
												.getTime())
										&& destinationDate.getTime() <= (arrTime
												.getTime())) {
									isFree = false;
								}
							}
							if (isFree) {
								train = t;
								break;
							}
						}
					}
					if (train == null) {
						dto.setTrainsLack(true);
//						dao.commit();
						return dto;
					} else {
						Journey j = dao.createJourney(train.getTrain_id(),
								route.getRoute_id(), departureDate);
						List<Shedule> steps = dao.getShedulesOfRoute(route
								.getRoute_id());
						for (Shedule step : steps) {
							dao.createSeats(j.getJourney_id(), step.getStep(),
									train.getTrain_seats());
						}
						SimpleDateFormat sdf2 = new SimpleDateFormat(
								"HH:mm   dd MMM", Locale.US);
						dto.setJourneyId(String.valueOf(j.getJourney_id()));
						dto.setRouteName(route.getRoute_name());
						dto.setDate(sdf2.format(departureDate));
						dto.setTrain(String.valueOf(train.getTrain_id()));
						log.debug(dto);
//						dao.commit();
						return dto;
					}
				} else {
//					dao.commit();
					return dto;
				}
			} else {
//				dao.commit();
				return dto;
			}
//		} finally {
//			dao.close();
//		}
	}
}
