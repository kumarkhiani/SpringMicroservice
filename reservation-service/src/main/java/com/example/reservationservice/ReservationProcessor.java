package com.example.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class ReservationProcessor {
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@ServiceActivator(inputChannel = Sink.INPUT)
	public void acceptNewReservation(String reservationName){
		reservationRepository.save(new Reservation(reservationName));
	}
}
