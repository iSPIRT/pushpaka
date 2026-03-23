package in.ispirt.pushpaka.flightauthorisation.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.FlightPlan;
import in.ispirt.pushpaka.registry.utils.DaoException;
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
