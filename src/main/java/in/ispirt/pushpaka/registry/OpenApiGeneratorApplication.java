package in.ispirt.pushpaka.registry;

import com.fasterxml.jackson.databind.Module;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@ComponentScan(
  basePackages = {
    "in.ispirt.pushpaka.registry",
    "in.ispirt.pushpaka.registry.api",
    "in.ispirt.pushpaka.registry.config"
  },
  nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class OpenApiGeneratorApplication {

  public static void main(String[] args) {
    SpringApplication.run(OpenApiGeneratorApplication.class, args);
    DaoInstance.getInstance();
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.OpenApiGeneratorApplication.jsonNullableModule"
  )
  public Module jsonNullableModule() {
    return new JsonNullableModule();
  }
}
