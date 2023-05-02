package com.crm;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "CRM APIs",
				description = "CRM App REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Thong Ly Minh",
						email = "lmthong98@gmail.com",
						url = "https://github.dev/lmthongit98"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.dev/lmthongit98"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "CRM App Documentation",
				url = "https://github.com/lmthongit98/crm-api"
		)
)
public class CrmProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmProjectApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

}
