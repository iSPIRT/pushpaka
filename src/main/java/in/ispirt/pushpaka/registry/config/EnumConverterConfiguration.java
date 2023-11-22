package in.ispirt.pushpaka.registry.config;

import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.PilotStatus;
import in.ispirt.pushpaka.registry.models.State;
import in.ispirt.pushpaka.registry.models.UasPropulsionCategory;
import in.ispirt.pushpaka.registry.models.UasStatus;
import in.ispirt.pushpaka.registry.models.UasWeightCategory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EnumConverterConfiguration {

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.operationCategoryConverter"
  )
  Converter<String, OperationCategory> operationCategoryConverter() {
    return new Converter<String, OperationCategory>() {

      @Override
      public OperationCategory convert(String source) {
        return OperationCategory.fromValue(source);
      }
    };
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.pilotStatusConverter"
  )
  Converter<String, PilotStatus> pilotStatusConverter() {
    return new Converter<String, PilotStatus>() {

      @Override
      public PilotStatus convert(String source) {
        return PilotStatus.fromValue(source);
      }
    };
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.stateConverter"
  )
  Converter<String, State> stateConverter() {
    return new Converter<String, State>() {

      @Override
      public State convert(String source) {
        return State.fromValue(source);
      }
    };
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.uasPropulsionCategoryConverter"
  )
  Converter<String, UasPropulsionCategory> uasPropulsionCategoryConverter() {
    return new Converter<String, UasPropulsionCategory>() {

      @Override
      public UasPropulsionCategory convert(String source) {
        return UasPropulsionCategory.fromValue(source);
      }
    };
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.uasStatusConverter"
  )
  Converter<String, UasStatus> uasStatusConverter() {
    return new Converter<String, UasStatus>() {

      @Override
      public UasStatus convert(String source) {
        return UasStatus.fromValue(source);
      }
    };
  }

  @Bean(
    name = "in.ispirt.pushpaka.registry.config.EnumConverterConfiguration.uasWeightCategoryConverter"
  )
  Converter<String, UasWeightCategory> uasWeightCategoryConverter() {
    return new Converter<String, UasWeightCategory>() {

      @Override
      public UasWeightCategory convert(String source) {
        return UasWeightCategory.fromValue(source);
      }
    };
  }
}
