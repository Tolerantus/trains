package com.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.dto.TicketContainer;
import com.dto.TicketListConstraints;
import com.entities.Dao;
import com.entities.Ticket;

@Service
public class TicketService {
	private Dao dao;
	@Autowired
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	@Transactional
	public List<Ticket> dispatch(TicketContainer container) {
		TicketListConstraints constraint = container.getConstraints();
		String startDate = container.getStartDate();
		String stopDate = container.getStopDate();
		switch (constraint) {
		case BETWEEN:
			return getTicketsBetween(startDate, stopDate);
		case AFTER:
			return getTicketsAfter(startDate);
		case BEFORE:
			return getTicketsBefore(stopDate);
		default:
			return null;
		}
	}
	
	public List<Ticket> getTicketsBetween(String startDate, String stopDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Ticket> tickets = null;
		try {
			tickets = dao.getTicketsBetweenDates(sdf.parse(startDate), sdf.parse(stopDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tickets;
	}
	
	public List<Ticket> getTicketsBefore(@RequestParam("stopDate") String stopDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Ticket> tickets = null;
		try {
			tickets = dao.getTicketsBefore(sdf.parse(stopDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tickets;
	}

	public List<Ticket> getTicketsAfter(@RequestParam("startDate") String startDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Ticket> tickets = null;
		try {
			tickets = dao.getTicketsAfter(sdf.parse(startDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tickets;
	}
}
