package per.ravisu.rest.ws.shoppingcart.netflixeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@EnableEurekaServer
@SpringBootApplication
public class NetflixEurekaNamingServerApplication { 	
	

	public static void main(String[] args) {
				
		SpringApplication.run(NetflixEurekaNamingServerApplication.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE; 
		
		
	}
}
