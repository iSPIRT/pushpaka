package in.ispirt.pushpaka.registry.utils;

import in.ispirt.pushpaka.registry.models.UasWeightCategory;

public class Utils {

  public static UasWeightCategory toOpenApiWeightCategory(Float mtow) {
    if (mtow <= 0.25f) {
      return UasWeightCategory.NANO;
    } else if (mtow <= 2.0f) {
      return UasWeightCategory.MICRO;
    } else if (mtow <= 25.0f) {
      return UasWeightCategory.SMALL;
    } else if (mtow <= 150.0f) {
      return UasWeightCategory.MEDIUM;
    } else {
      return UasWeightCategory.LARGE;
    }
  }
}
