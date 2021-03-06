package com.example.reservationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ReservationController {

	@Value("${message}")
	private String message;
	
	@RequestMapping("/message")
	public String getReservations(){
		
		return message;
	}

}
