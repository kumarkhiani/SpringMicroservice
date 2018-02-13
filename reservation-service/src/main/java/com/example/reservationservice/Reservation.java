package com.example.reservationservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Reservation {
 
	@javax.persistence.Id
	@GeneratedValue
	private Long Id;
	
	private String reservationName;

	public Reservation(){
	}
	
	public Reservation(String reservationName) {
		this.reservationName = reservationName;
	}

	public Long getId() {
		return Id;
	}

	public String getReservationName() {
		return reservationName;
	}

	@Override
	public String toString() {
		return "Reservation [Id=" + Id + ", reservationName=" + reservationName + "]";
	}
}
