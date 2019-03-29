package per.ravisu.rest.ws.shoppingcart.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import brave.sampler.Sampler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("per.ravisu.rest.ws.shoppingcart.order")
public class ShoppingCartOrderApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartOrderApp.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
		
		
	}

}
