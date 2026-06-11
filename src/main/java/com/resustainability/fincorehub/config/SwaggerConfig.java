package com.resustainability.fincorehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {


	    @Bean
	    public OpenAPI customOpenAPI() {
	        return new OpenAPI()
	                .info(new Info()
	                        .title("GL&PC Master")
	                        .description("API Documentation for Task GL&PC Master System")
	                        .version("1.0.0"));
	                        
	    }


}
