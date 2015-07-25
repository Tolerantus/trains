package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Passenger
 *
 */
@Entity

public class Passenger implements Serializable {
@Id
@GeneratedValue (strategy=GenerationType.AUTO)
private int passenger_id;
private String passenger_name;
private String passenger_surname;
@Temporal(TemporalType.TIMESTAMP)
private Date passenger_birthday;
public String getPassenger_surname() {
	return passenger_surname;
}

public void setPassenger_surname(String passenger_surname) {
	this.passenger_surname = passenger_surname;
}

public Date getPassenger_birthday() {
	return passenger_birthday;
}

public void setPassenger_birthday(Date passenger_birthday) {
	this.passenger_birthday = passenger_birthday;
}

private static final long serialVersionUID = 1L;
public Passenger(int passenger_id, String passenger_name) {
	super();
	this.passenger_id = passenger_id;
	this.passenger_name = passenger_name;
}





	@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
			* result
			+ ((passenger_birthday == null) ? 0 : passenger_birthday.hashCode());
	result = prime * result
			+ ((passenger_name == null) ? 0 : passenger_name.hashCode());
	result = prime * result
			+ ((passenger_surname == null) ? 0 : passenger_surname.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Passenger other = (Passenger) obj;
	if (passenger_birthday == null) {
		if (other.passenger_birthday != null)
			return false;
	} else if (!passenger_birthday.equals(other.passenger_birthday))
		return false;
	if (passenger_name == null) {
		if (other.passenger_name != null)
			return false;
	} else if (!passenger_name.equals(other.passenger_name))
		return false;
	if (passenger_surname == null) {
		if (other.passenger_surname != null)
			return false;
	} else if (!passenger_surname.equals(other.passenger_surname))
		return false;
	return true;
}

	public Passenger() {
		super();
	}

	public int getPassenger_id() {
		return passenger_id;
	}

	public void setPassenger_id(int passenger_id) {
		this.passenger_id = passenger_id;
	}

	public String getPassenger_name() {
		return passenger_name;
	}

	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}
   
}
