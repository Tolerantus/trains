package com.entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Shedule
 *
 */
@Entity

public class Shedule implements Serializable {
@Id
@GeneratedValue (strategy=GenerationType.AUTO)
private int shedule_id;
private int direction_id;
private int route_id;
private int step;
	
	public Shedule(int shedule_id, int direction_id, int route_id, int step) {
	super();
	this.shedule_id = shedule_id;
	this.direction_id = direction_id;
	this.route_id = route_id;
	this.step = step;
}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + shedule_id;
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
		Shedule other = (Shedule) obj;
		if (shedule_id != other.shedule_id)
			return false;
		return true;
	}

	public int getShedule_id() {
	return shedule_id;
}

public void setShedule_id(int shedule_id) {
	this.shedule_id = shedule_id;
}

public int getDirection_id() {
	return direction_id;
}

public void setDirection_id(int direction_id) {
	this.direction_id = direction_id;
}

public int getRoute_id() {
	return route_id;
}

public void setRoute_id(int route_id) {
	this.route_id = route_id;
}

public int getStep() {
	return step;
}

public void setStep(int step) {
	this.step = step;
}

	private static final long serialVersionUID = 1L;

	public Shedule() {
		super();
	}
   
}
