package com.dto;

import java.util.List;

import com.entities.Ticket;

public class TicketContainer {
	private List<Ticket> tickets;
	private TicketListConstraints constraints;
	private String startDate;
	private String stopDate;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public TicketListConstraints getConstraints() {
		return constraints;
	}

	public void setConstraints(TicketListConstraints constraints) {
		this.constraints = constraints;
	}
	
}
