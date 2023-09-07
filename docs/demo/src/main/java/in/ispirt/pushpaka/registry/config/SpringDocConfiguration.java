package in.ispirt.pushpaka.registry.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

  @Bean(name = "in.ispirt.pushpaka.registry.config.SpringDocConfiguration.apiInfo")
  OpenAPI apiInfo() {
    return new OpenAPI()
      .info(
        new Info()
          .title("Pushpaka Registry")
          .description(
            "This is a sample UasType Store Server based on the OpenAPI 3.0 specification.  You can find out more about Swagger at [http://swagger.io](http://swagger.io). In the third iteration of the uasType store, we've switched to the design first approach! You can now help us improve the API whether it's by making changes to the definition itself or to the code. That way, with time, we can improve the API in general, and expose some of the new features in OAS3.  Some useful links: - [The UasType Store repository](https://github.com/swagger-api/swagger-petstore) - [The source API definition for the UasType Store](https://github.com/swagger-api/swagger-petstore/blob/master/src/main/resources/openapi.yaml)"
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
      .components(
        new Components()
          .addSecuritySchemes(
            "registry_auth",
            new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
          )
          .addSecuritySchemes(
            "api_key",
            new SecurityScheme()
              .type(SecurityScheme.Type.APIKEY)
              .in(SecurityScheme.In.HEADER)
              .name("api_key")
          )
      );
  }
}
