package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Pilot;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PilotService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public Pilot create(Pilot pilot) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Pilot saved =
      in.ispirt.pushpaka.dao.entities.Pilot.create(sf(), Pilot.fromOa(pilot));
    return Pilot.toOa(saved);
  }

  public void delete(UUID pilotId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Pilot.delete(sf(), pilotId);
  }

  public List<Pilot> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Pilot.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Pilot::toOa)
      .collect(Collectors.toList());
  }

  public Pilot getById(UUID pilotId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Pilot entity =
      in.ispirt.pushpaka.dao.entities.Pilot.get(sf(), pilotId);
    return in.ispirt.pushpaka.models.Pilot.toOa(entity);
  }

  public Pilot update(UUID pilotId, Pilot pilot) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Pilot updated =
      in.ispirt.pushpaka.dao.entities.Pilot.update(sf(), pilotId, Pilot.fromOa(pilot));
    return in.ispirt.pushpaka.models.Pilot.toOa(updated);
  }
}
