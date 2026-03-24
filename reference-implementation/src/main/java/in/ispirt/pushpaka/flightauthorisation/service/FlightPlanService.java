package in.ispirt.pushpaka.flightauthorisation.service;

import com.nimbusds.jose.jwk.RSAKey;
import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.flightauthorisation.aut.AirspaceUsageTokenUtils;
import in.ispirt.pushpaka.models.FlightPlan;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.utils.Logging;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class FlightPlanService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public FlightPlan create(FlightPlan flightPlan, Jwt authentication) throws DaoException {
    in.ispirt.pushpaka.dao.entities.FlightPlan saved =
      in.ispirt.pushpaka.dao.entities.FlightPlan.create(sf(), FlightPlan.fromOa(flightPlan));

    // Auto-issue an AUT for the saved flight plan.
    try {
      in.ispirt.pushpaka.models.AirspaceUsageToken autModel =
        AirspaceUsageTokenUtils.createAirspaceUsageTokenObject(
          flightPlan.getUas(),
          flightPlan.getPilot(),
          in.ispirt.pushpaka.models.FlightPlan.toOa(saved),
          flightPlan.getStartTime(),
          flightPlan.getEndTime()
        );

      RSAKey rsaKey = AirspaceUsageTokenUtils.getDigitalSkyRsaKey();
      String autJwt = AirspaceUsageTokenUtils.signAirspaceUsageTokenObjectJWT(
        rsaKey,
        "digitalsky",
        autModel,
        "pushpaka-utm",
        "utm-client",
        authentication.getSubject(),
        60,
        0
      );

      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken autEntity =
        new in.ispirt.pushpaka.dao.entities.AirspaceUsageToken(autModel.getId());
      in.ispirt.pushpaka.dao.entities.Uas uasRef = new in.ispirt.pushpaka.dao.entities.Uas();
      uasRef.setId(saved.getUas().getId());
      autEntity.setFlightPlan(new in.ispirt.pushpaka.dao.entities.FlightPlan(saved.getId()));
      autEntity.setPilot(new in.ispirt.pushpaka.dao.entities.Pilot(saved.getPilot().getId()));
      autEntity.setUas(uasRef);
      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.createForFlightPlan(sf(), autEntity);

      Logging.info("AUT issued for FlightPlan " + saved.getId() + " jwt=" + autJwt);
    } catch (Exception e) {
      Logging.warning("AUT issuance failed for FlightPlan " + saved.getId() + ": " + e.getMessage());
    }

    return in.ispirt.pushpaka.models.FlightPlan.toOa(saved);
  }

  public void delete(UUID flightPlanId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.FlightPlan.delete(sf(), flightPlanId);
  }

  public List<FlightPlan> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.FlightPlan.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.FlightPlan::toOa)
      .collect(Collectors.toList());
  }

  public FlightPlan getById(UUID flightPlanId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.FlightPlan entity =
      in.ispirt.pushpaka.dao.entities.FlightPlan.get(sf(), flightPlanId);
    return in.ispirt.pushpaka.models.FlightPlan.toOa(entity);
  }

  public FlightPlan update(UUID flightPlanId, FlightPlan flightPlan) throws DaoException {
    in.ispirt.pushpaka.dao.entities.FlightPlan updated =
      in.ispirt.pushpaka.dao.entities.FlightPlan.update(sf(), flightPlanId, FlightPlan.fromOa(flightPlan));
    return in.ispirt.pushpaka.models.FlightPlan.toOa(updated);
  }
}
