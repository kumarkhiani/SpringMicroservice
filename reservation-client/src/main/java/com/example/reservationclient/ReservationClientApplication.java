package com.example.reservationclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableHystrix
@EnableZuulProxy
@EnableDiscoveryClient
@EnableBinding(Source.class)
@SpringBootApplication
@IntegrationComponentScan
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	
	@Bean
	public Sampler defaultSampler() {
	  return new AlwaysSampler();
	}
}

@RestController
@RequestMapping("/reservations")
class ReservationClientAPIGateway{
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Source source;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationClientAPIGateway.class);
	@RequestMapping(value="/writeReservations", method=RequestMethod.POST)
	public void writeReservations(@RequestBody Reservation reservation){

		LOGGER.info("LOGGER In Client - Before");
		Message<String> message = MessageBuilder.withPayload(reservation.getReservationName()).build();
		source.output().send(message);
		LOGGER.info("LOGGER In Client - After");
		System.out.println(message);
	}
	
	public Collection<String> getReservationsFallback(){
		return new ArrayList<String>();
	}
	
	@HystrixCommand(fallbackMethod="getReservationsFallback")
	@RequestMapping(value="/names",method=RequestMethod.GET)
	public Collection<String> getReservations(){
		
		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
		};
		
		LOGGER.info("LOGGER In Client /names - Before");
 		ResponseEntity<Resources<Reservation>> entity = restTemplate.exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);
		LOGGER.info("LOGGER In Client /names - AFTER");
 		return entity.getBody().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());
	}
}

class Reservation{
	
	private String reservationName;
	
	public String getReservationName(){
		return this.reservationName;
	}
}
