package per.ravisu.rest.ws.shoppingcart.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("per.ravisu.rest.ws.shoppingcart.customer")
public class ShoppingCartCustomerApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartCustomerApp.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
		
		
	}
}
