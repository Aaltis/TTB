package fi.breakwaterworks.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
@EnableSwagger2
public class SpringFoxConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(apiInfo())
				.useDefaultResponseMessages(false);
	}
	
	private ApiInfo apiInfo() {
		springfox.documentation.service.Contact contact = new springfox.documentation.service.Contact("Antti Ruotsalainen", null, null);

	    return new ApiInfo(
	      "TrackThatBarbell Rest API",
	      "API to track and analyze your workout load.",
	      "1.0",
	      "",
	      contact,
	      "",
	      "API license URL",
	      Collections.emptyList());
	   
	}
}