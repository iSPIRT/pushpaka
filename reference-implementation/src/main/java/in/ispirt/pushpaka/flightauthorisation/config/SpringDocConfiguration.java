package in.ispirt.pushpaka.flightauthorisation.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

  @Bean(name = "in.ispirt.pushpaka.registry.config.SpringDocConfiguration.apiInfo")
  OpenAPI apiInfo() {
    return new OpenAPI()
      .info(
        new Info()
          .title("Pushpaka Flight Authorisation")
          .description(
            "Flight Authorisation Service allows registered entities to create flight plans, receive authorisation tokens"
          )
          .termsOfService("http://swagger.io/terms/")
          .contact(new Contact().email("sayandeep@ispirt.in"))
          .license(
            new License()
              .name("Apache 2.0")
              .url("http://www.apache.org/licenses/LICENSE-2.0.html")
          )
          .version("1.0.17")
      )
      .servers(Arrays.asList(new Server().url("http://localhost:8083/")))
      .components(
        new Components()
        .addSecuritySchemes(
            "jwt",
            new SecurityScheme()
              .name("mysecurity")
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
          )
      );
  }
}
