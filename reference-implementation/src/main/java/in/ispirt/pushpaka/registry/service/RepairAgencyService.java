package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.RepairAgency;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RepairAgencyService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public RepairAgency create(RepairAgency repairAgency) throws DaoException {
    in.ispirt.pushpaka.dao.entities.RepairAgency saved =
      in.ispirt.pushpaka.dao.entities.RepairAgency.create(
        sf(),
        RepairAgency.fromOa(repairAgency)
      );
    return RepairAgency.toOa(saved);
  }

  public void delete(UUID repairAgencyId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.RepairAgency.delete(sf(), repairAgencyId);
  }

  public List<RepairAgency> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.RepairAgency.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.RepairAgency::toOa)
      .collect(Collectors.toList());
  }

  public RepairAgency getById(UUID repairAgencyId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.RepairAgency entity =
      in.ispirt.pushpaka.dao.entities.RepairAgency.get(sf(), repairAgencyId);
    return in.ispirt.pushpaka.models.RepairAgency.toOa(entity);
  }

  public RepairAgency update(UUID repairAgencyId, RepairAgency repairAgency) throws DaoException {
    in.ispirt.pushpaka.dao.entities.RepairAgency updated =
      in.ispirt.pushpaka.dao.entities.RepairAgency.update(
        sf(),
        repairAgencyId,
        RepairAgency.fromOa(repairAgency)
      );
    return in.ispirt.pushpaka.models.RepairAgency.toOa(updated);
  }
}
