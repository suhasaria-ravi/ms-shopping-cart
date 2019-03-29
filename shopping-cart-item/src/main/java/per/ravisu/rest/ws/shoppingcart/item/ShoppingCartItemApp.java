package per.ravisu.rest.ws.shoppingcart.item;

import org.springframework.boot.SpringApplication;
import brave.sampler.Sampler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("per.ravisu.rest.ws.shoppingcart.item")
public class ShoppingCartItemApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartItemApp.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
		
		
	}
}
