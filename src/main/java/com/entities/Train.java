package com.entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Train
 *
 */
@Entity

public class Train implements Serializable {
@Id 
@GeneratedValue(strategy=GenerationType.AUTO)
private int train_id;
private int train_seats;
	
	@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + train_id;
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
	Train other = (Train) obj;
	if (train_id != other.train_id)
		return false;
	return true;
}

	private static final long serialVersionUID = 1L;

	public Train() {
		super();
	}

	public Train(int train_id, int train_seats) {
		super();
		this.train_id = train_id;
		this.train_seats = train_seats;
	}

	public int getTrain_id() {
		return train_id;
	}

	public void setTrain_id(int train_id) {
		this.train_id = train_id;
	}

	public int getTrain_seats() {
		return train_seats;
	}

	public void setTrain_seats(int train_seats) {
		this.train_seats = train_seats;
	}
   
}
