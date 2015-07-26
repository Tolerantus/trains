package com.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




/**
 * Entity implementation class for Entity: Station
 *
 */
@Entity
public class Station implements Serializable {
@Id
@GeneratedValue (strategy=GenerationType.AUTO)
private int station_id;
private String station_name;	
	private static final long serialVersionUID = 1L;

	public Station(int station_id, String station_name) {
		super();
		this.station_id = station_id;
		this.station_name = station_name;
	}

	public Station() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + station_id;
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
		Station other = (Station) obj;
		if (station_id != other.station_id)
			return false;
		return true;
	}

	public int getStation_id() {
		return station_id;
	}

	public void setStation_id(int station_id) {
		this.station_id = station_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	@Override
	public String toString() {
		return station_name;
	}
   
}
