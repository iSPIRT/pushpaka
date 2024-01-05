package in.ispirt.pushpaka.flightauthorisation;

import com.fasterxml.jackson.databind.Module;
import in.ispirt.pushpaka.flightauthorisation.dao.DaoInstance;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@ComponentScan(
  basePackages = {
    "in.ispirt.pushpaka.flightauthorisation",
    "in.ispirt.pushpaka.flightauthorisation.api",
    "in.ispirt.pushpaka.flightauthorisation.config"
  },
  nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class FlightAuthorisationService {

  public static void main(String[] args) {
    SpringApplication.run(FlightAuthorisationService.class, args);
    DaoInstance.getInstance();
  }

  @Bean(
    name = "in.ispirt.pushpaka.flightauthorisation.OpenApiGeneratorApplication.jsonNullableModule"
  )
  public Module jsonNullableModule() {
    return new JsonNullableModule();
  }
}
