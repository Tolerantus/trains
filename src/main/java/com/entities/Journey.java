package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Journey
 *
 */
@Entity

public class Journey implements Serializable {
@Id
@GeneratedValue (strategy=GenerationType.AUTO)
private int journey_id;
private int route_id;
private int train_id;
@Temporal(TemporalType.TIMESTAMP)
private Date time_dep;
	
	

	@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + journey_id;
	return result;
}

@Override
	public String toString() {
		return "Journey [journey_id=" + journey_id + ", route_id=" + route_id
				+ ", train_id=" + train_id + ", time_dep=" + time_dep + "]\n";
	}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Journey other = (Journey) obj;
	if (journey_id != other.journey_id)
		return false;
	return true;
}

	public int getJourney_id() {
	return journey_id;
}

public Journey(int journey_id, int route_id, int train_id, Date time_dep) {
		super();
		this.journey_id = journey_id;
		this.route_id = route_id;
		this.train_id = train_id;
		this.time_dep = time_dep;
	}

public void setJourney_id(int journey_id) {
	this.journey_id = journey_id;
}

public int getRoute_id() {
	return route_id;
}

public void setRoute_id(int route_id) {
	this.route_id = route_id;
}

public int getTrain_id() {
	return train_id;
}

public void setTrain_id(int train_id) {
	this.train_id = train_id;
}

public Date getTime_dep() {
	return time_dep;
}

public void setTime_dep(Date time_dep) {
	this.time_dep = time_dep;
}

	private static final long serialVersionUID = 1L;

	public Journey() {
		super();
	}
   
}
