package in.ispirt.pushpaka.flightauthorisation.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.AirspaceUsageToken;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AirspaceUsageTokenService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public AirspaceUsageToken create(AirspaceUsageToken aut) throws DaoException {
    in.ispirt.pushpaka.dao.entities.AirspaceUsageToken saved =
      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.create(
        sf(),
        AirspaceUsageToken.fromOa(aut)
      );
    return in.ispirt.pushpaka.models.AirspaceUsageToken.toOa(saved);
  }

  public void delete(UUID autId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.delete(sf(), autId);
  }

  public List<AirspaceUsageToken> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.AirspaceUsageToken::toOa)
      .collect(Collectors.toList());
  }

  public AirspaceUsageToken getById(UUID autId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.AirspaceUsageToken entity =
      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.get(sf(), autId);
    return in.ispirt.pushpaka.models.AirspaceUsageToken.toOa(entity);
  }

  public AirspaceUsageToken getByFlightPlanId(UUID flightPlanId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.AirspaceUsageToken entity =
      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.getByFlightPlanId(sf(), flightPlanId);
    if (entity == null) {
      throw new DaoException(DaoException.Code.NOT_FOUND, "AirspaceUsageToken");
    }
    return in.ispirt.pushpaka.models.AirspaceUsageToken.toOa(entity);
  }

  public AirspaceUsageToken update(UUID autId, AirspaceUsageToken aut) throws DaoException {
    in.ispirt.pushpaka.dao.entities.AirspaceUsageToken updated =
      in.ispirt.pushpaka.dao.entities.AirspaceUsageToken.update(
        sf(),
        autId,
        AirspaceUsageToken.fromOa(aut)
      );
    return in.ispirt.pushpaka.models.AirspaceUsageToken.toOa(updated);
  }
}
