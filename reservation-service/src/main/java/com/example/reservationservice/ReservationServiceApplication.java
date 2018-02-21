package com.example.reservationservice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.stereotype.Component;

@EnableBinding(Sink.class)
@IntegrationComponentScan
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
	  return new AlwaysSampler();
	}
}

@Component
class DummyCommandLineRunner implements CommandLineRunner {

	ReservationRepository reservationRepository;

	@Autowired
	public DummyCommandLineRunner(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(String... arg0) throws Exception {

		Arrays.asList("Kumar,Mishika,Riya,Jyoti,Tarun".split(",")).stream()
				.forEach(n -> this.reservationRepository.save(new Reservation(n)));
		
		this.reservationRepository.findAll().stream().forEach(n -> System.out.println(n));
		System.out.println(System.getenv("CONFIG_SERVER"));
	}

}
