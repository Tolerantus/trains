package com.entities;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Entity implementation class for Entity: User_Tickets
 *
 */
@Entity
public class UserAndTicket implements Serializable {
	@Id	
	@JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
	@OneToOne
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Ticket ticket;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private User user;
	
	private static final long serialVersionUID = 1L;

	
	public UserAndTicket() {
		super();
	}


	public UserAndTicket(Ticket ticket, User user) {
		super();
		this.ticket = ticket;
		this.user = user;
	}


	public Ticket getTicket() {
		return ticket;
	}


	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

}
